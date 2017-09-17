package com.reger.test.user;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import com.reger.test.user.dao.UserMapper;
import com.reger.test.user.model.User;

@SpringBootApplication
public class DataSourceMasterSlaveSwapApplication implements CommandLineRunner {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(DataSourceMasterSlaveSwapApplication.class, args);
	}

	@Autowired
	private UserMapper mapper;
	@Autowired@Qualifier("userJdbcTemplate")
	JdbcTemplate userJdbcTemplate;

	@Override
	public void run(String... args) throws Exception {
		 for (int i = 0; i < 30; i++) {
			String id = UUID.randomUUID().toString().replace("-", "");
			State state=State.enable;
			String name= UUID.randomUUID().toString();
			String description= UUID.randomUUID().toString();
			User record=new User(id, name, state, description);
			mapper.insertSelective(record);
			}
		 
		mapper.selectAll().forEach( System.err::println);
	}

}
