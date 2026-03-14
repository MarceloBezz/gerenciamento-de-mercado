function toggleUserMenu(){
    const menu = document.getElementById("userDropdown");
    menu.classList.toggle("show");
}

window.onclick = function(event){
    if(!event.target.closest('.user-menu')){
        document.getElementById("userDropdown").classList.remove("show");
    }
}