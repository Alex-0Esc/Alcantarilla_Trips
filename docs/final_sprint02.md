# Sprint 02 – Execution & Review

## 1. Resultados obtenidos

Comparación con Sprint Goal:

El objetivo del Sprint era implementar la lógica de gestión de viajes e itinerarios siguiendo el patrón MVVM, con validación de datos, tests unitarios, soporte multilingüe y configuración persistente de usuario, cumpliendo con el Definition of Done del proyecto. A continuación se detalla el grado de cumplimiento por bloque.

---

## 2. Tareas completadas

| ID | Tarea | Responsable | Completada | Comentarios                           |
|----|-------|-------------|------------|---------------------------------------|
| T1.0 | Implement Travel Management Logic | Alex, Samuel | Sí | Parcialmente completada               |
| T1.1 | Implement inMemory CRUD for trips (addTrip, editTrip, deleteTrip) following MVVM | Samuel | Sí | CRUD operativo en ViewModel           |
| T1.2 | Implement inMemory CRUD for itinerary/activity items following MVVM | Alex | Sí | Estructura base implementada          |
| T1.3 | Ensure proper data validation (dates, required fields, DatePickers) | Samuel | Sí | Validación en ViewModel y UI          |
| T1.4 | Implement user settings with SharedPreferences (username, DOB, dark mode, language) | Alex | Sí | Persistencia funcionando parcialmente |
| T1.5 | Implement multi-language support (en, ca, es) | Samuel | Sí | 3 idiomas implementados               |
| T2.0 | Design and Implement Itinerary Flow | Alex, Samuel | Sí |                                       |
| T2.1 | Structure user interaction flow: Menu → Travel → Itinerary | Alex | Sí | Flujo UI → ViewModel → Repository     |
| T2.2 | Implement basic UI flow for adding and modifying trip details and itinerary | Samuel | Sí | Pantallas de creación y edición       |
| T2.3 | Ensure updates reflect dynamically in main trip list and itinerary list | Alex | Sí | StateFlow/collectAsState correcto     |
| T3.0 | Implement Data Validation and Testing | Alex, Samuel | Sí |                                       |
| T3.1 | Implement basic input validation in ViewModel and UI layer | Samuel | Sí | Mensajes de error visibles en UI      |
| T3.2 | Write unit tests for trip and itinerary CRUD operations | Alex | Sí | Tests en src/test/java                |
| T3.3 | Simulate user interactions and log errors or unexpected behaviors | Samuel | Sí | Logs en Logcat añadidos               |
| T3.4 | Update documentation with test results and fixes applied | Alex | Sí | README y docs actualizados            |
| T3.5 | Add Logcat logs and code commentaries following good practices | Alex, Samuel | Sí | Comentarios en clases principales     |

---

## 3. Desviaciones

- La implementación del patrón MVVM requirió más tiempo del estimado al principio del Sprint, dado que ninguno de los dos tenía experiencia previa con esta arquitectura en Android/Kotlin.
- La validación cruzada de fechas entre el rango del viaje y las actividades añadió complejidad no prevista, lo que retrasó ligeramente la finalización de T1.3 y T3.1.
- La integración de SharedPreferences con el cambio dinámico de idioma resultó más complicada de lo esperado, requiriendo iteraciones adicionales para que las preferencias se cargaran correctamente al iniciar la aplicación.
- El tiempo dedicado a los tests unitarios fue ajustado pero suficiente gracias a haber resuelto antes los bloqueantes de compilación (ubicación incorrecta del archivo de tests y nombres de funciones no permitidos en Android).

---

## 4. Retrospectiva

### Qué funcionó bien

- La división de tareas entre Alex y Samuel fue clara y se respetó durante todo el Sprint.
- La comunicación del equipo mejoró respecto al Sprint anterior, con revisiones de código más frecuentes.
- Seguir el patrón MVVM desde el inicio facilitó la escritura de tests unitarios al final del Sprint.
- El soporte multilingüe (es, ca, en) se implementó sin incidencias gracias a la estructura de strings.xml.

### Qué no funcionó

- La estimación inicial de algunas tareas técnicas (especialmente T1.3 y T1.4) fue optimista.
- Algunos commits no seguían la convención acordada, lo que dificultó el seguimiento del historial de cambios.

### Qué mejoraremos en el próximo sprint

- Revisar las estimaciones con mayor detalle antes de cerrar el Sprint Planning.
- Aplicar una convención de commits más estricta (tipo: feat, fix, test, docs).
- Añadir más comentarios en el código durante el desarrollo, no solo al final.

---

## 5. Autoevaluación del equipo (0-10)

**Nota: 6/10**

**Justificación:** Consideramos que hemos cumplido los mínimos, pero creemos que podríamos haberlo hecho mucho mejor.