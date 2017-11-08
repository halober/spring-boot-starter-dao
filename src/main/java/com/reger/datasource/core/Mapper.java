package com.reger.datasource.core;

public enum Mapper {
	DEFAULT(tk.mybatis.mapper.common.Mapper.class.getName()),
	MYSQL(tk.mybatis.mapper.common.MySqlMapper.class.getName()+"," + DEFAULT.mapper), 
	MSSQL(tk.mybatis.mapper.common.SqlServerMapper.class.getName()+"," + DEFAULT.mapper);
	
	String mapper;

	private Mapper(String mapper) {
		this.mapper = mapper;
	}

	@Override
	public String toString() {
		return mapper;
	}
	public static final Mapper valueOfDialect(Dialect dialect) {
		switch (dialect) {
			case Mysql:
				return Mapper.MYSQL;
			case SqlServer:
			case SqlServer2012:
				return Mapper.MYSQL;
			default:
				return Mapper.DEFAULT;
		}
	}
}