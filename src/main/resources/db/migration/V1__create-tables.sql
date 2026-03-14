-- Tabela de Usuários

CREATE TABLE usuario (
    id_usu SERIAL PRIMARY KEY,
    nome VARCHAR(255),
    senha VARCHAR(255),
    email VARCHAR(155) UNIQUE,
    ativo BOOLEAN DEFAULT TRUE,
    cargo VARCHAR(50),

    CONSTRAINT email_formato_invalido
	
    CHECK (email ~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- Tabela de Produtos

CREATE TABLE produto (
    id_prod SERIAL PRIMARY KEY,
    nome VARCHAR(255),
    valor NUMERIC(10,2),
    estoque_saldo INTEGER,
    data_validade DATE,

    CONSTRAINT valor_negativo CHECK (valor >= 0),
    CONSTRAINT saldo_negativo CHECK (estoque_saldo >= 0)
);

-- Tabela de pontos

CREATE TABLE ponto (
    id_ponto SERIAL PRIMARY KEY,
    usuario_id INTEGER,
    data DATE,
    hora_entrada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    hora_saida TIME,

    FOREIGN KEY (usuario_id) REFERENCES usuario(id_usu)
);

-- Tabela de vendas

CREATE TABLE venda (
    id_vend SERIAL PRIMARY KEY,
    id_vendedor INTEGER,
    data DATE,
    total NUMERIC(10,2) DEFAULT 0,

    FOREIGN KEY (id_vendedor) REFERENCES usuario(id_usu)
);

-- Tabela relacional de vendas e produtos

CREATE TABLE venda_produto (
    id_venda_produto SERIAL PRIMARY KEY,
    quantidade INTEGER,
    id_produto INTEGER,
    id_venda INTEGER,
    valor NUMERIC(10,2),

    CONSTRAINT quantidade_positiva CHECK (quantidade > 0),

    FOREIGN KEY (id_produto) REFERENCES produto(id_prod),
    FOREIGN KEY (id_venda) REFERENCES venda(id_vend)
);

-- Tabela de log

CREATE TABLE log_produto (
    id_log SERIAL PRIMARY KEY,
    id_produto INTEGER,
    operacao VARCHAR(10),
    data_operacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valor_antigo NUMERIC(10,2),
    valor_novo NUMERIC(10,2)
);

--Funções e Triggers
-- Função para Atualizar o total da venda

CREATE OR REPLACE FUNCTION ATUALIZAR_TOTAL_VENDA()
RETURNS TRIGGER AS $$
BEGIN

UPDATE VENDA
SET TOTAL = COALESCE(TOTAL,0) + (NEW.QUANTIDADE * NEW.VALOR)
WHERE ID_VEND = NEW.ID_VENDA;

RETURN NEW;

END;
$$ LANGUAGE plpgsql;

-- Trigger para Atualizar o total da venda

CREATE TRIGGER TRIGGER_ATUALIZAR_TOTAL
AFTER INSERT ON VENDA_PRODUTO
FOR EACH ROW
EXECUTE FUNCTION ATUALIZAR_TOTAL_VENDA();

-- Função para baixar estoque

CREATE OR REPLACE FUNCTION BAIXAR_ESTOQUE()
RETURNS TRIGGER AS $$
BEGIN

UPDATE PRODUTO
SET ESTOQUE_SALDO = ESTOQUE_SALDO - NEW.QUANTIDADE
WHERE ID_PROD = NEW.ID_PRODUTO;

RETURN NEW;

END;
$$ LANGUAGE plpgsql;

-- Trigger para baixar estoque

CREATE TRIGGER TRIGGER_BAIXAR_ESTOQUE
AFTER INSERT ON VENDA_PRODUTO
FOR EACH ROW
EXECUTE FUNCTION BAIXAR_ESTOQUE();

-- Função para registrar log de alteração do produto

CREATE OR REPLACE FUNCTION LOG_ALTERACAO_PRODUTO()
RETURNS TRIGGER AS $$
BEGIN

INSERT INTO LOG_PRODUTO (
    ID_PRODUTO,
    OPERACAO,
    VALOR_ANTIGO,
    VALOR_NOVO
)
VALUES (
    OLD.ID_PROD,
    'UPDATE',
    OLD.VALOR,
    NEW.VALOR
);

RETURN NEW;

END;
$$ LANGUAGE plpgsql;

-- Trigger para registrar log de alteração do produto

CREATE TRIGGER TRIGGER_LOG_PRODUTO
AFTER UPDATE ON PRODUTO
FOR EACH ROW
EXECUTE FUNCTION LOG_ALTERACAO_PRODUTO();