package com.demo.utils;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Utils {

	private Utils() {
	}

	public static UUID stringToUUID(String uuidString) {
		try {
			return UUID.fromString(uuidString);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid UUID string: " + uuidString, e);
		}
	}

	public static String stringValue(Object o) {
		return Objects.isNull(o) ? null : o.toString();
	}

	public static Long toLongValue(Object obj) {
		return Optional.ofNullable(obj).filter(Long.class::isInstance).map(Long.class::cast).orElse(0L);
	}

	public static Long convertUUIDToLong(UUID uuid) {
		long mostSignificantBits = uuid.getMostSignificantBits();
		long leastSignificantBits = uuid.getLeastSignificantBits();
		return (mostSignificantBits & Long.MAX_VALUE) | (leastSignificantBits & Long.MAX_VALUE);
	}

}
