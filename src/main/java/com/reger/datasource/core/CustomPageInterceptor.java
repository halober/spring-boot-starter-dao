package com.reger.datasource.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.github.pagehelper.PageInterceptor;

@Intercepts(
    {
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
    }
)
public class CustomPageInterceptor implements Interceptor {

	private static final Map<Object, PageInterceptor> pageHelpers = new HashMap<Object, PageInterceptor>();
	
	static{
		pageHelpers.put(Dialect.H2, new PageInterceptor());
		pageHelpers.put(Dialect.DB2, new PageInterceptor());
		pageHelpers.put(Dialect.Derby, new PageInterceptor());
		pageHelpers.put(Dialect.Mysql, new PageInterceptor());
		pageHelpers.put(Dialect.SQLite, new PageInterceptor());
		pageHelpers.put(Dialect.Oracle, new PageInterceptor());
		pageHelpers.put(Dialect.Hsqldb, new PageInterceptor());
		pageHelpers.put(Dialect.Phoenix, new PageInterceptor());
		pageHelpers.put(Dialect.MariaDB, new PageInterceptor());
		pageHelpers.put(Dialect.Informix, new PageInterceptor());
		pageHelpers.put(Dialect.SqlServer, new PageInterceptor());
		pageHelpers.put(Dialect.PostgreSQL, new PageInterceptor());
		pageHelpers.put(Dialect.SqlServer2012, new PageInterceptor());
		pageHelpers.put(Dialect.other, new PageInterceptor());
	}

	private final Interceptor pageHelper ;

	public CustomPageInterceptor(Dialect dialect) {
		pageHelper=pageHelpers.get(dialect);
		Properties properties=new Properties();
//		if(dialect!=Dialect.other){
//			properties.setProperty("dialect", dialect.name().toLowerCase());
//		}
		pageHelper.setProperties(properties);
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		return pageHelper.intercept(invocation);
	}

	@Override
	public Object plugin(Object target) {
		return pageHelper.plugin(target);
	}

	@Override
	public void setProperties(Properties properties) {
		pageHelper.setProperties(properties);
	}
}