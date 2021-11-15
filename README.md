# PGR203 Avansert Java eksamen

[![Java CI with Maven](https://github.com/kristiania-pgr203-2021/pgr203-exam-attichouse/actions/workflows/maven.yml/badge.svg)](https://github.com/kristiania-pgr203-2021/pgr203-exam-attichouse/actions/workflows/maven.yml)



## Beskriv hvordan programmet skal testes:

Eksamen har gått ut på å opprette spørreundersøkelser. Vårt prosjekt sin funksjonalitet går ut på at man kan opprette ulike spørreundersøkelser, ta spørreundersøkelsene, endre spørsmålene og liste ut svarene på de ulike spørsmålene. 

Du kjører programmet fra metoden main i klassen Program og trykker deg inn på http://localhost:8080/index.html. Der starter du med å trykke på den øverste knappen som heter «Create a new Survey». Der velger du hva spørreundersøkelsen din skal hete og trykker «Submit». Så legger du inn et eller flere spørsmål som du ønsker å ha med i undersøkelsen og legger til svaralternativer som du skal skille med semikolon. Til slutt trykker du på den nederste «Submit» knappen.

Etter du har gjort dette kan du trykke på «back» for å komme tilbake til startsiden. Her kan du videre velge «Take Survey». Der velger du hvilken spørreundersøkelse du ønsker og ta og så kommer alternativene som er lagt inn opp. Du velger det du ønsker å svare på de ulike spørsmålene og trykker på «Submit». 

Den tredje knappen på siden heter «Change questions» og her kan du endre på de spørsmålene som er lagt inn. Først velger du hvilken survey og så hvilket spørsmål du ønsker å endre før du trykker på «Submit».

På den siste knappen som heter «Print answers» kan du først velge en survey, deretter velge spørsmål og skrive ut alle svarene som har kommet inn på hvert spørsmål.

## Korreksjoner av eksamensteksten i Wiseflow:

**DET ER EN FEIL I EKSEMPELKODEN I WISEFLOW:**

* I `addOptions.html` skulle url til `/api/tasks` vært `/api/alternativeAnswers` (eller noe sånt)

**Det er viktig å være klar over at eksempel HTML i eksamensoppgaven kun er til illustrasjon. Eksemplene er ikke tilstrekkelig for å løse alle ekstraoppgavene og kandidatene må endre HTML-en for å være tilpasset sin besvarelse**


## Sjekkliste

## Vedlegg: Sjekkliste for innlevering

* [ ] Dere har lest eksamensteksten
* [ ] Dere har lastet opp en ZIP-fil med navn basert på navnet på deres Github repository
* [ ] Koden er sjekket inn på github.com/pgr203-2021-repository
* [ ] Dere har committed kode med begge prosjektdeltagernes GitHub konto (alternativt: README beskriver arbeidsform)

### README.md

* [ ] `README.md` inneholder en korrekt link til Github Actions
* [ ] `README.md` beskriver prosjektets funksjonalitet, hvordan man bygger det og hvordan man kjører det
* [ ] `README.md` beskriver eventuell ekstra leveranse utover minimum
* [ ] `README.md` inneholder et diagram som viser datamodellen

### Koden

* [ ] `mvn package` bygger en executable jar-fil
* [ ] Koden inneholder et godt sett med tester
* [ ] `java -jar target/...jar` (etter `mvn package`) lar bruker legge til og liste ut data fra databasen via webgrensesnitt
* [ ] Serveren leser HTML-filer fra JAR-filen slik at den ikke er avhengig av å kjøre i samme directory som kildekoden
* [ ] Programmet leser `dataSource.url`, `dataSource.username` og `dataSource.password` fra `pgr203.properties` for å connecte til databasen
* [ ] Programmet bruker Flywaydb for å sette opp databaseskjema
* [ ] Server skriver nyttige loggmeldinger, inkludert informasjon om hvilken URL den kjører på ved oppstart

### Funksjonalitet

* [ ] Programmet kan opprette spørsmål og lagrer disse i databasen (påkrevd for bestått)
* [ ] Programmet kan vise spørsmål (påkrevd for D)
* [ ] Programmet kan legge til alternativ for spørsmål (påkrevd for D)
* [ ] Programmet kan registrere svar på spørsmål (påkrevd for C)
* [ ] Programmet kan endre tittel og tekst på et spørsmål (påkrevd for B)

### Ekstraspørsmål (dere må løse mange/noen av disse for å oppnå A/B)

* [ ] Før en bruker svarer på et spørsmål hadde det vært fint å la brukeren registrere navnet sitt. Klarer dere å implementere dette med cookies? Lag en form med en POST request der serveren sender tilbake Set-Cookie headeren. Browseren vil sende en Cookie header tilbake i alle requester. Bruk denne til å legge inn navnet på brukerens svar
* [ ] Når brukeren utfører en POST hadde det vært fint å sende brukeren tilbake til dit de var før. Det krever at man svarer med response code 303 See other og headeren Location
* [ ] Når brukeren skriver inn en tekst på norsk må man passe på å få encoding riktig. Klarer dere å lage en <form> med action=POST og encoding=UTF-8 som fungerer med norske tegn? Klarer dere å få æøå til å fungere i tester som gjør både POST og GET?
* [ ] Å opprette og liste spørsmål hadde vært logisk og REST-fult å gjøre med GET /api/questions og POST /api/questions. Klarer dere å endre måten dere hånderer controllers på slik at en GET og en POST request kan ha samme request target?
* [ ] Vi har sett på hvordan å bruke AbstractDao for å få felles kode for retrieve og list. Kan dere bruke felles kode i AbstractDao for å unngå duplisering av inserts og updates?
* [ ] Dersom noe alvorlig galt skjer vil serveren krasje. Serveren burde i stedet logge dette og returnere en status code 500 til brukeren
* [ ] Dersom brukeren går til http://localhost:8080 får man 404. Serveren burde i stedet returnere innholdet av index.html
* [ ] Et favorittikon er et lite ikon som nettleseren viser i tab-vinduer for en webapplikasjon. Kan dere lage et favorittikon for deres server? Tips: ikonet er en binærfil og ikke en tekst og det går derfor ikke an å laste den inn i en StringBuilder
* [ ] I forelesningen har vi sett på å innføre begrepet Controllers for å organisere logikken i serveren. Unntaket fra det som håndteres med controllers er håndtering av filer på disk. Kan dere skrive om HttpServer til å bruke en FileController for å lese filer fra disk?
* [ ] Kan dere lage noen diagrammer som illustrerer hvordan programmet deres virker?
* [ ] JDBC koden fra forelesningen har en feil ved retrieve dersom id ikke finnes. Kan dere rette denne?
* [ ] I forelesningen fikk vi en rar feil med CSS når vi hadde `<!DOCTYPE html>`. Grunnen til det er feil content-type. Klarer dere å fikse det slik at det fungerer å ha `<!DOCTYPE html>` på starten av alle HTML-filer?
* [ ] Klarer dere å lage en Coverage-rapport med GitHub Actions med Coveralls? (Advarsel: Foreleser har nylig opplevd feil med Coveralls så det er ikke sikkert dere får det til å virke)
* [ ] FARLIG: I løpet av kurset har HttpServer og tester fått funksjonalitet som ikke lenger er nødvendig. Klarer dere å fjerne alt som er overflødig nå uten å også fjerne kode som fortsatt har verdi? (Advarsel: Denne kan trekke ned dersom dere gjør det feil!)
