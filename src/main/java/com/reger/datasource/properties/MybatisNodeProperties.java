package com.reger.datasource.properties;

import java.util.List;

import com.reger.datasource.core.Dialect;
import com.reger.datasource.core.Mapper;
import com.reger.datasource.core.Order;

public class MybatisNodeProperties {
	/**
	 * mybatis mapper接口文件的配置位置，该值不可以为空
	 */
	private String basePackage;
	/**
	 * mybatis mapper 的xml配置文件的位置 ，该值最好不要为空，
	 */
	private String mapperPackage;
	/**
	 * model类所在的包
	 */
	private String typeAliasesPackage;
	/**
	 * mybatis生成主键的顺序，如果要在insert into 数据前获得主键，配置为 BEFORE，否则配置为 AFTER
	 * </br> 默认值为 BEFORE
	 */
	private Order order = Order.BEFORE;
	/**
	 * mybatis通用mapper使用的mapper接口集合,mysqldb时使用使用 MYSQL,SqlServer 时使用MSSQL,其它db类型时使用DEFAULT，可以根据dialect自动判断
	 * <br> 默认值是 null
	 */
	@Deprecated
	private Mapper mapper = null;
	
	/**
	 * 数据库方言，默认值为空,如果为空可以根据数据库链接字符串判断
	 */
	private Dialect dialect=null;
	/**
	 * 是否是默认的DB对象， 
	 * </br默认值  false，
	 * </br>最多只有一个为true，
	 * </br>如果配置多个只有第一个生效
	 */
	private boolean primary;
	/**
	 * 主库配置对象，不可以为空
	 */
	private DruidProperties master;
	/**
	 * 多个丛库的配置信息，可以为空
	 */
	private List<DruidProperties> slaves;

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	public DruidProperties getMaster() {
		return master;
	}

	public void setMaster(DruidProperties master) {
		this.master = master;
	}

	public List<DruidProperties> getSlaves() {
		return slaves;
	}

	public void setSlaves(List<DruidProperties> slaves) {
		this.slaves = slaves;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getMapperPackage() {
		return mapperPackage;
	}

	public void setMapperPackage(String mapperPackage) {
		this.mapperPackage = mapperPackage;
	}

	public String getTypeAliasesPackage() {
		return typeAliasesPackage;
	}

	public void setTypeAliasesPackage(String typeAliasesPackage) {
		this.typeAliasesPackage = typeAliasesPackage;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Mapper getMapper() {
		return mapper;
	}

	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	public Dialect getDialect() {
		return dialect;
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}
	
}