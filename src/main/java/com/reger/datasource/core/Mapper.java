package com.reger.datasource.core;

public enum Mapper {
	DEFAULT("tk.mybatis.mapper.common.Mapper"),
	MYSQL("tk.mybatis.mapper.common.MySqlMapper," + DEFAULT.mapper), 
	MSSQL("tk.mybatis.mapper.common.SqlServerMapper," + DEFAULT.mapper);
	String mapper;

	private Mapper(String mapper) {
		this.mapper = mapper;
	}

	@Override
	public String toString() {
		return mapper;
	}
}