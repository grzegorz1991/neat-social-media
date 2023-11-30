var currentMessageDetails = null;

// Function to show the message details
function showMessageDetails(row) {
    // Check if there is already a visible message details box
    if (currentMessageDetails) {
        // Hide the current message details box
        currentMessageDetails.style.display = "none";
    }

    // Get the message details from the clicked row
    const sender = row.querySelector("td:nth-child(1)").textContent;
    const receiver = row.querySelector("td:nth-child(2)").textContent;
    const title = row.querySelector("td:nth-child(3)").textContent;
    const timestamp = row.querySelector("td:nth-child(4)").textContent;

    // Create the message details box
    const messageDetailsBox = document.createElement("div");
    messageDetailsBox.classList.add("message-details");

    // Populate the message details box with the message data
    messageDetailsBox.innerHTML = `
            <h4>Message Details</h4>
            <p><strong>Sender:</strong> ${sender}</p>
            <p><strong>Receiver:</strong> ${receiver}</p>
            <p><strong>Title:</strong> ${title}</p>
            <p><strong>Timestamp:</strong> ${timestamp}</p>
        `;

    // Append the message details box to the page
    document.getElementById("messageList").appendChild(messageDetailsBox);

    // Set the current message details box to the newly created box
    currentMessageDetails = messageDetailsBox;
}