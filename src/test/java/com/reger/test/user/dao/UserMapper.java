package com.reger.test.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.reger.test.user.model.User;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface UserMapper extends Mapper<User>,MySqlMapper<User> {
	
	@Select("select * from t_sys_user")
	List<User> findById();

	@Insert("<script>INSERT INTO USER(id)  <foreach collection='ids' item='id' open='VALUES' separator=',' close=' '> ('${id}') </foreach></script>")
	int is(@Param("ids") List<String> ids);
	
	@Insert("<script>INSERT INTO user_num(id)  <foreach collection='ids' item='id' open='VALUES' separator=',' close=' '> (${id}) </foreach></script>")
	int in(@Param("ids") List<Long> ids);
	 
}