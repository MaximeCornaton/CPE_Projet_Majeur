function createDeleteButton(vehicleId) {
    const deleteButton = document.createElement('a');
    deleteButton.className = 'btn btn-danger btn-icon-split';
    const iconSpan = document.createElement('span');
    iconSpan.className = 'icon text-white-50';
    const icon = document.createElement('i');
    icon.className = 'fas fa-trash';
    iconSpan.appendChild(icon);
    const textSpan = document.createElement('span');
    textSpan.className = 'text';
    textSpan.textContent = 'Supprimer';
    deleteButton.appendChild(iconSpan);
    deleteButton.appendChild(textSpan);
    deleteButton.addEventListener('click', () => {
        // Supprimez le véhicule correspondant ici
        deleteDeleteVehicle(vehicleId);
    });
    return deleteButton;
}

function createTable(promise) {
    promise.then(json => {
        // Obtenez une référence à l'élément de tableau dans votre page HTML
        const tableElement = document.getElementById('dataTable');
        tableElement.innerHTML = '';

        // Créez l'en-tête du tableau
        const tableHeader = document.createElement('thead');
        const headerRow = document.createElement('tr');
        const tableFooter = document.createElement('tfoot');
        const footerRow = document.createElement('tr');
        tableHeader.appendChild(headerRow);
        tableFooter.appendChild(footerRow);

        // Obtenez les clés de l'objet JSON
        const keys = Object.keys(json[0]);

        // Parcourez les clés et ajoutez-les en tant que colonnes d'en-tête
        keys.forEach(key => {
            const headerCell = document.createElement('th');
            const footerCell = document.createElement('th');
            headerCell.textContent = key;
            footerCell.textContent = key;
            headerRow.appendChild(headerCell);
            footerRow.appendChild(footerCell);
        });

        // Ajoutez l'en-tête au tableau
        tableElement.appendChild(tableHeader);
        tableElement.appendChild(tableFooter);

        // Créez le corps du tableau
        const tableBody = document.createElement('tbody');

        // Parcourez les objets JSON et créez une ligne pour chaque objet
        json.forEach(obj => {
            const row = document.createElement('tr');

            // Parcourez les clés et ajoutez les valeurs correspondantes en tant que cellules de ligne
            keys.forEach(key => {
                const cell = document.createElement('td');
                cell.textContent = obj[key];
                row.appendChild(cell);
            });

            // Ajoutez la ligne au corps du tableau
            tableBody.appendChild(row);
        });

        // Ajoutez le corps du tableau au tableau
        tableElement.appendChild(tableBody);
    });
}

//function qui ajoute une colonne avec un bouton pour supprimer un camion
// Fonction pour créer le tableau avec le bouton de suppression
function createTableWithDelete(promise) {
    promise.then(json => {
        // Obtenez une référence à l'élément de tableau dans votre page HTML
        const tableElement = document.getElementById('dataTable');
        tableElement.innerHTML = '';

        // Créez l'en-tête du tableau
        const tableHeader = document.createElement('thead');
        const headerRow = document.createElement('tr');
        const tableFooter = document.createElement('tfoot');
        const footerRow = document.createElement('tr');
        tableHeader.appendChild(headerRow);
        tableFooter.appendChild(footerRow);

        // Obtenez les clés de l'objet JSON
        const keys = Object.keys(json[0]);

        // Ajoutez une colonne supplémentaire pour le bouton de suppression
        keys.push('action');

        // Parcourez les clés et ajoutez-les en tant que colonnes d'en-tête
        keys.forEach(key => {
            const headerCell = document.createElement('th');
            const footerCell = document.createElement('th');
            headerCell.textContent = key;
            footerCell.textContent = key;
            headerRow.appendChild(headerCell);
            footerRow.appendChild(footerCell);
        });


        // Ajoutez l'en-tête au tableau
        tableElement.appendChild(tableHeader);
        tableElement.appendChild(tableFooter);

        // Créez le corps du tableau
        const tableBody = document.createElement('tbody');

        // Parcourez les objets JSON et créez une ligne pour chaque objet
        json.forEach(obj => {
            const row = document.createElement('tr');

            // Parcourez les clés et ajoutez les valeurs correspondantes en tant que cellules de ligne
            keys.forEach(key => {
                const cell = document.createElement('td');
                if (key === 'action') {
                    // Créez un bouton de suppression pour la colonne Supprimer
                    const deleteButton = createDeleteButton(obj.id);

                    cell.appendChild(deleteButton);
                    cell.style.textAlign = 'center';
                    //cell.style.border = 'none';
                } else {
                    cell.textContent = obj[key];
                }
                row.appendChild(cell);
            });

            // Ajoutez la ligne au corps du tableau
            tableBody.appendChild(row);
        });

        // Ajoutez le corps du tableau au tableau
        tableElement.appendChild(tableBody);
    });
}


function tableVehicles() {
    createTableWithDelete(getVehicles());

    function updateTable() {
        createTableWithDelete(getVehicles());
    }

    // Actualiser le tableau toutes les secondes
    setInterval(updateTable, 1000);

    updateTable();
}

function tableFires() {
    createTable(getFires());

    function updateTable() {
        createTable(getFires());
    }

    // Actualiser le tableau toutes les secondes
    setInterval(updateTable, 1000);

    updateTable();
}

function tableFireStation() {
    createTable(getFireStation());

    function updateTable() {
        createTable(getFireStation());
    }

    // Actualiser le tableau toutes les secondes
    setInterval(updateTable, 1000);

    updateTable();
}

function tableInterventions() {
    createTable(getInterventions());

    function updateTable() {
        createTable(getInterventions());
    }

    // Actualiser le tableau toutes les secondes
    setInterval(updateTable, 1000);

    updateTable();
}
