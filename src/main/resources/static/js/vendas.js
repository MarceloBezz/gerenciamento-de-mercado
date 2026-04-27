// Filtros
let valorFiltro = "", valorStatus = ""
const btn1 = document.getElementById("btn1")
const btn4 = document.getElementById("btn4")
const linkSelected = document.getElementById("link-estoque")
const filtroProduto = document.getElementById("filtro-produto")
const btnLimparFiltro = document.querySelector(".btn-limpar-filtro")
const selectStatus = document.querySelector(".filtro-status")





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
                carregarVendas();
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
            carregarVendas();

            const diferenca = Number(botaoSelecionado.textContent) - Number(botao.textContent)
            alterarTextoMostrandoProdutos(diferenca)
        }
    })
}

async function carregarVendas(){
    //TODO, implementar a busca de vendas
    let url = `http://localhost:8080/api/financeiro/dashboard?page=${page}&size=${size}&produto=${valorFiltro}`
    if(valorStatus !== "") url += `&status=${valorStatus}`
    const resposta = await fetch(url);
    const vendas = await resposta.json();
    preencherTabela(vendas.content);
}

// ======================= PREENCHIMENTOS ========================================
function preencherTabela(vendas) {
    const tbody = document.querySelector("#tabela-vendas tbody");
    tbody.innerHTML = "";
    produtos.forEach(item => {
        const linhaStatus = `
    <td class="status-container">
        ${status.map(s => `<span class="status ${s.classe}">${s.texto}</span>`).join("")}
    </td>
`;
        const linha = `
            <tr>
                <td>${venda.id}</td>
                <td>${venda.valor}</td>
                <td>${venda.vendedor}</td>
                <td>${venda.data}</td>
            </tr>
        `;
        tbody.insertAdjacentHTML("beforeend", linha);
    });
}