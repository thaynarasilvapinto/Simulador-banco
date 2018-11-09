CREATE TABLE IF NOT EXISTS conta (
  id        varchar(255) not null,
  data_hora TIMESTAMP,
  saldo     FLOAT       NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS cliente (
  id        VARCHAR(255) NOT NULL,
  cpf       VARCHAR(255),
  data_hora TIMESTAMP,
  nome      VARCHAR(255),
  conta_id  VARCHAR(255),
  PRIMARY KEY (id),
  FOREIGN KEY (conta_id) REFERENCES conta
);

CREATE TABLE IF NOT EXISTS operacao (
  id_operacao        VARCHAR(255) NOT NULL,
  data_hora_operacao TIMESTAMP,
  tipo_operacao      VARCHAR(255),
  valor_operacao     FLOAT       NOT NULL,
  conta_id_destino   VARCHAR(255),
  conta_id_origem    VARCHAR(255),
  PRIMARY KEY (id_operacao),
  FOREIGN KEY (conta_id_destino) REFERENCES conta,
  FOREIGN KEY (conta_id_origem) REFERENCES conta
);