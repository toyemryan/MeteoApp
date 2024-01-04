# App Meteo

Benvenuto nell'applicazione meteo! Questa app permette di consultare facilmente le previsioni meteo del tempo per la giornata, le prossime ore (ogni 3 ore) e i prossimi cinque giorni. I dati meteorologici sono ottenuti in tempo reale grazie all'API di OpenWeatherMap.L'obiettive dell'applicazione è di fornire informazioni meteorologiche per qualsiasi città del mondo, in base all''utente che utilizza l'applicazione, ha la possibilità di cambiare la lingua e anche dell'unità di temperatura.

### Approccio dell'applicazione

La nostra applicazione propone un'interfaccia utente dove ogni utente dispone un profilo personale facilitando la navigazione tra le diverse funzionalità.

### Adatabilità
L'Applicazione si adatta alle preferenze individuali (la scelta della lingua , l'unita di temperatura, e anche la scelta della città per il quale si desidera visualizzare il meteo).

## Presentazione dei requisiti funzionli e non funzionali 

## Diagrama dei casi d'uso

![usecase](https://github.com/toyemryan/MeteoApp/assets/90463687/911553bb-e7ca-40c0-b8c8-434524392d51)

### Visualizzazione dati meteo:

- Previsioni per l'attuale giornata.
- Previsioni ogni 3 ore per la giornata.
- Previsioni per i prossimi cinque giorni.


 ### Localizzazione automatica:
 
Alla prima apertura dell'applicazione, viene richiesta all'utente l'attivazione della localizzazione del telefono. Se l'utente accetta, i dati meteo della sua posizione attuale vengono recuperati e mostrati a schermo. In caso di rifiuto, viene visualizzata una città predefinita (selezionata in cityList.dart).

### Filtraggio delle città:

Possibilità di filtrare una città nella lista delle città (file cityList.dart), per trovare facilmente una citta

## Configurazione

1 - API OpenWeatherMap: Ottieni una chiave API gratuita sul sito di OpenWeatherMap o usare direttamente questa chiave API: '0332ad98e737e68c5fac5e96935461a0' da sostituire nel codice nella directori (lib/api/api.dart)

2 - Autorizzazioni Android:

L'aggiungere delle autorizzazioni nel file (AndroidManifest.xml) sono necessarie sono neccessari per usare la connessione internet.

## Installazione

- Clonare il repository: git clone https://github.com/c-ode6968/meteoapp.git

- Installare le dipendenze e aggiornare se neccessario:

     flutter pub get

- Eseguire l'applicazione:
     flutter run

## Autori
- Toyem Ryan
- Djouaka Lionel
