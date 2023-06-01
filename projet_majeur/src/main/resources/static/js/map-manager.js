map = createMap('map');
let vehicleMarkers = {};
let fireMarkers = {};

const icon_fire_truck = L.icon({
    iconUrl: '../img/icons/fire-truck.png',
    iconSize: [32, 32],
    iconAnchor: [16, 16],
});

const icon_fire_truck_moving = L.icon({
    iconUrl: '../img/icons/fire-truck.gif',
    iconSize: [32, 32],
    iconAnchor: [16, 16],
});

const icon_fire = L.icon({
    iconUrl: '../img/icons/fire.gif',
    iconSize: [32, 32],
    iconAnchor: [16, 16],
});


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
        // Supprimer les feux existants de la carte
        for (const id in fireMarkers) {
            if (!fires.some(fire => fire.id === id)) {
                const marker = fireMarkers[id];
                map.removeLayer(marker);
                delete fireMarkers[id];
            }
        }

        // Ajouter ou mettre à jour les marqueurs des nouveaux feux
        fires.forEach(fire => {
            if (fire.id in fireMarkers) {
                // Le feu existe déjà, aucune mise à jour nécessaire
                return;
            }

            const marker = L.marker([fire.lat, fire.lon], { icon: icon_fire }).addTo(map);

            const popupContent = `
                <strong>ID:</strong> ${fire.id}<br>
            `;

            marker.bindPopup(popupContent);

            fireMarkers[fire.id] = marker;
        });
    });
}


function displayVehicles(map) {
    getVehicles().then(vehicles => {
        // Supprimer les véhicules existants de la carte
        for (const id in vehicleMarkers) {
            if (!vehicles.some(vehicle => vehicle.id === id)) {
                const marker = vehicleMarkers[id];
                map.removeLayer(marker);
                delete vehicleMarkers[id];
            }
        }

        vehicles.forEach(vehicle => {
            if (vehicle.id in vehicleMarkers) {
                // Mettre à jour la position du marqueur existant
                const marker = vehicleMarkers[vehicle.id];
                marker.setLatLng([vehicle.lat, vehicle.lon]);

                //Si la position du vehicule est differente de se pasotion precedente
                if (marker.getLatLng().lat != vehicle.lat || marker.getLatLng().lng != vehicle.lon) {
                    marker.setIcon(icon_fire_truck_moving);
                }else {
                    marker.setIcon(icon_fire_truck);
                }
            } else {
                const marker = L.marker([vehicle.lat, vehicle.lon], { icon: icon_fire_truck }).addTo(map);

                const popupContent = `
                    <strong>ID:</strong> ${vehicle.id}<br>
                    <strong>Type:</strong> ${vehicle.type}<br>
                    <strong>Type de Liquide:</strong> ${vehicle.liquidType}<br>
                    <strong>Quantit&#xE9 de Liquide:</strong> ${vehicle.liquidQuantity}<br>
                    <strong>Essence:</strong> ${vehicle.fuel}<br>
                `;

                marker.bindPopup(popupContent);

                vehicleMarkers[vehicle.id] = marker;
            }
        });
    });
}


function displayData(map) {
    displayFires(map);
    displayVehicles(map);
}

displayData(map);
setInterval(function() {
    displayData(map);
}, 1000);


