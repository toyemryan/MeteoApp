# App Meteo

Benvenuto nell'applicazione meteo! Questa app permette di consultare facilmente le previsioni del tempo per la giornata, le prossime ore (ogni 3 ore) e i prossimi cinque giorni. I dati meteorologici sono ottenuti in tempo reale grazie all'API di OpenWeatherMap.

## Funzionalità

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
