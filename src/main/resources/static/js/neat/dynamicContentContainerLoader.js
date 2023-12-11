const pageHistory = [];


document.addEventListener("DOMContentLoaded", function () {

    attachEventListeners();
    loadDynamicContent('/home/home-fragment');
});

function attachEventListeners() {
    attachLogoClickListener();
    attachNavigationListeners();
}

function attachNavigationListeners() {
    const navigationItems = [
        ['showInbox', 'showInboxDash'],
        ['settingsDropdown', 'settingsDash'],
        'newMessageDash',
        'sentMessageDash',
        'homeDash'
    ];

    navigationItems.forEach(item => {
        const elements = Array.isArray(item) ? item.map(subItem => document.getElementById(subItem)) : [document.getElementById(item)];

        elements.forEach(element => {
            if (element) {
                element.addEventListener('click', () => {
                    const endpoint = Array.isArray(item) ?
                        `/home/${element.id.replace('Dash', '').toLowerCase()}-fragment` :
                        `/home/${item.replace('Dash', '').toLowerCase()}-fragment`;

                    console.log('Clicked on:', endpoint);
                    loadDynamicContent(endpoint);
                });
            }
        });
    });
}

function attachLogoClickListener() {
    const logoElements = document.querySelectorAll('.sidebar-brand.brand-logo, .sidebar-brand.brand-logo-mini, .navbar-brand.brand-logo-mini');

    logoElements.forEach(element => {
        element.addEventListener('click', function (event) {
            event.preventDefault();
            loadDynamicContent('/home/home-fragment');
        });
    });
}

function attachButtonClickListeners() {
    document.querySelectorAll("#dynamicContentContainer button").forEach(button => {
        button.addEventListener("click", function (event) {
            handleButtonClick(event.target);
        });
    });

    function handleButtonClick(button) {
        const buttonId = button.id;

        if (buttonId === "goBack") {
            console.log("goBack button");
            goBack();
        } else if (buttonId === "previousOutboxPageButton") {
            const totalPages = document.querySelector("#totalPagesSpan").textContent;
            const currentPage = document.querySelector("#currentPageSpan").textContent;
            handlePreviousOutboxButtonClick(currentPage, totalPages);

        } else if (buttonId === "nextOutboxPageButton") {
            const currentPage = document.querySelector("#currentPageSpan").textContent;
            const totalPages = document.querySelector("#totalPagesSpan").textContent;
            handleNextOutboxButtonClick(currentPage, totalPages);

        } else if (buttonId === "previousInboxPageButton") {
            const totalPages = document.querySelector("#totalPagesSpan").textContent;
            const currentPage = document.querySelector("#currentPageSpan").textContent;
            handlePreviousInboxButtonClick(currentPage, totalPages);

        } else if (buttonId === "nextInboxPageButton") {
            const currentPage = document.querySelector("#currentPageSpan").textContent;
            const totalPages = document.querySelector("#totalPagesSpan").textContent;
            handleNextInboxButtonClick(currentPage, totalPages);

        } else if (buttonId === "reply") {
            console.log("reply button");

        } else if (buttonId === "archiveIncomingMessage") {
            const messageId = $('#archiveIncomingMessage').data('message-id');
            showArchiveConfirmation(messageId);
        }

    }
}


function attachRowListener(selector, clickHandler) {
    const clickableRows = document.querySelectorAll(selector);
    console.log("row listener attached to " + clickHandler);
    clickableRows.forEach(row => {
        row.addEventListener('click', () => {
            clickHandler(row);
        });
    });
}





function loadDynamicContent(endpoint) {
    pageHistory.push(endpoint);

    fetch(endpoint)
        .then(response => response.text())
        .then(content => {
            document.getElementById("dynamicContentContainer").innerHTML = content;
            attachButtonClickListeners();
            attachRowListener('.clickable-row, .outbox-row', handleOutboxRowClick);
            attachRowListener('.read-message, .unread-message', handleInboxRowClick);


            updateUnreadMessagesCount();
            // updateUnreadMessagesDropdown();
        })
        .catch(error => {
            console.error("Error loading dynamic content:", error);
        });
}

