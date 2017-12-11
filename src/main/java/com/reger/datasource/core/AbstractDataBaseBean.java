package com.reger.datasource.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.alibaba.druid.util.JdbcUtils;
import com.reger.datasource.properties.DruidProperties;
import com.reger.datasource.properties.MybatisNodeProperties;

import tk.mybatis.mapper.code.Style;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

public abstract class AbstractDataBaseBean {

	static Logger log = LoggerFactory.getLogger(AbstractDataBaseBean.class);

	protected final AbstractBeanDefinition createDataSource(MybatisNodeProperties druidNodeConfig,
			DruidProperties defaultConfig, String dataSourceName) {
		Assert.notNull(druidNodeConfig, String
				.format("DynamicDataSource 未初始化 ,dataSourceName=%s,失败原因: 配置参数为空,你的配置可能存在问题!", dataSourceName + ""));
		BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DynamicDataSource.class);
		definitionBuilder.addConstructorArgValue(druidNodeConfig);
		definitionBuilder.addConstructorArgValue(defaultConfig);
		definitionBuilder.addConstructorArgValue(dataSourceName);
		definitionBuilder.setInitMethodName("init");
		definitionBuilder.setDestroyMethodName("close");
		return definitionBuilder.getRawBeanDefinition();
	}

	protected AbstractBeanDefinition createDataSourceMaster(String dataSourceName) {
		return BeanDefinitionBuilder
				.genericBeanDefinition(DynamicDataSource.class)
				.setFactoryMethodOnBean("masterDataSource", dataSourceName)
				.getRawBeanDefinition();
	}

	protected final AbstractBeanDefinition createTransactionManager(String dataSourceName) {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(DataSourceTransactionManager.class);
		bdb.addConstructorArgReference(dataSourceName);
		return bdb.getRawBeanDefinition();
	}

	protected final AbstractBeanDefinition createJdbcTemplate(String dataSourceName) {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(JdbcTemplate.class);
		bdb.addConstructorArgReference(dataSourceName);
		return bdb.getRawBeanDefinition();
	}

	protected Configuration cloneConfiguration(Configuration configuration) { 
		Configuration relust=new Configuration();
		relust.setEnvironment(configuration.getEnvironment());
		relust.setSafeRowBoundsEnabled(configuration.isSafeRowBoundsEnabled());
		relust.setSafeResultHandlerEnabled(configuration.isSafeResultHandlerEnabled());
		relust.setMapUnderscoreToCamelCase(configuration.isMapUnderscoreToCamelCase());
		relust.setAggressiveLazyLoading(configuration.isAggressiveLazyLoading());
		relust.setMultipleResultSetsEnabled(configuration.isMultipleResultSetsEnabled());
		relust.setUseGeneratedKeys(configuration.isUseGeneratedKeys());
		relust.setUseColumnLabel(configuration.isUseColumnLabel());
		relust.setCacheEnabled(configuration.isCacheEnabled());
		relust.setCallSettersOnNulls(configuration.isCallSettersOnNulls());
		relust.setUseActualParamName(configuration.isUseActualParamName());
		relust.setReturnInstanceForEmptyRow(configuration.isReturnInstanceForEmptyRow());
		relust.setLogPrefix(configuration.getLogPrefix());
		relust.setLogImpl(configuration.getLogImpl());
		relust.setVfsImpl(configuration.getVfsImpl());
		relust.setLocalCacheScope(configuration.getLocalCacheScope());
		relust.setJdbcTypeForNull(configuration.getJdbcTypeForNull());
		relust.setLazyLoadTriggerMethods(configuration.getLazyLoadTriggerMethods());
		relust.setDefaultStatementTimeout(configuration.getDefaultStatementTimeout());
		relust.setDefaultFetchSize(configuration.getDefaultFetchSize());
		relust.setDefaultExecutorType(configuration.getDefaultExecutorType());
		relust.setAutoMappingBehavior(configuration.getAutoMappingBehavior());
		relust.setAutoMappingUnknownColumnBehavior(configuration.getAutoMappingUnknownColumnBehavior());
		relust.setVariables(configuration.getVariables());
		relust.setReflectorFactory(configuration.getReflectorFactory());
		relust.setObjectFactory(configuration.getObjectFactory());
		relust.setObjectWrapperFactory(configuration.getObjectWrapperFactory());
		relust.setLazyLoadingEnabled(configuration.isLazyLoadingEnabled());
		relust.setProxyFactory(configuration.getProxyFactory());
		relust.setDatabaseId(configuration.getDatabaseId());
		relust.setConfigurationFactory(configuration.getConfigurationFactory());
		relust.getTypeHandlerRegistry().setDefaultEnumTypeHandler(GlobalEnumTypeHandler.class);
		return relust;
	}
	
	protected String getDbType(DruidProperties nodeProperties, DruidProperties defaultProperties) {
		String rawUrl = nodeProperties.getUrl();
		if (StringUtils.isEmpty(nodeProperties.getUrl())) {
			rawUrl = defaultProperties.getUrl();
		}
		return JdbcUtils.getDbType(rawUrl, null);
	}
	

	protected final AbstractBeanDefinition createSqlSessionFactoryBean(String dataSourceName, String mapperPackage,
			String typeAliasesPackage, Dialect dialect, Configuration configuration) {
		configuration.setDatabaseId(dataSourceName);
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(SqlSessionFactoryBean.class);
		bdb.addPropertyValue("configuration", configuration);
		bdb.addPropertyValue("failFast", true);
		bdb.addPropertyValue("typeAliases", this.saenTypeAliases(typeAliasesPackage));
		bdb.addPropertyReference("dataSource", dataSourceName);
		bdb.addPropertyValue("plugins", new Interceptor[] { new CustomPageInterceptor(dialect) });
		if (!StringUtils.isEmpty(mapperPackage)) {
			try {
				mapperPackage = new StandardEnvironment().resolveRequiredPlaceholders(mapperPackage);
				String mapperPackages = ClassUtils.convertClassNameToResourcePath(mapperPackage);
				String mapperPackagePath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + mapperPackages + "/*.xml";
				Resource[] resources = new PathMatchingResourcePatternResolver().getResources(mapperPackagePath);
				bdb.addPropertyValue("mapperLocations", resources);
			} catch (Exception e) {
				log.error("初始化失败", e);
				throw new RuntimeException( String.format("SqlSessionFactory 初始化失败  mapperPackage=%s", mapperPackage + ""));
			}
		}
		return bdb.getBeanDefinition();
	}

	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
	private static final String SEPERATOR = ",";

	private Class<?>[] saenTypeAliases(String typeAliasesPackage) {
		if (typeAliasesPackage == null || typeAliasesPackage.trim().isEmpty()) {
			return new Class<?>[] {};
		}
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);

		String[] aliasList = typeAliasesPackage.split(SEPERATOR);
		List<Class<?>> result = new ArrayList<Class<?>>();
		for (String alias : aliasList) {
			if (alias == null || (alias = alias.trim()).isEmpty())
				continue;
			String aliasesPackages = ClassUtils.convertClassNameToResourcePath(alias);
			alias = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + aliasesPackages + "/" + DEFAULT_RESOURCE_PATTERN;
			try {
				Resource[] resources = resolver.getResources(alias);
				if (resources != null && resources.length > 0) {
					MetadataReader metadataReader = null;
					for (Resource resource : resources) {
						if (resource.isReadable()) {
							metadataReader = metadataReaderFactory.getMetadataReader(resource);
							try {
								result.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toArray(new Class<?>[0]);
	}

	protected final AbstractBeanDefinition createScannerConfigurerBean(String sqlSessionFactoryName, String basepackage,
			Mapper mappers, Order order,Style style) {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
		Properties properties = new Properties();
		if(style!=null){
			properties.setProperty("style", style.name());
		}
		properties.setProperty("notEmpty", "true");
		properties.setProperty("ORDER", order != null ? order.order : Order.BEFORE.order);
		properties.setProperty("mappers", mappers != null ? mappers.mapper : Mapper.DEFAULT.mapper);
		bdb.addPropertyValue("properties", properties);
		bdb.addPropertyValue("sqlSessionFactoryBeanName", sqlSessionFactoryName);
		bdb.addPropertyValue("basePackage", basepackage);
		return bdb.getRawBeanDefinition();
	}

}
