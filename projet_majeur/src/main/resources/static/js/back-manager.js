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