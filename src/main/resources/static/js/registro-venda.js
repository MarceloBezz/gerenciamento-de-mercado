let produtosEstoque = [];
    let itensVenda = [];
    let totalVenda = 0;
    let page = 0;
    let size = 10;
    let valorFiltro = "";
    let valorStatus = "";

    // ================= MODAL =================
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

        console.log("API:", dados); // DEBUG

        produtosEstoque = dados.content || [];

        renderizarProdutosModal(produtosEstoque);

      } catch (error) {
        console.error("Erro ao carregar produtos:", error);
      }
    }

    // ================= RENDERIZAR MODAL =================
    function renderizarProdutosModal(lista) {
  const tbody = document.getElementById("lista-produtos-modal");
  tbody.innerHTML = "";

  if (!lista || lista.length === 0) {
    tbody.innerHTML = `<tr><td colspan="6">Nenhum produto encontrado</td></tr>`;
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

        <!-- MOSTRAR ESTOQUE -->
        <td>${estoque}</td>

        <!-- INPUT COM LIMITE -->
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
          <button onclick="adicionarNaCompra(${produto.id}, \`${produto.nome}\`, ${valor}, ${estoque})">
            Adicionar
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

  // BLOQUEIO ABSOLUTO
  if (totalDesejado > estoque) {
    alert(`Estoque insuficiente!\nDisponível: ${estoque}\nJá na venda: ${totalAtual}`);
    return;
  }

  if (itemExistente) {
    itemExistente.quantidade += quantidade;
  } else {
    itensVenda.push({
      id,
      nome,
      valor,
      quantidade,
      estoque // salva estoque também
    });
  }

  atualizarTabelaVenda();
  fecharPopup();
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
          >
        </td>
        <td>R$ ${subtotal.toFixed(2)}</td>
      </tr>
    `;

    tbody.insertAdjacentHTML("beforeend", linha);
  });

  document.getElementById("valor-total").textContent =
    `R$ ${totalVenda.toFixed(2)}`;
}
function editarQuantidade(id, novaQtd) {
  const item = itensVenda.find(i => i.id === id);
  const qtd = parseInt(novaQtd);

  if (qtd > item.estoque) {
    alert("Quantidade maior que o estoque!");
    atualizarTabelaVenda();
    return;
  }

  item.quantidade = qtd;
  atualizarTabelaVenda();
}

function proximaPagina() {
  page++;
  carregarProdutosModal();
}

function paginaAnterior() {
  if (page > 0) {
    page--;
    carregarProdutosModal();
  }
}
function atualizarBotoesPaginacao() {
  const btnPrev = document.getElementById("btn-prev");
  const btnNext = document.getElementById("btn-next");

  btnPrev.disabled = page === 0;
  btnNext.disabled = page >= totalPages - 1;
}