function handleInboxRowClick(row) {
    const messageId = row.dataset.messageId;
    console.log("Clicked on inbox row with message ID: " + messageId);
    loadInboxMessagesDetailsFragment(messageId);
}

function handleOutboxRowClick(row) {
    const messageId = row.dataset.messageId;
    console.log("Clicked on outbox row with message ID: " + messageId);
    loadOutboxMessagesDetailsFragment(messageId);
}

function loadOutboxMessagesDetailsFragment(messageId) {
    const endpoint = '/home/message-outbox-details-fragment';
    const fullEndpoint = messageId ? `${endpoint}?messageId=${messageId}` : endpoint;
    return loadDynamicContent(fullEndpoint);
}

function loadInboxMessagesDetailsFragment(messageId) {
    const endpoint = '/home/message-inbox-details-fragment';
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

function updateUnreadMessagesDropdown() {
    console.log("updateUnreadMessages dropdown");

    $.get('/get-latest-unread-messages', function (data) {
        console.log("Data received:", data);  // Add this log to check the data

        var messagesContainer = $('#messagesContainer');
        var existingMessages = messagesContainer.find('.preview-container');

        // Check if the content needs an update
        if (true) {
            // Clear the existing messages in the container
            messagesContainer.empty();
            console.log("empty the container");
            // Update the rows with the latest unread messages
            data.forEach(function (message) {
                var row = '<div class="dropdown-item preview-item" data-message-id="' + message.messageId + '">' +
                    '<div class="preview-thumbnail">' +
                    '<img src="/images/faces/face3.jpg" alt="image" class="rounded-circle profile-pic">' +
                    '</div>' +
                    '<div class="preview-item-content">' +
                    '<p class="preview-subject ellipsis mb-1">' + message.title + '</p>' +
                    '<p class="text-muted mb-0">' + message.relativeTime + '</p>' +
                    '</div>' +
                    '</div>';

                messagesContainer.append(row);
            });

            // Attach event listeners after appending new elements
            attachLatestMessagesRowListener();
        }

        // Remove read messages from the container
        data.forEach(function (message) {
            if (message.read) {
                console.log("message :" + message.messageId + " is read and will be deleted");
                messagesContainer.find('[data-message-id="' + message.messageId + '"]').remove();
            }
        });
    });
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
    updateUnreadMessagesDropdown();
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

function showArchiveConfirmation(messageId) {
    // Show SweetAlert2 popup
    Swal.fire({
        position: top,
        title: 'Are you sure?',
        text: 'You won\'t be able to revert this!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, archive it!',
        color: "#495057",
        background: "#495057",
    }).then((result) => {
        if (result.isConfirmed) {
            // If the user confirms, make an AJAX request to the controller endpoint
            console.log("confirmed");
            archiveMessage(messageId);
        }
    });
}

function archiveMessage(messageId) {
    fetch('/home/archive-message/',
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',

            },
            body: JSON.stringify({messageId}),
        })
        .then(response => {
            if (response.ok) {
                console.log('Message archived successfully');
                showSuccessConfirmation();

            } else {
                console.error('Error:', response.status);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function showSuccessConfirmation() {
    Swal.fire({

        title: 'Success!',
        text: 'Message archived successfully.',
        showConfirmButton: false,
        timer: 1500,
        timerProgressBar: true,
        icon: 'success',
        color: "#495057",
        background: "#495057"
    }).then(() => {
        goBack();
    });
}

function attachLatestMessagesRowListener() {
    console.log("attachLatestMessagesRowListener");
    const latestMessagesRows = document.querySelectorAll('.preview-item');
    latestMessagesRows.forEach(function (row) {
        row.addEventListener('click', function () {
            console.log("lates message row click add" + row.id);
            handleLatestMessagesRowClick(row);
        });
    });
}

function handleLatestMessagesRowClick(row) {
    const messageId = row.dataset.messageId;
    console.log(messageId + "message Id");
    loadInboxMessagesDetailsFragment(messageId);
}