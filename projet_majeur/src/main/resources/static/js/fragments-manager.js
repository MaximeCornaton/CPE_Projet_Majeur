// Récupérer tous les éléments <fragment>
const fragmentElements = document.getElementsByTagName('fragment');

// Fonction pour charger le contenu du fragment
function loadFragment(fragmentElement, fragmentName) {
    // Effectuer une requête HTTP asynchrone pour récupérer le contenu du fragment
    var xhr = new XMLHttpRequest();
    xhr.open('GET', 'fragments/' + fragmentName + '.html', true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let fragmentContent = xhr.responseText;

            // Si le fragment est la balise <head>, on remplace le titre de la page
            if (fragmentName === "head") {
                document.title = fragmentElement.getAttribute('data-title');
            }

            // Remplacer l'élément <fragment> par le contenu récupéré
            fragmentElement.outerHTML = fragmentContent;
        }
    };
    xhr.send();
}

// Parcourir tous les éléments <fragment>
for (let i = 0; i < fragmentElements.length; i++) {
    let fragmentElement = fragmentElements[i];
    let fragmentName = fragmentElement.getAttribute('data-fragment');

    // Charger le contenu du fragment en utilisant la fonction définie ci-dessus
    loadFragment(fragmentElement, fragmentName);
}
