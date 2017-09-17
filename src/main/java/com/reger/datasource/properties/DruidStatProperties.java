package com.reger.datasource.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(DruidStatProperties.druidStat)
public class DruidStatProperties {
	
	public final static String druidStat="spring.druid.stat";
	
	/**
	 * 是否启用druid监控界面
	 */
	private boolean enable;
	/**
	 * # IP白名单 (没有配置或者为空，则允许所有访问)
	 */
	private String  allow;
	/**
	 * # IP黑名单 (存在共同时，deny优先于allow)
	 */
	private String deny;
	/**
	 * #禁用HTML页面上的“Reset All”功能
	 */
	private boolean resetEnable;
	/**
	 * : '*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*' # WebStatFilter忽略资源
	 */
	private String  exclusions;
	/**
	 * # 用户名
	 */
	private String loginUsername;
	/**
	 * # 密码
	 */
	private String loginPassword;
	
	
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public String getAllow() {
		return allow;
	}
	public void setAllow(String allow) {
		this.allow = allow;
	}
	public String getDeny() {
		return deny;
	}
	public void setDeny(String deny) {
		this.deny = deny;
	}
	public boolean getResetEnable() {
		return resetEnable;
	}
	public void setResetEnable(boolean resetEnable) {
		this.resetEnable = resetEnable;
	}
	public String getExclusions() {
		return exclusions;
	}
	public void setExclusions(String exclusions) {
		this.exclusions = exclusions;
	}
	public String getLoginUsername() {
		return loginUsername;
	}
	public void setLoginUsername(String loginUsername) {
		this.loginUsername = loginUsername;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
}
