function attachNewArticlePostClickListeners() {
    document.getElementById('newArticlePost').addEventListener('click', function () {
        handleNewArticlePostClick('newArticlePost');
    });


    document.getElementById('newPostMediaPost').addEventListener('click', function () {
        handleNewArticlePostClick('newPostMediaPost');
    });

    document.getElementById('newPostEventAnnouncement').addEventListener('click', function () {
        handleNewArticlePostClick('newPostEventAnnouncement');
    });

    function handleNewArticlePostClick(itemId) {
        switch (itemId) {
            case "newArticlePost":
               openNewArticlePostPopup();
                console.log("Clicked 'newArticlePost'");
                break;
            case "newPostMediaPost":

                console.log("Clicked newPostMediaPost");
                break;
            case "newPostEventAnnouncement":

                console.log("Clicked 'newPostEventAnnouncement'");
                break;
            default:
                console.log(`Clicked item with ID ${itemId}`);

        }
    }
}

// Example usage
attachNewArticlePostClickListeners();


function openNewArticlePostPopup() {
    console.log('popup');
    document.getElementById('overlay').style.display = 'block';
    document.getElementById('popup').style.display = 'block';

    // Load the form into the popup using AJAX
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById('popup').innerHTML = this.responseText;
        }
    };
    xmlhttp.open("GET", "/home/newarticlepost-fragment", true);
    xmlhttp.send();
}

// Function to close the popup
function closeNewArticlePopup() {
    document.getElementById('content').value = '';

    // Hide the overlay and popup
    document.getElementById('overlay').style.display = 'none';
    document.getElementById('popup').style.display = 'none';
}

// Attach the openNewArticlePostPopup function to the button click event
document.getElementById('openPopupButton').addEventListener('click', openNewArticlePostPopup);

