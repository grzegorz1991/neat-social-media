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
    $.get('/get-user-list')
        .done(function (users) {
            populateUserList(users);

        })
        .fail(function (error) {
            console.error('Error fetching users:', error);
        });
}

function filterSearchFunction() {
    var input = $('#myInput');

    if (input.length === 0 || input.val() === undefined) {
        console.error('#myInput not found or has no value');
        return;
    }

    var filter = input.val().toUpperCase();
    var table = $('.table-hover');
    var rows = table.find('tbody tr');

    rows.each(function () {
        var userColumn = $(this).find('td:nth-child(2)'); // Assuming the "User" column is the second column
        var username = userColumn.find('span').text().toUpperCase();

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
