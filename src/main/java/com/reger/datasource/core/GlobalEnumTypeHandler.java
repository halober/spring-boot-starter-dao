package com.reger.datasource.core;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reger.datasource.annotation.EnumTypeHandler;

public class GlobalEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {
	private final Logger logger = LoggerFactory.getLogger("mybatis.EnumTypeHandler");
	
	private final Class<E> type;
	private final static Set<String> defSET = new HashSet<>(Arrays.asList("name", "ordinal"));
	
	private final static String defNull="DEF.FIELD.NULL.ENUM";
	/**
	 * 缓存枚举与值得对应
	 */
	private final Map<E, Object> mapEnum = new ConcurrentHashMap<>();
	/**
	 * 保存值于枚举的对应
	 */
	private final Map<Object, E> enumMap = new ConcurrentHashMap<>();
	/**
	 * 缓存值转字符串与枚举的对应
	 */
	private final Map<String, E> strEnumMap = new ConcurrentHashMap<>();
	/**
	 * 缓存枚举名字与枚举的对应
	 */
	private final Map<String, E> nameEnumMap = new ConcurrentHashMap<>();

	public GlobalEnumTypeHandler(Class<E> type) {
		if (type == null) {
			throw new IllegalArgumentException("Type argument cannot be null");
		}
		this.type = type;
		Field field = getEnumTypeHandlerField();
		if (field != null) {
			this.initMapEnum(field);
		} else {
			this.initMapEnum();
		}
	}
	
	/**
	 * 初始化没有注解的枚举类
	 */
	private void initMapEnum() {
		for (E _enum : this.type.getEnumConstants()) {
			mapEnum.put(_enum, _enum.name());
			nameEnumMap.put(_enum.name(), _enum);
		}
	}
	
	/**
	 * 初始化有注解的枚举类
	 * @param field
	 */
	private void initMapEnum(Field field) {
		boolean isAccessible = field.isAccessible();
		if (!isAccessible)
			field.setAccessible(true);
		for (E _enum : this.type.getEnumConstants()) {
			Object object = getField(field, _enum);
			if(object==null)
				object=defNull;
			logger.debug("mybatis将把枚举{}.{}转化为{}存储", type.getName(), _enum, object);
			mapEnum.put(_enum, object);
			if (enumMap.containsKey(object))
				logger.warn("请检查枚举类{},枚举项{}和枚举项{}定义了完全一样的标识{}", type.getName(), _enum, enumMap.get(object), object);
			enumMap.put(object, _enum);
			strEnumMap.put(String.valueOf(object), _enum);
			nameEnumMap.put(_enum.name(), _enum);
		}
		if (!isAccessible)
			field.setAccessible(isAccessible);
	}

	/**
	 * 获取枚举的注解指定的参数
	 * @return
	 */
	private Field getEnumTypeHandlerField() {
		EnumTypeHandler handler = type.getAnnotation(EnumTypeHandler.class);
		if (handler == null) {
			return null;
		}
		Field field = null;
		String fieldName = handler.field();
		try {
			field = type.getDeclaredField(fieldName);
		} catch (NoSuchFieldException | SecurityException e) {
			if (defSET.contains(fieldName)) {
				try {
					field = Enum.class.getDeclaredField(fieldName);
				} catch (NoSuchFieldException | SecurityException e1) {
					logger.warn("加载枚举{}信息失败", type.getName(), e);
				}
			} else {
				logger.warn("加载枚举{}信息失败", type.getName(), e);
			}
		}
		return field;
	}
	
	/**
	 * 根据数据库返回值得到枚举
	 * @param object
	 * @return
	 */
	private E findObject(Object object) {
		if (object == null){
			return enumMap.get(defNull);
		}
		if (enumMap.containsKey(object)){
			return enumMap.get(object);
		}
		String objectStr = String.valueOf(object);
		if (nameEnumMap.containsKey(objectStr)){
			return nameEnumMap.get(objectStr);
		}
		if (strEnumMap.containsKey(objectStr)){
			return strEnumMap.get(objectStr);
		}
		logger.error("数据{}转化为枚举{}时没有找到匹配的枚举", object,type.getName());
		return null;
	}
 
	/**
	 * 根据加有的注解返回枚举的参数
	 * @param field
	 * @param _enum
	 * @return
	 */
	private Object getField(Field field, E _enum) {
		try {
			return field.get(_enum);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			logger.warn("取枚举{}.{}的field:{}失败", type.getName(), _enum, field.getName(), e);
		}
		return _enum.name();
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		if (jdbcType == null) {
			ps.setObject(i, mapEnum.get(parameter));
		} else {
			ps.setObject(i, mapEnum.get(parameter), jdbcType.TYPE_CODE); // see
																			// r3589
		}
	}

	@Override
	public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
		Object s = rs.getObject(columnName);
		return this.findObject(s);
	}

	@Override
	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		Object s = rs.getObject(columnIndex);
		return this.findObject(s);
	}

	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		Object s = cs.getObject(columnIndex);
		return this.findObject(s);
	}
 
}