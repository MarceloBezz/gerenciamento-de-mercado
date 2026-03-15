-- Tabela de Usuários

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    email VARCHAR(155) UNIQUE NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    cargo VARCHAR(50) NOT NULL,

    CONSTRAINT email_formato_invalido
	
    CHECK (email ~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- Tabela de Produtos

CREATE TABLE produtos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    valor NUMERIC(10,2) NOT NULL,
    descricao VARCHAR(255),

    CONSTRAINT valor_negativo CHECK (valor >= 0)
);

-- Tabela de estoque

CREATE TABLE estoque (
	produto_id INTEGER PRIMARY KEY,
	quantidade INTEGER NOT NULL,

	FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

-- Tabela de lotes

CREATE TABLE lotes (
	id SERIAL PRIMARY KEY,
	produto_id INTEGER,
	quantidade INTEGER,
	validade DATE,

	FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

-- Tabela de pontos

CREATE TABLE ponto (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER,
    data DATE,
    hora_entrada TIME,
    hora_saida TIME,

    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Tabela de vendas

CREATE TABLE vendas (
    id SERIAL PRIMARY KEY,
    vendedor_id INTEGER,
    data_venda DATE,
    total NUMERIC(10,2) DEFAULT 0,

    FOREIGN KEY (vendedor_id) REFERENCES usuarios(id)
);

-- Tabela relacional de vendas e produtos

CREATE TABLE venda_produto (
	produto_id INTEGER,
    venda_id INTEGER,
    quantidade INTEGER,
    valor NUMERIC(10,2),

    CONSTRAINT quantidade_positiva CHECK (quantidade > 0),

    PRIMARY KEY (produto_id, venda_id),

    FOREIGN KEY (produto_id) REFERENCES produtos(id),
    FOREIGN KEY (venda_id) REFERENCES vendas(id)
);

-- Tabela de log

CREATE TABLE log_produtos (
    id SERIAL PRIMARY KEY,
    produto_id INTEGER,
    operacao VARCHAR(10),
    data_operacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valor_antigo NUMERIC(10,2),
    valor_novo NUMERIC(10,2),

    FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

--Funções e Triggers
-- Função para registrar os dados da alteração do produto

CREATE OR REPLACE FUNCTION LOG_ALTERACAO_PRODUTO()
RETURNS TRIGGER AS $$
BEGIN

INSERT INTO LOG_PRODUTOS (
    PRODUTO_ID,
    OPERACAO,
    VALOR_ANTIGO,
    VALOR_NOVO
)
VALUES (
    OLD.ID,
    'UPDATE',
    OLD.VALOR,
    NEW.VALOR
);

RETURN NEW;

END;
$$ LANGUAGE plpgsql;

-- Trigger para registrar log de alteração do produto

CREATE TRIGGER TRIGGER_LOG_PRODUTO
AFTER UPDATE ON PRODUTOS
FOR EACH ROW
EXECUTE FUNCTION LOG_ALTERACAO_PRODUTO();
