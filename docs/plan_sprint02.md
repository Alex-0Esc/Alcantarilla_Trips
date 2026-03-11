# Sprint 02 - Planning Document

## 1. Sprint Goal

- Implementar la lógica de gestión de viajes e itinerarios siguiendo el patrón MVVM, con validación de datos, tests unitarios, soporte multilingüe y configuración persistente de usuario, cumpliendo con el Definition of Done del proyecto.

---

## 2. Sprint Backlog

| ID     | Tarea                                                                 | Responsable    | Estimación (h) | Prioridad |
|--------|-----------------------------------------------------------------------|----------------|----------------|-----------|
| T1.0   | Implement Travel Management Logic                                     | Alex, Samuel   | 4 pts          | Alta      |
| T1.1   | Implement inMemory CRUD for trips (addTrip, editTrip, deleteTrip) following MVVM | Samuel | 3 | Alta |
| T1.2   | Implement inMemory CRUD for itinerary/activity items (addActivity, updateActivity, deleteActivity) following MVVM | Alex | 3 | Alta |
| T1.3   | Ensure proper data validation (dates in future, required fields, DatePickers, date range logic) | Samuel | 2 | Alta |
| T1.4   | Implement user settings persisted with SharedPreferences (username, DOB, dark mode, language) | Alex | 2 | Alta |
| T1.5   | Implement multi-language support (minimum: en, ca, es)               | Samuel         | 2              | Media     |
| T2.0   | Design and Implement Itinerary Flow                                   | Alex, Samuel   | 3 pts          | Alta      |
| T2.1   | Structure user interaction flow: Menu → Travel → Itinerary (CRUD), UI → ViewModel → Repository → Data Source | Alex | 2 | Alta |
| T2.2   | Implement basic UI flow for adding and modifying trip details and itinerary (Menu → Travel → Itinerary) | Samuel | 3 | Alta |
| T2.3   | Ensure updates reflect dynamically in the main trip list and itinerary list | Alex | 1 | Media |
| T3.0   | Implement Data Validation and Testing                                 | Alex, Samuel   | 3 pts          | Alta      |
| T3.1   | Implement basic input validation in ViewModel and UI layer (empty fields, incorrect dates, out-of-range activity dates) | Samuel | 2 | Alta |
| T3.2   | Write unit tests for trip and itinerary CRUD operations               | Alex           | 2              | Alta      |
| T3.3   | Simulate user interactions and log errors or unexpected behaviors     | Samuel         | 1              | Media     |
| T3.4   | Update documentation with test results and fixes applied              | Alex           | 1              | Media     |
| T3.5   | Add Logcat logs and code commentaries following good practices        | Alex, Samuel   | 1              | Media     |

---

## 3. Definition of Done (DoD)

Un incremento del producto se considera **Done** cuando se cumplen todos los siguientes criterios:

### Código y ejecución
- El código está **completado**, integrado en la rama principal y **sin errores de compilación**.
- El proyecto se **ejecuta correctamente en Android Studio**, ya sea en emulador o dispositivo físico.
- Se han aplicado los **estándares de codificación** acordados por el equipo.
- La arquitectura **MVVM** está correctamente implementada: UI → ViewModel → Repository → Data Source.

### Gestión de viajes e itinerario
- Las operaciones CRUD de viajes y actividades funcionan correctamente en memoria.
- Los viajes contienen los campos mínimos: `title`, `startDate`, `endDate`, `description`.
- Las actividades contienen los campos mínimos: `title`, `description`, `date (LocalDate)`, `time (LocalTime)`.
- Todos los campos de fecha utilizan **DatePicker** y no permiten texto libre.
- La fecha de inicio del viaje es anterior a la fecha de fin.
- Las actividades están dentro del rango de fechas del viaje.

### Configuración y multilenguaje
- Los ajustes de usuario (`username`, `date of birth`, `dark mode`, `language`) se persisten correctamente con **SharedPreferences**.
- Las preferencias se cargan al iniciar la aplicación.
- La aplicación soporta un mínimo de **3 idiomas**: inglés (`en`), catalán (`ca`) y español (`es`).

### Validación y testing
- La validación de datos se implementa tanto en la capa **ViewModel** como en la **UI**.
- La UI muestra **mensajes de error claros** ante: campos vacíos, fechas incorrectas y actividades fuera del rango del viaje.
- Existen **tests unitarios** para las operaciones CRUD de viajes y actividades.
- Se han añadido **logs en Logcat** y comentarios en el código aplicando buenas prácticas.
- La documentación está actualizada con los resultados de los tests y los arreglos aplicados.

### Repositorio y control de versiones
- El código ha sido **subido y versionado correctamente**.
- El Sprint finaliza con una **release publicada** (`v0.2.0`).

### Entregable
- El incremento es **potencialmente entregable**.
- No existen **bloqueos críticos** que impidan continuar con el siguiente Sprint.

---

## 4. Riesgos identificados

- Falta de experiencia con el patrón MVVM en Android/Kotlin.
- Posibles dificultades integrando `SharedPreferences` con el cambio dinámico de idioma.
- La validación cruzada de fechas (rango viaje ↔ actividad) puede añadir complejidad no prevista.
- El tiempo estimado para los tests unitarios puede ser insuficiente si la implementación se retrasa.
- Gestión de estados en la UI al actualizar listas dinámicamente puede generar bugs difíciles de reproducir.