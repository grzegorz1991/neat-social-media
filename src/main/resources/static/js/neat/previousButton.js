
// Event listener for previous button
document.getElementById('previousButton').addEventListener('click', () => {
    if (currentPage > 0) {
        currentPage--;
        console.log("prv button");
        getMessages(currentPage);
    }
});