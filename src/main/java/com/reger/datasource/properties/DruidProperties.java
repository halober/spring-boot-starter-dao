package com.reger.datasource.properties;

import java.sql.SQLException;
import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.alibaba.druid.pool.DruidDataSource;
@ConfigurationProperties(DruidProperties.druidDefault)
public class DruidProperties {
	public final static  String druidDefault="spring.druid.default";
	/**
	 * jdbc的链接url </br> 默认值 为空，不可以为空
	 */
	private String url;
	/**
	 * 访问数据库的用户名 </br> 默认值 为空，不可以为空
	 */
	private String username;
	/**
	 * 访问数据库的密码 </br> 默认值 为空，不可以为空
	 */
	private String password;
	/**
	 * jdbc驱动， </br> 默认值 为空，可以为空， 如果为空 Druid可以根据url判断出该值
	 */
	private String driverClassName;
	/**
	 * jdbc的连接参数
	 */
	private Properties connectProperties;
	/**
	 * 连接池初始化大小 </br>
	 * 默认值 1
	 */
	private Integer initialSize;
	/**
	 * 最小空闲个数</br> 默认值 1
	 */
	private Integer minIdle;
	/**
	 * 最大激活的连接数</br> 默认值  20
	 */
	private Integer maxActive;
	/**
	 * 获取链接最大等待时间 </br> 默认值 60000L
	 */
	private Long maxWait;
	/**
	 * Druid开启的filter 默认值  wall,stat
	 */
	private String filters="wall,stat";
	/**
	 * 是否自动提交事务</br> 默认值  true
	 */
	private Boolean defaultAutoCommit;
	/**
	 * </br> 默认值 6000L
	 */
	private Long timeBetweenConnectErrorMillis;
	/**
	 * </br> 默认值 
	 */
	private String validationQuery;
	/**
	 * 链接在空闲时测试 </br> 默认值  true
	 */
	private Boolean testWhileIdle;
	/**
	 * </br> 默认值  false
	 */
	private Boolean testOnBorrow;
	/**
	 * </br> 默认值 false
	 */
	private Boolean testOnReturn;
	/**
	 * </br> 默认值 
	 */
	private Boolean poolPreparedStatements;
	/**
	 * </br> 默认值 
	 */
	private Boolean clearFiltersEnable;
	/**
	 * </br> 默认值  false
	 */
	private Boolean defaultReadOnly;
	/**
	 * </br> 默认值 
	 */
	private Boolean asyncCloseConnectionEnable;
	/**
	 * 获取jdbc链接失败后重试的次数 </br> 默认值 3
	 */
	private Integer connectionErrorRetryAttempts;
	/**
	 * </br> 默认值 
	 */
	private Boolean breakAfterAcquireFailure;
	private Boolean dupCloseLogEnable;
	/**
	 * </br> 默认值 
	 */
	private Boolean enable;
	/**
	 * </br> 默认值 
	 */
	private Boolean logAbandoned;
	/**
	 * </br> 默认值 
	 */
	private Boolean logDifferentThread;
	/**
	 * </br> 默认值 
	 */
	private Integer loginTimeout;
	/**
	 * </br> 默认值 
	 */
	private Boolean accessToUnderlyingConnectionAllowed;
	/**
	 * </br> 默认值 
	 */
	private Integer maxPoolPreparedStatementPerConnectionSize;
	/**
	 * </br> 默认值 
	 */
	private Integer queryTimeout;
	/**
	 * </br> 默认值 
	 */
	private Boolean failFast;
	/**
	 * </br> 默认值 
	 */
	private Integer maxCreateTaskCount;
	/**
	 * </br> 默认值 
	 */
	private Boolean removeAbandoned;
	/**
	 * </br> 默认值 
	 */
	private Long removeAbandonedTimeoutMillis;
	/**
	 * </br> 默认值 
	 */
	private Integer defaultTransactionIsolation;
	/**
	 * </br> 默认值  60000L
	 */
	private Long timeBetweenEvictionRunsMillis;
	/**
	 * </br> 默认值  300000L
	 */
	private Long minEvictableIdleTimeMillis;
	/**
	 * </br> 默认值 
	 */
	private Long maxEvictableIdleTimeMillis;
	/**
	 * </br> 默认值 
	 */
	private Integer maxOpenPreparedStatements;
	/**
	 * </br> 默认值 
	 */
	private Integer notFullTimeoutRetryCount;
	/**
	 * </br> 默认值 
	 */
	private Long timeBetweenLogStatsMillis;
	/**
	 * </br> 默认值  28000 ,mysql链接失效时间28800，最好不要超过这个至
	 */
	private Integer validationQueryTimeout;

