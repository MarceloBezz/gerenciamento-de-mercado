const btn1 = document.getElementById("btn1")
const linkSelected = document.getElementById("link-estoque")
let page = 0, size = 8;

let mostrandoProdutos = {
    inicio: 1,
    fim: size,
    total: 0
}

document.addEventListener("DOMContentLoaded", async () => {
    await carregarProdutos();
    await carregaResumo();
    btn1.classList.add("pagina-ativa")
    linkSelected.classList.add("active")
});

async function carregaResumo() {
    const resposta = await fetch(`http://localhost:8080/api/financeiro/dashboard/resumo`);
    const resumo = await resposta.json();
    preencheDados(resumo)
}

async function carregarProdutos() {
    const resposta = await fetch(`http://localhost:8080/api/financeiro/dashboard?page=${page}&size=${size}`);
    const produtos = await resposta.json();
    preencherTabela(produtos.content);
}

function preencherTabela(produtos) {
    const tbody = document.querySelector("#tabela-produtos tbody");
    tbody.innerHTML = "";
    produtos.forEach(item => {
        const status = calcularStatus(item);
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
    });
}

function preencheDados(resumo) {
    mostrandoProdutos.total = resumo.totalProdutos;

    document.getElementById("valor-totalProdutos").textContent = resumo.totalProdutos
    document.getElementById("valor-estoque-baixo").textContent = resumo.estoqueAbaixo
    document.getElementById("valor-vencidos").textContent = resumo.vencidos
    document.getElementById("valor-valor-total").textContent = `R$${resumo.valorTotal.toFixed(2)}`
    document.getElementById("texto-mostrando-produtos").textContent = `Mostrando ${mostrandoProdutos.inicio} - ${mostrandoProdutos.fim} de ${mostrandoProdutos.total} produtos`
    let restoDivisao = mostrandoProdutos.total % size
    if (restoDivisao === 0) document.getElementById("btn4").textContent = Math.floor(mostrandoProdutos.total / size)
    else document.getElementById("btn4").textContent = Math.floor(mostrandoProdutos.total / size) + 1
}

function calcularStatus(item) {
    const hoje = new Date();
    const validade = new Date(item.validade);

    if (validade < hoje) return { texto: "Vencido", classe: "vencido" };

    if (item.estoque.quantidade <= item.estoque.quantidadeMinima) return { texto: "Estoque Baixo", classe: "baixo" };

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
                if (i === 3 && (Number(botoes[4].textContent) - Number(botoes[i].textContent) > 1)) {
                    for (let j = 1; j <= 3; j++) {
                        botoes[j].textContent = Number(botoes[j].textContent) + 1
                    }
                    page = Number(botoes[i].textContent) - 1;
                } else {
                    botoes[i].classList.remove("pagina-ativa");
                    botoes[i + 1].classList.add("pagina-ativa");
                    page = Number(botoes[i + 1].textContent) - 1;
                }
                carregarProdutos(page);
                alterarTextoMostrandoProdutos(1)
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
                page = Number(botoes[i - 1].textContent) - 1;
                carregarProdutos(page);
                alterarTextoMostrandoProdutos(-1)
            } else if (i === 1 && (Number(botoes[i].textContent) > 1)) {
                for (let j = 1; j <= 3; j++) {
                    botoes[j].textContent = Number(botoes[j].textContent) - 1
                }
                page = Number(botoes[i].textContent) - 1;
                carregarProdutos(page);
                alterarTextoMostrandoProdutos(-1)
            }
            break;
        }
    }
}

function alterarTextoMostrandoProdutos(valor) {
    mostrandoProdutos.inicio += (size * valor)
    mostrandoProdutos.fim += (size * valor)
    document.getElementById("texto-mostrando-produtos").textContent = `Mostrando ${mostrandoProdutos.inicio} - ${mostrandoProdutos.fim} de ${mostrandoProdutos.total} produtos`
}

function selecionarBotao(botaoSelecionado) {
    const botoes = document.querySelectorAll(".paginacao button")
    botoes.forEach(botao => {
        if (botao.classList.contains("pagina-ativa") && botao.id != botaoSelecionado.id) {
            botao.classList.remove("pagina-ativa")
            botaoSelecionado.classList.add("pagina-ativa")
            page = Number(botaoSelecionado.textContent) - 1;
            carregarProdutos(page);

            const diferenca = Number(botaoSelecionado.textContent) - Number(botao.textContent)
            alterarTextoMostrandoProdutos(diferenca)
        }
    })
}