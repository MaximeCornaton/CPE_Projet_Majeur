map = createMap('map');
let vehicleMarkers = {};
let fireMarkers = {};
let fireStationMarkers = {};

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

const icon_fire_station = L.icon({
    iconUrl: '../img/icons/fire-station.png',
    iconSize: [32, 32],
    iconAnchor: [16, 16],
});

//fonction qui cree la carte
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

//foction qui affiche la caserne
function displayFireStation(map) {
    getFireStation().then(fireStation => {

        // Supprimer les véhicules qui n'existent plus
        for (const id in fireStationMarkers) {
            if (!fireStation.some(fireStation => fireStation.id === id)) {
                removeFireStation(map, id);
            }
        }

        fireStation.forEach(fireStation => {

        const marker = L.marker([fireStation.lat, fireStation.lon], { icon: icon_fire_station }).addTo(map);

        const popupContent = `
            <strong>ID:</strong> ${fireStation.id}<br>
            <strong>Nom:</strong> ${fireStation.name}<br>
            <strong>Capacité:</strong> ${fireStation.peopleCapacity}<br>
        `;

        marker.bindPopup(popupContent);
        fireStationMarkers[fireStation.id] = marker;
    });
    });
}


//fonction qui affiche les feux
function displayFires(map) {
    getFires().then(fires => {
        // Supprimer les feux qui n'existent plus
        for (const id in fireMarkers) {
            if (!fires.some(fire => fire.id === id)) {
                removeFire(map, id);
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

//fonction qui affiche les vehicules
function displayVehicles(map) {
    getVehicles().then(vehicles => {
        // Supprimer les véhicules qui n'existent plus
        for (const id in vehicleMarkers) {
            if (!vehicles.some(vehicle => vehicle.id === id)) {
                removeVehicle(map, id);
            }
        }

        vehicles.forEach(vehicle => {
            if (vehicle.id in vehicleMarkers) {
                const marker = vehicleMarkers[vehicle.id];

                //Si la position du vehicule est differente de se pasotion precedente
                if (marker.getLatLng().lat != vehicle.lat || marker.getLatLng().lng != vehicle.lon) {
                    marker.setIcon(icon_fire_truck_moving);
                }else {
                    marker.setLatLng([vehicle.lat, vehicle.lon]);
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

//fonction qui desaffiche les feux de la carte
function removeFires(map) {
    for (const id in fireMarkers) {
        const marker = fireMarkers[id];
        map.removeLayer(marker);
        delete fireMarkers[id];
    }
}

//fonction qui desaffiche un feu de la carte
function removeFire(map, id) {
    const marker = fireMarkers[id];
    map.removeLayer(marker);
    delete fireMarkers[id];
}

//fonction qui desaffiche les vehicules de la carte
function removeVehicles(map) {
    for (const id in vehicleMarkers) {
        const marker = vehicleMarkers[id];
        map.removeLayer(marker);
        delete vehicleMarkers[id];
    }
}

//fonction qui desaffiche un vehicule de la carte
function removeVehicle(map, id) {
    const marker = vehicleMarkers[id];
    map.removeLayer(marker);
    delete vehicleMarkers[id];
}

//fonction qui desaffiche la caserne de la carte
function removeFireStations(map) {
    for (const id in fireStationMarkers) {
        const marker = fireStationMarkers[id];
        map.removeLayer(marker);
        delete fireStationMarkers[id];
    }
}

//fonction qui desaffiche une caserne de la carte
function removeFireStation(map, id) {
    const marker = fireStationMarkers[id];
    map.removeLayer(marker);
    delete fireStationMarkers[id];
}

//fonction qui affiche les feux et les vehicules sur la carte
function displayData(map) {
    displayFires(map);
    displayVehicles(map);
    displayFireStation(map);
}

displayData(map);
setInterval(function() {
    displayData(map);
}, 1000);


