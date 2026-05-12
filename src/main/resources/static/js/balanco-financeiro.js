// const opcaoMes = document.getElementById("periodo");
const opcaoSemana = document.getElementById("semana");
const linkSelected = document.getElementById("link-balanco-financeiro");

document.addEventListener("DOMContentLoaded", () => {
    linkSelected.classList.add("active");
})

function bloqueiaOpcao() {
    console.log(opcaoMes.value)
    if (opcaoMes.value === "semanal") {
        opcaoSemana.disabled = false;
    } else {
        opcaoSemana.disabled = true;
    }
}
// opcaoMes.addEventListener("change", bloqueiaOpcao)

// ========================= DADOS =========================
// TODO: GET /api/financeiro/semanal
const dadosEntradasSaidas = {
    labels: ["Semana 1","Semana 2","Semana 3","Semana 4"],
    entradas: [12000,15000,11000,18000],
    saidas: [8000,9000,7000,9500]
};

// TODO: GET /api/financeiro/vendedores
const dadosVendedores = {
    labels: ["João","Maria","Carlos","Ana", "Vínicius"],
    vendas: [25000,18000,15000,22000, 100000]
};

// TODO: GET /api/financeiro/produtos
const dadosProdutos = {
    labels: ["Semana 1","Semana 2","Semana 3","Semana 4"],
    quantidade: [120,150,100,190]
};

// TODO: GET /api/financeiro/lucro
const dadosLucro = {
    labels: ["Semana 1","Semana 2","Semana 3","Semana 4"],
    lucro: [4000,6000,4000,8500]
};

// ========================= GRÁFICOS =========================
// GRÁFICO 1 — Entradas vs Saídas
new Chart(document.getElementById("graficoEntradasSaidas"), {
    type: "bar",
    data: {
        labels: dadosEntradasSaidas.labels,
        datasets: [
            {
                label: "Entradas",
                data: dadosEntradasSaidas.entradas
            },
            {
                label: "Saídas",
                data: dadosEntradasSaidas.saidas
            }
        ]
    },
    options: {
        responsive: true
    }
});

// GRÁFICO 2 — Vendas por vendedor
new Chart(document.getElementById("graficoVendedores"), {
    type: "bar",
    data: {
        labels: dadosVendedores.labels,
        datasets: [
            {
                label: "Faturamento",
                data: dadosVendedores.vendas
            }
        ]
    },
    options: {
        responsive: true
    }
});

// GRÁFICO 3 — Produtos vendidos
new Chart(document.getElementById("graficoProdutos"), {
    type: "bar",
    data: {
        labels: dadosProdutos.labels,
        datasets: [
            {
                label: "Produtos vendidos",
                data: dadosProdutos.quantidade
            }
        ]
    },
    options: {
        responsive: true
    }
});

// GRÁFICO 4 — Lucro mensal
new Chart(document.getElementById("graficoLucro"), {
    type: "line",
    data: {
        labels: dadosLucro.labels,
        datasets: [{
            label: "Lucro líquido",
            data: dadosLucro.lucro,
            tension: 0.3
        }]
    }
});

// GRÁFICO 5 — Participação por vendedor 
new Chart(document.getElementById("graficoParticipacao"), {
    type: "doughnut",
    data: {
        labels: dadosVendedores.labels,
        datasets: [{
            data: dadosVendedores.vendas
        }]
    }
});