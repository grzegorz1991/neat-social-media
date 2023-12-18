$(document).ready(function () {
    // Fetch the list of users when the page is loaded
    fetchUsers();

    // Attach the filter function to the keyup event
    $('#myInput').on('keyup', filterSearchFunction);

    // Add hover effects to table rows
    $('.table-row').hover(
        function () {
            $(this).addClass('table-row-hover');
        },
        function () {
            $(this).removeClass('table-row-hover');
        }
    );
});

function fetchUsers() {
    console.log("fetching");
    $.get('/get-user-list')
        .done(function (users) {
            populateUserList(users);
            console.log(users);
        })
        .fail(function (error) {
            console.error('Error fetching users:', error);
        });
}

function filterSearchFunction() {
    var input = $('#myInput');
    var filter = input.val().toUpperCase();
    var table = $('.table-hover');
    var rows = table.find('tbody tr');

    rows.each(function () {
        var usernameCell = $(this).find('td:first-child'); // Assuming the username is in the first column
        var username = usernameCell.text().toUpperCase();

        if (username.indexOf(filter) > -1) {
            $(this).show();
        } else {
            $(this).hide();
        }
    });
}

function populateUserList(users) {
    var ul = $('#myUL');
    ul.empty(); // Clear the existing list

    users.forEach(function (user) {
        var li = $('<li><a href="#">' + user.username + '</a></li>');
        ul.append(li);
    });
}
