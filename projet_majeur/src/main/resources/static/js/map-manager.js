
function createMap(divId) {
    let map = L.map(divId).setView([45.7735, 4.8745], 13);
    L.tileLayer('https://api.mapbox.com/styles/v1/maximecrnt/cli36w9f700ko01pgahnu8l6o/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWF4aW1lY3JudCIsImEiOiJjbGkzNmdzbW4wcTJtM2ZvM2Z4azYxbTcwIn0.AmmVszgHGmq9KMbzdmhe7A', {
        attribution: 'Map data &copy; <a href="https://www.mapbox.com/">Mapbox</a>',
        maxZoom: 18,
        tileSize: 512,
        zoomOffset: -1,
    }).addTo(map);

    return map;
}


function displayFires(map) {
getFires().then(fires => {
        fires.forEach(fire => {
            const icon = L.icon({
                iconUrl: '../img/icons/fire.gif', // Remplacez par l'URL de votre image de camion de pompier
                iconSize: [32, 32], // Taille de l'icône en pixels
                iconAnchor: [16, 16], // Point d'ancrage de l'icône en pixels
            });

            L.marker([fire.lat, fire.lon], { icon: icon }).addTo(map)
                .bindPopup('Feu')
                .openPopup();
        });
    });
}

function displayVehicles(map) {
    getVehicles().then(vehicles => {
        vehicles.forEach(vehicle => {
            const icon = L.icon({
                iconUrl: '../img/icons/fire-truck.png', // Remplacez par l'URL de votre image de camion de pompier
                iconSize: [32, 32], // Taille de l'icône en pixels
                iconAnchor: [16, 16], // Point d'ancrage de l'icône en pixels
            });

            L.marker([vehicle.lat, vehicle.lon], { icon: icon }).addTo(map)
                .bindPopup('Camion')
                .openPopup();
        });
    });
}


map = createMap('map');
displayFires(map);
displayVehicles(map);
