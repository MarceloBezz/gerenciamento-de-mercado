const btn1 = document.getElementById("btn1")
const linkSelected = document.getElementById("link-estoque")
let totalProdutos = 0, estoqueBaixo = 0, vencidos = 0, valorTotal = 0;
document.addEventListener("DOMContentLoaded", async () => {
    carregarProdutos();
    btn1.classList.add("pagina-ativa")
    linkSelected.classList.add("active")
});

async function carregarProdutos() {
    // simulando resposta da API
    const produtos = [
        {
            estoque: {
                produto: {
                    id: 1,
                    nome: "Arroz 5kg",
                    valor: 25.90,
                    descricao: "Arroz da marca X"
                },
                quantidade: 50,
                quantidadeMinima: 10,
            },
            validade: "2026-12-30"
        },
        {
            estoque: {
                produto: {
                    id: 2,
                    nome: "Feijão 1kg",
                    valor: 9.50,
                    descricao: "Feijão da marca abc"
                },
                quantidade: 5,
                quantidadeMinima: 10,
            },
            validade: "2027-08-15"
        },
        {
            estoque: {
                produto: {
                    id: 3,
                    nome: "Leite 1L",
                    valor: 4.50,
                    descricao: "Leite da Piracanjuba"
                },
                quantidade: 20,
                quantidadeMinima: 15,
            },
            validade: "2024-05-10"
        },
        {
            estoque: {
                produto: {
                    id: 4,
                    nome: "Açúcar 2kg",
                    valor: 8.00,
                    descricao: "Açúcar da marca felicidade"
                },
                quantidade: 100,
                quantidadeMinima: 20,
            },
            validade: "2026-11-22"
        },
        {
            estoque: {
                produto: {
                    id: 5,
                    nome: "Óleo 900ml",
                    valor: 6.20,
                    descricao: "OLÉÉÉÉÉ"
                },
                quantidade: 8,
                quantidadeMinima: 10,
            },
            validade: "2025-10-05"
        }
    ];
    preencherTabela(produtos);
    // const resposta = await fetch("http://localhost:8080/api/financeiro/dashboard");
    // const produtos = await resposta.json();
    // console.log(produtos);

    preencherTabela(produtos);
}

function preencherTabela(produtos) {
    const tbody = document.querySelector("#tabela-produtos tbody");
    tbody.innerHTML = "";
    totalProdutos = produtos.length;
    produtos.forEach(item => {
        const status = calcularStatus(item);
        valorTotal += item.estoque.produto.valor;
        const linha = `
            <tr>
                <td>${item.estoque.produto.id}</td>
                <td>${item.estoque.produto.nome}</td>
                <td>${item.estoque.quantidade}</td>
                <td>${item.estoque.produto.descricao}</td>
                <td>${item.estoque.quantidadeMinima}</td>
                <td>R$ ${item.estoque.produto.valor.toFixed(2)}</td>
                <td>${formatarData(item.validade)}</td>
                <td><span class="status ${status.classe}">${status.texto}</span></td>
                <td class="acoes">✏️ 🗑️</td>
            </tr>
        `;

        tbody.insertAdjacentHTML("beforeend", linha);
        preencheDados();
    });
}

function preencheDados() {
    document.getElementById("valor-totalProdutos").textContent = totalProdutos
    document.getElementById("valor-estoque-baixo").textContent = estoqueBaixo
    document.getElementById("valor-vencidos").textContent = vencidos
    document.getElementById("valor-valor-total").textContent = `R$${valorTotal.toFixed(2)}`
}

function calcularStatus(item) {

    const hoje = new Date();
    const validade = new Date(item.validade);

    if (validade < hoje) {
        vencidos++;
        return { texto: "Vencido", classe: "vencido" };
    }

    if (item.estoque.quantidade <= item.estoque.quantidadeMinima) {
        estoqueBaixo++;
        return { texto: "Estoque Baixo", classe: "baixo" };
    }

    return { texto: "Normal", classe: "normal" };
}

function formatarData(dataISO) {
    const data = new Date(dataISO);
    return data.toLocaleDateString("pt-BR");
}

function moverAtivoMais() {
    const botoes = document.querySelectorAll(".paginacao button");

    for (let i = 1; i < botoes.length; i++) {
        if (botoes[i].classList.contains("pagina-ativa")) {
            if (i < botoes.length - 2) {
                botoes[i].classList.remove("pagina-ativa");
                botoes[i + 1].classList.add("pagina-ativa");
            }
            break;
        }
    }
}

function moverAtivoMenos() {
    const botoes = document.querySelectorAll(".paginacao button");

    for (let i = botoes.length - 1; i >= 1; i--) {
        if (botoes[i].classList.contains("pagina-ativa")) {
            if (i > 1) {
                botoes[i].classList.remove("pagina-ativa");
                botoes[i - 1].classList.add("pagina-ativa");
            }
            break;
        }
    }
}