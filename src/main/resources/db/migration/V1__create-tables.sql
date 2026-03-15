-- Tabela de Usuários

CREATE TABLE usuarios (
    id_usuario SERIAL PRIMARY KEY,
    nome VARCHAR(255),
    senha VARCHAR(255),
    email VARCHAR(155) UNIQUE,
    ativo BOOLEAN DEFAULT TRUE,
    cargo VARCHAR(50),

    CONSTRAINT email_formato_invalido
	
    CHECK (email ~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- Tabela de Produtos

CREATE TABLE produtos (
    id_produto SERIAL PRIMARY KEY,
    nome VARCHAR(255),
    valor NUMERIC(10,2)

    CONSTRAINT valor_negativo CHECK (valor >= 0)
);

-- Tabela de estoque

CREATE TABLE estoque (
	id_produto INTEGER PRIMARY KEY,
	quantidade INTEGER,

	FOREIGN KEY (id_produto) REFERENCES produtos(id_produto)
)

-- Tabela de lotes

CREATE TABLE lotes (
	id_lote SERIAL PRIMARY KEY,
	id_produto INTEGER,
	quantidade_recebida INTEGER,
	validade DATE,

	FOREIGN KEY (id_produto) REFERENCES produtos(id_produto)
)

-- Tabela de pontos

CREATE TABLE ponto (
    id_registro SERIAL PRIMARY KEY,
    usuario_id INTEGER,
    data DATE,
    hora_entrada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    hora_saida TIMESTAMP,

    FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario)
);

-- Tabela de vendas

CREATE TABLE vendas (
    id_venda SERIAL PRIMARY KEY,
    id_vendedor INTEGER,
    data_venda DATE,
    total NUMERIC(10,2) DEFAULT 0,

    FOREIGN KEY (id_vendedor) REFERENCES usuarios(id_usuario)
);

-- Tabela relacional de vendas e produtos

CREATE TABLE venda_produto (
	id_produto INTEGER,
    id_venda INTEGER,
    quantidade INTEGER,
    valor NUMERIC(10,2),

    CONSTRAINT quantidade_positiva CHECK (quantidade > 0),

    PRIMARY KEY (id_produto, id_venda),

    FOREIGN KEY (id_produto) REFERENCES produtos(id_produto),
    FOREIGN KEY (id_venda) REFERENCES vendas(id_venda)
);

-- Tabela de log

CREATE TABLE log_produtos (
    id_log SERIAL PRIMARY KEY,
    id_produto INTEGER,
    operacao VARCHAR(10),
    data_operacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valor_antigo NUMERIC(10,2),
    valor_novo NUMERIC(10,2)
);

--Funções e Triggers
-- Função para registrar os dados da alteração do produto

CREATE OR REPLACE FUNCTION LOG_ALTERACAO_PRODUTO()
RETURNS TRIGGER AS $$
BEGIN

INSERT INTO LOG_PRODUTOS (
    ID_PRODUTO,
    OPERACAO,
    VALOR_ANTIGO,
    VALOR_NOVO
)
VALUES (
    OLD.ID_PRODUTO,
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














-- Inserir usuários
INSERT INTO usuarios (nome, senha, email, cargo) VALUES
('Pedro', '123456', 'pedro@email.com', 'vendedor'),
('Maria', 'abcdef', 'maria@email.com', 'gerente');

-- Inserir produtos
INSERT INTO produtos (nome, valor) VALUES
('Notebook', 3500.00),
('Mouse', 80.00),
('Teclado', 150.00);

-- Inserir estoque
INSERT INTO estoque (id_produto, quantidade) VALUES
(1, 10),
(2, 50),
(3, 30);

-- Inserir lotes
INSERT INTO lotes (id_produto, quantidade_recebida, validade) VALUES
(1, 5, '2027-12-31'),
(2, 30, '2028-01-01'),
(3, 20, '2027-06-10');

-- Inserir ponto
INSERT INTO ponto (usuario_id, data, hora_saida) VALUES
(1, '2026-03-15', '2026-03-15 18:00:00'),
(1, '2026-03-14', '2026-03-14 17:55:00'),
(2, '2026-03-15', '2026-03-15 18:20:00'),
(2, '2026-03-14', '2026-03-14 18:10:00');

-- Inserir venda
INSERT INTO vendas (id_vendedor, data_venda) VALUES
(1, CURRENT_DATE);

-- Inserir produtos da venda
INSERT INTO venda_produto (id_produto, id_venda, quantidade, valor) VALUES
(1, 1, 1, 3500.00),
(2, 1, 2, 80.00);

-- Atualizar produto para testar o trigger
UPDATE produtos
SET valor = 3600
WHERE id_produto = 1;

-- Selects para visualização

SELECT * FROM usuarios;
SELECT * FROM produtos;
SELECT * FROM estoque;
SELECT * FROM lotes;
SELECT * FROM ponto;
SELECT * FROM vendas;
SELECT * FROM venda_produto;
SELECT * FROM log_produtos;






