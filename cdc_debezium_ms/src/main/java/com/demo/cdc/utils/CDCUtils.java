package com.demo.cdc.utils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class CDCUtils {

	private CDCUtils() {
	}

	/**
	 * Retrieves a map value from an object.
	 *
	 * @param obj the object to extract the map value from
	 * @return the map value if the object is of type Map, otherwise an empty map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMapValue(Object obj) {
		return Optional.ofNullable(obj).filter(Map.class::isInstance).map(map -> (Map<String, Object>) map)
				.orElse(Collections.emptyMap());
	}

	/**
	 * Retrieves a string value from an object.
	 *
	 * @param obj the object to extract the string value from
	 * @return the string value if the object is of type String, otherwise an empty
	 *         string
	 */
	public static String toStringValue(Object obj) {
		return Optional.ofNullable(obj).filter(String.class::isInstance).map(String.class::cast).orElse("");
	}

	/**
	 * Retrieves a list of maps from an object.
	 *
	 * @param obj the object to extract the list of maps from
	 * @return the list of maps if the object is of type List, otherwise an empty
	 *         list
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> toListOfMap(Object obj) {
		return Optional.ofNullable(obj).filter(List.class::isInstance).map(list -> (List<Map<String, Object>>) list)
				.orElse(Collections.emptyList());
	}

	/**
	 * Retrieves a long value from an object.
	 *
	 * @param obj the object to extract the long value from
	 * @return the long value if the object is of type Long, otherwise 0L
	 */
	public static Long toLongValue(Object obj) {
		return Optional.ofNullable(obj).filter(Long.class::isInstance).map(Long.class::cast).orElse(0L);
	}

	/**
	 * Retrieves an integer value from an object.
	 *
	 * @param obj the object to extract the integer value from
	 * @return the integer value if the object is of type Integer, otherwise 0
	 */
	public static Integer toIntegerValue(Object obj) {
		return Optional.ofNullable(obj).filter(Integer.class::isInstance).map(Integer.class::cast).orElse(0);
	}

	public static UUID convertToUUID(Object obj) {
		if (obj instanceof UUID) {
			return (UUID) obj;
		} else if (obj instanceof String) {
			try {
				return UUID.fromString((String) obj);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("L'oggetto non può essere convertito in UUID.");
			}
		} else {
			throw new IllegalArgumentException("L'oggetto non può essere convertito in UUID.");
		}
	}

	public static Number convertToNumber(Object obj) {
		if (obj instanceof BigDecimal) {
			return (BigDecimal) obj;
		} else if (obj instanceof Long || obj instanceof Integer || obj instanceof Short || obj instanceof Byte) {
			return BigDecimal.valueOf(((Number) obj).longValue());
		} else {
			throw new IllegalArgumentException(
					"L'oggetto fornito non è un tipo supportato (BigDecimal, Long, Integer, Short, Byte).");
		}
	}
}
