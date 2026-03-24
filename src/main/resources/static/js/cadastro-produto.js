const campoValor = document.getElementById("preco");

campoValor.addEventListener("input", () => {
    campoValor.value = campoValor.value.replace(/,/g, ".");
});