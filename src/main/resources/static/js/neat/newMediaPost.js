// script.js

$(document).ready(function () {
    // Capture file input input event
    $('#mediaUpload').on('input', function () {
        // Get selected file
        var file = this.files[0];

        // Display selected image in the container
        displaySelectedImage(file);
    });
});

// Function to display selected image
function displaySelectedImage(file) {
    var reader = new FileReader();

    reader.onload = function (e) {
        console.log('FileReader.onload:', e);

        // Create an image element and set its source to the selected file
        var image = $('<img>').attr('src', e.target.result).addClass('img-fluid');
        console.log('Created Image Element:', image);

        // Display the image in the selected image container
        $('#selectedImageContainer').empty().append(image);
        console.log('Image Displayed in Container');
    };

    // Read the selected file as a data URL
    reader.readAsDataURL(file);
    console.log('Reading File as Data URL...');
}
