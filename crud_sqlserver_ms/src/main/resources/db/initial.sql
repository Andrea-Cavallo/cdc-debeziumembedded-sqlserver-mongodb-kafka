IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'outboxshowcase')
BEGIN
    EXEC('CREATE SCHEMA outboxshowcase;');
END

CREATE TABLE outboxshowcase.users (
    id BIGINT NOT NULL IDENTITY(1,1),
    name VARCHAR(4096) NOT NULL,
    surname VARCHAR(4096) NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY CLUSTERED (id)
);

CREATE TABLE outboxshowcase.orders (
    id BIGINT NOT NULL IDENTITY(1,1),
    user_id BIGINT NOT NULL,
    CONSTRAINT orders_pkey PRIMARY KEY CLUSTERED (id),
    CONSTRAINT FK_user_order FOREIGN KEY (user_id) REFERENCES tempdb.users(id)
);

CREATE TABLE outboxshowcase.products (
    id BIGINT NOT NULL IDENTITY(1,1),
    name VARCHAR(4096) NOT NULL,
    price FLOAT NOT NULL,
    order_id BIGINT NOT NULL,
    CONSTRAINT products_pkey PRIMARY KEY CLUSTERED (id),
    CONSTRAINT FK_order_product FOREIGN KEY (order_id) REFERENCES tempdb.orders(id)
);

CREATE TABLE outboxshowcase.outbox (
    id BIGINT NOT NULL IDENTITY(1,1),
    aggregate_id BIGINT NOT NULL,
    aggregate_type VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    version INT,
    payload NVARCHAR(MAX) NOT NULL,
    published BIT NOT NULL,
    created_at DATETIME2 NOT NULL,
    CONSTRAINT outbox_pkey PRIMARY KEY CLUSTERED (id)
);



