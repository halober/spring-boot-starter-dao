package com.reger.datasource.properties;

import java.util.Map;

import org.apache.ibatis.session.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = DaoProperties.dbprefix)
public class DaoProperties {
	public final static String dbprefix = "spring.mybatis";
	
	/**
	 * 具体的DB配置参数
	 */
	private Map<String, MybatisNodeProperties> nodes;
	
	/**
	 * mybatis配置参数
	 */
	private Configuration configuration;

	public Map<String, MybatisNodeProperties> getNodes() {
		return nodes;
	}

	public void setNodes(Map<String, MybatisNodeProperties> nodes) {
		this.nodes = nodes;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	
}
