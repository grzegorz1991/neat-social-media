document.addEventListener("DOMContentLoaded", function () {
    console.log("DOM content loaded");

    const dynamicContent = document.getElementById("dynamicContent");
    const messageItems = dynamicContent.getElementsByClassName("message-item");

    console.log("Number of elements found:", messageItems.length);

    if (messageItems.length > 0) {
        dynamicContent.addEventListener("mouseenter", handleEvent);
        dynamicContent.addEventListener("click", handleEvent);
    } else {
        console.error("No elements found with the class '.message-item'");
    }

    function handleEvent(event) {
        if (event.target.classList.contains("message-item")) {
            event.target.classList.toggle("expanded");
        }
    }


    });



document.addEventListener("DOMContentLoaded", function() {
    // Your existing code here

    document.getElementById('nextButton').addEventListener('click', () => {
        currentPage++;
        console.log("next button");
        getMessages(currentPage);
    });
});