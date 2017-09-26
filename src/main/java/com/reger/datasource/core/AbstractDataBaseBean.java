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

import com.github.pagehelper.PageInterceptor;
import com.reger.datasource.properties.MybatisNodeProperties;
import com.reger.datasource.properties.DruidProperties;

import tk.mybatis.spring.mapper.MapperScannerConfigurer;

public abstract class AbstractDataBaseBean{

	static Logger log = LoggerFactory.getLogger(AbstractDataBaseBean.class);

	public final  AbstractBeanDefinition createDataSource(MybatisNodeProperties druidNodeConfig,DruidProperties defaultConfig ,String dataSourceName) {
		Assert.notNull(druidNodeConfig,String.format("DynamicDataSource 未初始化 ,dataSourceName=%s,失败原因: 配置参数为空,你的配置可能存在问题!", dataSourceName+""));
		BeanDefinitionBuilder definitionBuilder= BeanDefinitionBuilder.genericBeanDefinition(DynamicDataSource.class);
		definitionBuilder.addConstructorArgValue(druidNodeConfig);
		definitionBuilder.addConstructorArgValue(defaultConfig);
		definitionBuilder.addConstructorArgValue(dataSourceName);
		definitionBuilder.setInitMethodName("init");
		definitionBuilder.setDestroyMethodName("close");
		return definitionBuilder.getRawBeanDefinition();
	}

	public AbstractBeanDefinition createDataSourceMaster(String dataSourceName) {
		return BeanDefinitionBuilder.
			genericBeanDefinition(DynamicDataSource.class)
			.setFactoryMethodOnBean("masterDataSource", dataSourceName)
			.getRawBeanDefinition();
	}
	
	public final  AbstractBeanDefinition createTransactionManager(String dataSourceName) {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(DataSourceTransactionManager.class);
		bdb.addConstructorArgReference(dataSourceName);
		return bdb.getRawBeanDefinition(); 
	}
	
	public final  AbstractBeanDefinition createJdbcTemplate(String dataSourceName) {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(JdbcTemplate.class);
		bdb.addConstructorArgReference(dataSourceName);
		return bdb.getRawBeanDefinition(); 
	}
	 
	private final Configuration configuration(Configuration configuration){
		configuration.getTypeHandlerRegistry().setDefaultEnumTypeHandler(GlobalEnumTypeHandler.class);
		return configuration;
	}
	
	public final AbstractBeanDefinition createSqlSessionFactoryBean(String dataSourceName,String mapperPackage,String typeAliasesPackage,Configuration configuration) {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(SqlSessionFactoryBean.class);
		bdb.addPropertyValue("configuration", this.configuration(configuration));
		bdb.addPropertyValue("failFast", true); 
		bdb.addPropertyValue("typeAliases", this.saenTypeAliases(typeAliasesPackage));
		bdb.addPropertyReference("dataSource", dataSourceName);
		PageInterceptor pageInterceptor=new PageInterceptor();
		Properties p=new Properties();
		pageInterceptor.setProperties(p);
		bdb.addPropertyValue("plugins", new Interceptor[]{pageInterceptor});
		if(!StringUtils.isEmpty(mapperPackage))
			try {
				String mapperPackagePath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX+ ClassUtils.convertClassNameToResourcePath(new StandardEnvironment().resolveRequiredPlaceholders(mapperPackage)) + "/*.xml";
				bdb.addPropertyValue("mapperLocations", new PathMatchingResourcePatternResolver().getResources(mapperPackagePath));
			} catch (Exception e) {
				log.error("初始化失败",e);
				Assert.isTrue( false,String.format("SqlSessionFactory 初始化失败  mapperPackage=%s",mapperPackage+""));
			}
		return bdb.getBeanDefinition();
	}	
	
	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";  
    private static final String SEPERATOR = ","; 
    private Class<?>[] saenTypeAliases(String typeAliasesPackage) {
		ResourcePatternResolver resolver =  new PathMatchingResourcePatternResolver();  
	    MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);  
	    
	    String[] aliasList = typeAliasesPackage.split(SEPERATOR); 
	    List<Class<?>> result = new ArrayList<Class<?>>();  
	    for(String alias:aliasList){
	    	if(alias==null||alias.isEmpty())
	    		continue;
	    	alias = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(alias) + "/" + DEFAULT_RESOURCE_PATTERN;  
	        try {
	            Resource[] resources =  resolver.getResources(alias);  
	            if(resources != null && resources.length > 0){  
	                MetadataReader metadataReader = null;  
	                for(Resource resource : resources){  
	                    if(resource.isReadable()){  
	                       metadataReader =  metadataReaderFactory.getMetadataReader(resource);  
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
    
	public final AbstractBeanDefinition createScannerConfigurerBean(String sqlSessionFactoryName,String basepackage,Mapper mappers,Order order){
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
		Properties properties=new Properties();
		properties.setProperty("notEmpty", "true");
		properties.setProperty("ORDER",  order!=null ?order.order:Order.BEFORE.order);
		properties.setProperty("mappers",mappers!=null?mappers.mapper: Mapper.DEFAULT.mapper);
		bdb.addPropertyValue("properties", properties);
		bdb.addPropertyValue("sqlSessionFactoryBeanName", sqlSessionFactoryName);
		bdb.addPropertyValue("basePackage", basepackage);
		return bdb.getRawBeanDefinition();
	}

}
