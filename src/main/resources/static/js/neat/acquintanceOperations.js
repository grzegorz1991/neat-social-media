$(document).on('click', '.reply-button', function() {
    var recipientId = $(this).data('recipient-id');
    loadDynamicContent(`/home/replyMessage-fragment?reply=${recipientId}`);
});

$(document).on('click', '.unfriend-button', function() {
    var acquaintanceId = $(this).data('acquaintance-id');

    function breakTiesWithAcquintance(acquaintanceId) {
        const endpointUrl = `/breakTies`;

        // Make a POST request to your backend with acquaintanceId
        fetch(endpointUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                acquaintanceId: acquaintanceId,
            }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.text();
            })
            .then(data => {
                console.log("Request successful:", data);

                // Show SweetAlert confirmation with timeout
                Swal.fire({
                    icon: "success",
                    title: "Success!",
                    text: data,
                    timer: 2500, // Set the timeout in milliseconds
                    showConfirmButton: false,
                    color: "#495057",
                    background: "#495057",
                }).then(() => {
                    loadDynamicContent('/home/seeacquaintance-fragment');
                    console.log("GGOGO");
                });
            })
            .catch(error => {
                console.error("Error during request:", error);
            });
    }
    breakTiesWithAcquintance(acquaintanceId);
});
