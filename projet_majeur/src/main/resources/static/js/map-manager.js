map = createMap('map');

const vehicles_ = {};
const fires_ = {};
const fireStations_ = {};
const vehiclesMarkers_ = {};
const firesMarkers_ = {};
const fireStationsMarkers_ = {};

const icon_fire_truck = L.icon({
    iconUrl: '../img/icons/fire-truck.png',
    iconSize: [32, 32],
    iconAnchor: [16, 16],
});

const icon_fire_truck_moving = L.icon({
    iconUrl: '../img/icons/fire-truck.gif',
    iconSize: [40, 40],
    iconAnchor: [20, 20],
});

const icon_fire = L.icon({
    iconUrl: '../img/icons/fire.gif',
    iconSize: [32, 32],
    iconAnchor: [16, 16],
});

const icon_fire_station = L.icon({
    iconUrl: '../img/icons/fire-station.png',
    iconSize: [46, 46],
    iconAnchor: [23, 23],
});

const icon_fire_station_moving = L.icon({
    iconUrl: '../img/icons/fire-station.gif',
    iconSize: [62, 62],
    iconAnchor: [31, 31],
})

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

//fonction qui créer les marqueurs
function createMarker(map, lat, lon, icon) {
    return L.marker([lat, lon], { icon: icon }).addTo(map);
}

//fonction qui modifie l'icon du marqueur
function updateMarkerIcon(marker, icon) {
    marker.setIcon(icon);
}

//fonction qui suppime un marqueur
function removeMarker(marker) {
    marker.remove();
}


//fonction qui affiche les stations de pompiers
function displayFireStations(map) {
    // Ajouter ou mettre à jour les marqueurs des nouvelles stations
    for (const id in fireStations_) {
        if (fireStationExists(id)) {
            const fireStation = fireStations_[id];
            if (!fireStationIsDisplayed(id)) {
                const marker = createMarker(map, fireStation.lat, fireStation.lon, icon_fire_station);

                const popupContent = `
                    <strong>ID:</strong> ${fireStation.id}<br>
                    <strong>Nom:</strong> ${fireStation.name}<br>
                    <strong>Capacit&#xE9;:</strong> ${fireStation.peopleCapacity}<br>
                `;

                marker.bindPopup(popupContent);

                // Événement d'affichage de la popup
                marker.on('popupopen', () => {
                    updateMarkerIcon(marker,icon_fire_station_moving);
                });

                // Événement de désaffichage de la popup
                marker.on('popupclose', () => {
                    updateMarkerIcon(marker,icon_fire_station);
                });

                fireStationsMarkers_[id] = marker;
            }
        }
    }

    // Supprimer les marqueurs des stations qui n'existent plus
    for (const id in fireStationsMarkers_) {
        if (!fireStationExists(id)) {
            undisplayFireStation(id)
        }
    }
}

//fonction qui récupère les stations de pompiers
function refreshFireStations() {
    getFireStation().then(fireStations => {
        fireStations.forEach(fireStation => {
            fireStations_[fireStation.id] = fireStation;
        });
    });
}

//fonction qui vérifie si une station de pompiers existe
function fireStationExists(id) {
    return fireStations_.hasOwnProperty(id);
}

//fonction qui verifie si un feu est affiché
function fireStationIsDisplayed(id) {
    return fireStationsMarkers_.hasOwnProperty(id);
}

//fonction qui deaffiche une station de pompiers
function undisplayFireStation(id) {
    if (fireStationIsDisplayed(id)) {
        removeMarker(fireStationsMarkers_[id]);
        delete fireStationsMarkers_[id];
    }
}


//fonction qui affiche les feux
function displayFires(map) {
    // Ajouter ou mettre à jour les marqueurs des nouveaux feux
    for (const id in fires_) {
        if (fireExists(id)) {
            const fire = fires_[id];
            if (!fireIsDisplayed(id)) {
                const marker = createMarker(map, fire.lat, fire.lon, icon_fire);

                const popupContent = `
                    <strong>ID:</strong> ${fire.id}<br>
                    <strong>Type:</strong> ${fire.type}<br>
                    <strong>Intensit&#xE9;:</strong> ${fire.intensity}<br>
                    <strong>Surface:</strong> ${fire.range}<br>
                `;

                marker.bindPopup(popupContent);

                firesMarkers_[id] = marker;
            }
        }
    }

    // Supprimer les marqueurs des feux qui n'existent plus
    for (const id in firesMarkers_) {
        if (!fireExists(id)) {
            undisplayFire(id)
        }
    }
}

