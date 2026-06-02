// Filtros
let valorFiltro = "", valorStatus = ""
const btn1 = document.getElementById("btn1")
const btn4 = document.getElementById("btn4")
const linkSelected = document.getElementById("link-vendas")
const filtroProduto = document.getElementById("filtro-produto")
const btnLimparFiltro = document.querySelector(".btn-limpar-filtro")
const selectStatus = document.querySelector(".filtro-status")

let page = 0, size = 8;
let vendasCarregadas = [];

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
    vendasCarregadas = vendas.content;
    preencherTabela(vendas.content);
    preencheTotal(vendas.totalElements);
}

// ======================= PREENCHIMENTOS ========================================
function preencherTabela(vendas) {
    const tbody = document.querySelector("#tabela-vendas tbody");
    tbody.innerHTML = "";
    vendas.forEach(item => {
        const linha = `
            <tr>
                <td>${item.idVenda}</td>
                <td>R$ ${item.total.toFixed(2)}</td>
                <td>${item.idVendedor}</td>
                <td>${item.dataVenda}</td>
                 <td>
                    <button class="btn-produtos" onclick="abrirPopup(${item.idVenda})">
                        Produtos
                    </button>
                </td>
            </tr>
        `;
        tbody.insertAdjacentHTML("beforeend", linha);
    });
}

window.abrirPopup = function (idVenda) {
    document.getElementById("popup").style.display = "flex";
    carregarTabelaModal(idVenda);
};

window.fecharPopup = function () {
    document.getElementById("popup").style.display = "none";
};

// ================= RENDERIZAR MODAL =================
function carregarTabelaModal(idVenda) {
    const tbody = document.getElementById("lista-produtos-vendidos-modal");
    tbody.innerHTML = "";

    const venda = vendasCarregadas.find(
        v => v.idVenda === idVenda
    );

    venda.itens.forEach(item => {
        const valor = Number(item.valor || 0);

        const linha = `
            <tr>
                <td>${item.produtoId}</td>
                <td>${item.nomeProduto}</td>
                <td>${item.quantidade}</td>
                <td>R$ ${valor.toFixed(2)}</td>
            </tr>
        `;

        tbody.insertAdjacentHTML("beforeend", linha);
    });
}
//TODO, melhorar essa implementação
function listarData() {
    const dataInicial = document.getElementById("dataInicial").value;
    const dataFinal = document.getElementById("dataFinal").value;

    const vendasFiltradas = vendasCarregadas.filter(venda => {
        const dataVenda = venda.dataVenda;

        return (!dataInicial || dataVenda >= dataInicial) &&
            (!dataFinal || dataVenda <= dataFinal);
    });
    preencherTabela(vendasFiltradas);
}

function limparFiltro(){
    document.getElementById("dataInicial").value = "";
    document.getElementById("dataFinal").value = "";
    carregarVendas();
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