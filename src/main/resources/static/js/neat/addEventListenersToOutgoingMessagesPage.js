var currentPage = 1; // Set default values
var totalPages = 3;

function addEventListenersToButtons() {
    // Use event delegation on the dynamicContent container
    const dynamicContent = document.getElementById('dynamicContent');

    dynamicContent.addEventListener('click', function (event) {
        const targetButton = event.target;

        if (targetButton.tagName === 'BUTTON') {
            const buttonId = targetButton.id;
            console.log(currentPage + " Current Page");
            console.log(totalPages + " Total Pages");
            switch (buttonId) {
                case 'previousButton':
                    console.log('Previous button clicked');
                    handlePreviousButtonClick();
                    break;
                case 'nextButton':
                    console.log('Next button clicked');
                    handleNextButtonClick();
                    break;
                // Add more cases for other buttons if needed

                default:
                    // Handle other buttons or actions
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
        console.log('updatePage');
        $.ajax({
            url: "/home/messages-sent-fragment?page=" + currentPage,
            success: function(data) {
                // Update the table body with the new messages
                $("#messageTableBody").html(data);
                console.log(currentPage + " Current Page");
                console.log(totalPages + " Total Pages");
            }
        });
    }
}

// Call the function when the page loads
document.addEventListener('DOMContentLoaded', function () {
    addEventListenersToButtons();
});
