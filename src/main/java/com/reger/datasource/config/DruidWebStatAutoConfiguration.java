package com.reger.datasource.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.reger.datasource.properties.DruidStatProperties;

@Configuration
@EnableConfigurationProperties(DruidStatProperties.class)
@ConditionalOnClass(name="javax.servlet.http.HttpServlet")
@ConditionalOnProperty(name="spring.druid.stat.enable",havingValue="true")
public class DruidWebStatAutoConfiguration {

	Map<String, String> druidStatParameters(DruidStatProperties druidStatConfig) {
		Map<String, String> druidStatParameters=new HashMap<String, String>();
		druidStatParameters.put("allow", druidStatConfig.getAllow());
		druidStatParameters.put("deny", druidStatConfig.getDeny());
		druidStatParameters.put("resetEnable", ""+druidStatConfig.getResetEnable());
		druidStatParameters.put("exclusions", druidStatConfig.getExclusions());
		druidStatParameters.put("loginUsername", druidStatConfig.getLoginUsername());
		druidStatParameters.put("loginPassword", druidStatConfig.getLoginPassword());
		return druidStatParameters;
	}

	@Bean
	ServletRegistrationBean servletRegistration(DruidStatProperties druidStatConfig) {
		ServletRegistrationBean filterRegistration = new ServletRegistrationBean(new StatViewServlet());
		filterRegistration.setAsyncSupported(true);
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlMappings("/druid/*");
		filterRegistration.setInitParameters(druidStatParameters(druidStatConfig));
		return filterRegistration;
	}

	@Bean
	FilterRegistrationBean filterRegistration(DruidStatProperties druidStatConfig) {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean(new WebStatFilter());
		filterRegistration.setAsyncSupported(true);
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns(druidStatConfig.getFilterUrlPatterns());
		filterRegistration.setInitParameters(druidStatParameters(druidStatConfig));
		return filterRegistration;
	}
}
