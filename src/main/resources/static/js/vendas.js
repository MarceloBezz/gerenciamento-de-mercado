// Filtros
let valorFiltro = "", valorStatus = ""
const btn1 = document.getElementById("btn1")
const btn4 = document.getElementById("btn4")
const linkSelected = document.getElementById("link-vendas")
const filtroProduto = document.getElementById("filtro-produto")
const btnLimparFiltro = document.querySelector(".btn-limpar-filtro")
const selectStatus = document.querySelector(".filtro-status")

let page = 0, size = 8;

let mostrandoVendas = {
    inicio: 1,
    fim: size,
    total: 0
}


// ======================= BOTÕES INFERIORES ================================
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
                carregarVendas();
                alterarTextoMostrandoVendas(1)
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
                carregarVendas();
                alterarTextoMostrandoVendas(-1)
            } else if (i === 1 && (Number(botoes[i].textContent) > 1)) {
                for (let j = 1; j <= 3; j++) {
                    botoes[j].textContent = Number(botoes[j].textContent) - 1
                }
                page = Number(botoes[i].textContent) - 1;
                carregarVendas();
                alterarTextoMostrandoVendas(-1)
            }
            break;
        }
    }
}

function selecionarBotao(botaoSelecionado) {
    const botoes = document.querySelectorAll(".paginacao button")
    botoes.forEach(botao => {
        if (botao.classList.contains("pagina-ativa") && botao.id != botaoSelecionado.id) {
            botao.classList.remove("pagina-ativa")
            botaoSelecionado.classList.add("pagina-ativa")
            page = Number(botaoSelecionado.textContent) - 1;
            carregarVendas();

            const diferenca = Number(botaoSelecionado.textContent) - Number(botao.textContent)
            alterarTextoMostrandoVendas(diferenca)
        }
    })
}

async function carregarVendas() {
    let url = `http://localhost:8080/api/vendas?page=${page}&size=${size}`
    const resposta = await fetch(url);
    const vendas = await resposta.json();
    preencherTabela(vendas.content);
    preencheTotal(vendas.totalElements);
}

/* async function carregaResumoVendas() {
    const resposta = await fetch(`http://localhost:8080/api/vendas/dashboard`);
    const resumo = await resposta.json();
    preencheTotal(resumo)
} */

// ======================= PREENCHIMENTOS ========================================
function preencherTabela(vendas) {
    const tbody = document.querySelector("#tabela-vendas tbody");
    tbody.innerHTML = "";
    vendas.forEach(item => {
        //TODO, implementar tela pop-up com produtos da venda
        const linha = `
            <tr>
                <td>${item.idVenda}</td>
                <td>${item.total}</td>
                <td>${item.idVendedor}</td>
                <td>${item.dataVenda}</td>
            </tr>
        `;
        tbody.insertAdjacentHTML("beforeend", linha);
    });
}

function preencheTotal(resumo) {
    mostrandoVendas.total = resumo;
    document.getElementById("texto-mostrando-vendas").textContent = `Mostrando ${mostrandoVendas.inicio} - ${mostrandoVendas.fim} de ${mostrandoVendas.total} vendas`
    let restoDivisao = mostrandoVendas.total % size
    if (restoDivisao === 0) document.getElementById("btn4").textContent = Math.floor(mostrandoVendas.total / size)
    else document.getElementById("btn4").textContent = Math.floor(mostrandoVendas.total / size) + 1
}

function alterarTextoMostrandoVendas(valor) {
    mostrandoVendas.inicio += (size * valor)
    mostrandoVendas.fim += (size * valor)
    document.getElementById("texto-mostrando-vendas").textContent = `Mostrando ${mostrandoVendas.inicio} - ${mostrandoVendas.fim} de ${mostrandoVendas.total} vendas`
}

document.addEventListener("DOMContentLoaded", () => {
    carregarVendas();
    btn1.classList.add("pagina-ativa");
    linkSelected.classList.add("active");
});