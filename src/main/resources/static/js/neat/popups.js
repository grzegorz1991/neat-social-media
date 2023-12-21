function showSuccessConfirmation() {
    Swal.fire({
        title: 'Success!',
        text: 'Message archived successfully.',
        showConfirmButton: false,
        timer: 1500,
        timerProgressBar: true,
        icon: 'success',
        color: "#495057",
        background: "#495057"
    }).then(() => {
        goBack();
    });
}

function showAlertPopup(message) {
    // Custom logic for displaying an alert popup with a progress bar
    Swal.fire({
        title: 'Alert Notification',
        html: message,
        icon: 'warning',
        iconColor: '#fc424a',
        showConfirmButton: false, // Hide the default "OK" button
        timer: 3000, // Auto-close after 3 seconds
        timerProgressBar: true, // Display a progress bar
        onBeforeOpen: () => {
            Swal.showLoading(); // Show loading animation
        },
        onClose: () => {
            Swal.hideLoading(); // Hide loading animation on close
        },
        background: "#495057",
    });
}

function showInformationPopup(message) {
    // Custom logic for displaying an info popup
    Swal.fire({
        title: 'Info Notification',
        html: message,
        showConfirmButton: true,
        confirmButtonText: 'OK',
        icon: 'info',
        iconColor: '#8f5fe8',
        color: "#495057",
        iconColor: '#28a745',
        background: "#495057"
    });

}

function showRequestPopup(message, notificationId) {
    // Custom logic for displaying a request popup with three buttons
    Swal.fire({
        title: 'Request Notification',
        html: message,
        showCancelButton: true,
        showDenyButton: true,
        showConfirmButton: true,
        confirmButtonText: 'Accept',
        denyButtonText: 'Decline',
        cancelButtonText: 'Cancel',
        icon: 'info',
        iconColor: '#00609b',
        color: "#495057",
        background: "#495057",
    }).then((result) => {
        if (result.isConfirmed) {
            // User clicked Accept
            handleAcceptRequest(notificationId);
        } else if (result.isDenied) {
            // User clicked Decline
            handleDeclineRequest();
        } else {
            // User clicked Cancel or closed the popup
            handleCancelRequest();
        }
    });
}

function showOtherPopup(message) {
    // Custom logic for displaying an info popup
    Swal.fire({
        title: 'Other Notification',
        html: message,
        showConfirmButton: true,
        confirmButtonText: 'OK',
        icon: 'info',
        iconColor: '#ffab00',
        color: "#495057",
        background: "#495057"
    });
}

function showArchiveConfirmation(messageId, archiveTarget  ) {
    // Show SweetAlert2 popup
    Swal.fire({
        position: top,
        title: 'Are you sure?',
        text: 'You won\'t be able to revert this!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, archive it!',
        color: "#495057",
        background: "#495057",
    }).then((result) => {
        if (result.isConfirmed) {

            archiveMessage(messageId, archiveTarget );
        }
    });
}