package com.reger.test.user;

import com.reger.datasource.annotation.EnumTypeHandler;

@EnumTypeHandler(field= "intCode")
public enum State {
	delete(-1), disable(0), enable(1);
	
	private final byte byteCode;
	private final short shortCode;
	private final int intCode;

	private State(final int code) {
		this.byteCode = (byte) code;
		this.shortCode=(short) code;
		this.intCode=code;
	}

	public byte byteCode() {
		return byteCode;
	}

	public short shortCode() {
		return shortCode;
	}

	public int intCode() {
		return intCode;
	}

	public boolean equals(final Byte byteCode) {
		if (byteCode == null)
			return false;
		return byteCode.equals(this.byteCode);
	}
	
	public boolean equals(final Short shortCode) {
		if (shortCode == null)
			return false;
		return shortCode.equals(this.shortCode);
	}
	
	public boolean equals(final Integer intCode) {
		if (intCode == null)
			return false;
		return intCode.equals(this.intCode);
	}
	
	@Override
	public String toString() {
		return this.name();
	}
}
