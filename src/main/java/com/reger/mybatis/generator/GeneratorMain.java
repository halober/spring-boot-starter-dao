package com.reger.mybatis.generator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.bind.PropertiesConfigurationFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.alibaba.druid.util.JdbcUtils;
import com.reger.datasource.core.Dialect;
import com.reger.datasource.core.Mapper;
import com.reger.datasource.properties.DaoProperties;
import com.reger.datasource.properties.DruidProperties;
import com.reger.datasource.properties.MybatisNodeProperties;

/**
 * 生成mybatis，model，dao，mapper
 * 
 * @author leige
 *
 */
@SpringBootConfiguration
public class GeneratorMain implements CommandLineRunner, EnvironmentAware {
	/**
	 * @param args
	 *            入参
	 * @throws Exception
	 *             异常
	 */
	public static void main(String[] args) throws Exception {
		SpringApplication.run(GeneratorMain.class, args);
	}

	ConfigurableEnvironment environment;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = (ConfigurableEnvironment) environment;
	}

	private <T> T getDruidConfig(String prefix, Class<T> claz) {
		PropertiesConfigurationFactory<T> factory = new PropertiesConfigurationFactory<T>(claz);
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

	private MybatisNodeProperties getNodeBYName(String name) {
		DaoProperties daoProperties = this.getDruidConfig(DaoProperties.dbprefix, DaoProperties.class);
		DruidProperties druidProperties = this.getDruidConfig(DruidProperties.druidDefault, DruidProperties.class);
		Map<String, MybatisNodeProperties> nodes = daoProperties.getNodes();
		Assert.isTrue(nodes.containsKey(name), "节点" + name + "不存在");
		MybatisNodeProperties node = nodes.get(name);
		DruidProperties master = node.getMaster();
		if (master == null) {
			master = new DruidProperties();
			node.setMaster(master);
		}
		master.merge(druidProperties);
		return node;
	}

	@Override
	public void run(String... args) throws Exception {
		Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("generator.properties"));
		String nodeName = properties.getProperty("mybatis.nodeName", null);
		Assert.hasText(nodeName, "节点名不可以为空");
		MybatisNodeProperties node = this.getNodeBYName(nodeName);
		this.buildProperties(properties, node);

		Assert.hasText(properties.getProperty("package.model"), "mapper的model目录不可以为空，配置项 package.model");
		Assert.hasText(properties.getProperty("package.repo"), "mapper的接口目录不可以为空，配置项 package.repo");
		Assert.hasText(properties.getProperty("package.mapper"), "mapper的xml目录不可以为空，配置项 package.mapper");

		this.generate(properties);

	}

	private void buildProperties(Properties properties, MybatisNodeProperties node) throws SQLException {

		DruidProperties master = node.getMaster().defaultEmpty();
		String driverClassName = JdbcUtils.getDriverClassName(master.getUrl());
		String dbtype = JdbcUtils.getDbType(master.getUrl(), driverClassName);
		Dialect dialect = node.getDialect();
		if (null == dialect) {
			dialect = Dialect.valoueOfName(dbtype);
		}
		Mapper mapper = node.getMapper();
		if (mapper == null) {
			mapper = Mapper.valueOfDialect(dialect);
		}
		properties.setProperty("jdbc.url", master.getUrl());
		properties.setProperty("jdbc.username", master.getUsername());
		properties.setProperty("jdbc.password", master.getPassword());
		properties.setProperty("jdbc.driverClassName", driverClassName);
		properties.setProperty("jdbc.type", dbtype);
		properties.setProperty("extends.Mapper", mapper.toString());
		if (!StringUtils.hasText(properties.getProperty("extends.modelClass"))) {
			properties.setProperty("extends.modelClass", Object.class.getName());
		}
		if (!StringUtils.hasText(properties.getProperty("java.delimiter"))) {
			properties.setProperty("java.delimiter", "");
		}
		if (!StringUtils.hasText(properties.getProperty("java.encoding"))) {
			properties.setProperty("java.encoding", "UTF-8");
		}
		if (!StringUtils.hasText(properties.getProperty("package.model"))) {
			properties.setProperty("package.model", this.getFirstPackage(node.getTypeAliasesPackage()));
		}
		if (!StringUtils.hasText(properties.getProperty("package.repo"))) {
			properties.setProperty("package.repo", this.getFirstPackage(node.getBasePackage()));
		}
		if (!StringUtils.hasText(properties.getProperty("package.mapper"))) {
			properties.setProperty("package.mapper", this.getFirstPackage(node.getMapperPackage()));
		}
	}

	private String getFirstPackage(String packageStrs) {
		if (!StringUtils.hasText(packageStrs))
			return "";
		String[] packages = packageStrs.split(",");
		for (String packageStr : packages) {
			packageStr = packageStr.trim();
			if (packageStr.indexOf("*") == -1) {
				return packageStr;
			}
		}
		return "";
	}

	private void generate(Properties properties)throws IOException, XMLParserException, SQLException, InterruptedException, InvalidConfigurationException {
		ClassPathResource resource = new ClassPathResource("META-INF/generator/generatorconfig.xml");
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