//fonction qui récupère les feux
function refreshFires() {
    getFires().then(fires => {
        fires.forEach(fire => {
            fires_[fire.id] = fire;
        });
    });
}

//fonction qui vérifie si un feu existe
function fireExists(id) {
    return fires_.hasOwnProperty(id);
}

//fonction qui vérifie si un feu est affiché
function fireIsDisplayed(id) {
    return firesMarkers_.hasOwnProperty(id);
}

//fonction qui deaffiche un feu
function undisplayFire(id) {
    if (fireIsDisplayed(id)) {
        removeMarker(firesMarkers_[id]);
        delete firesMarkers_[id];
    }
}


//fonction qui affiche les véhicules
function displayVehicles(map) {
    // Ajouter ou mettre à jour les marqueurs des nouveaux véhicules
    for (const id in vehicles_) {
        if (vehicleExists(id)) {
            const vehicle = vehicles_[id];
            if (!vehicleIsDisplayed(id)) {
                const marker = createMarker(map, vehicle.lat, vehicle.lon, icon_fire_truck);

                const popupContent = `
                    <strong>ID:</strong> ${vehicle.id}<br>
                    <strong>Type:</strong> ${vehicle.type}<br>
                    <strong>Type de poudre:</strong> ${vehicle.liquidType}<br>
                    <strong>Quantit&#xE9; de poudre:</strong> ${vehicle.liquidQuantity}<br>
                    <strong>Carburant:</strong> ${vehicle.fuel}<br>
                    <strong>Nombre de membres d'&#xE9;quipage:</strong> ${vehicle.crewMember}<br>
                `;

                marker.bindPopup(popupContent);

                vehiclesMarkers_[id] = marker;

            } else {
                if (isVehicleMoving(id)) {
                    vehiclesMarkers_[id].setIcon(icon_fire_truck_moving);
                }else{
                    vehiclesMarkers_[id].setIcon(icon_fire_truck);
                }
                vehiclesMarkers_[id].setLatLng([vehicle.lat, vehicle.lon]);
            }
        }
    }

    // Supprimer les marqueurs des véhicules qui n'existent plus
    for (const id in vehiclesMarkers_) {
        if (!vehicleExists(id)) {
            undisplayVehicle(id)
        }
    }
}


//fonction qui récupère les camions
function refreshVehicles() {
    getVehicles().then(vehicles => {
        vehicles.forEach(vehicle => {
            vehicles_[vehicle.id] = vehicle;
        });
    });
}

//fonction qui vérifie si un camion existe
function vehicleExists(id) {
    return vehicles_.hasOwnProperty(id);
}

//fonction qui vérifie si un camion est affiché
function vehicleIsDisplayed(id) {
    return vehiclesMarkers_.hasOwnProperty(id);
}

//fonction qui deaffiche un camion
function undisplayVehicle(id) {
    if (vehicleIsDisplayed(id)) {
        removeMarker(vehiclesMarkers_[id]);
        delete vehiclesMarkers_[id];
    }
}

//fonction qui vérifie si un camion est en mouvement
function isVehicleMoving(id) {
    return vehiclesMarkers_[id].getLatLng().lat !== vehicles_[id].lat || vehiclesMarkers_[id].getLatLng().lng !== vehicles_[id].lon;
}

//fonction qui affiche les feux et les vehicules sur la carte
function displayData(map) {
    displayFires(map);
    displayVehicles(map);
    displayFireStations(map)
}

//fonction qui recuperes les feux et vehicules
function refreshData() {
    refreshFires();
    refreshVehicles();
    refreshFireStations();
}

function initMap() {
    refreshData();
    displayData(map);
}

setInterval(function() {
    displayData(map);
}, 100);

setInterval(function() {
    refreshData();
}, 1000);


initMap();