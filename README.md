# Macros – Kalorientracker Webanwendung

Eine Webanwendung zum Tracken von Kalorien, Suchen von Gerichten über eine externe API, Anzeige des Wetters und Aktivitätsvorschläge basierend auf dem Wetter.

## Features

- Mahlzeiten erfassen, bearbeiten und löschen (CRUD)
- Tägliche Kalorienübersicht (gegessen vs. Ziel)
- Kalorienberechnung basierend auf Gewicht, Größe, Alter, Geschlecht und Ziel (Mifflin-St. Jeor-Formel)
- Rezeptsuche über Spoonacular API mit Nährwertinfos und Bildern
- Rezepte werden in der Datenbank gecacht um API-Tokens zu sparen
- Wetter anzeigen basierend auf der Stadt des Benutzers
- Aktivitätsvorschläge basierend auf Wetterbedingungen mit dynamischer Kalorienberechnung (MET-Werte × Gewicht)
- Swagger/OpenAPI Dokumentation

## Architektur

```
┌──────────┐      API     ┌──────────┐      API     ┌────────────────────┐
│ Frontend │ ◄──────────► │ Backend  │ ◄──────────► │ Spoonacular API    │
│ (React)  │              │ (Spring  │              │ OpenWeatherMap API │
└──────────┘              │  Boot)   │              └────────────────────┘
                          │          │
                          │    ▼     │
                          │ ┌──────┐ │
                          │ │  H2  │ │
                          │ └──────┘ │
                          └──────────┘
```

### Datenfluss

1. Der User interagiert mit dem **React-Frontend** (Meals erstellen, Rezepte suchen, Profil anlegen)
2. Das Frontend sendet HTTP-Requests an die **Spring Boot REST-API** (über den Vite Proxy)
3. Das Backend verarbeitet die Anfrage in der **Service-Schicht** (Geschäftslogik, Kalorienberechnung)
4. Daten werden über **Spring Data JPA** in der H2-Datenbank gespeichert oder abgerufen
5. Bei Rezeptsuche prüft das Backend zuerst die **Datenbank** (Cache) – nur wenn nichts gefunden wird, fragt es die **Spoonacular API** und speichert das Ergebnis
6. Bei Aktivitätsvorschlägen holt das Backend das Wetter von **OpenWeatherMap** und generiert passende Vorschläge basierend auf Wetterlage und Benutzergewicht

### Warum diese Architektur?

- **Frontend und Backend getrennt:** Können unabhängig entwickelt und deployed werden
- **API-Caching:** Spart Drittanbieter-Tokens, da Ergebnisse in der DB gespeichert werden
- **Schichtenarchitektur im Backend:** Klare Trennung von Verantwortlichkeiten (Separation of Concerns)

### Backend – Schichtenarchitektur

```
macros.macros
├── controller/     → HTTP-Endpoints, Statuscodes
├── service/        → Geschäftslogik
├── repository/     → Datenbankzugriff (Spring Data JPA)
├── model/          → JPA-Entitäten
├── dto/            → Data Transfer Objects (Request/Response)
├── mapper/         → Entity ↔ DTO Umwandlung
├── config/         → Konfiguration
└── exception/      → Fehlerbehandlung
```

### Frontend – Struktur

```
frontend/src
├── api/            → Typisierte API-Schicht (fetch)
├── pages/          → Seiten-Komponenten
├── types.ts        → TypeScript Typdefinitionen
├── App.tsx         → Router und Navigation
└── App.css         → Styling (getrennt von Logik)
```

## Technologie-Stack

| Komponente | Technologie |
|------------|-------------|
| Backend | Java 21, Spring Boot 4, Spring Data JPA |
| Frontend | React, TypeScript, Vite |
| Datenbank | H2 (file-basiert) |
| Build-Tool | Maven |
| Externe APIs | Spoonacular, OpenWeatherMap |
| API-Dokumentation | Swagger/OpenAPI (springdoc) |

## Entitäten und Beziehungen

```
User (1) ──── (n) Meal
User (1) ──── (n) ActivitySuggestion
Recipe (eigenständig, Cache für API-Ergebnisse)
```

- **User** – Benutzerprofil mit Kalorienzielen (Gewicht, Größe, Alter, Geschlecht, PAL-Wert, Ziel, Stadt)
- **Meal** – Mahlzeit mit Nährwerten (Name, Kalorien, Protein, Fett, Kohlenhydrate, Datum)
- **ActivitySuggestion** – Aktivitätsvorschlag (Typ, geschätzte verbrannte Kalorien, Wetterbedingung)
- **Recipe** – Gecachtes Rezept aus der Spoonacular API

## API-Endpoints

