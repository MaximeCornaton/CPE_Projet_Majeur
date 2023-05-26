function createFrame(){
    // Créer l'élément iframe
    const iframe = document.createElement('iframe');
    iframe.src = "https://api.mapbox.com/styles/v1/maximecrnt/cli36w9f700ko01pgahnu8l6o.html?title=false&access_token=pk.eyJ1IjoibWF4aW1lY3JudCIsImEiOiJjbGkzNmdzbW4wcTJtM2ZvM2Z4azYxbTcwIn0.AmmVszgHGmq9KMbzdmhe7A&zoomwheel=false#11.93/45.76267/4.84871";
    iframe.title = "Monochrome";
    iframe.style.border = "none";
    iframe.style.width = "100%";
    iframe.style.height = "100%";

    getFires();

    return iframe;
}

function getFires() {
    fetch('http://localhost:8000/fire-service/fires', {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            },
        })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            // Faites quelque chose avec les données JSON récupérées ici
        })
        .catch(error => {
            console.error('Une erreur s\'est produite:', error);
        });
}


function getCaserne(){

}