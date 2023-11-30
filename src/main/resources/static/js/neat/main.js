function loadDefaultContent() {
    fetch('/home/default-fragment')
        .then(response => response.text())
        .then(data => {
            document.getElementById('dynamicContent').innerHTML = data;
        })
        .catch(error => console.error('Error:', error));

        // Function to attach event listeners to the buttons
        function attachEventListeners() {
        const leftButton = document.getElementById('leftButton');
        const rightButton = document.getElementById('rightButton');
            console.log('Left clicked and right clicked expected');
        leftButton.addEventListener('click', () => {
        console.log('Left clicked');
    });

        rightButton.addEventListener('click', () => {
        console.log('Right clicked');
    });
    }

        // Call attachEventListeners after the content is loaded
        document.addEventListener('DOMContentLoaded', attachEventListeners);

}

function loadSettings() {
    fetch('/home/settings-fragment')
        .then(response => response.text())
        .then(data => {
            document.getElementById('dynamicContent').innerHTML = data;
        })
        .catch(error => console.error('Error:', error));
}

function loadMessages() {
    fetch('/home/messages-fragment')
        .then(response => response.text())
        .then(data => {
            document.getElementById('dynamicContent').innerHTML = data;
        })
        .catch(error => console.error('Error:', error));
}

function loadSentMessages() {
    fetch('/home/messages-sent-fragment')
        .then(response => response.text())
        .then(data => {
            document.getElementById('dynamicContent').innerHTML = data;
        })
        .catch(error => console.error('Error:', error));


        // Function to attach event listeners to the buttons
        function attachEventListeners() {
        const leftButton = document.getElementById('leftButton');
        const rightButton = document.getElementById('rightButton');

        leftButton.addEventListener('click', () => {
        console.log('Left clicked');
    });

        rightButton.addEventListener('click', () => {
        console.log('Right clicked');
    });
    }

        // Call attachEventListeners after the content is loaded
        document.addEventListener('DOMContentLoaded', attachEventListeners);

}

function loadMessageForm() {
    fetch('/home/new-messages-fragment')
        .then(response => response.text())
        .then(data => {
            document.getElementById('dynamicContent').innerHTML = data;
        })
        .catch(error => console.error('Error:', error));
}

// Function to attach event listener to logo
function attachLogoClickListener() {
    const logoElement = document.querySelector('.sidebar-brand.brand-logo');
    if (logoElement) {
        logoElement.addEventListener('click', function (event) {
            event.preventDefault();
            loadDefaultContent();
        });
    }
}

// Load settings content when the page loads
document.addEventListener('DOMContentLoaded', function () {
    loadDefaultContent();
    attachLogoClickListener();
    const url = new URL(window.location.href);
    const errorParam = url.searchParams.get('error');
    if (errorParam) {
        loadSettings(errorParam);
    } else {
        loadDefaultContent();
    }
    attachLogoClickListener();
});


// Loading sub-content
document.getElementById('showInboxPage').addEventListener('click', function (event) {
    event.preventDefault();
    loadMessages();
});
document.getElementById('settingsDropdownItem').addEventListener('click', function (event) {
    event.preventDefault();
    loadSettings();
});
document.getElementById('messageDashButtonItem').addEventListener('click', function (event) {
    event.preventDefault();
    loadMessages();
});
document.getElementById('settingsDashButtonItem').addEventListener('click', function (event) {
    event.preventDefault();
    loadSettings();
});
document.getElementById('newMessageDashButtonItem').addEventListener('click', function (event) {
    event.preventDefault();
    console.log("'newMessageDashButtonItem'");
    loadMessageForm();
});
document.getElementById('sentMessageDashButtonItem').addEventListener('click', function (event) {
    event.preventDefault();
    console.log("sentMessadeDashButtonItem");
    loadSentMessages();
});

