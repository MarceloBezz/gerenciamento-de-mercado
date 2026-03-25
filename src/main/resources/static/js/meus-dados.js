const chkEmail = document.getElementById("chkEmail")
const inputEmail = document.getElementById("email")

chkEmail.addEventListener("change", () => {
    if (chkEmail.checked)
        inputEmail.disabled = false;
    else
        inputEmail.disabled = true;
})

setTimeout(() => {
    const alert = document.getElementById("alert-sucesso");
    if (alert) {
        alert.style.opacity = "0";
        setTimeout(() => alert.remove(), 500);
    }
}, 4500);