// Filtros
let valorFiltro = "", valorStatus = ""

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

async function carregarVendas(){
    let url = `http://localhost:8080/api/financeiro/dashboard?page=${page}&size=${size}&produto=${valorFiltro}`
    if(valorStatus !== "") url += `&status=${valorStatus}`
    const resposta = await fetch(url);
    const vendas = await resposta.json();
    preencherTabela(vendas.content);
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