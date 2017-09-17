package com.reger.test.user.model;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;

import com.reger.test.user.State;


@Table(name = "USER")
public class User implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
    private String id;
	private String name;
	private State state;
	private String description;
    public User() {
	}

 
	public User(String id, String name, State state, String description) {
		super();
		this.id = id;
		this.name = name;
		this.state = state;
		this.description = description;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", state=" + state + ", description=" + description + "]";
	}
    
}