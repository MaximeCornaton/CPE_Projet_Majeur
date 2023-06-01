function createTable(promise) {
    promise.then(json => {
        // Obtenez une référence à l'élément de tableau dans votre page HTML
        const tableElement = document.getElementById('dataTable');

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

// Utilisation de la fonction createTable avec la promesse retournée par getVehicles()
createTable(getVehicles());


