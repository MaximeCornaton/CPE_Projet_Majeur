map = createMap('map');
const firesLayer = L.layerGroup().addTo(map);
const vehiclesLayer = L.layerGroup().addTo(map);
const fireStationsLayer = L.layerGroup().addTo(map);
firesLayer.setZIndex(10);
vehiclesLayer.setZIndex(20);
fireStationsLayer.setZIndex(30);

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
function createMarker(layer, lat, lon, icon) {
    return L.marker([lat, lon], { icon: icon }).addTo(layer);
}

//fonction qui modifie l'icon du marqueur
function updateMarkerIcon(marker, icon) {
    marker.setIcon(icon);
}

//fonction qui supprime un marqueur
function removeMarker(marker) {
    marker.remove();
}

//fonction qui cache un marqueur
function hideMarker(marker) {
    marker.setOpacity(0);
}

//fonction qui affiche un marqueur
function showMarker(marker) {
    marker.setOpacity(1);
}


//fonction qui modifie la popup d'un marqueur
function updateMarkerPopup(marker, popupContent) {
    marker.setPopupContent(popupContent);
}


//fonction qui affiche les stations de pompiers
function displayFireStations(map) {
    const existingFireStations = []; // Stocker les identifiants des stations de pompiers existantes

    // Ajouter ou mettre à jour les marqueurs des nouvelles stations
    for (const id in fireStations_) {
        if (fireStationExists(id)) {
                const fireStation = fireStations_[id];
                existingFireStations.push(id); // Ajouter l'identifiant à existingFireStations

            if (!fireStationIsDisplayed(id)) {
                const marker = createMarker(fireStationsLayer, fireStation.lat, fireStation.lon, icon_fire_station);

                const popupContent = `
                    <strong>ID:</strong> ${fireStation.id}<br>
                    <strong>Nom:</strong> ${fireStation.name}<br>
                    <strong>Nombre de personne:</strong> ${fireStation.peopleCapacity}<br>
                    <strong>Nombre de v&#xE9;hicule max.:</strong> ${fireStation.maxVehicleSpace}<br>
                    <strong>Garage:</strong><br>
                `;

                marker.bindPopup(popupContent);

                // Événement d'affichage de la popup
                marker.on('popupopen', () => {
                    updateMarkerIcon(marker, icon_fire_station_moving);
                });

                // Événement de désaffichage de la popup
                marker.on('popupclose', () => {
                    updateMarkerIcon(marker, icon_fire_station);
                });

                fireStationsMarkers_[id] = marker;
            } else {
                popupContent = `
                    <strong>ID:</strong> ${fireStation.id}<br>
                    <strong>Nom:</strong> ${fireStation.name}<br>
                    <strong>Nombre de personne:</strong> ${fireStation.peopleCapacity}<br>
                    <strong>Nombre de v&#xE9;hicule max.:</strong> ${fireStation.maxVehicleSpace}<br>
                    <strong>Garage:</strong><br>
                `;
                for(const id_vehicle of getFireStationVehicles(id)){
                    const vehicle = vehicles_[id_vehicle];
                    if(isVehicleInFireStationId(id_vehicle, id)) {
                        popupContent += `
                            &#x25A0;  <span style="font-size: smaller;"> ${vehicle.type} - ${vehicle.id} - ${vehicle.liquidType}  </span><br>
                        `;
                    }else{
                        popupContent += `
    &#x25A0; <span style="color: #ff0000;font-size: smaller;cursor: pointer">${vehicle.type} - ${vehicle.id} - ${vehicle.liquidType}    </span><button onclick="returnVehicleToFireStation('${vehicle.id}');" class="btn-circle btn-sm"><i class="fas fa-home"></i></button><br>
                        `;

                    }
                }
                updateFireStationPopup(id, popupContent);

            }
        }
    }

    // Supprimer les marqueurs des stations qui n'existent plus
    for (const id in fireStationsMarkers_) {
        if (!existingFireStations.includes(id)) {
            undisplayFireStation(id);
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

//fonction qui vérifie si une station de pompiers est vide
function isFireStationEmpty(id_station) {
    //si les coordonnées d'un camion de pompiers sont égales à celles de la caserne alors la caserne est pas vide
        const fireStation = fireStations_[id_station];
        for (const id in vehicles_) {
            const vehicle = vehicles_[id];
            if (vehicle.lat.toFixed(3) === fireStation.lat.toFixed(3) && vehicle.lon.toFixed(3) === fireStation.lon.toFixed(3)) {
                return false;
            }
        }
        return true;
}

//fonction qui retourne les positions des stations de pompiers
function getFireStationsPosition() {
    const positions = {};
    for (const id in fireStations_) {
        if (fireStationExists(id)) {
            const fireStation = fireStations_[id];
            positions[id] = { lat: fireStation.lat, lon: fireStation.lon };
        }
    }
    return positions;
}

//fonction qui retourne les véhicules présents dans une station
function getVehiclesInFireStation(id_station) {
    const vehicules = [];
    for (const id in vehicles_) {
        const vehicle = vehicles_[id];
        //console.log(fireStations_[id_station]);
        if (vehicle.lat.toFixed(3) === fireStations_[id_station].lat.toFixed(3) && vehicle.lon.toFixed(3) === fireStations_[id_station].lon.toFixed(3)) {
            vehicules.push(vehicle.id);
        }
    }
    return vehicules;
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

//fonction qui modifie la popup d'une station de pompiers
function updateFireStationPopup(id, popupContent) {
    if (fireStationIsDisplayed(id)) {
        updateMarkerPopup(fireStationsMarkers_[id], popupContent);
    }
}

//fonction qui retourne les camions d'une station
function getFireStationVehicles(id_station) {
    return fireStations_[id_station].vehicleIdSet;
}


//fonction qui affiche les feux
function displayFires(map) {
    const existingFires = []; // Stocker les identifiants des stations de pompiers existantes

    // Ajouter ou mettre à jour les marqueurs des nouveaux feux
    for (const id in fires_) {
        if (fireExists(id)) {
            const fire = fires_[id];
            existingFires.push(id);
            if (!fireIsDisplayed(id)) {
                const marker = createMarker(firesLayer, fire.lat, fire.lon, icon_fire);

                const popupContent = `
                    <strong>ID:</strong> ${fire.id}<br>
                    <strong>Type:</strong> ${fire.type}<br>
                    <strong>Intensit&#xE9;:</strong> ${fire.intensity}<br>
                    <strong>Surface:</strong> ${fire.range}<br>
                    <select id="option-${fire.id}" onchange="updateVehicleOptions(${fire.id})">
                        <option value="0">--Option</option>
                        <option value="1">Type</option>
                    </select>
                    <select id="selectVehicle-${fire.id}">
                        
                        ${getVehicleOptions(fire.type, `option-${fire.id}`)}
                    </select>
                    <button onclick="sendVehicle(${fire.id}, document.getElementById('selectVehicle-${fire.id}').value)">Envoyer</button>
                `;

                marker.bindPopup(popupContent);

                firesMarkers_[id] = marker;

            }
        }
    }

    // Supprimer les marqueurs des feux qui n'existent plus
    for (const id in firesMarkers_) {
        if (!existingFires.includes(id)) {
            undisplayFire(id);
        }
    }
}

//uptade la popup d'un feu
function uptadeFirePopup(id, popupContent) {
    if (fireStationIsDisplayed(id)) {
        updateMarkerPopup(fireStationsMarkers_[id], popupContent);
    }
}

function updateVehicleOptions(fireId) {
    const optionSelected = document.getElementById(`option-${fireId}`).value;
    const selectVehicle = document.getElementById(`selectVehicle-${fireId}`);

    const fireType = fires_[fireId].type;
    selectVehicle.innerHTML = getVehicleOptions(fireType, optionSelected);
}

function getVehicleOptions(fire_type, option_selected) {
    let options = '<option value="0">--V&#xE9;hicule</option>';

    for (const id in vehicles_) {
        if (vehicleExists(id)) {

            const vehicle = vehicles_[id];
            if (option_selected === "1") {
                if (vehicle.type === fire_type) {
                    options += `<option value="${vehicle.id}">${vehicle.type} - ${vehicle.id}</option>`;
                }
            } else {
                options += `<option value="${vehicle.id}">${vehicle.type} - ${vehicle.id}</option>`;
            }
        }
    }
    return options;
}



//fonction qui envoie un véhicule
function sendVehicle(fireId, vehicleId) {
    console.log(fireId, vehicleId);
    if(vehicleId == 0) {
        console.log('Véhicule non sélectionné');
        return;
    }
    postIntervention(fireId, vehicleId).then(() => {
        console.log('Intervention envoyée');
    });
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
                const marker = createMarker(vehiclesLayer, vehicle.lat, vehicle.lon, icon_fire_truck);

                const popupContent = `
                    <strong>ID:</strong> ${vehicle.id}<br>
                    <strong>Type:</strong> ${vehicle.type}<br>
                    <strong>Type de liquide:</strong> ${vehicle.liquidType}<br>
                    <strong>Quantit&#xE9; de poudre:</strong> ${vehicle.liquidQuantity}<br>
                    <strong>Carburant:</strong> ${vehicle.fuel}<br>
                    <strong>Nombre de membres d'&#xE9;quipage:</strong> ${vehicle.crewMember}<br>
                `;

                marker.bindPopup(popupContent);

                // marker.on('popupopen', function() {
                //     updateVehicleOptions(id);
                // });

                vehiclesMarkers_[id] = marker;

            } else {
                if (isVehicleMoving(id)) {
                    vehiclesMarkers_[id].setIcon(icon_fire_truck_moving);
                }else{
                    if(isVehicleInFireStation(id)){
                        hideMarker(vehiclesMarkers_[id]);
                    }else{
                        showMarker(vehiclesMarkers_[id]);
                        vehiclesMarkers_[id].setIcon(icon_fire_truck);
                    }
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

//fonction qui verifie si un camion est dans une caserne
function isVehicleInFireStation(id_) {
    const fireStationPositions = getFireStationsPosition();
    for (const id in fireStationPositions) {
            if (fireStationPositions[id].lat.toFixed(3) === vehicles_[id_].lat.toFixed(3) && fireStationPositions[id].lon.toFixed(3) === vehicles_[id_].lon.toFixed(3)) {
            return true;
        }
    }
    return false;
}

//fonction qui verifie si un camion est dans une caserne donnée
function isVehicleInFireStationId(id_vehicle, id) {
    const fireStationPositions = getFireStationsPosition();
    return fireStationPositions[id].lat.toFixed(3) === vehicles_[id_vehicle].lat.toFixed(3) && fireStationPositions[id].lon.toFixed(3) === vehicles_[id_vehicle].lon.toFixed(3);
}

//fonction qui fait rentrer un camion dans sa caserne
function returnVehicleToFireStation(id) {
    console.log(id);
    if (isVehicleInFireStation(id)) {
        console.log('Le camion est déjà dans sa caserne');
        return;
    }
    postReturnVehicle(id).then(() => {
        console.log('Le camion est retourné à sa caserne');
    });
}

//fonction qui affiche les feux et les vehicules sur la carte
function displayData(map) {
    displayVehicles(map);
    displayFires(map)
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
