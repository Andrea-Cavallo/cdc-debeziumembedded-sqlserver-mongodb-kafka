package com.demo.cdc.conf.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		// Elenco degli indirizzi dei broker
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
		// Classe di serializzazione per le chiavi
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		// Classe di serializzazione per i valori
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		// Conferme del produttore (acks). Impostare a 'all' significa che il produttore
		// riceverà
		// una conferma solo quando tutte le repliche del broker saranno sincronizzate
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		// Numero di tentativi di ripetizione in caso di fallimento dell'invio del
		// messaggio
		props.put(ProducerConfig.RETRIES_CONFIG, 10);
		// Ridurre il numero di richieste in volo (concurrenti) a meno di 1
		// Questo impedisce la possibilità di perdere messaggi in caso di riordinamento
		// dei messaggi inviati.
		props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
		// Abilita l'idempotenza per garantire che un messaggio non venga mai duplicato
		// nel topic
		// a causa di tentativi di ripetizione.
		props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
		// Tipo di compressione. 'snappy' è uno schema di compressione che fornisce una
		// buona velocità
		// e un buon rapporto di compressione, ed è spesso preferito per i dati Kafka.
		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
		// Dimensione del batch. La dimensione del lotto inviato dal produttore al
		// broker.
		// Un valore più grande potrebbe migliorare le prestazioni, ma aumenterà anche
		// la latenza.
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 32 * 1024); // 32 KB batch size
		// Linger, ritardo massimo prima che il messaggio venga inviato.
		// Questo può permettere di inviare più messaggi in un unico batch, migliorando
		// l'efficienza.
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);

		return props;
	}

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
}
