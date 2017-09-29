package com.reger.datasource.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.scheduling.annotation.Async;

import com.alibaba.druid.pool.DruidDataSource;
import com.reger.datasource.properties.DruidProperties;
import com.reger.datasource.properties.MybatisNodeProperties;

/**
 * 配置主从数据源后，根据选择，返回对应的数据源。多个从库的情况下，会平均的分配从库，用于负载均衡。
 *
 */

public class DynamicDataSource extends AbstractDataSource {

	private static final Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);
	
	private String dataSourceName;
	private DruidDataSource masterDataSource;
	private Map<String, DruidDataSource> slaveDataSources = new LinkedHashMap<String, DruidDataSource>();
	private List<String> slavesDataSourceNames = new ArrayList<>();
	private List<String> slavesFailureDataSourceNames = new ArrayList<>();
	private static final ThreadLocal<Stack<Boolean>> ismaster = new ThreadLocal<Stack<Boolean>>() {
		protected Stack<Boolean> initialValue() {
			return new Stack<Boolean>();
		}
	};
 
	private volatile int selectnum = 0;

	protected static void useMaster() {
		ismaster.get().push(true);
	}

	protected static void useSlave() {
		ismaster.get().push(false);
	}

	protected static void reset() {
		ismaster.get().pop();
		if (ismaster.get().size() == 0)
			ismaster.remove();
	}

	@Override
	public Connection getConnection() throws SQLException {
		try {
			return determineTargetDataSourceConnection();
		} catch (SQLException e) {
			logger.info("获取jdbc链接失败,重试开始");
			return determineTargetDataSourceConnection();
		}
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		try {
			return determineTargetDataSourceConnection(username, password);
		} catch (SQLException e) {
			logger.info("获取jdbc链接失败,重试开始");
			return determineTargetDataSourceConnection(username, password);
		}
	}

	private Connection determineTargetDataSourceConnection() throws SQLException {
		String lookupKey = determineCurrentLookupKey();
		DataSource datasource = determineTargetDataSource(lookupKey);
		try {
			return datasource.getConnection();
		} catch (SQLException e) {
			this.recordFailure(datasource, lookupKey, e);
			throw e;
		}
	}

	private Connection determineTargetDataSourceConnection(String username, String password) throws SQLException {
		String lookupKey = determineCurrentLookupKey();
		DataSource datasource = determineTargetDataSource(lookupKey);
		try {
			return datasource.getConnection(username, password);
		} catch (SQLException e) {
			this.recordFailure(datasource, lookupKey, e);
			throw e;
		}
	}

	private void recordFailure(DataSource datasource, String lookupKey, SQLException e) {
		if (this.masterDataSource.equals(datasource)) {
			logger.error("数据库主库出现异常，请检查主库状态。。。异常信息:{} {}  {}", e.getMessage(), e.getSQLState(), e.getErrorCode());
			return;
		}
		slavesDataSourceNames.remove(lookupKey);
		slavesFailureDataSourceNames.add(lookupKey);
		logger.warn("数据库从库{}出现异常，已下线。。。异常信息:{} {}  {}", lookupKey, e.getMessage(), e.getSQLState(), e.getErrorCode());
	}

	@Async
	public void retryFailureSlavesDataSource() {
		if (slavesFailureDataSourceNames.isEmpty())
			return;
		Iterator<String> it = slavesFailureDataSourceNames.iterator();
		while (it.hasNext()) {
			String lookupKey = (String) it.next();
			try {
				determineTargetDataSource(lookupKey).getConnection();
				slavesFailureDataSourceNames.remove(lookupKey);
				slavesDataSourceNames.add(lookupKey);
				logger.info("数据库从库{}从异常中恢复过来", lookupKey);
			} catch (SQLException e) {
				logger.debug("测试链接失效的从库{}还没有活过来", lookupKey, e);
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T unwrap(Class<T> iface) throws SQLException {
		if (iface.isInstance(this)) {
			return (T) this;
		}
		String lookupKey = determineCurrentLookupKey();
		return determineTargetDataSource(lookupKey).unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		String lookupKey = determineCurrentLookupKey();
		return (iface.isInstance(this) || determineTargetDataSource(lookupKey).isWrapperFor(iface));
	}

	protected DataSource determineTargetDataSource(String lookupKey) {
		DataSource dataSource = this.slaveDataSources.get(lookupKey);
		if (dataSource != null)
			return dataSource;
		return this.masterDataSource;
	}

	/**
	 * 如果是选择使用从库，且从库的数量大于1，则通过取模来控制从库的负载
	 * @return 选中的库名
	 */
	protected String determineCurrentLookupKey() {
		if (ismaster.get().isEmpty() || ismaster.get().peek()) {
			return null;
		}
		if (!slavesDataSourceNames.isEmpty()) {
			int slaveCount=slavesDataSourceNames.size();
			String slavesDataSourceName = slavesDataSourceNames.get(selectnum% slaveCount);
			selectnum = (++selectnum % slaveCount);
			logger.debug("切换到从库{}中查询", slavesDataSourceName);
			return slavesDataSourceName;
		}else{
			logger.info("{}的从库不可用，将使用主库查询",this.dataSourceName);
		}
		return null;
	}

	public static DynamicDataSource create(MybatisNodeProperties druidNode, DruidProperties defaultDruidProperties,
			String dataSourceName) throws SQLException {
		return new DynamicDataSource(druidNode, defaultDruidProperties, dataSourceName);
	}

	public DynamicDataSource() {
	}

	public DynamicDataSource(MybatisNodeProperties druidNode, DruidProperties defaultDruidProperties, String dataSourceName)
			throws SQLException {
		this.dataSourceName=dataSourceName;
		DruidProperties master = druidNode.getMaster();
		if (master == null)
			master = new DruidProperties();
		master.merge(defaultDruidProperties).defaultEmpty().setDefaultReadOnly(false);
		this.masterDataSource = master.createDataSource();
		this.masterDataSource.setName(dataSourceName + "-Master");
		List<DruidProperties> slaves = druidNode.getSlaves();
		if (slaves != null && !slaves.isEmpty()) {
			for (int i = 0; i < slaves.size(); i++) {
				DruidProperties slave = slaves.get(i);
				if (slave == null)
					continue;
				slave.merge(defaultDruidProperties).defaultEmpty().setDefaultReadOnly(true);
				String slaveDatasourceName = dataSourceName + "-Slave-" + i;
				this.slavesDataSourceNames.add(slaveDatasourceName);
				DruidDataSource datasourc = slave.createDataSource();
				datasourc.setName(slaveDatasourceName);
				this.slaveDataSources.put(slaveDatasourceName, datasourc);
			}
		}
	}
	
	public void init() throws SQLException {
		logger.debug("初始化 DynamicDataSource {}...",this.dataSourceName);
		this.masterDataSource.init();
		this.slaveDataSources.values().forEach(ds->{
			try {
				ds.init();
			} catch (SQLException e) {
				logger.warn("从库{}初始化失败", ds.getName());
			}
		});
	}
	
	public void close() {
		logger.debug("销毁 DynamicDataSource {}...",this.dataSourceName);
		this.masterDataSource.close();
		this.slaveDataSources.values().forEach(ds->{
			try {
				ds.close();
			} catch (Exception e) {
				logger.warn("关闭从库{}失败", ds.getName());
			}
		});
	}
	
	public DruidDataSource masterDataSource() {
		return masterDataSource;
	}
}