	public DruidProperties() {
	}

	public DruidDataSource createDataSource() throws SQLException {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setDriverClassName(driverClassName);
		dataSource.setConnectProperties(connectProperties);
		dataSource.setInitialSize(initialSize);
		dataSource.setMinIdle(minIdle);
		dataSource.setMaxActive(maxActive);
		dataSource.setMaxWait(maxWait);
		dataSource.setFilters(filters);
		dataSource.setDefaultAutoCommit(defaultAutoCommit);
		dataSource.setTimeBetweenConnectErrorMillis(timeBetweenConnectErrorMillis);
		dataSource.setValidationQuery(validationQuery);
		dataSource.setValidationQueryTimeout(validationQueryTimeout);
		dataSource.setTestWhileIdle(testWhileIdle);
		dataSource.setTestOnBorrow(testOnBorrow);
		dataSource.setTestOnReturn(testOnReturn);
		dataSource.setPoolPreparedStatements(poolPreparedStatements);
		dataSource.setClearFiltersEnable(clearFiltersEnable);
		dataSource.setDefaultReadOnly(defaultReadOnly);
		dataSource.setAsyncCloseConnectionEnable(asyncCloseConnectionEnable);
		dataSource.setConnectionErrorRetryAttempts(connectionErrorRetryAttempts);
		dataSource.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
		dataSource.setDupCloseLogEnable(dupCloseLogEnable);
		dataSource.setEnable(enable);
		dataSource.setLogAbandoned(logAbandoned);
		dataSource.setLogDifferentThread(logDifferentThread);
		dataSource.setLoginTimeout(loginTimeout);
		dataSource.setAccessToUnderlyingConnectionAllowed(accessToUnderlyingConnectionAllowed);
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		dataSource.setQueryTimeout(queryTimeout);
		dataSource.setFailFast(failFast);
		dataSource.setMaxCreateTaskCount(maxCreateTaskCount);
		dataSource.setRemoveAbandoned(removeAbandoned);
		dataSource.setRemoveAbandonedTimeoutMillis(removeAbandonedTimeoutMillis);
		dataSource.setDefaultTransactionIsolation(defaultTransactionIsolation);
		dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		dataSource.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);
		dataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
		dataSource.setNotFullTimeoutRetryCount(notFullTimeoutRetryCount);
		dataSource.setTimeBetweenLogStatsMillis(timeBetweenLogStatsMillis);
		return dataSource;
	}

	public DruidProperties defaultEmpty() {
		DruidProperties druidProperties = new DruidProperties();
		druidProperties.initialSize = 5;
		druidProperties.minIdle = 10;
		druidProperties.maxActive = 500;
		
		druidProperties.maxWait = 60000L;

		druidProperties.timeBetweenEvictionRunsMillis = 60000L;
		druidProperties.minEvictableIdleTimeMillis=300000L;

		druidProperties.validationQuery = "SELECT 0 ;";
		druidProperties.validationQueryTimeout = 28000;
		
		druidProperties.testWhileIdle = true;
		druidProperties.testOnBorrow = false;
		druidProperties.testOnReturn = false;
		
		druidProperties.poolPreparedStatements = true;
		druidProperties.maxPoolPreparedStatementPerConnectionSize = 20;
		
		druidProperties.timeBetweenConnectErrorMillis = 6000L;
		
		druidProperties.filters = "wall,stat";
		druidProperties.defaultAutoCommit = true;
		druidProperties.clearFiltersEnable = false;
		druidProperties.defaultReadOnly = false;
		druidProperties.asyncCloseConnectionEnable = true;
		druidProperties.connectionErrorRetryAttempts = 3;
		druidProperties.breakAfterAcquireFailure = false;
		druidProperties.dupCloseLogEnable = true;
		druidProperties.enable = true;
		druidProperties.logAbandoned = true;
		druidProperties.logDifferentThread = true;
		druidProperties.loginTimeout = 5000;
		druidProperties.accessToUnderlyingConnectionAllowed = true;
		druidProperties.queryTimeout = 3000;
		druidProperties.failFast = true;
		druidProperties.maxCreateTaskCount = 4;
		druidProperties.removeAbandoned = true;
		druidProperties.removeAbandonedTimeoutMillis = 3600000L;
		druidProperties.defaultTransactionIsolation = 1;
		druidProperties.maxEvictableIdleTimeMillis = 3000000L;
		druidProperties.maxOpenPreparedStatements = 200;
		druidProperties.notFullTimeoutRetryCount = 500;
		druidProperties.timeBetweenLogStatsMillis = 300000L;
		return this.merge(druidProperties);
	}

	public DruidProperties merge(DruidProperties druidProperties) {
		if (druidProperties == null)
			return this;
		if (this.url == null && druidProperties.url != null)
			this.url = druidProperties.url;
		if (this.username == null && druidProperties.username != null)
			this.username = druidProperties.username;
		if (this.password == null && druidProperties.password != null)
			this.password = druidProperties.password;
		if (this.driverClassName == null && druidProperties.driverClassName != null)
			this.driverClassName = druidProperties.driverClassName;
		if (this.initialSize == null && druidProperties.initialSize != null)
			this.initialSize = druidProperties.initialSize;
		if (this.minIdle == null && druidProperties.minIdle != null)
			this.minIdle = druidProperties.minIdle;
		if (this.maxActive == null && druidProperties.maxActive != null)
			this.maxActive = druidProperties.maxActive;
		if (this.maxWait == null && druidProperties.maxWait != null)
			this.maxWait = druidProperties.maxWait;
		if (this.filters == null && druidProperties.filters != null)
			this.filters = druidProperties.filters;
		if (this.connectProperties == null)
			connectProperties = new Properties();
		if (druidProperties.connectProperties != null)
			this.connectProperties.putAll(druidProperties.connectProperties);
		if (this.defaultAutoCommit == null && druidProperties.defaultAutoCommit != null)
			this.defaultAutoCommit = druidProperties.defaultAutoCommit;

		if (this.timeBetweenConnectErrorMillis == null && druidProperties.timeBetweenConnectErrorMillis != null)
			this.timeBetweenConnectErrorMillis = druidProperties.timeBetweenConnectErrorMillis;
		if (this.validationQuery == null && druidProperties.validationQuery != null)
			this.validationQuery = druidProperties.validationQuery;
		if (this.testWhileIdle == null && druidProperties.testWhileIdle != null)
			this.testWhileIdle = druidProperties.testWhileIdle;
		if (this.testOnBorrow == null && druidProperties.testOnBorrow != null)
			this.testOnBorrow = druidProperties.testOnBorrow;
		if (this.testOnReturn == null && druidProperties.testOnReturn != null)
			this.testOnReturn = druidProperties.testOnReturn;
		if (this.poolPreparedStatements == null && druidProperties.poolPreparedStatements != null)
			this.poolPreparedStatements = druidProperties.poolPreparedStatements;
		if (this.clearFiltersEnable == null && druidProperties.clearFiltersEnable != null)
			this.clearFiltersEnable = druidProperties.clearFiltersEnable;
		if (this.defaultReadOnly == null && druidProperties.defaultReadOnly != null)
			this.defaultReadOnly = druidProperties.defaultReadOnly;
		if (this.asyncCloseConnectionEnable == null && druidProperties.asyncCloseConnectionEnable != null)
			this.asyncCloseConnectionEnable = druidProperties.asyncCloseConnectionEnable;
		if (this.connectionErrorRetryAttempts == null && druidProperties.connectionErrorRetryAttempts != null)
			this.connectionErrorRetryAttempts = druidProperties.connectionErrorRetryAttempts;
		if (this.breakAfterAcquireFailure == null && druidProperties.breakAfterAcquireFailure != null)
			this.breakAfterAcquireFailure = druidProperties.breakAfterAcquireFailure;
		if (this.dupCloseLogEnable == null && druidProperties.dupCloseLogEnable != null)
			this.dupCloseLogEnable = druidProperties.dupCloseLogEnable;
		if (this.enable == null && druidProperties.enable != null)
			this.enable = druidProperties.enable;
		if (this.logAbandoned == null && druidProperties.logAbandoned != null)
			this.logAbandoned = druidProperties.logAbandoned;
		if (this.logDifferentThread == null && druidProperties.logDifferentThread != null)
			this.logDifferentThread = druidProperties.logDifferentThread;
		if (this.loginTimeout == null && druidProperties.loginTimeout != null)
			this.loginTimeout = druidProperties.loginTimeout;
		if (this.accessToUnderlyingConnectionAllowed == null
				&& druidProperties.accessToUnderlyingConnectionAllowed != null)
			this.accessToUnderlyingConnectionAllowed = druidProperties.accessToUnderlyingConnectionAllowed;
		if (this.maxPoolPreparedStatementPerConnectionSize == null
				&& druidProperties.maxPoolPreparedStatementPerConnectionSize != null)
			this.maxPoolPreparedStatementPerConnectionSize = druidProperties.maxPoolPreparedStatementPerConnectionSize;
		if (this.queryTimeout == null && druidProperties.queryTimeout != null)
			this.queryTimeout = druidProperties.queryTimeout;
		if (this.failFast == null && druidProperties.failFast != null)
			this.failFast = druidProperties.failFast;
		if (this.maxCreateTaskCount == null && druidProperties.maxCreateTaskCount != null)
			this.maxCreateTaskCount = druidProperties.maxCreateTaskCount;
		if (this.removeAbandoned == null && druidProperties.removeAbandoned != null)
			this.removeAbandoned = druidProperties.removeAbandoned;
		if (this.removeAbandonedTimeoutMillis == null && druidProperties.removeAbandonedTimeoutMillis != null)
			this.removeAbandonedTimeoutMillis = druidProperties.removeAbandonedTimeoutMillis;
		if (this.defaultTransactionIsolation == null && druidProperties.defaultTransactionIsolation != null)
			this.defaultTransactionIsolation = druidProperties.defaultTransactionIsolation;
		if (this.timeBetweenEvictionRunsMillis == null && druidProperties.timeBetweenEvictionRunsMillis != null)
			this.timeBetweenEvictionRunsMillis = druidProperties.timeBetweenEvictionRunsMillis;
		if (this.minEvictableIdleTimeMillis == null && druidProperties.minEvictableIdleTimeMillis != null)
			this.minEvictableIdleTimeMillis = druidProperties.minEvictableIdleTimeMillis;
		if (this.maxEvictableIdleTimeMillis == null && druidProperties.maxEvictableIdleTimeMillis != null)
			this.maxEvictableIdleTimeMillis = druidProperties.maxEvictableIdleTimeMillis;
		if (this.maxOpenPreparedStatements == null && druidProperties.maxOpenPreparedStatements != null)
			this.maxOpenPreparedStatements = druidProperties.maxOpenPreparedStatements;
		if (this.notFullTimeoutRetryCount == null && druidProperties.notFullTimeoutRetryCount != null)
			this.notFullTimeoutRetryCount = druidProperties.notFullTimeoutRetryCount;
		if (this.timeBetweenLogStatsMillis == null && druidProperties.timeBetweenLogStatsMillis != null)
			this.timeBetweenLogStatsMillis = druidProperties.timeBetweenLogStatsMillis;
		if (this.validationQueryTimeout == null && druidProperties.validationQueryTimeout != null)
			this.validationQueryTimeout = druidProperties.validationQueryTimeout;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public Integer getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(Integer initialSize) {
		this.initialSize = initialSize;
	}

	public Integer getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	public Integer getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(Integer maxActive) {
		this.maxActive = maxActive;
	}

	public Long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(Long maxWait) {
		this.maxWait = maxWait;
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public Properties getConnectProperties() {
		return connectProperties;
	}

	public void setConnectProperties(Properties connectProperties) {
		this.connectProperties = connectProperties;
	}

	public Boolean isDefaultAutoCommit() {
		return defaultAutoCommit;
	}

	public void setDefaultAutoCommit(Boolean defaultAutoCommit) {
		this.defaultAutoCommit = defaultAutoCommit;
	}

	public Long getTimeBetweenConnectErrorMillis() {
		return timeBetweenConnectErrorMillis;
	}

	public void setTimeBetweenConnectErrorMillis(Long timeBetweenConnectErrorMillis) {
		this.timeBetweenConnectErrorMillis = timeBetweenConnectErrorMillis;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	public Boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(Boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public Boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(Boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public Boolean isTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(Boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public Boolean isPoolPreparedStatements() {
		return poolPreparedStatements;
	}

	public void setPoolPreparedStatements(Boolean poolPreparedStatements) {
		this.poolPreparedStatements = poolPreparedStatements;
	}

	public Boolean isClearFiltersEnable() {
		return clearFiltersEnable;
	}

	public void setClearFiltersEnable(Boolean clearFiltersEnable) {
		this.clearFiltersEnable = clearFiltersEnable;
	}

	public Boolean isDefaultReadOnly() {
		return defaultReadOnly;
	}

	public void setDefaultReadOnly(Boolean defaultReadOnly) {
		this.defaultReadOnly = defaultReadOnly;
	}

	public Boolean isAsyncCloseConnectionEnable() {
		return asyncCloseConnectionEnable;
	}

	public void setAsyncCloseConnectionEnable(Boolean asyncCloseConnectionEnable) {
		this.asyncCloseConnectionEnable = asyncCloseConnectionEnable;
	}

	public Integer getConnectionErrorRetryAttempts() {
		return connectionErrorRetryAttempts;
	}

	public void setConnectionErrorRetryAttempts(Integer connectionErrorRetryAttempts) {
		this.connectionErrorRetryAttempts = connectionErrorRetryAttempts;
	}

	public Boolean isBreakAfterAcquireFailure() {
		return breakAfterAcquireFailure;
	}

	public void setBreakAfterAcquireFailure(Boolean breakAfterAcquireFailure) {
		this.breakAfterAcquireFailure = breakAfterAcquireFailure;
	}

	public Boolean isDupCloseLogEnable() {
		return dupCloseLogEnable;
	}

	public void setDupCloseLogEnable(Boolean dupCloseLogEnable) {
		this.dupCloseLogEnable = dupCloseLogEnable;
	}

	public Boolean isEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Boolean isLogAbandoned() {
		return logAbandoned;
	}

	public void setLogAbandoned(Boolean logAbandoned) {
		this.logAbandoned = logAbandoned;
	}

	public Boolean isLogDifferentThread() {
		return logDifferentThread;
	}

	public void setLogDifferentThread(Boolean logDifferentThread) {
		this.logDifferentThread = logDifferentThread;
	}

	public Integer getLoginTimeout() {
		return loginTimeout;
	}

	public void setLoginTimeout(Integer loginTimeout) {
		this.loginTimeout = loginTimeout;
	}

	public Boolean isAccessToUnderlyingConnectionAllowed() {
		return accessToUnderlyingConnectionAllowed;
	}

	public void setAccessToUnderlyingConnectionAllowed(Boolean accessToUnderlyingConnectionAllowed) {
		this.accessToUnderlyingConnectionAllowed = accessToUnderlyingConnectionAllowed;
	}

	public Integer getMaxPoolPreparedStatementPerConnectionSize() {
		return maxPoolPreparedStatementPerConnectionSize;
	}

	public void setMaxPoolPreparedStatementPerConnectionSize(Integer maxPoolPreparedStatementPerConnectionSize) {
		this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
	}

	public Integer getQueryTimeout() {
		return queryTimeout;
	}

	public void setQueryTimeout(Integer queryTimeout) {
		this.queryTimeout = queryTimeout;
	}

	public Boolean isFailFast() {
		return failFast;
	}

	public void setFailFast(Boolean failFast) {
		this.failFast = failFast;
	}

	public Integer getMaxCreateTaskCount() {
		return maxCreateTaskCount;
	}

	public void setMaxCreateTaskCount(Integer maxCreateTaskCount) {
		this.maxCreateTaskCount = maxCreateTaskCount;
	}

	public Boolean isRemoveAbandoned() {
		return removeAbandoned;
	}

	public void setRemoveAbandoned(Boolean removeAbandoned) {
		this.removeAbandoned = removeAbandoned;
	}

	public Long getRemoveAbandonedTimeoutMillis() {
		return removeAbandonedTimeoutMillis;
	}

	public void setRemoveAbandonedTimeoutMillis(Long removeAbandonedTimeoutMillis) {
		this.removeAbandonedTimeoutMillis = removeAbandonedTimeoutMillis;
	}

	public Integer getDefaultTransactionIsolation() {
		return defaultTransactionIsolation;
	}

	public void setDefaultTransactionIsolation(Integer defaultTransactionIsolation) {
		this.defaultTransactionIsolation = defaultTransactionIsolation;
	}

	public Long getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(Long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public Long getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(Long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public Long getMaxEvictableIdleTimeMillis() {
		return maxEvictableIdleTimeMillis;
	}

	public void setMaxEvictableIdleTimeMillis(Long maxEvictableIdleTimeMillis) {
		this.maxEvictableIdleTimeMillis = maxEvictableIdleTimeMillis;
	}

	public Integer getMaxOpenPreparedStatements() {
		return maxOpenPreparedStatements;
	}

	public void setMaxOpenPreparedStatements(Integer maxOpenPreparedStatements) {
		this.maxOpenPreparedStatements = maxOpenPreparedStatements;
	}

	public Integer getNotFullTimeoutRetryCount() {
		return notFullTimeoutRetryCount;
	}

	public void setNotFullTimeoutRetryCount(Integer notFullTimeoutRetryCount) {
		this.notFullTimeoutRetryCount = notFullTimeoutRetryCount;
	}

	public Long getTimeBetweenLogStatsMillis() {
		return timeBetweenLogStatsMillis;
	}

	public void setTimeBetweenLogStatsMillis(Long timeBetweenLogStatsMillis) {
		this.timeBetweenLogStatsMillis = timeBetweenLogStatsMillis;
	}

	public Integer getValidationQueryTimeout() {
		return validationQueryTimeout;
	}

	public void setValidationQueryTimeout(Integer validationQueryTimeout) {
		this.validationQueryTimeout = validationQueryTimeout;
	}
}
