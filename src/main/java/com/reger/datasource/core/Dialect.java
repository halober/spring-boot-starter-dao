package com.reger.datasource.core;

public enum Dialect {
	H2,
	DB2,
	Mysql,
	Derby,
	Oracle,
	SQLite,
	Hsqldb,
	MariaDB,
	Phoenix,
	Informix,
	SqlServer,
	PostgreSQL,
	SqlServer2012,
	other
	;
	public final static Dialect valoueOfName(String name) {
		for (Dialect dialect : Dialect.values()) {
			if(name!=null&&dialect.name().toLowerCase().equals(name.toLowerCase())){
				return dialect;
			}
		}
		return Dialect.other;
	}
}
