#  Progetto di Simulazione di Cattura da un DB Legacy

Questo progetto simula la necessità di catturare cambiamenti da un database legacy (in questo caso, SQLServer) utilizzando una REST API creata ad hoc. Il cuore di questa simulazione sfrutta il Change Data Capture (CDC), in particolare tramite l'uso di SpringBoot e Debezium Embedded.

## Panoramica
La nostra architettura è strutturata in modo tale che tutti i cambiamenti sulla tabella dbo.outbox vengano catturati e gestiti. In aggiunta, è stato applicato il pattern outbox per garantire l'atomicità delle operazioni e la consistenza dei dati.

## Kafka e Docker Compose
Per simulare l'ambiente distribuito, abbiamo incorporato Kafka e un'interfaccia Kafka UI, entrambi eseguiti attraverso Docker Compose. In questo modo, possiamo simulare facilmente l'invio di messaggi e l'interazione tra i microservizi.
Secondo Microservizio e Kafka Producer

## Primo Microservizio [crud-sqlserver-ms]
Il primo microservizio svolge un ruolo fondamentale all'interno del nostro progetto: simula un database legacy, nel nostro caso SQLServer. Questo si rende possibile attraverso l'implementazione di un modello di dati e delle funzionalità che riflettono quelle di un tipico database SQLServer.
In aggiunta alla simulazione del DB, il primo microservizio applica anche il pattern Outbox. Questo modello di design consente di assicurare l'atomicità delle operazioni e la consistenza dei dati nel contesto di un sistema distribuito.

## Secondo Microservizio [cdc-debezium-ms]
Dopo aver catturato i cambiamenti, il secondo microservizio si occupa di creare un topic attraverso un Kafka Producer. Questo topic viene poi utilizzato per coinvolgere l'ascolto di un terzo microservizio.
Terzo Microservizio e MongoDB

## Terzo Microservizio [consumerapp-ms]
Il terzo microservizio ha il compito di ascoltare il topic creato dal producer. Dopo aver ricevuto i dati, il microservizio esegue alcune logiche (specifiche al caso d'uso) e persiste i dati su MongoDB.



## Autore
Andrea Cavallo
