const pageHistory = [];


document.addEventListener("DOMContentLoaded", function () {
    attachLogoClickListener();
    loadDynamicContent('/home/default-fragment');

    document.getElementById('showInboxPage').addEventListener("click", () => {
        loadDynamicContent('/home/messages-inbox-fragment');
    });

    document.getElementById('settingsDropdownItem').addEventListener("click", () => {
        loadDynamicContent('/home/settings-fragment');
    });

    document.getElementById('inboxMessageDashButtonItem').addEventListener("click", () => {
        loadDynamicContent('/home/messages-inbox-fragment');
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
    document.getElementById('homeDashButtonItem').addEventListener("click", () => {
        loadDynamicContent('/home/default-fragment');
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
            const totalPages = document.querySelector("#totalPagesSpan").textContent;
            const currentPage = document.querySelector("#currentPageSpan").textContent;
            console.log("total:" + totalPages + " current:"+ currentPage);
            handlePreviousOutboxButtonClick(currentPage, totalPages);

        } else if (buttonId === "nextOutboxPageButton") {
            const currentPage = document.querySelector("#currentPageSpan").textContent;
            const totalPages = document.querySelector("#totalPagesSpan").textContent;
            handleNextOutboxButtonClick(currentPage, totalPages);
            console.log("nextOutboxPageButton");
        }
        else if (buttonId === "previousInboxPageButton") {
            console.log("previousInboxPageButton");
            const totalPages = document.querySelector("#totalPagesSpan").textContent;
            const currentPage = document.querySelector("#currentPageSpan").textContent;
            console.log("total:" + totalPages + " current:"+ currentPage + " prevInbosPageButton");
            handlePreviousInboxButtonClick(currentPage, totalPages);

        } else if (buttonId === "nextInboxPageButton") {
            const currentPage = document.querySelector("#currentPageSpan").textContent;
            const totalPages = document.querySelector("#totalPagesSpan").textContent;
            console.log("nextInbosPageButton");
            handleNextInboxButtonClick(currentPage, totalPages);

        }
    }
}

function attachOutboxRowListener() {
    console.log("attachRowListener");
    const clickableRows = document.querySelectorAll('.clickable-row');
    clickableRows.forEach(function (row) {
        row.addEventListener('click', function () {
            handleOutboxRowClick(row);
        });
    });

    function handleOutboxRowClick(row) {
        const messageId = row.dataset.messageId;
        console.log("Clicked on outbox row with message ID: " + messageId);
        loadOutboxMessagesDetailsFragment(messageId);
    }
}

function attachInboxRowListener() {
    console.log("attachRowListener");
    // Add click event listeners to clickable rows
    const clickableRows = document.querySelectorAll('.read-message, .unread-message');
    clickableRows.forEach(function (row) {
        row.addEventListener('click', function () {
            handleInboxRowClick(row);
        });
    });

    function handleInboxRowClick(row) {
        const messageId = row.dataset.messageId;
        console.log("Clicked on inbox row with message ID: " + messageId);
        loadInboxMessagesDetailsFragment(messageId);
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
            attachOutboxRowListener();
            attachInboxRowListener();
            updateUnreadMessagesCount();
        })
        .catch(error => {
            console.error("Error loading dynamic content:", error);
        });
}

function loadOutboxMessagesDetailsFragment(messageId) {
    const endpoint = '/home/messages-outbox-details-fragment';
    const fullEndpoint = messageId ? `${endpoint}?messageId=${messageId}` : endpoint;
    return loadDynamicContent(fullEndpoint);
}

function loadInboxMessagesDetailsFragment(messageId) {
    const endpoint = '/home/messages-inbox-details-fragment';
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

function updateUnreadMessagesCount() {
    console.log("updateMessageCound");
    $.get('/get-unread-messages-count', function (data) {
        // Update the content of the span with the received data
        $('#unreadMessagesCount').text(data);
        $('#unreadMessagesCount2').text(data);

        if (data > 0) {
            $('#unreadMessagesCount').addClass('bg-success');
        } else {
            $('#unreadMessagesCount').removeClass('bg-success');
        }

    });
}

// Call the function when the page loads
$(document).ready(function () {
    updateUnreadMessagesCount();
});

function handlePreviousOutboxButtonClick(currentPage, totalPages) {
    if (currentPage > 0) {
        currentPage--;
        updateOutboxPage(currentPage, totalPages);
    }
}

function handleNextOutboxButtonClick(currentPage, totalPages) {
    if (currentPage < totalPages - 1) {
        currentPage++;
        updateOutboxPage(currentPage, totalPages);
    }
}
function handlePreviousInboxButtonClick(currentPage, totalPages) {
    if (currentPage > 0) {
        currentPage--;
        updateInboxPage(currentPage, totalPages);
    }
}

function handleNextInboxButtonClick(currentPage, totalPages) {
    if (currentPage < totalPages - 1) {
        currentPage++;
        updateInboxPage(currentPage, totalPages);
    }
}
function updateOutboxPage(currentPage, totalPages) {

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
            attachOutboxRowListener();
        }

    });
}

function updateInboxPage(currentPage, totalPages) {

    // Perform an AJAX request to retrieve the next or previous page
    console.log(currentPage + " Current Page " + totalPages + " Total Pages at update method");
    $.ajax({
        url: "/home/messages-inbox-fragment?page=" + currentPage,
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
            attachInboxRowListener();
        }

    });
}

function handleFormSubmission(event) {
    event.preventDefault(); // Prevent the form from submitting normally

    // Get the form data
    const formData = new FormData(event.target);

    // Make a request to the server to send the form data
    fetch('/send-message', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Display the customized success notification
                Swal.fire({
                    position: "top",
                    title: 'Success',
                    text: data.message,
                    showConfirmButton: false,
                    timer: 1500,
                    timerProgressBar: true,
                    icon: 'success',
                    color: "#495057",
                    background: "#495057",
                   // footer: '<a href="' + data.redirectUrl + '">Message was sent succesfully</a>'
                }).then(() => {
                    window.location.href = data.redirectUrl;
                });
            } else {
                Swal.fire('Error', data.message, 'error');
            }
        })
        .catch(error => {
            // Display an error message if there's an error with the request
            Swal.fire('Error', 'An error occurred.', 'error');
        });
}