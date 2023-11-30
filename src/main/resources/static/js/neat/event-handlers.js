document.addEventListener("DOMContentLoaded", function () {
    console.log("DOM content loaded");

    const dynamicContent = document.getElementById("dynamicContent");

    dynamicContent.addEventListener("click", handleEvent);

    function handleEvent(event) {
        if (event.target.classList.contains("message-item")) {
            event.target.classList.toggle("expanded");
        }
    }
});