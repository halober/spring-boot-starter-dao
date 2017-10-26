package com.reger.datasource.config;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.bind.PropertiesConfigurationFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import com.reger.datasource.aspect.DataSourceAspect;
import com.reger.datasource.core.AbstractDataBaseBean;
import com.reger.datasource.core.DataSourceInvalidRetry;
import com.reger.datasource.core.Mapper;
import com.reger.datasource.core.Order;
import com.reger.datasource.properties.DaoProperties;
import com.reger.datasource.properties.DruidProperties;
import com.reger.datasource.properties.MybatisNodeProperties;

@org.springframework.context.annotation.Configuration
@EnableTransactionManagement(proxyTargetClass=true,order=Ordered.HIGHEST_PRECEDENCE)
public class DataSourceAutoConfiguration extends AbstractDataBaseBean implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

	static Logger log = LoggerFactory.getLogger(DataSourceAutoConfiguration.class);

	ConfigurableEnvironment environment;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = (ConfigurableEnvironment) environment;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		DaoProperties druidConfig = this.getDruidConfig(DaoProperties.dbprefix, DaoProperties.class);
		DruidProperties defaultConfig =  this.getDruidConfig(DruidProperties.druidDefault, DruidProperties.class);

		Configuration configuration = druidConfig.getConfiguration();
		Map<String, MybatisNodeProperties> druidNodeConfigs = druidConfig.getNodes();
		if (druidNodeConfigs == null || druidNodeConfigs.isEmpty())
			throw new RuntimeException("至少需要配置一个DataBase(配置DataBase参数在" + DaoProperties.dbprefix + ".nodes)");
		Iterator<Entry<String, MybatisNodeProperties>> it = this.setPrimary(druidNodeConfigs).entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry< String, MybatisNodeProperties> entry = (Map.Entry< String, MybatisNodeProperties>) it.next();
			String druidNodeName = entry.getKey();
			MybatisNodeProperties druidNodeConfig = entry.getValue();
			try {
				Configuration _configuration;
				if(configuration==null)
					_configuration=new Configuration();
				else
					_configuration=configuration;
				this.registryBean(druidNodeName, druidNodeConfig, defaultConfig, _configuration, registry);
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
	}

	private Map<String, MybatisNodeProperties> setPrimary(Map<String, MybatisNodeProperties> druidNodeConfigs) {
		int primarys = 0;
		MybatisNodeProperties defDruidNode = null;
		for (Entry<String, MybatisNodeProperties> entry : druidNodeConfigs.entrySet()) {
			MybatisNodeProperties druidNode = entry.getValue();
			if (druidNode != null && druidNode.isPrimary()) {
				primarys++;
				if (primarys > 1)
					druidNode.setPrimary(false);
			}
			if (druidNode != null && defDruidNode == null)
				defDruidNode = druidNode;
		}
		if (primarys == 0 && defDruidNode != null)
			defDruidNode.setPrimary(true);
		return druidNodeConfigs;
	}

	private <T> T getDruidConfig(String prefix,Class<T> claz) {
		PropertiesConfigurationFactory<T> factory = new PropertiesConfigurationFactory<T>( claz);
		factory.setPropertySources(environment.getPropertySources());
		factory.setConversionService(environment.getConversionService());
		factory.setIgnoreInvalidFields(false);
		factory.setIgnoreUnknownFields(true);
		factory.setIgnoreNestedProperties(false);
		factory.setTargetName(prefix);
		try {
			factory.bindPropertiesToTarget();
			return factory.getObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	private void registryBean(String druidNodeName, MybatisNodeProperties nodeProperties, DruidProperties druidProperties, Configuration configuration, BeanDefinitionRegistry registry) {
		if (nodeProperties == null)
			return;
		String mapperPackage = nodeProperties.getMapperPackage();
		String typeAliasesPackage=nodeProperties.getTypeAliasesPackage();
		Order order = nodeProperties.getOrder();
		Mapper mappers = nodeProperties.getMapper();
		String basepackage = nodeProperties.getBasePackage();
		if (StringUtils.isEmpty(basepackage)) {
			log.warn("BasePackage为空，db配置异常,当前配置数据源对象的名字{}", druidNodeName);
			basepackage="";
		}
		boolean primary = nodeProperties.isPrimary();
		String dataSourceName = druidNodeName+"DataSource" ;
		String dataSourceMasterName = druidNodeName+"DataSource-Master" ;
		String jdbcTemplateName = druidNodeName+"JdbcTemplate" ;
		String transactionManagerName = druidNodeName;
		String sqlSessionFactoryBeanName = druidNodeName + "RegerSqlSessionFactoryBean";
		String scannerConfigurerName = druidNodeName + "RegerScannerConfigurer";

		AbstractBeanDefinition dataSource = super.createDataSource(nodeProperties, druidProperties, dataSourceName);
		AbstractBeanDefinition dataSourceMaster = super.createDataSourceMaster(dataSourceName);
		AbstractBeanDefinition jdbcTemplate = super.createJdbcTemplate(dataSourceName);
		AbstractBeanDefinition transactionManager = super.createTransactionManager(dataSourceMasterName);

		AbstractBeanDefinition sqlSessionFactoryBean = super.createSqlSessionFactoryBean(dataSourceName, mapperPackage, typeAliasesPackage, configuration);
		AbstractBeanDefinition scannerConfigurer = super.createScannerConfigurerBean(sqlSessionFactoryBeanName, basepackage,mappers, order);

		dataSource.setLazyInit(true);
		dataSource.setPrimary(primary);
		dataSource.setScope(BeanDefinition.SCOPE_SINGLETON);
		dataSourceMaster.setLazyInit(true);
		dataSourceMaster.setScope(BeanDefinition.SCOPE_SINGLETON);
		jdbcTemplate.setLazyInit(true);
		jdbcTemplate.setPrimary(primary);
		jdbcTemplate.setScope(BeanDefinition.SCOPE_SINGLETON);
		transactionManager.setLazyInit(true);
		transactionManager.setPrimary(primary);
		transactionManager.setScope(BeanDefinition.SCOPE_SINGLETON);
		sqlSessionFactoryBean.setLazyInit(true);
		sqlSessionFactoryBean.setPrimary(primary);
		sqlSessionFactoryBean.setScope(BeanDefinition.SCOPE_SINGLETON);
		scannerConfigurer.setLazyInit(true);
		scannerConfigurer.setPrimary(primary);
		scannerConfigurer.setScope(BeanDefinition.SCOPE_SINGLETON);
		
		registry.registerBeanDefinition(dataSourceName, dataSource);
		registry.registerBeanDefinition(dataSourceMasterName, dataSourceMaster);
		registry.registerBeanDefinition(jdbcTemplateName, jdbcTemplate);
		registry.registerBeanDefinition(transactionManagerName, transactionManager);
		registry.registerBeanDefinition(sqlSessionFactoryBeanName, sqlSessionFactoryBean);
		registry.registerBeanDefinition(scannerConfigurerName, scannerConfigurer);
		
		if(primary){
			registry.registerAlias(dataSourceName, "dataSource");
			registry.registerAlias(jdbcTemplateName, "jdbcTemplate");
			registry.registerAlias(transactionManagerName, "transactionManager");
		}
	}

	@Bean
	public DataSourceAspect dataSourceAspect(){
		return new DataSourceAspect();
	}
	
	@Bean
	public DataSourceInvalidRetry dataSourceInvalidRetry(){
		return new DataSourceInvalidRetry();
	}
}
