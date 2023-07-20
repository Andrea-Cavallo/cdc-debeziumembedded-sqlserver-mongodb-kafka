package com.demo.cdc.transcode;

import static com.demo.cdc.utils.Constants.EMPTY;
import static com.demo.cdc.utils.Constants.THE_TRANSCODED_OUTBOX_ENTITY_IS;

import org.springframework.stereotype.Component;

import com.demo.cdc.model.Outbox;
import com.demo.cdc.model.User;
import com.demo.cdc.model.UtenteIta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Transcoder {

	private final ObjectMapper objectMapper;

	public Transcoder(ObjectMapper objectMapper) {
		super();
		this.objectMapper = objectMapper;
	}

	public Outbox transcode(Outbox outbox) throws JsonProcessingException {
		var utenteIta = new UtenteIta();
		var outboxTranscoded = getUtenteItaSerialized(utenteIta, outbox);
		log.info(THE_TRANSCODED_OUTBOX_ENTITY_IS, outboxTranscoded);

		return outboxTranscoded;
	}

	private Outbox getUtenteItaSerialized(UtenteIta utenteIta, Outbox outboxRead) throws JsonProcessingException {
		String payload = outboxRead.getPayload();
		var user = objectMapper.readValue(payload, User.class);
		utenteIta.setNominativo(
				new StringBuilder().append(user.getName()).append(EMPTY).append(user.getSurname()).toString());
		utenteIta.setId(user.getId().toString());
		String serializedUtenteIta = objectMapper.writeValueAsString(utenteIta);
		outboxRead.setPayload(serializedUtenteIta);

		return outboxRead;
	}

}
