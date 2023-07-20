package com.demo.consumerapp.conf.mongodb;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class LocalDateTimeReadConverter implements Converter<Date, OffsetDateTime> {

	@Override
	public OffsetDateTime convert(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime();
	}
}
