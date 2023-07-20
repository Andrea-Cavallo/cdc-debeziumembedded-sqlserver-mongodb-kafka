package com.demo.cdc.model.enums;

/**
 * Enum to derive the operation performed.
 */
public enum Operation {

	READ("r"), CREATE("c"), UPDATE("u"), DELETE("d");

	private final String code;

	private Operation(String code) {
		this.code = code;
	}

	public String code() {
		return this.code;
	}

	/**
	 * Retrieves the Operation enum value based on the provided code.
	 *
	 * @param code The code representing the Operation.
	 * @return The Operation enum value corresponding to the provided code, or null
	 *         if no matching value is found.
	 */
	public static Operation forCode(String code) {
		Operation[] var1 = values();
		int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			Operation op = var1[var3];
			if (op.code().equalsIgnoreCase(code)) {
				return op;
			}
		}
		return null;
	}
}