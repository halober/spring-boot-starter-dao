package com.reger.datasource.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;

import com.reger.datasource.annotation.DataSourceChange;
import com.reger.datasource.core.Proxy;
import com.reger.datasource.core.Proxy.SwitchExecute;

/**
 * 注解的方法，调用时会切换到指定的数据源
 *@author leige
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataSourceAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

	@Around(value = "@annotation(com.reger.datasource.annotation.DataSourceChange)", argNames = "point")
	public Object doAround(final ProceedingJoinPoint point) throws Throwable {
		MethodSignature ms = (MethodSignature) point.getSignature();
		Method method = ms.getMethod();

		Class<?> targetClass = point.getTarget().getClass();
		Method targetMethod = targetClass.getMethod(method.getName(), method.getParameterTypes());
		DataSourceChange annotation = AnnotationUtils.findAnnotation(targetMethod, DataSourceChange.class);
		if (annotation == null)
			return point.proceed();
		SwitchExecute<Object> execute= new SwitchExecute<Object>() {
			@Override
			public Object run() throws Throwable {
				return point.proceed();
			}
			 
		};
		if (annotation.slave()) {
			logger.debug("注解到从库执行");
			return Proxy.slave(execute);
		} else {
			logger.debug("注解到主库执行");
			return Proxy.master(execute);
		}
	}

}
