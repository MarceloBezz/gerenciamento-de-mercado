INSERT INTO usuarios (nome, senha, email, ativo, cargo) VALUES ( 'Administrador', '$2a$10$SOEsoiPMYBS1H8at6K3Hruj.KiZkmOq3YydnXA86zegbQb1vRgkoi', 'admin@admin.com', true, 'ADMIN');
--ALTER SEQUENCE usuario_id_seq RESTART WITH 2;

-- Inserir produtos
INSERT INTO produtos (nome, valor, descricao) VALUES
('Notebook', 3500.00, 'Notebook gamer'),
('Mouse', 80.00, 'Mouse gamer'),
('Teclado', 150.00, 'Teclado gamer');

-- Inserir estoque
INSERT INTO estoque (produto_id, quantidade, quantidade_minima) VALUES
(1, 10, 5),
(2, 50, 25),
(3, 30, 10);

-- Inserir lotes
INSERT INTO lotes (produto_id, quantidade, validade) VALUES
(1, 5, '2027-12-31'),
(2, 30, '2028-01-01'),
(3, 20, '2027-06-10');

-- Inserir venda
INSERT INTO vendas (vendedor_id, data_venda, total) VALUES
(1, CURRENT_DATE, 3660.00);

-- Inserir produtos da venda
INSERT INTO venda_produto (produto_id, venda_id, quantidade, valor) VALUES
(1, 1, 1, 3500.00),
(2, 1, 2, 80.00);
