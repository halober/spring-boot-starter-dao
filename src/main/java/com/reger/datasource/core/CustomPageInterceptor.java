package com.reger.datasource.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.PageInterceptor;

@Intercepts(
    {
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
    }
)
public class CustomPageInterceptor implements Interceptor {
	
	private static final Logger log = LoggerFactory.getLogger(CustomPageInterceptor.class);

	private static final Map<Dialect, PageInterceptor> pageHelpers = new HashMap<Dialect, PageInterceptor>();
	
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
		Iterator<Entry<Dialect, PageInterceptor>> it = pageHelpers.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Dialect, PageInterceptor> entry = (Entry<Dialect, PageInterceptor>) it.next();
			Properties properties=new Properties();
			if(entry.getKey()!=Dialect.other){
				properties.setProperty("helperDialect", entry.getKey().name());
			}
			entry.getValue().setProperties(properties);
		}
	}

	private final Interceptor pageHelper ;
	private final Dialect dialect;
	public CustomPageInterceptor(Dialect dialect) {
		this.dialect=dialect;
		this.pageHelper=pageHelpers.get(dialect);
		Properties properties=new Properties();
		if(dialect!=Dialect.other){
			properties.setProperty("helperDialect", dialect.name());
		}
		this.pageHelper.setProperties(properties);
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object object=invocation.getArgs()[0];
		if(object instanceof MappedStatement){
			MappedStatement  statement=(MappedStatement) object;
			Configuration config = statement.getConfiguration();
			DataSource dataSource= config.getEnvironment().getDataSource();
			if(dataSource instanceof DynamicDataSource){
				DynamicDataSource dynamicDataSource=((DynamicDataSource)dataSource); 
				Dialect dialect= dynamicDataSource.getDialect();
				if(pageHelpers.containsKey(dialect)){
					log.debug("PageHelper 将使用{}的....",dialect);
					return pageHelpers.get(dialect).intercept(invocation);
				}else{
					log.debug("PageHelper 将使用默认的({})的....",this.dialect);
				}
			}else{
				log.debug("PageHelper 将使用默认的({})的....",this.dialect);
			}
		}else{
			log.debug("PageHelper 将使用默认的({})的....",this.dialect);
		}
		return pageHelper.intercept(invocation);
	}

	@Override
	public Object plugin(Object target) {  
		if (target instanceof Executor) {
	        return Plugin.wrap(target, this);
	    } else {
	        return target;
	    }
	}

	@Override
	public void setProperties(Properties properties) {
		pageHelper.setProperties(properties);
	}
}