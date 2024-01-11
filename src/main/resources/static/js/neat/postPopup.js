
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
    attachNewArticlePostClickListeners();
    function displaySelectedImage() {
        console.log("Function triggered!");
        let displayedImageFile = document.getElementById("displayedImageFile");
        let inputImageFile = document.getElementById("inputImageFile");
        let fileHint = document.querySelector('.file-upload-info'); // Get the file hint element

        if (inputImageFile.files && inputImageFile.files[0]) {
            displayedImageFile.src = URL.createObjectURL(inputImageFile.files[0]);
            fileHint.value = inputImageFile.files[0].name; // Update the file hint with the selected file name
        }
    }
    // Example usage


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


    // Attach the closeNewArticlePopup function to the close button click event
    document.getElementById('closePopupButton').addEventListener('click', function()  {
       // closeNewArticlePopupfunction();
        console.log('Change event triggered!');
     //   displaySelectedImage();
    });

    document.getElementById('inputImageFile').addEventListener('change', function() {
        console.log('Change event triggered!');
        displaySelectedImage();
    });



