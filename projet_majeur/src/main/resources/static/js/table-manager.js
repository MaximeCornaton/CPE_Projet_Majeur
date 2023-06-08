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



function tableVehicles() {

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

    //function qui ajoute une colonne avec un bouton pour supprimer un camion
// Fonction pour créer le tableau avec le bouton de suppression
    function createTableWithDelete(promise) {
        promise.then(json => {
            const tableElement = document.getElementById('dataTable');
            tableElement.innerHTML = '';

            if (json.length === 0) {
                const emptyRow = document.createElement('tr');
                const emptyCell = document.createElement('td');
                emptyCell.setAttribute('colspan', '4');
                emptyCell.textContent = 'Tableau vide';
                emptyRow.appendChild(emptyCell);
                tableElement.appendChild(emptyRow);
                return;
            }

            const tableHeader = document.createElement('thead');
            const headerRow = document.createElement('tr');
            const tableFooter = document.createElement('tfoot');
            const footerRow = document.createElement('tr');
            tableHeader.appendChild(headerRow);
            tableFooter.appendChild(footerRow);

            const keys = Object.keys(json[0]);
            keys.push('action');

            keys.forEach(key => {
                const headerCell = document.createElement('th');
                const footerCell = document.createElement('th');
                headerCell.textContent = key;
                footerCell.textContent = key;
                headerRow.appendChild(headerCell);
                footerRow.appendChild(footerCell);
            });

            tableElement.appendChild(tableHeader);
            tableElement.appendChild(tableFooter);

            const tableBody = document.createElement('tbody');

            json.forEach(obj => {
                const row = document.createElement('tr');
                keys.forEach(key => {
                    const cell = document.createElement('td');
                    if (key === 'action') {
                        const deleteButton = createDeleteButton(obj.id);
                        cell.appendChild(deleteButton);
                        cell.style.textAlign = 'center';
                    } else {
                        cell.textContent = obj[key];
                    }
                    row.appendChild(cell);
                });
                tableBody.appendChild(row);
            });

            tableElement.appendChild(tableBody);
        });
    }

    function updateTable() {
        createTableWithDelete(getVehicles());
    }

    // Actualiser le tableau toutes les secondes
    setInterval(updateTable, 1000);

    updateTable();
}

function tableFires() {

    function updateTable() {
        createTable(getFires());
    }

    // Actualiser le tableau toutes les secondes
    setInterval(updateTable, 1000);

    updateTable();
}

function tableFireStation() {

    function updateTable() {
        createTable(getFireStation());
    }

    // Actualiser le tableau toutes les secondes
    setInterval(updateTable, 1000);

    updateTable();
}

function tableInterventions() {
    let filter = 'all';
    function getInterventionsWithFilter(filter) {
        if(filter === 'all') {
            return getInterventions();
        } else if(filter === 'in_progress') {
            return getInterventionsInProgress();
        } else if(filter === 'finished') {
            return getInterventionsDone();
        }
    }

    function updateTable(filter) {
        createTable(getInterventionsWithFilter(filter));
    }

    function createTable(promises) {
        const table = document.getElementById('dataTable');
        table.innerHTML = '';
        const thead = document.createElement('thead');
        const tfoot = document.createElement('tfoot');
        const tbody = document.createElement('tbody');

        // Create table headers
        const headers = ['idFire', 'idVehicle', 'status'];
        const headerRow = document.createElement('tr');
        const footerRow = document.createElement('tr');
        headers.forEach((headerText) => {
            const header = document.createElement('th');
            const footer = document.createElement('th');
            header.appendChild(document.createTextNode(headerText));
            footer.appendChild(document.createTextNode(headerText));
            headerRow.appendChild(header);
            footerRow.appendChild(footer);
        });
        thead.appendChild(headerRow);
        tfoot.appendChild(footerRow);

        // Create table rows with data
        promises.then((promise) => {
            promise.forEach((intervention) => {
                const row = document.createElement('tr');
                const idFire = document.createElement('td');
                const idVehicle = document.createElement('td');
                const status = document.createElement('td');
                idFire.appendChild(document.createTextNode(intervention.idFire));
                idVehicle.appendChild(document.createTextNode(intervention.idVehicle));
                status.appendChild(document.createTextNode(intervention.status));
                row.appendChild(idFire);
                row.appendChild(idVehicle);
                row.appendChild(status);
                tbody.appendChild(row);
            });
        });


        table.appendChild(thead);
        table.appendChild(tbody);
        table.appendChild(tfoot);
    }


    setInterval(function() {
        updateTable(filter);
    }, 1000);

    updateTable('all');

    const filters = document.querySelectorAll('.filter');
    filters.forEach(filterElement => {
        filterElement.addEventListener('click', function(event) {
            filter = event.target.getAttribute('data-filter');
            updateTable(filter);
        });
    });
}
