const pageHistory = [];
const API_BASE_URL = '/home';

var users = [];

document.addEventListener("DOMContentLoaded", function () {
    attachEventListeners();
    loadDynamicContent('/home/home-fragment');
});

function attachEventListeners() {
    attachLogoClickListener();
    attachNavigationListeners();
    initializeRecipientTypeahead();
    fetchUserList();
}

function attachNavigationListeners() {
    const navigationItems = [
        ['showInbox', 'showInboxDash'],
        ['settingsDropdown', 'settingsDash'],
        'newMessageDash',
        'sentMessageDash',
        'homeDash',
        'archivedMessageDash',
        'newAcquaintanceDash' ,
        'seeAcquaintanceDash',
        'notificationsDash',
        'notifications'
    ];

    navigationItems.forEach(item => {
        const elements = Array.isArray(item) ? item.map(subItem => document.getElementById(subItem)) : [document.getElementById(item)];
        elements.forEach(element => {
            if (element) {
                element.addEventListener('click', () => {
                    const endpoint = Array.isArray(item) ?
                        `${API_BASE_URL}/${element.id.replace('Dash', '').toLowerCase()}-fragment` :
                        `${API_BASE_URL}/${item.replace('Dash', '').toLowerCase()}-fragment`;
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

function getAcquinted(recipientId) {
    // Assuming your backend endpoint URL
    const endpointUrl = "/sendRequest";

    // Make a POST request to your backend with recipientId
    fetch(endpointUrl, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            recipientId: recipientId,
        }),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.text();
        })
        .then(data => {
            console.log("Request successful:", data);

            // Show SweetAlert confirmation with timeout
            Swal.fire({
                icon: "success",
                title: "Success!",
                text: data,
                timer: 2500, // Set the timeout in milliseconds
                showConfirmButton: false,
                color: "#495057",
                background: "#495057",
            });
        })
        .catch(error => {
            console.error("Error during request:", error);
        });
    updateUnreadNotificationsCount();
    updateUnreadNotificationsDropdown();
}


function handleGetAcquintedButtonClick(recipientId, recipientName) {
        console.log(recipientName);
        Swal.fire({
            title: "Do you want to send a friend request to " + recipientName + "?",
            icon: "question",
            showCancelButton: true,
            confirmButtonText: "Yes",
            cancelButtonText: "No",
            color: "#495057",
            background: "#495057",
        }).then((result) => {
            if (result.isConfirmed) {
                getAcquinted(recipientId);
            } else if (result.isDenied) {               
                
            } else {                
               
            }
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
            var recipientId = $('#reply').data('recipient-id');
            loadDynamicContent(`/home/replyMessage-fragment?reply=${recipientId}`);


        } else if (buttonId === "archiveIncomingMessage") {
            const messageId = $('#archiveIncomingMessage').data('message-id');
            showArchiveConfirmation(messageId , "receiver");

        } else if (buttonId === "archiveOutgoingMessage"){
            const messageId = $('#archiveOutgoingMessage').data('message-id');
            showArchiveConfirmation(messageId, "sender");

        } else if( buttonId === "getAcquinted"){
            var recipientId = $('#getAcquinted').data('acquaintance-id');
            var recipientName = $('#getAcquinted').data('acquaintance-name');
            handleGetAcquintedButtonClick(recipientId, recipientName);
        }
    }
}

function attachRowListener(selector, clickHandler) {
    const clickableRows = document.querySelectorAll(selector);

    clickableRows.forEach(row => {
        // Check if listeners are already attached
        if (row.dataset.listenersAttached === 'true') {
            // Remove existing click event listeners

            row.removeEventListener('click', row.clickHandler);
        }

        row.addEventListener('click', row.clickHandler = () => {
            clickHandler(row);
        });

        // Add a flag to indicate that listeners have been attached
        row.dataset.listenersAttached = 'true';
    });
}


function loadDynamicContent(endpoint) {

    pageHistory.push(endpoint);
   // console.log(endpoint);
    fetch(endpoint)
        .then(response => response.text())
        .then(content => {
            document.getElementById("dynamicContentContainer").innerHTML = content;
            attachButtonClickListeners();
            attachRowListener('.clickable-row, .outbox-row', handleOutboxRowClick);
            attachRowListener('.read-message, .unread-message', handleInboxRowClick);
            attachRowListener('.archive-row', handleArchiveRowClick);
            attachRowListener('.user-row', handleAcquaintancesRowClick);
            fetchUserList();
            updateUnreadMessagesCount();
            updateUnreadNotificationsCount();
            updateUnreadNotificationsDropdown();
            updateUnreadMessagesDropdown();

            filterSearchFunction();
        })
        .catch(error => {
            console.error("Error loading dynamic content:", error);
        });
}

function handleInboxRowClick(row) {
    const messageId = row.dataset.messageId;
    console.log("handleInboxRowClick");
    loadInboxMessagesDetailsFragment(messageId);
}

function handleOutboxRowClick(row) {
   const messageId = row.dataset.messageId;
   loadOutboxMessagesDetailsFragment(messageId);
}
function handleArchiveRowClick(row) {
    const messageId = row.dataset.messageId;
    loadArchivedMessagesDetailsFragment(messageId);
}
function handleAcquaintancesRowClick(row) {
    const userId = row.dataset.userId;
    loadAcquintanceProfileDetailsFragment(userId);
}
function loadAcquintanceProfileDetailsFragment(userId) {
    const endpoint = '/home/acquintanceprofile-details-fragment.html';
    const fullEndpoint = userId ? `${endpoint}?userId=${userId}` : endpoint;
    return loadDynamicContent(fullEndpoint);
}
function loadArchivedMessagesDetailsFragment(messageId) {
    const endpoint = '/home/message-archived-details-fragment';
    const fullEndpoint = messageId ? `${endpoint}?messageId=${messageId}` : endpoint;
    return loadDynamicContent(fullEndpoint);
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
    $.get('/get-latest-unread-messages')
        .done(function (data) {
            // Select the messages container
            var messagesContainer = $('#messageContainer');
            messagesContainer.empty();
            if (data.length > 0) {
                data.forEach(function (message) {
                    var row = `
                        <div class="dropdown-divider"></div>
                        <div class="dropdown-item preview-item preview-message-item" data-message-id="${message.messageId}">
                            
                                <div class="preview-thumbnail">
                                    <img src="${message.sender.imagePath}" alt="imaget" 
                                    class="rounded-circle profile-pic ${(message.sender.isActive) ? 'active' : ''}">
                                </div>
                                <div class="preview-item-content">
                                    <p class="preview-subject ellipsis mb-1">${message.title}</p>
                                    <p class="text-muted mb-0">${message.relativeTime}</p>
                                    
                                </div>
                            
                            <div class="dropdown-divider"></div>
                        </div>`;

                    messagesContainer.append(row);
                });
                attachLatestMessagesRowListener();
            }
            data.forEach(function (message) {
                if (message.read) {
                    messagesContainer.find('[data-message-id="' + message.messageId + '"]').remove();
                }
            });
        })
        .fail(function (error) {
            console.error("Error fetching unread messages:", error);
        });
}



function updateUnreadNotificationsDropdown() {
    $.get('/get-latest-unread-notifications')
        .done(function (data) {

            var notificationsContainer = $('#notificationContainer');
                notificationsContainer.empty();
            if (data.length > 0) {
                data.forEach(function (notification) {
                    var iconClass = getBellIconClass(notification.notificationType);
                    var row = `
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item preview-item preview-notification-item" data-notification-id="${notification.id}">
                            <div class="preview-thumbnail">
                                <div class="preview-icon bg-dark rounded-circle">
                                    <i class="${iconClass}"></i>
                                </div>
                            </div>
                            <div class="preview-item-content">
                                <p class="preview-subject ellipsis mb-1">${notification.message}</p>                                
                            </div>
                            <div class="dropdown-divider"></div>
                        </a>`;

                    notificationsContainer.append(row);
                });

                attachLatestNotificationsRowListener();
            }
        })
        .fail(function (error) {
            console.error("Error fetching unread notifications:", error);
        });
}
function getBellIconClass(notificationType) {
    switch (notificationType) {
        case 'ALERT':
            return 'mdi mdi-alert-circle-outline text-danger';
        case 'INFORMATION':
            return 'mdi mdi-information-outline text-success';
        case 'REQUEST':
            return 'mdi mdi-account-plus text-info';
        default:
            return 'mdi mdi-newspaper text-warning';
    }
}

function updateUnreadMessagesCount() {

    $.get('/get-unread-messages-count', function (data) {


        $('#unreadMessagesCount2').text(data);

        if (data > 0) {
            $('#displayUnreadMessagesCount').text(data);
            $('#unreadMessagesCount').addClass('bg-success');
        } else {
            $('#displayUnreadMessagesCount').text('');
            $('#unreadMessagesCount').removeClass('bg-success');

        }
    });
}

function updateUnreadNotificationsCount() {

    $.get('/get-unread-notifications-count', function (data) {
        if (data > 0) {
            $('#displayUnreadNotificationsCount').text(data);
            $('#unreadNotificationsCount').addClass('bg-danger');
        } else {
            $('#displayUnreadNotificationsCount').text('');
            $('#unreadNotificationsCount').removeClass('bg-danger');
        }
    });
}
$(document).ready(function () {

    updateUnreadMessagesCount();
    updateUnreadMessagesDropdown();
    updateUnreadNotificationsCount();
    updateUnreadNotificationsDropdown();
    const messageDropdown = $('#messageDropdown');
    messageDropdown.on('click', function () {
        updateUnreadMessagesDropdown();
        updateUnreadNotificationsDropdown();
    });
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
    $.ajax({
        url: "/home/sentmessage-fragment?page=" + currentPage,
        success: function (data) {

            const tableFragment = $(data).find('#messageTableBody');

            $("#messageTableBody").html(tableFragment.html());
            $('#currentPageSpan').text(currentPage);
            $('#totalPagesSpan').text(totalPages);
            let pageNumber = currentPage + 1;

            $('#currentPageSpan2').text(pageNumber);
            $('#totalPagesSpan2').text(totalPages);

            attachRowListener('.clickable-row, .outbox-row', handleOutboxRowClick);
            attachRowListener('.read-message, .unread-message', handleInboxRowClick);
        }
    });
}

function updateInboxPage(currentPage, totalPages) {
    $.ajax({
        url: "/home/showinbox-fragment?page=" + currentPage,
        success: function (data) {

            const tableFragment = $(data).find('#messageTableBody');

            $("#messageTableBody").html(tableFragment.html());

            $('#currentPageSpan').text(currentPage);
            $('#totalPagesSpan').text(totalPages);
            let pageNumber = currentPage + 1;

            $('#currentPageSpan2').text(pageNumber);
            $('#totalPagesSpan2').text(totalPages);

            attachRowListener('.clickable-row, .outbox-row', handleOutboxRowClick);
            attachRowListener('.read-message, .unread-message', handleInboxRowClick);
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


function handleLatestNotificationClick(notificationId){
    fetchAndShowNotification(notificationId);
};
function fetchAndShowNotification(notificationId) {
    fetch(`/home/get-notification-content/${notificationId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error fetching notification content. Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            const notificationDTO = data;
            showNotificationContentPopup(notificationDTO);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function showNotificationContentPopup(notificationDTO) {
    const {notificationId, type, message } = notificationDTO;
    console.log(type + "TYPE " + notificationId + " ID");
    sendNotificationClickToBackend(notificationId);
    function sendNotificationClickToBackend(notificationId) {
        console.log("notificationID" + notificationId);
        // Replace 'your-backend-endpoint' with the actual URL of your backend endpoint
        fetch(`/home/set-notification-asread/${notificationId}`, {

            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            // You can pass additional data in the request body if needed
            body: JSON.stringify({ type, message }),
        })
            .catch(error => {
                console.error('Error:', error);
            });
    }
    switch (type) {
        case 'REQUEST':
            showRequestPopup(message, notificationId);
            break;

        case 'INFORMATION':
            showInformationPopup(message);
            break;

        case 'ALERT':
            showAlertPopup(message);
            break;
        case 'OTHER':
            showOtherPopup(message);
            break;
        // Add more cases as needed for different types
        default:
            // Default behavior if type is not recognized
            Swal.fire({
                title: 'Notification Content',
                html: message,
                showConfirmButton: true,
                confirmButtonText: 'OK',
                icon: 'info',
                color: "#495057",
                background: "#495057"
            });

            break;
    }
    updateUnreadNotificationsCount();
    updateUnreadNotificationsDropdown();
}

// Function to handle Accept request
function handleAcceptRequest(notificationId) {

        const endpointUrl = "/acceptRequest";
        fetch(endpointUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                notificationId: notificationId,
            }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.text();
            })
            .then(data => {
                console.log("Accept request successful:", data);


                Swal.fire({
                    icon: "success",
                    title: "Success!",
                    text: data,
                    showConfirmButton: false, // Hide the default "OK" button
                    timer: 3000, // Auto-close after 3 seconds
                    timerProgressBar: true, // Display a progress bar
                    onBeforeOpen: () => {
                        Swal.showLoading(); // Show loading animation
                    },
                    onClose: () => {
                        Swal.hideLoading(); // Hide loading animation on close
                    },
                    background: "#495057",
                });
            })
            .catch(error => {
                console.error("Error during accept request:", error);
            });
}
function handleDeclineRequest() {
    console.log('Declined request');
}
function handleCancelRequest() {
    console.log('Cancelled request');

}
function archiveMessage(messageId, archiveTarget ) {
    fetch('/home/archive-message/',
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({messageId, archiveTarget}),
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
function attachLatestNotificationsRowListener() {
    $('.preview-notification-item').on('click', function () {
        var notificationId = $(this).data('notification-id');
        console.log("notification:" + notificationId);
        handleLatestNotificationClick(notificationId);
    });
}
function attachLatestMessagesRowListener() {
    const latestMessagesRows = document.querySelectorAll('.preview-message-item');
    latestMessagesRows.forEach(function (row) {
        row.addEventListener('click', function () {
            handleLatestMessagesRowClick(row);
        });
    });
}
function handleLatestMessagesRowClick(row) {
    console.log("handle latest message rowclick");
    const messageId = row.dataset.messageId;
    loadInboxMessagesDetailsFragment(messageId);
}
function initializeRecipientTypeahead() {
    var usersBloodhound = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('username'),
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        local: users
    });

    $('#recipientTypeahead .typeahead').typeahead({
        hint: true,
        highlight: true,
        minLength: 1
    }, {
        name: 'users',
        displayKey: 'username',
        source: usersBloodhound
    });

    // Event handler when a suggestion is selected
    $('#recipientTypeahead .typeahead').on('typeahead:select', function (event, suggestion) {
        // Convert the selected user's ID to an integer
        var recipientId = parseInt(suggestion.id);
        // Update the hidden input with the converted ID
        $('#hiddenReceiverId').val(recipientId.toString());
        console.log("Receiver ID: " + recipientId);
    });
}
function fetchUserList() {
    $.ajax({
        url: '/get-user-list',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            // Update the users variable with the fetched data
            users = data;
            initializeRecipientTypeahead(); // Reinitialize Typeahead.js with updated data
        },
        error: function (error) {
            console.error('Error fetching user list:', error);
        }
    });
}
