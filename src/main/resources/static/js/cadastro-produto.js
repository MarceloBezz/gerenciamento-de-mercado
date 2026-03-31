const campoValor = document.getElementById("preco");
const campoNome = document.getElementById("nome");
const campoDescricao = document.getElementById("descricao");
const btnSalvar = document.getElementById("btn-salvar");
const linkSelected = document.getElementById("link-cadastro-produto")

campoValor.addEventListener("input", () => {
    campoValor.value = campoValor.value.replace(/,/g, ".");
});

document.addEventListener("DOMContentLoaded", () => {
    if (campoNome.value != "") {
        btnSalvar.textContent = "Alterar Produto"
        campoNome.readOnly = true;
        campoDescricao.readOnly = true;
    } 
    linkSelected.classList.add("active")
})

setTimeout(() => {
    const alert = document.getElementById("alert-sucesso");
    if (alert) {
        alert.style.opacity = "0";
        setTimeout(() => alert.remove(), 500);
    }
}, 4500);