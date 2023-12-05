var currentPageSpan = document.getElementById('currentPageSpan');
var currentPage = parseInt(currentPageSpan.innerText);
console.log(currentPage + "Current pafe");

var totalPagesSpan = document.getElementById('totalPagesSpan');
var totalPages = parseInt(totalPagesSpan.innerText);
console.log(totalPages + "totalPages");

extracted();

function addEventListenersToButtons() {


    const dynamicContent = document.getElementById('dynamicContent');

    dynamicContent.addEventListener('click', function (event) {
        const targetButton = event.target;

        if (targetButton.tagName === 'BUTTON') {
            const buttonId = targetButton.id;
            console.log(currentPage + " Current Page " + totalPages + " Total Pages at switch statement");

            switch (buttonId) {
                case 'previousButton':
                    console.log('Previous button clicked');
                    handlePreviousButtonClick();
                    break;
                case 'nextButton':
                    console.log('Next button clicked');
                    handleNextButtonClick();
                    break;

                default:

                    break;
            }
        }
    });

    function handlePreviousButtonClick() {
        if (currentPage > 0) {
            currentPage--;
            updatePage();

        }
    }

    function handleNextButtonClick() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            updatePage();
        }
    }

    function updatePage() {
        // Perform an AJAX request to retrieve the next or previous page
        console.log(currentPage + " Current Page " + totalPages + " Total Pages at update method");
        $.ajax({
            url: "/home/messages-sent-fragment?page=" + currentPage,
            success: function (data) {
                // Parse the HTML response to extract the table body
                const tableFragment = $(data).find('#messageTableBody');

                // Update the table body with the new messages
                $("#messageTableBody").html(tableFragment.html());

                // Update the spans with the new values
                $('#currentPageSpan').text(currentPage);
                $('#totalPagesSpan').text(totalPages);
                let pageNumber = currentPage + 1;

                $('#currentPageSpan2').text(pageNumber);
                $('#totalPagesSpan2').text(totalPages);
                extracted();
            }
        });
    }
}

// Call the function when the page loads
document.addEventListener('DOMContentLoaded', function () {
    addEventListenersToButtons();

});

function loadMessagesDetailsFragment(messageId) {
    fetch('/home/messages-get-details-fragment?messageId=' + messageId)
        .then(response => response.text())
        .then(data => {
            document.getElementById('dynamicContent').innerHTML = data;
            loadAdditionalScriptsForMessageDetailsPage();
        })
        .catch(error => console.error('Error:', error));
}

// Example of loading additional scripts for the message details page
function loadAdditionalScriptsForMessageDetailsPage() {
// Add logic to load additional scripts if needed
    console.log("Loading additional scripts for the message details page");

    // Add a button listener for the "Go Back" button
    const goBackButton = document.getElementById('goBackToOutgoingMessages');

    if (goBackButton) {
        goBackButton.addEventListener('click', function () {

            loadSentMessagesFragment();
        });
    }
}



function extracted() {
    console.log("extracted");
    // Add click event listeners to clickable rows
    const clickableRows = document.querySelectorAll('.clickable-row');
    clickableRows.forEach(function (row) {
        row.addEventListener('click', function () {
            console.log("clicked");
            const messageId = row.dataset.messageId;
            console.log("Clicked on row with message ID: " + messageId);


            // Update the URL using the History API
            const newUrl = "/home/messages-get-details?messageId=" + messageId;
            window.history.pushState({path: newUrl}, '', newUrl);

            // Load the content for the new URL
            loadMessagesDetailsFragment(messageId);
            loadAdditionalScriptsForMessageDetailsPage();

        });
    });

}
