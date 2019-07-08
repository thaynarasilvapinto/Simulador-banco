CREATE TABLE IF NOT EXISTS customer (
  id          VARCHAR(60)  NOT NULL PRIMARY KEY,
  name        VARCHAR(100) NOT NULL,
  create_date TIMESTAMP,
  update_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS account (
  id           VARCHAR(60) NOT NULL PRIMARY KEY,
  customer_id  VARCHAR(60),
  balance      NUMERIC,
  account_type VARCHAR(30) CHECK (account_type = 'CURRENT' OR account_type = 'SAVINGS'),
  status       VARCHAR(30) CHECK (status = 'PENDING' OR status = 'CANCELED' OR status = 'ACTIVE'),
  create_date  TIMESTAMP,
  update_date  TIMESTAMP
);

ALTER TABLE account
  ADD CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES account (id);

CREATE TABLE IF NOT EXISTS document (
  id            VARCHAR(60) NOT NULL PRIMARY KEY,
  customer_id   VARCHAR(60),
  number        VARCHAR(50),
  document_type VARCHAR(30) CHECK (document_type = 'RG' OR document_type = 'CPF'),
  create_date   TIMESTAMP,
  update_date   TIMESTAMP
);

ALTER TABLE document
  ADD CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES account (id);

CREATE TABLE IF NOT EXISTS transaction (
  id                     VARCHAR(60) NOT NULL PRIMARY KEY,
  account_origin_id      VARCHAR(50),
  account_destination_id VARCHAR(50),
  value                  NUMERIC     NOT NULL,
  transaction_type       VARCHAR(30) CHECK (
    transaction_type = 'WITHDRAW' OR transaction_type = 'DEPOSIT' OR transaction_type = 'TRANSFER' OR transaction_type =
                                                                                                      'TRANSFER_RECEIVEMENT'),
  status                 VARCHAR(30) CHECK (status = 'PENDING' OR status = 'CANCELED' OR
                                            status = 'ACTIVE') DEFAULT 'PENDING',
  create_date            TIMESTAMP,
  update_date            TIMESTAMP
);

ALTER TABLE transaction
  ADD CONSTRAINT fk_account_origin FOREIGN KEY (account_origin_id) REFERENCES account (id);
ALTER TABLE transaction
  ADD CONSTRAINT fk_account_destination FOREIGN KEY (account_destination_id) REFERENCES account (id);