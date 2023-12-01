// Function to add event listeners to buttons in the dynamic content
function addEventListenersToButtons() {
    // Use event delegation on the dynamicContent container
    const dynamicContent = document.getElementById('dynamicContent');

    // Check if the button already has an event listener
    dynamicContent.addEventListener('click', function (event) {
        const targetButton = event.target;

        if (targetButton.tagName === 'BUTTON') {
            const buttonId = targetButton.id;

            if (!targetButton.hasEventListener) {
                console.log(`Button ${buttonId} website pressed`);
                targetButton.hasEventListener = true;
            }
        }
    });
}