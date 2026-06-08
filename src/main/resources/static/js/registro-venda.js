let produtosEstoque = [];
let itensVenda = [];
let totalVenda = 0;
let page = 0;
let size = 10;
let totalPages = 0;
let valorFiltro = "";
let valorStatus = "";
let mostrandoProdutos = {
    inicio: 1,
    fim: size,
    total: 0
};


const linkSelected = document.getElementById("link-registro-venda");

document.addEventListener("DOMContentLoaded", () => {
    linkSelected.classList.add("active");
});

// ================= MODAL =================
document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("btn1").classList.add("pagina-ativa");
}); 

window.abrirPopup = function () {
  document.getElementById("popup").style.display = "flex";
  carregarProdutosModal();
};

window.fecharPopup = function () {
  document.getElementById("popup").style.display = "none";
};

window.onclick = function (event) {
  const modal = document.getElementById("popup");
  if (event.target === modal) fecharPopup();
};

// ================= BUSCAR PRODUTOS =================
async function carregarProdutosModal() {
  try {
    let url = `http://localhost:8080/api/financeiro/dashboard?page=${page}&size=${size}&produto=${valorFiltro}`;

    if (valorStatus !== "") {
      url += `&status=${valorStatus}`;
    }

    const response = await fetch(url);

    if (!response.ok) {
      throw new Error("Erro HTTP: " + response.status);
    }

    const dados = await response.json();
    produtosEstoque = dados.content || [];
    totalPages = dados.totalPages || 0;

    mostrandoProdutos.total = dados.totalElements || 0;

    renderizarProdutosModal(produtosEstoque);
    atualizarBotoesPaginacao();

  } catch (error) {
    console.error("Erro ao carregar produtos:", error);
  }
  

}


// ================= RENDERIZAR MODAL =================
function renderizarProdutosModal(lista) {
  const tbody = document.getElementById("lista-produtos-modal");
  tbody.innerHTML = "";

  if (!lista || lista.length === 0) {
    tbody.innerHTML = `<tr><td colspan="6" style="text-align: center;">Nenhum produto encontrado</td></tr>`;
    return;
  }

  lista.forEach(item => {
    const produto = item?.estoque?.produto;
    const estoque = item?.estoque?.quantidade || 0;

    if (!produto) return;

    const valor = Number(produto.valor || 0);

    const linha = `
      <tr>
        <td>${produto.id}</td>
        <td>${produto.nome}</td>
        <td>R$ ${valor.toFixed(2)}</td>
        <td>${estoque}</td>
        <td>
          <input 
            type="number" 
            min="1" 
            max="${estoque}" 
            value="1" 
            id="qtd-${produto.id}" 
            style="width:60px;"
          >
        </td>
        <td>
          <button class="btn-adicionar" onclick="adicionarNaCompra(${produto.id}, \`${produto.nome}\`, ${valor}, ${estoque})">
            + Adicionar
          </button>
        </td>
      </tr>
    `;

    tbody.insertAdjacentHTML("beforeend", linha);
  });
}

// ================= FILTRO =================
window.filtrarProdutos = function () {
  valorFiltro = document.getElementById("filtroProduto").value;
  page = 0;
  carregarProdutosModal();
};

document.getElementById("filtroProduto")
.addEventListener('keydown', (event) =>{
    if(event.key === "Enter"){
        filtrarProdutos();
        
    }
});

window.limparFiltro = function(){
    document.getElementById("filtroProduto").valeu = "";
    valorFiltro = "";
    page = 0;
    carregarProdutosModal();
}


// ================= ADICIONAR VENDA =================
window.adicionarNaCompra = function (id, nome, valor, estoque) {
  const inputQtd = document.getElementById(`qtd-${id}`);
  let quantidade = parseInt(inputQtd.value) || 1;

  if (quantidade <= 0) {
    alert("Quantidade inválida!");
    return;
  }

  const itemExistente = itensVenda.find(item => item.id === id);
  let totalAtual = itemExistente ? itemExistente.quantidade : 0;
  let totalDesejado = totalAtual + quantidade;

  if (totalDesejado > estoque) {
    alert(`Estoque insuficiente!\nDisponível: ${estoque}\nJá na venda: ${totalAtual}`);
    return;
  }

  if (itemExistente) {
    itemExistente.quantidade += quantidade;
  } else {
    itensVenda.push({ id, nome, valor, quantidade, estoque });
  }

  atualizarTabelaVenda();
  
};

