console.log("JavaScript file loaded!");

fetch("http://localhost:8080/api/data")
    .then(response => response.json())
    .then(data => {
        console.log("Data from REST API:", data);
    })
    .catch(error => {
        console.error("Error fetching data:", error);
    });
