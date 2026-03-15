-- Tabela de Usuários

CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
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
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100),
    descricao VARCHAR(255),
    valor NUMERIC(10,2),

    CONSTRAINT valor_negativo CHECK (valor >= 0)
);

-- Tabela de pontos

CREATE TABLE ponto (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER,
    data DATE,
    hora_entrada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    hora_saida TIMESTAMP,

    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- Tabela de vendas

CREATE TABLE venda (
    id SERIAL PRIMARY KEY,
    id_vendedor INTEGER,
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total NUMERIC(10,2) DEFAULT 0,

    FOREIGN KEY (id_vendedor) REFERENCES usuario(id)
);

-- Tabela relacional de vendas e produtos

CREATE TABLE venda_produto (
    id SERIAL PRIMARY KEY,
    quantidade INTEGER,
    id_produto INTEGER,
    id_venda INTEGER,
    valor NUMERIC(10,2),

    CONSTRAINT quantidade_positiva CHECK (quantidade > 0),

    FOREIGN KEY (id_produto) REFERENCES produto(id),
    FOREIGN KEY (id_venda) REFERENCES venda(id)
);

-- Tabela de log

CREATE TABLE log_produto (
    id SERIAL PRIMARY KEY,
    id_produto INTEGER,
    operacao VARCHAR(10),
    data_operacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valor_antigo NUMERIC(10,2),
    valor_novo NUMERIC(10,2)
);

CREATE TABLE estoque (
    id_produto INTEGER PRIMARY KEY,
    quantidade INTEGER,

    FOREIGN KEY (id_produto) REFERENCES produto(id)
);

CREATE TABLE lote (
    id SERIAL PRIMARY KEY,
    id_produto INTEGER,
    quantidade INTEGER,
    validade DATE,

    FOREIGN KEY (id_produto) REFERENCES produto(id)
);

--Funções e Triggers
-- Função para Atualizar o total da venda

CREATE OR REPLACE FUNCTION ATUALIZAR_TOTAL_VENDA()
RETURNS TRIGGER AS $$
BEGIN

UPDATE VENDA
SET TOTAL = COALESCE(TOTAL,0) + (NEW.QUANTIDADE * NEW.VALOR)
WHERE ID = NEW.ID_VENDA;

RETURN NEW;

END;
$$ LANGUAGE plpgsql;

-- Trigger para Atualizar o total da venda

CREATE TRIGGER TRIGGER_ATUALIZAR_TOTAL
AFTER INSERT ON VENDA_PRODUTO
FOR EACH ROW
EXECUTE FUNCTION ATUALIZAR_TOTAL_VENDA();

-- Função para baixar estoque

CREATE OR REPLACE FUNCTION baixar_estoque()
RETURNS TRIGGER AS $$
BEGIN

UPDATE estoque
SET quantidade = quantidade - NEW.quantidade
WHERE id_produto = NEW.id_produto;

RETURN NEW;

END;
$$ LANGUAGE plpgsql;

-- Trigger para baixar estoque

CREATE TRIGGER trigger_baixar_estoque
AFTER INSERT ON venda_produto
FOR EACH ROW
EXECUTE FUNCTION baixar_estoque();

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
AFTER UPDATE ON PRODUTO
FOR EACH ROW
EXECUTE FUNCTION LOG_ALTERACAO_PRODUTO();