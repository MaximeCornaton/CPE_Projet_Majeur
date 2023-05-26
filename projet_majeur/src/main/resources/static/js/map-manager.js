
function createMap(divId) {
    console.log(divId);
    let map = L.map(divId).setView([45.7735, 4.8745], 13);

    L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}', {
        attribution: 'Map data &copy; <a href="https://www.mapbox.com/">Mapbox</a>',
        maxZoom: 18,
        id: 'mapbox/streets-v11',
        tileSize: 512,
        zoomOffset: -1,

        accessToken: 'pk.eyJ1IjoibWF4aW1lY3JudCIsImEiOiJjbGkzNmdzbW4wcTJtM2ZvM2Z4azYxbTcwIn0.AmmVszgHGmq9KMbzdmhe7A'
    }).addTo(map);

    return map;
}

function displayFires(map) {
getFires().then(fires => {
        fires.forEach(fire => {
            L.marker([fire.lat, fire.lon]).addTo(map)
                .bindPopup('Feu')
                .openPopup();
        });
    });

}


function getFires() {
    return fetch('http://localhost:8000/fire-service/fires', {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        },
    })
        .then(response => response.json())
        .catch(error => {
            console.error('Une erreur s\'est produite:', error);
        });
}