// ================= TABELA VENDA =================
function atualizarTabelaVenda() {
  const tbody = document.getElementById("itens-venda");
  tbody.innerHTML = "";
  totalVenda = 0;

  itensVenda.forEach(item => {
    const subtotal = item.valor * item.quantidade;
    totalVenda += subtotal;

    const linha = `
      <tr>
        <td>${item.nome}</td>
        <td>
          <input 
            type="number" 
            value="${item.quantidade}" 
            min="1" 
            max="${item.estoque || 999}"
            onchange="editarQuantidade(${item.id}, this.value)"
            style="width:60px;"
          >
        </td>
        <td>R$ ${subtotal.toFixed(2)}</td>
      </tr>
    `;

    tbody.insertAdjacentHTML("beforeend", linha);
  });

  document.getElementById("valor-total").textContent = `R$ ${totalVenda.toFixed(2)}`;
}

window.editarQuantidade = function(id, novaQtd) {
  const item = itensVenda.find(i => i.id === id);
  const qtd = parseInt(novaQtd);

  if (qtd > item.estoque) {
    alert("Quantidade maior que o estoque!");
    atualizarTabelaVenda(); 
    return;
  }
  
  if (qtd <= 0) {
      itensVenda = itensVenda.filter(i => i.id !== id);
  } else {
      item.quantidade = qtd;
  }
  
  atualizarTabelaVenda();
};

// ================= PAGINAÇÃO =================
function atualizarBotoesPaginacao() {
    const btn4 = document.getElementById("btn4");

    mostrandoProdutos.total = totalPages * size;

    btn4.textContent = totalPages;

    document.getElementById("texto-mostrando-produtos").textContent =
        `Mostrando ${mostrandoProdutos.inicio} - ${mostrandoProdutos.fim} de ${mostrandoProdutos.total} produtos`;
}
function alterarTextoMostrandoProdutos(valor) {
    mostrandoProdutos.inicio += size * valor;
    mostrandoProdutos.fim += size * valor;

    if (mostrandoProdutos.inicio < 1)
        mostrandoProdutos.inicio = 1;

    if (mostrandoProdutos.fim > mostrandoProdutos.total)
        mostrandoProdutos.fim = mostrandoProdutos.total;

    document.getElementById("texto-mostrando-produtos").textContent =
        `Mostrando ${mostrandoProdutos.inicio} - ${mostrandoProdutos.fim} de ${mostrandoProdutos.total} produtos`;
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
                carregarProdutosModal();
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
                carregarProdutosModal();
                alterarTextoMostrandoProdutos(-1)
            } else if (i === 1 && (Number(botoes[i].textContent) > 1)) {
                for (let j = 1; j <= 3; j++) {
                    botoes[j].textContent = Number(botoes[j].textContent) - 1
                }
                page = Number(botoes[i].textContent) - 1;
                carregarProdutosModal();
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
            carregarProdutosModal();
            const diferenca = Number(botaoSelecionado.textContent) - Number(botao.textContent)
            alterarTextoMostrandoProdutos(diferenca)
        }
    })
}

// ================= FINALIZAR VENDA =================
window.finalizarVenda = async function () {
  if (itensVenda.length === 0) {
    alert("O carrinho está vazio!");
    return;
  }

  const dtos = itensVenda.map(item => ({
    idProduto: item.id,
    quantidade: item.quantidade
  }));

  try {
    const csrfToken = document.querySelector("meta[name='_csrf']")?.content;
    const csrfHeader = document.querySelector("meta[name='_csrf_header']")?.content;

    const headers = { "Content-Type": "application/json" };
    if (csrfToken && csrfHeader) headers[csrfHeader] = csrfToken;

    const response = await fetch("http://localhost:8080/api/vendas", {
      method: "POST",
      headers: headers,
      body: JSON.stringify(dtos, )
    });

    if (response.ok) {
      alert("Venda realizada com sucesso!");
      itensVenda = [];
      atualizarTabelaVenda();
    } else {
      const erro = await response.text();
      alert("Erro: " + erro);
    }
  } catch (error) {
    alert("Erro de comunicação com o servidor.");
  }
};