const btn1 = document.getElementById("btn1")
const btn4 = document.getElementById("btn4")
const linkSelected = document.getElementById("link-estoque")
const filtroProduto = document.getElementById("filtro-produto")
const btnLimparFiltro = document.querySelector(".btn-limpar-filtro")
const selectStatus = document.querySelector(".filtro-status")

// Paginação
let page = 0, size = 8;

// Para span de quantos e quais produtos estão sendo mostrados
let mostrandoProdutos = {
    inicio: 1,
    fim: size,
    total: 0
}

// Filtros
let valorFiltro = "", valorStatus = ""

// ======================= EVENTOS ITENS HTML ================================
filtroProduto.addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
        valorFiltro = encodeURIComponent(filtroProduto.value)
        carregarProdutos();
        // if (btn1.classList.contains("pagina-ativa"))
        //     carregarProdutos();
        // else if (btn4.classList.contains("pagina-ativa")) {
        //     selecionarBotao(btn1);
        //     moverAtivoMenos();
        // }
    }
})

btnLimparFiltro.addEventListener("click", () => {
    filtroProduto.textContent = "";
    selectStatus.value = "todos";
    valorFiltro = "";
    valorStatus = "";
    carregarProdutos();
    // if (btn1.classList.contains("pagina-ativa"))
    //     carregarProdutos();
    // else if (btn4.classList.contains("pagina-ativa")) {
    //     selecionarBotao(btn1);
    //     moverAtivoMenos();
    // }
})

selectStatus.addEventListener("change", () => {
    valorStatus = selectStatus.value;
    carregarProdutos();
    // if (btn1.classList.contains("pagina-ativa"))
    //     carregarProdutos();
    // else if (btn4.classList.contains("pagina-ativa")) {
    //     selecionarBotao(btn1);
    //     moverAtivoMenos();
    // }
})

document.addEventListener("DOMContentLoaded", () => {
    carregarProdutos();
    carregaResumo();
    btn1.classList.add("pagina-ativa")
    linkSelected.classList.add("active")
});

// ======================= BUSCA DE DADOS NA API ================================
async function carregaResumo() {
    const resposta = await fetch(`http://localhost:8080/api/financeiro/dashboard/resumo`);
    const resumo = await resposta.json();
    preencheDados(resumo)
}

async function carregarProdutos() {
    let url = `http://localhost:8080/api/financeiro/dashboard?page=${page}&size=${size}&produto=${valorFiltro}`
    if (valorStatus !== "") url += `&status=${valorStatus}`
    const resposta = await fetch(url);
    const produtos = await resposta.json();
    preencherTabela(produtos.content);
}

// ======================= PREENCHIMENTOS ========================================
function preencherTabela(produtos) {
    const tbody = document.querySelector("#tabela-produtos tbody");
    tbody.innerHTML = "";
    produtos.forEach(item => {
        const status = calcularStatus(item);
        const linhaStatus = `
    <td class="status-container">
        ${status.map(s => `<span class="status ${s.classe}">${s.texto}</span>`).join("")}
    </td>
`;
        const linha = `
            <tr>
                <td>${item.estoque.produto.id}</td>
                <td><a href="/produtos/cadastrar?id=${item.estoque.produto.id}" title="Editar produto" class="link-produto">${item.estoque.produto.nome}</a></td>
                <td>${item.estoque.quantidade}</td>
                <td>${item.estoque.produto.descricao}</td>
                <td>${item.estoque.quantidadeMinima}</td>
                <td>R$ ${item.estoque.produto.valor.toFixed(2)}</td>
                <td>${formatarData(item.validade)}</td>
                ${linhaStatus}
                <td class="acoes">
                    <button class="btn-add-lote" title="Adicionar Lote" onclick="abrirModalLote(${item.estoque.produto.id}, '${item.estoque.produto.nome}')">
                        <i class="bi bi-box-seam"></i>
                    </button>
                </td>
            </tr>
        `;
        // <a href="/produtos/${item.estoque.produto.id}" class="btn-editar" title="Editar produto">
        //                 ✏️
        //             </a>
        // <i class="bi bi-pencil"></i>
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

function alterarTextoMostrandoProdutos(valor) {
    mostrandoProdutos.inicio += (size * valor)
    mostrandoProdutos.fim += (size * valor)
    document.getElementById("texto-mostrando-produtos").textContent = `Mostrando ${mostrandoProdutos.inicio} - ${mostrandoProdutos.fim} de ${mostrandoProdutos.total} produtos`
}

// ======================= FUNÇÕES AUXILIARES ================================
function calcularStatus(item) {
    let status = [];
    const hoje = new Date().setHours(0, 0, 0, 0);
    const validade = new Date(item.validade).setHours(0, 0, 0, 0);

    if (validade < hoje)
        status.push({ texto: "Vencido", classe: "vencido" });

    if (item.estoque.quantidade <= item.estoque.quantidadeMinima)
        status.push({ texto: "Estoque Baixo", classe: "baixo" });

    if (status.length === 0)
        status.push({ texto: "Normal", classe: "normal" })

    return status;
}

function formatarData(dataISO) {
    const data = new Date(dataISO);
    return data.toLocaleDateString("pt-BR");
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
                carregarProdutos();
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
                carregarProdutos();
                alterarTextoMostrandoProdutos(-1)
            } else if (i === 1 && (Number(botoes[i].textContent) > 1)) {
                for (let j = 1; j <= 3; j++) {
                    botoes[j].textContent = Number(botoes[j].textContent) - 1
                }
                page = Number(botoes[i].textContent) - 1;
                carregarProdutos();
                alterarTextoMostrandoProdutos(-1)
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
            carregarProdutos();

            const diferenca = Number(botaoSelecionado.textContent) - Number(botao.textContent)
            alterarTextoMostrandoProdutos(diferenca)
        }
    })
}

// ======================= OVERLAY ================================
function abrirModalLote(produtoId, produtoNome) {
    document.getElementById("modal-lote").style.display = "flex";

    document.getElementById("produtoId").value = produtoId;
    document.getElementById("produtoNome").value = produtoNome;
}

function fecharModalLote() {
    document.getElementById("modal-lote").style.display = "none";
}

// fechar clicando fora
window.onclick = function (event) {
    const modal = document.getElementById("modal-lote");
    if (event.target === modal) {
        fecharModalLote();
    }
};

document.getElementById("form-lote").addEventListener("submit", async function (e) {
    e.preventDefault();

    const payload = {
        produtoID: document.getElementById("produtoId").value,
        quantidade: document.getElementById("quantidadeLote").value,
        validade: document.getElementById("validadeLote").value
    };
    const token = document.querySelector('meta[name="_csrf"]').getAttribute("content");
    const header = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");


    const resposta = await fetch("/api/lote", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            [header]: token
        },
        body: JSON.stringify(payload)
    });
    if (!resposta.ok) {
        const erro = await resposta.text();
        alert("Erro ao cadastrar Lote!")
        console.error(erro)
        return;
    }

    alert("Lote cadastrado com sucesso!");
    fecharModalLote();
    carregarProdutos();
    carregaResumo();
});