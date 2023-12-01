// Get references to the buttons and the dynamicContent element
const button1 = document.getElementById('button1');
const button2 = document.getElementById('button2');
const dynamicContent = document.getElementById('dynamicContent');

// Add event listeners to the buttons
button1.addEventListener('click', () => {
    loadContent('/home/content1');
});

button2.addEventListener('click', () => {
    loadContent('/home/content2');
});

// Function to load content based on the given URL
function loadContent(url) {
    // Make an AJAX request
    const xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                // Update the content of the dynamicContent element
                dynamicContent.innerHTML = xhr.responseText;
                loadAdditionalScripts();
                console.log("loadAdditionalScripts()");
            } else {
                console.error('Failed to load content:', xhr.status);
            }
        }
    };
    xhr.onerror = function () {
        console.error('An error occurred while making the AJAX request');
    };
    xhr.send();
}

// Function to load additional scripts for the dynamically loaded content
function loadAdditionalScripts() {
    const script = document.createElement('script');
    script.src = '/js/neat/addEventListenersToButtons.js';
    // Set the onload callback to execute after the script is loaded
    script.onload = function () {
        // Optional: Call a function from the loaded script if needed
        addEventListenersToButtons();
    };
    document.body.appendChild(script);
}