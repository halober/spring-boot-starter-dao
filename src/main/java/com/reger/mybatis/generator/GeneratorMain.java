package com.reger.mybatis.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;


/**
 * 生成mybatis，model，dao，mapper
 * @author leige
 *
 */
public class GeneratorMain {
	/**
	 * @param args 入参
	 * @throws  Exception 异常
	 */
	public static void main(String[] args) throws Exception {
		ClassPathResource resourceProperties=new ClassPathResource("generator.properties");
		Properties properties= PropertiesLoaderUtils.loadProperties(resourceProperties);

		Assert.hasText(properties.getProperty("package.model"), "mapper的model目录不可以为空，配置项 package.model");
		Assert.hasText(properties.getProperty("package.repo"), "mapper的接口目录不可以为空，配置项 package.repo");
		Assert.hasText(properties.getProperty("package.mapper"), "mapper的xml目录不可以为空，配置项 package.mapper");
		
		Assert.hasText(properties.getProperty("jdbc.driverClassName"), "jdbc的driverClassName不可以为空，配置项jdbc.driverClassName");
		Assert.hasText(properties.getProperty("jdbc.type"), "jdbc的type不可以为空，配置项 jdbc.type");
		Assert.hasText(properties.getProperty("jdbc.username"), "jdbc的username不可以为空，配置项 jdbc.username");
		Assert.hasText(properties.getProperty("jdbc.password"), "jdbc的password不可以为空，配置项 jdbc.password");
		Assert.hasText(properties.getProperty("jdbc.url"), "jdbc的url不可以为空，配置项 jdbc.url");
		
		
		ClassPathResource resource=new ClassPathResource("META-INF/generator/generatorconfig.xml");
		List<String> warnings = new ArrayList<String>();
		ConfigurationParser cp = new ConfigurationParser(properties, warnings);
		Configuration config = cp.parseConfiguration(resource.getInputStream());
		System.err.println("#############################################################################################");
		System.err.println("######################################开始生成##################################################");
		System.err.println("#############################################################################################");
		new MyBatisGenerator(config, new DefaultShellCallback(true), warnings).generate(new VerboseProgressCallback());
		System.err.println("#############################################################################################");
		System.err.println("#######################################生成完毕#################################################");
		System.err.println("#############################################################################################");
	}
}
