package com.demo.consumerapp.conf.mongodb;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class LocalDateTimeWriteConverter implements Converter<OffsetDateTime, Date> {

	@Override
	public Date convert(OffsetDateTime date) {
		return Date.from(date.toLocalDateTime().atZone(ZoneId.systemDefault()).toInstant());
	}
}
