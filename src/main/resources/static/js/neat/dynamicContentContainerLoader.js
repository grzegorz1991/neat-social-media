const pageHistory = [];


document.addEventListener("DOMContentLoaded", function () {
    attachLogoClickListener();
    loadDynamicContent('/home/default-fragment');

    document.getElementById('showInboxPage').addEventListener("click", () => {
        loadDynamicContent('/home/messages-fragment');
    });

    document.getElementById('settingsDropdownItem').addEventListener("click", () => {
        loadDynamicContent('/home/settings-fragment');
    });

    document.getElementById('messageDashButtonItem').addEventListener("click", () => {
        loadDynamicContent('/home/messages-fragment');
    });

    document.getElementById('settingsDashButtonItem').addEventListener("click", () => {
        loadDynamicContent('/home/settings-fragment');
    });

    document.getElementById('newMessageDashButtonItem').addEventListener("click", () => {
        loadDynamicContent('/home/new-message-fragment');
    });

    document.getElementById('sentMessageDashButtonItem').addEventListener("click", () => {
        loadDynamicContent('/home/messages-outbox-fragment');
    });



});

function attachLogoClickListener() {
    const bigLogoElement = document.querySelector('.sidebar-brand.brand-logo');
    const smallLogoElement = document.querySelector('.sidebar-brand.brand-logo-mini');
    if (bigLogoElement) {
        bigLogoElement.addEventListener('click', function (event) {
            event.preventDefault();
            loadDynamicContent('/home/default-fragment');
        });
    }
    if (smallLogoElement) {
        smallLogoElement.addEventListener('click', function (event) {
            event.preventDefault();
            loadDynamicContent('/home/default-fragment');
        });
    }
}

function attachButtonListeners() {
    // Attach button listeners to the dynamically loaded content
    document.querySelectorAll("#dynamicContentContainer button").forEach(button => {
        button.addEventListener("click", function (event) {
            // Handle the button click
            handleButtonClick(event.target);
        });
    });

    // Function to handle button clicks
    function handleButtonClick(button) {
        const buttonId = button.id;

        if (buttonId === "goBack") {
            console.log("goBack button");
            goBack();
        } else if (buttonId === "cancelButton") {
            // Do something when the cancel button is clicked
        }
        //Outbox Message Page buttons
        else if (buttonId === "previousOutboxPageButton") {
            console.log("previousOutboxPageButton");

            var totalPages = document.querySelector("#totalPagesSpan").textContent;
            var currentPage = document.querySelector("#currentPageSpan").textContent;
            console.log("total:" + totalPages + " current:"+ currentPage);

            handlePreviousButtonClick(currentPage, totalPages);

        } else if (buttonId === "nextOutboxPageButton") {
            const currentPage = document.querySelector("#currentPageSpan").textContent;
            const totalPages = document.querySelector("#totalPagesSpan").textContent;
            handleNextButtonClick(currentPage, totalPages);
            console.log("nextOutboxPageButton");
        }
    }
}

function attachRowListener() {
    console.log("attachRowListener");
    // Add click event listeners to clickable rows
    const clickableRows = document.querySelectorAll('.clickable-row');
    clickableRows.forEach(function (row) {
        row.addEventListener('click', function () {
            handleRowClick(row);
        });
    });

    function handleRowClick(row) {
        const messageId = row.dataset.messageId;
        console.log("Clicked on row with message ID: " + messageId);
        loadMessagesDetailsFragment(messageId);
    }
}



function loadDynamicContent(endpoint) {

    // Save the current endpoint to the history
    pageHistory.push(endpoint);

    fetch(endpoint)
        .then(response => response.text())
        .then(content => {
            document.getElementById("dynamicContentContainer").innerHTML = content;
            attachButtonListeners();
            attachRowListener();
        })
        .catch(error => {
            console.error("Error loading dynamic content:", error);
        });
}

function loadMessagesDetailsFragment(messageId) {
    const endpoint = '/home/messages-get-details-fragment';
    const fullEndpoint = messageId ? `${endpoint}?messageId=${messageId}` : endpoint;
    return loadDynamicContent(fullEndpoint);
}

function goBack() {
    if (pageHistory.length > 1) {
        const currentEndpoint = pageHistory.pop();
        const previousEndpoint = pageHistory[pageHistory.length - 1];

        // Check if the current page is the same as the previous page
        if (currentEndpoint !== previousEndpoint) {
            loadDynamicContent(previousEndpoint);
        } else {
            // If the current page is the same as the previous page, pop again
            goBack();
        }
    } else {
        console.log("No previous page in history");
    }
}
function handlePreviousButtonClick(currentPage, totalPages) {

    if (currentPage > 0) {
        currentPage--;
        updatePage(currentPage, totalPages);

    }
}

function handleNextButtonClick(currentPage, totalPages) {
    if (currentPage < totalPages - 1) {
        console.log(currentPage + "current Page next button click before");
        currentPage++;
        console.log(currentPage + "current Page next button click before");
        updatePage(currentPage, totalPages);
    }
}

function updatePage(currentPage, totalPages) {

    // Perform an AJAX request to retrieve the next or previous page
    console.log(currentPage + " Current Page " + totalPages + " Total Pages at update method");
    $.ajax({
        url: "/home/messages-outbox-fragment?page=" + currentPage,
        success: function (data) {

            // Parse the HTML response to extract the table body
            const tableFragment = $(data).find('#messageTableBody');

            // Update the table body with the new messages
            $("#messageTableBody").html(tableFragment.html());
            //
            // Update the spans with the new values
            $('#currentPageSpan').text(currentPage);
            $('#totalPagesSpan').text(totalPages);
            let pageNumber = currentPage + 1;
            //
            $('#currentPageSpan2').text(pageNumber);
            $('#totalPagesSpan2').text(totalPages);
            //
            attachRowListener();
        }

    });
}