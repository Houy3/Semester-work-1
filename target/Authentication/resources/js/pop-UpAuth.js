let isView = false

document.getElementById("authBut").addEventListener("click", function () {

    if (isView) {
        document.getElementById("auth").style.display = "none";
    } else {
        document.getElementById("auth").style.display = "block";
    }
    isView = !isView;
})
