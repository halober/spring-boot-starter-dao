package com.reger.datasource.core;

public enum Order {
	AFTER("after"), BEFORE("before");
	String order;

	private Order(String order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return order;
	}
}