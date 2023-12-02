const apiUrl = '/retrive-messages'; // Replace with your API endpoint

const limit = 5; // Number of messages to retrieve per page
let currentPage = 0; // Current page number

// Function to retrieve messages for a specific page
function getMessages(page) {
    fetch(`${apiUrl}?page=${page}&pageSize=${limit}`)
        .then(response => response.json())
        .then(data => {
            // Clear the table body
            const tableBody = document.getElementById('messageTableBody');
            tableBody.innerHTML = '';

            // Append each message to the table
            data.content.forEach(message => {
                const row = document.createElement('tr');

                // Add click event listener to the row
                row.addEventListener('click', () => showMessageDetails(row));

                const senderCell = document.createElement('td');
                senderCell.textContent = message.sender.name;

                const receiverCell = document.createElement('td');
                receiverCell.textContent = message.receiver.name;

                const titleCell = document.createElement('td');
                titleCell.textContent = message.title;

                const timestampCell = document.createElement('td');
                timestampCell.textContent = message.timestamp;

                row.appendChild(senderCell);
                row.appendChild(receiverCell);
                row.appendChild(titleCell);
                row.appendChild(timestampCell);

                tableBody.appendChild(row);
            });

            // Enable or disable the previous/next buttons based on pagination info
            const previousButton = document.getElementById('previousButton');
            const nextButton = document.getElementById('nextButton');

            previousButton.disabled = data.first;
            nextButton.disabled = data.last;
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

// Event listener for previous button
document.getElementById('previousButton').addEventListener('click', () => {
    if (currentPage > 0) {
        currentPage--;
        console.log("prv button");
        getMessages(currentPage);
    }
});

// Event listener for next button
document.getElementById('nextButton').addEventListener('click', () => {
    currentPage++;
    console.log("nex button");
    getMessages(currentPage);
});

// Function to show the message details
function showMessageDetails(row) {
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
}