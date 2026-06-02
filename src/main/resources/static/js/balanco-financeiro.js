const linkSelected = document.getElementById("link-balanco-financeiro");
const selectMes = document.getElementById("mes");
const inputAno = document.getElementById("ano");
const checkboxFiltrar = document.getElementById("filtrarPor");
const btnFiltrar = document.querySelector(".btn-filtrar");
const totalEntradaEl = document.getElementById("totalEntradas");
const totalSaidaEl = document.getElementById("totalSaidas");
const totalLucroEl = document.getElementById("totalLucro");
const totalProdutosVendidosEl = document.getElementById("totalProdutosVendidos");

let graficoEntradasSaidas;
let graficoVendedores;
let graficoProdutos;
let graficoLucro;
let graficoParticipacao;

document.addEventListener("DOMContentLoaded", () => {
    linkSelected.classList.add("active");
    inicializarGraficos();
    // Por padrão carrega dados do ano atual inteiro (sem parâmetros)
    carregarDadosFinanceiros().catch(error => console.error('Erro ao carregar dados financeiros:', error));

    // Habilita/desabilita controles quando checkbox muda
    checkboxFiltrar?.addEventListener('change', () => {
        const checked = checkboxFiltrar.checked;
        if (selectMes) selectMes.disabled = !checked;
        if (inputAno) inputAno.disabled = !checked;
    });

    btnFiltrar?.addEventListener("click", () => carregarDadosFinanceiros().catch(error => console.error('Erro ao carregar dados financeiros:', error)));
});

async function carregarDadosFinanceiros() {
    // Se o usuário não marcou "Filtrar por:", solicita os dados do ano atual inteiro (sem query string)
    let query = '';
    if (checkboxFiltrar && checkboxFiltrar.checked) {
        const mes = Number(selectMes?.value) || (new Date().getMonth() + 1);
        const ano = Number(inputAno?.value) || (new Date().getFullYear());
        query = `?mes=${mes}&ano=${ano}`;
    }
    const responses = await Promise.allSettled([
        fetchJson(`/api/financeiro/semanal${query}`),
        fetchJson(`/api/financeiro/vendedores${query}`),
        fetchJson(`/api/financeiro/produtos${query}`),
        fetchJson(`/api/financeiro/lucro${query}`)
    ]);

    const [entradasSaidas, vendedores, produtos, lucro] = responses.map(result => result.status === 'fulfilled' ? result.value : null);

    if (entradasSaidas) {
        atualizarGraficoEntradasSaidas(entradasSaidas);
    }

    if (vendedores) {
        atualizarGraficoVendedores(vendedores);
        atualizarGraficoParticipacao(vendedores);
    }

    if (produtos) {
        atualizarGraficoProdutos(produtos);
    }

    if (lucro) {
        atualizarGraficoLucro(lucro);
    }

    preencherCards(entradasSaidas, produtos, lucro);
}

async function fetchJson(url) {
    const response = await fetch(url);
    if (!response.ok) {
        throw new Error(`${response.status} ${response.statusText}`);
    }
    return response.json();
}

function formatCurrency(value) {
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL',
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    }).format(value);
}

function safeSum(values = []) {
    return values.reduce((acc, current) => acc + Number(current || 0), 0);
}

function preencherCards(entradasSaidas, produtos, lucro) {
    if (entradasSaidas && totalEntradaEl && totalSaidaEl) {
        const entradaTotal = safeSum(entradasSaidas.entradas);
        const saidaTotal = safeSum(entradasSaidas.saidas);

        totalEntradaEl.innerHTML = `<strong style="color: #2E7D32">${formatCurrency(entradaTotal)}</strong>`;
        totalSaidaEl.innerHTML = `<strong style="color: #C62828">${formatCurrency(saidaTotal)}</strong>`;
    }

    if (lucro && totalLucroEl) {
        const lucroTotal = safeSum(lucro.lucro);
        totalLucroEl.innerHTML = `<strong style="color: #6A1B9A">${formatCurrency(lucroTotal)}</strong>`;
    }

    if (produtos && totalProdutosVendidosEl) {
        const totalProdutos = safeSum(produtos.quantidade);
        totalProdutosVendidosEl.innerHTML = `<strong style="color: #EF6C00">${totalProdutos}</strong>`;
    }
}

function inicializarGraficos() {
    graficoEntradasSaidas = new Chart(document.getElementById("graficoEntradasSaidas"), {
        type: "bar",
        data: {
            labels: [],
            datasets: [
                { label: "Entradas", data: [], backgroundColor: '#4CAF50' },
                { label: "Saídas", data: [], backgroundColor: '#F44336' }
            ]
        },
        options: { responsive: true }
    });

    graficoVendedores = new Chart(document.getElementById("graficoVendedores"), {
        type: "bar",
        data: { labels: [], datasets: [{ label: "Faturamento", data: [], backgroundColor: '#2196F3' }] },
        options: { responsive: true }
    });

    graficoProdutos = new Chart(document.getElementById("graficoProdutos"), {
        type: "bar",
        data: { labels: [], datasets: [{ label: "Produtos vendidos", data: [], backgroundColor: '#FF9800' }] },
        options: { responsive: true }
    });

    graficoLucro = new Chart(document.getElementById("graficoLucro"), {
        type: "line",
        data: { labels: [], datasets: [{ label: "Lucro líquido", data: [], tension: 0.3, borderColor: '#9C27B0', backgroundColor: 'rgba(156,39,176,0.1)' }] },
        options: { responsive: true }
    });

    graficoParticipacao = new Chart(document.getElementById("graficoParticipacao"), {
        type: "doughnut",
        data: { labels: [], datasets: [{ data: [], backgroundColor: ['#4CAF50', '#2196F3', '#FF9800', '#9C27B0', '#F44336'] }] },
        options: { responsive: true }
    });
}

function atualizarGraficoEntradasSaidas(dados) {
    graficoEntradasSaidas.data.labels = dados.labels;
    graficoEntradasSaidas.data.datasets[0].data = dados.entradas;
    graficoEntradasSaidas.data.datasets[1].data = dados.saidas;
    graficoEntradasSaidas.update();
}

function atualizarGraficoVendedores(dados) {
    graficoVendedores.data.labels = dados.labels;
    graficoVendedores.data.datasets[0].data = dados.vendas;
    graficoVendedores.update();
}

function atualizarGraficoProdutos(dados) {
    graficoProdutos.data.labels = dados.labels;
    graficoProdutos.data.datasets[0].data = dados.quantidade;
    graficoProdutos.update();
}

function atualizarGraficoLucro(dados) {
    graficoLucro.data.labels = dados.labels;
    graficoLucro.data.datasets[0].data = dados.lucro;
    graficoLucro.update();
}

function atualizarGraficoParticipacao(dados) {
    graficoParticipacao.data.labels = dados.labels;
    graficoParticipacao.data.datasets[0].data = dados.vendas;
    graficoParticipacao.update();
}
