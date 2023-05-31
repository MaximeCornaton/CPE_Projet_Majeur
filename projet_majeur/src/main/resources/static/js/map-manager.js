
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
                iconUrl: '../img/icons/fire.gif',
                iconSize: [32, 32],
                iconAnchor: [16, 16],
            });

            L.marker([fire.lat, fire.lon], { icon: icon }).addTo(map);

            const popupContent = `
                <strong>ID:</strong> ${fire.id}<br>
                
            `;

            marker.bindPopup(popupContent);

            marker.on('click', function() {
                const newIcon = L.icon({
                    iconUrl: '../img/icons/fire.gif',
                    iconSize: [40, 40],
                    iconAnchor: [20, 20],
                });
                marker.setIcon(newIcon);
            });

            marker.on('popupclose', function() {
                marker.setIcon(icon);
            });

        });
    });
}

function displayVehicles(map) {
    getVehicles().then(vehicles => {
        vehicles.forEach(vehicle => {
            const icon = L.icon({
                iconUrl: '../img/icons/fire-truck.png',
                iconSize: [32, 32],
                iconAnchor: [16, 16],
            });

            const marker = L.marker([vehicle.lat, vehicle.lon], { icon: icon }).addTo(map);

            const popupContent = `
                <strong>ID:</strong> ${vehicle.id}<br>
                <strong>Type:</strong> ${vehicle.type}<br>
                <strong>Type de Liquide:</strong> ${vehicle.liquidType}<br>
                <strong>Quantit&#xE9 de Liquide:</strong> ${vehicle.liquidQuantity}<br>
                <strong>Essence:</strong> ${vehicle.fuel}<br>
            `;

            marker.bindPopup(popupContent);

            marker.on('popupopen', function() {
                // Changez l'icône du camion lorsque le marqueur est cliqué
                const newIcon = L.icon({
                    iconUrl: '../img/icons/fire-truck.gif',
                    iconSize: [40, 40],
                    iconAnchor: [20, 20],
                });
                marker.setIcon(newIcon);
            });

            marker.on('popupclose', function() {
                marker.setIcon(icon);
            });

        });
    });
}

map = createMap('map');
displayFires(map);
displayVehicles(map);
