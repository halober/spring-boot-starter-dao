package com.reger.mybatis.generator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 生成mybatis，model，dao，mapper
 * @author leige
 *
 */
public class GeneratorMain {
	/**
	 * @param args
	 * @throws InvalidConfigurationException
	 * @throws XMLParserException
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public static void main(String[] args) throws InvalidConfigurationException, IOException, XMLParserException, SQLException, InterruptedException {
		ClassPathResource resourceProperties=new ClassPathResource("generator.properties");
		ClassPathResource resource=new ClassPathResource("META-INF/generator/generatorconfig.xml");
		List<String> warnings = new ArrayList<String>();
		ConfigurationParser cp = new ConfigurationParser(PropertiesLoaderUtils.loadProperties(resourceProperties), warnings);
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
