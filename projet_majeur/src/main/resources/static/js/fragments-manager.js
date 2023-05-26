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

            if (fragmentName === "map") {
                fragmentContent = processMapFragment(fragmentElement, fragmentContent);
                // Arrêter l'exécution de la fonction après le traitement du fragment "map"
            }

            // Remplacer l'élément <fragment> par le contenu récupéré
            fragmentElement.outerHTML = fragmentContent;
        }
    };
    xhr.send();
}

// Fonction spécifique pour traiter le fragment "map"
function processMapFragment(fragmentElement, fragmentContent) {
    // Créer une div temporaire pour contenir le fragment
    const tempDiv = document.createElement('div');
    tempDiv.innerHTML = fragmentContent;

    // Récupérer la div contenant la carte à partir de l'élément temporaire
    const mapDiv = tempDiv.querySelector('#map');

    // Créer l'élément iframe
    const iframe = document.createElement('iframe');
    iframe.src = "https://api.mapbox.com/styles/v1/maximecrnt/cli36w9f700ko01pgahnu8l6o.html?title=false&access_token=pk.eyJ1IjoibWF4aW1lY3JudCIsImEiOiJjbGkzNmdzbW4wcTJtM2ZvM2Z4azYxbTcwIn0.AmmVszgHGmq9KMbzdmhe7A&zoomwheel=false#11.93/45.76267/4.84871";
    iframe.title = "Monochrome";
    iframe.style.border = "none";
    iframe.style.width = "100%";
    iframe.style.height = "100%";

    // Vider le contenu de l'élément fragment
    fragmentElement.innerHTML = "";

    // Ajouter l'iframe à la div #map dans l'élément fragment
    fragmentElement.appendChild(iframe);

    return fragmentElement.outerHTML;
}


// Parcourir tous les éléments <fragment>
for (let i = 0; i < fragmentElements.length; i++) {
    let fragmentElement = fragmentElements[i];
    let fragmentName = fragmentElement.getAttribute('data-fragment');

    // Charger le contenu du fragment en utilisant la fonction définie ci-dessus
    loadFragment(fragmentElement, fragmentName);
}
