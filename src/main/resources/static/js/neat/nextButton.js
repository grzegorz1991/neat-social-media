

// Event listener for next button
document.getElementById('nextButton').addEventListener('click', () => {
    currentPage++;
    console.log("nex button");
    getMessages(currentPage);
});