### Meals
| Methode | Endpoint | Beschreibung |
|---------|----------|--------------|
| GET | /api/meals | Alle Meals abrufen |
| GET | /api/meals/{id} | Meal nach ID |
| GET | /api/meals/user/{userId} | Meals eines Users |
| POST | /api/meals | Neues Meal erstellen |
| PUT | /api/meals/{id} | Meal aktualisieren |
| DELETE | /api/meals/{id} | Meal löschen |

### Users
| Methode | Endpoint | Beschreibung |
|---------|----------|--------------|
| GET | /api/users | Alle User abrufen |
| GET | /api/users/{id} | User nach ID |
| GET | /api/users/{id}/calories | Täglichen Kalorienbedarf berechnen |
| POST | /api/users | Neuen User erstellen |
| PUT | /api/users/{id} | User aktualisieren |
| DELETE | /api/users/{id} | User löschen |

### Rezeptsuche (Spoonacular)
| Methode | Endpoint | Beschreibung |
|---------|----------|--------------|
| GET | /api/recipes/search?query={query} | Rezepte suchen (erst DB, dann API) |

### Wetter und Aktivitäten (OpenWeatherMap)
| Methode | Endpoint | Beschreibung |
|---------|----------|--------------|
| GET | /api/weather?city={city} | Aktuelles Wetter abrufen |
| GET | /api/weather/activities/{userId} | Aktivitätsvorschläge basierend auf Wetter |
| GET | /api/weather/activities/{userId}/history | Bisherige Vorschläge anzeigen |

### Swagger UI
Die vollständige API-Dokumentation ist unter `/swagger-ui.html` erreichbar.

## Lokal starten

### Voraussetzungen
- Java 21
- Node.js 18+
- Maven (oder den mitgelieferten Maven Wrapper nutzen)

### Backend starten

```bash
cd Macros
./mvnw spring-boot:run
```

Das Backend läuft auf http://localhost:8080

### Frontend starten

```bash
cd Macros/frontend
npm install
npm run dev
```

Das Frontend läuft auf http://localhost:5173

### API Keys

Die API Keys werden aus der Datei `API_KEY` im Projektroot geladen. Erstelle diese Datei mit folgendem Inhalt:

```
SPOONACULAR_API_KEY=dein_spoonacular_key
OPENWEATHERMAP_API_KEY=dein_openweathermap_key
```

- Spoonacular Key: https://spoonacular.com/food-api (kostenlos)
- OpenWeatherMap Key: https://openweathermap.org/api (kostenlos)

### Tests ausführen

```bash
./mvnw test
```

## Drittanbieter-APIs

### Spoonacular API
- Rezeptsuche mit Nährwertinformationen
- Ergebnisse werden in der Datenbank gecacht um API-Aufrufe zu minimieren
- https://spoonacular.com/food-api

### OpenWeatherMap API
- Aktuelles Wetter für eine Stadt abrufen
- Wird genutzt um passende Aktivitätsvorschläge zu generieren
- https://openweathermap.org/api

## Kalorienberechnung

Die App verwendet die Mifflin-St. Jeor-Formel:

- Männer: BMR = (10 × Gewicht) + (6.25 × Größe) - (5 × Alter) + 5
- Frauen: BMR = (10 × Gewicht) + (6.25 × Größe) - (5 × Alter) - 161
- Gesamtumsatz = BMR × PAL-Wert
- Abnehmen: Gesamtumsatz - 500 kcal
- Zunehmen: Gesamtumsatz + 500 kcal

## Tests

13 Tests im Backend:
- **MealServiceTest** – CRUD-Operationen (create, findById, getMealsByUserIdAndDate, delete)
- **UserServiceTest** – Kalorienberechnung für Männer und Frauen (LOSE, MAINTAIN, GAIN)
- **MacrosApplicationTests** – Spring Context laden

## Docker

Die gesamte Anwendung kann mit Docker gestartet werden:

```bash
docker compose up --build
```

- Backend: http://localhost:8080
- Frontend: http://localhost:5173

**Hinweis:** Docker verwendet eine eigene H2-Datenbank. Die lokal gespeicherten Daten werden nicht mit Docker geteilt.

**Rezept-Cache:** Die Spoonacular API-Ergebnisse werden in der Datenbank gecacht. Wenn die API neue Rezepte für den gleichen Suchbegriff hat, werden diese nicht automatisch aktualisiert. Um den Cache zu leeren, kann die Datenbank-Datei `data/macrosdb.mv.db` gelöscht werden. Beim nächsten Start wird sie neu erstellt.

## KI-Nutzung

- Rechtschreibhilfe, da ich Legastheniker bin
- Amazon Q als Unterstützung um durch den Code zu schauen und Git Commits zu formulieren
- Ideenfindung für das Webprojekt
- Frontend Design mithilfe von KI erstellt und auch die Struktur mithilfe von KI aufgebaut
- Die README wurde mithilfe von KI sehr übersichtlich aufbereitet