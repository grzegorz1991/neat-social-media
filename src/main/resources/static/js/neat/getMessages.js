const apiUrl = '/home/messages-sent-fragment';

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
