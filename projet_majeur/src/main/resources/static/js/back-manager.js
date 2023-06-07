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

function getVehicles() {
    return fetch('http://localhost:8000/fleet-service/vehicles', {
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

function getFireStation() {
    return fetch('http://localhost:8000/facility-service/facility/team', {
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

function postIntervention(fireId, vehicleId) {
    return fetch(`http://localhost:8000/inter-service/intervention?fireId=${fireId}&vehicleId=${vehicleId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
    })
}

//fonction pour ramener un camion a une caserne
function postReturnVehicle(vehicleId) {
    return fetch(`http://localhost:8000/fleet-service/vehicles/${vehicleId}/return`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
    })
    console.log("camion retourne");
}


//function qui supprime un camion
function deleteDeleteVehicle(vehicleId) {
    return fetch(`http://localhost:8000/fleet-service/vehicle/${vehicleId}/delete`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json"
        },
    })
    console.log("camion supprime");
}

//function pour ajouter un camion
function postAddVehicle(lon, lat, type, liquidType, liquidQuantity, fuel, crewMember, facilityRef) {
    return fetch(`http://localhost:8000/fleet-service/vehicle/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "lon": lon,
            "lat": lat,
            "type": type,
            "liquidType": liquidType,
            "liquidQuantity": liquidQuantity,
            "fuel": fuel,
            "crewMember": crewMember,
            "facilityRef": facilityRef
        })
    })
    console.log("camion ajoute");
}