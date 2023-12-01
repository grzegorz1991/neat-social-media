function loadDefaultFragment() {
    fetch('/home/default-fragment')
        .then(response => response.text())
        .then(data => {
            document.getElementById('dynamicContent').innerHTML = data;
        })
        .catch(error => console.error('Error:', error));
}

function loadSettingsFragment() {
    fetch('/home/settings-fragment')
        .then(response => response.text())
        .then(data => {
            document.getElementById('dynamicContent').innerHTML = data;
        })
        .catch(error => console.error('Error:', error));
}

function loadMessagesFragment() {
    fetch('/home/messages-fragment')
        .then(response => response.text())
        .then(data => {
            document.getElementById('dynamicContent').innerHTML = data;
        })
        .catch(error => console.error('Error:', error));
}

function loadSentMessagesFragment() {
    fetch('/home/messages-sent-fragment')
        .then(response => response.text())
        .then(data => {
            document.getElementById('dynamicContent').innerHTML = data;
        })
        .catch(error => console.error('Error:', error));
}

function loadMessageFormFragment() {
    fetch('/home/new-messages-fragment')
        .then(response => response.text())
        .then(data => {
            document.getElementById('dynamicContent').innerHTML = data;
        })
        .catch(error => console.error('Error:', error));
}



function attachLogoClickListener() {
    const bigLogoElement = document.querySelector('.sidebar-brand.brand-logo');
    const smallLogoElement = document.querySelector('.sidebar-brand.brand-logo-mini');

    if (bigLogoElement) {
        bigLogoElement.addEventListener('click', function (event) {
            event.preventDefault();
            loadDefaultFragment();
        });
    }

    if (smallLogoElement) {
        smallLogoElement.addEventListener('click', function (event) {
            event.preventDefault();
            loadDefaultFragment();
        });
    }
}


// Load settings content when the page loads
document.addEventListener('DOMContentLoaded', function () {
    loadDefaultFragment();
    attachLogoClickListener();
    const url = new URL(window.location.href);
    const errorParam = url.searchParams.get('error');
    if (errorParam) {
        loadSettingsFragment(errorParam);
    } else {
        loadDefaultFragment();
    }
    attachLogoClickListener();
});


// Loading sub-content
document.getElementById('showInboxPage').addEventListener('click', function (event) {
    event.preventDefault();
    loadMessagesFragment();
});
document.getElementById('settingsDropdownItem').addEventListener('click', function (event) {
    event.preventDefault();
    loadSettingsFragment();
});
document.getElementById('messageDashButtonItem').addEventListener('click', function (event) {
    event.preventDefault();
    loadMessagesFragment();
});
document.getElementById('settingsDashButtonItem').addEventListener('click', function (event) {
    event.preventDefault();
    loadSettingsFragment();
});
document.getElementById('newMessageDashButtonItem').addEventListener('click', function (event) {
    event.preventDefault();
    loadMessageFormFragment();
});
document.getElementById('sentMessageDashButtonItem').addEventListener('click', function (event) {
    event.preventDefault();
    loadSentMessagesFragment();
});

