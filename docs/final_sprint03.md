# Sprint 03 – Execution & Review

## 1. Resultados obtenidos

### Comparación con Sprint Goal

El objetivo del Sprint era implementar la persistencia de datos con Room (SQLite), el sistema de autenticación de usuarios mediante Firebase (login, registro y recuperación de contraseña), la persistencia de información de usuario y viajes multi-usuario, y las pruebas y validaciones correspondientes, cumpliendo con el Definition of Done del proyecto. A continuación se detalla el grado de cumplimiento por bloque.

---

## 2. Tareas completadas

| ID   | Tarea                                                                                          | Responsable  | Completada | Comentarios                                                      |
|------|------------------------------------------------------------------------------------------------|--------------|------------|------------------------------------------------------------------|
| T1.0 | Implement SQLite Persistence using Room                                                        | Alex, Samuel | Sí         | Base de datos Room operativa                                     |
| T1.1 | Create Room Database class                                                                     | Samuel       | Sí         | Clase AppDatabase creada y configurada                           |
| T1.2 | Define Entities for Trip and ItineraryItem                                                     | Alex         | Sí         | Entidades con campos datetime, text e integer                    |
| T1.3 | Create DAOs for database operations                                                            | Samuel       | Sí         | DAOs con operaciones CRUD completas                              |
| T1.4 | Implement CRUD operations using DAO for trips and itineraries                                  | Alex         | Sí         | CRUD funcional con Room                                          |
| T1.5 | Modify ViewModels to use Room Database instead of in-memory storage                            | Samuel       | Sí         | Migración de in-memory a Room completada                         |
| T1.6 | Ensure UI updates when database changes                                                        | Alex         | Sí         | Flow/collectAsState correcto en todas las pantallas              |
| T2.0 | Login and Logout                                                                               | Alex, Samuel | Sí         | Autenticación Firebase funcional                                 |
| T2.1 | Connect app to Firebase                                                                        | Samuel       | Sí         | google-services.json integrado correctamente                     |
| T2.2 | Design android login screen (login form)                                                       | Alex         | Sí         | Pantalla de login con validación de campos                       |
| T2.3 | Implement login actions using Firebase (auth & password)                                       | Samuel       | Sí         | Login con email y contraseña operativo                           |
| T2.4 | Create logout action in the app                                                                | Alex         | Sí         | Logout redirige correctamente a la pantalla de login             |
| T2.5 | Use Logcat to track all operations and errors                                                  | Alex, Samuel | Sí         | Logs añadidos en flujos de autenticación y base de datos         |
| T3.0 | Register and Recover Password                                                                  | Alex, Samuel | Sí         | Flujo completo de registro y recuperación implementado           |
| T3.1 | Design android register screen (register form)                                                 | Alex         | Sí         | Pantalla de registro con todos los campos requeridos             |
| T3.2 | Implement registration using Firebase con patrón repositorio e email verification              | Samuel       | Sí         | Verificación de email activada; patrón repositorio aplicado      |
| T3.3 | Implement password recovery view and actions                                                   | Alex         | Sí         | Diálogo de recuperación por email funcional                      |
| T4.0 | Persist User Information and Trip                                                              | Alex, Samuel | Sí         | Persistencia multi-usuario operativa                             |
| T4.1 | Persist user info in local DB con verificación de username único                               | Samuel       | Sí         | Campos: login, username, birthdate, address, country, phone, accept_emails |
| T4.2 | Modify trip table to support multiple users                                                    | Alex         | Sí         | Filtrado por usuario autenticado correcto                        |
| T4.3 | Update documentation with database schema at design.md                                         | Samuel       | Sí         | design.md actualizado con esquema completo                       |
| T4.4 | Persist app access log (userid + datetime de cada login/logout)                                | Alex         | Sí         | Tabla AccessLog implementada y funcional                         |
| T5.0 | Testing and Debugging                                                                          | Alex, Samuel | Sí         | Tests y validaciones completados                                 |
| T5.1 | Write unit tests for DAOs and database interactions                                            | Alex         | Sí         | Tests en src/test/java con Room in-memory                        |
| T5.2 | Implement data validation (duplicate trip names, valid dates)                                  | Samuel       | Sí         | Validación en ViewModel y UI                                     |
| T5.3 | Use Logcat to track database operations and errors                                             | Alex, Samuel | Sí         | Logs en operaciones CRUD y errores de autenticación              |
| T5.4 | Update documentation with database schema and usage at design.md                               | Samuel       | Sí         | Documentación de tests y fixes incluida                          |

---

## 3. Desviaciones

- La integración de Firebase Authentication con el patrón repositorio requirió más tiempo del estimado, ya que era la primera vez que ambos miembros trabajaban con Firebase en Android/Kotlin.
- La migración del almacenamiento en memoria a Room implicó una refactorización más extensa de los ViewModels del Sprint 02 de la prevista, especialmente en la gestión de corrutinas y los flujos de datos.
- La implementación de verificación de email añadió complejidad al flujo de registro, requiriendo iteraciones adicionales para gestionar correctamente los estados de usuario verificado/no verificado en la UI.
- La sincronización entre la sesión Firebase (remota) y los datos de usuario persistidos en Room (local) generó inconsistencias puntuales que se resolvieron con lógica adicional en el repositorio.
- El tiempo dedicado a los tests de DAOs fue ajustado gracias a utilizar una base de datos Room en memoria durante los tests, lo que simplificó considerablemente la configuración del entorno de pruebas.

---

## 4. Retrospectiva

### Qué funcionó bien

- La separación clara de responsabilidades con el patrón MVVM facilitó la integración de Room sin necesidad de modificar la capa UI de forma significativa.
- La división de tareas entre Alex y Samuel se mantuvo coherente y sin bloqueos durante la mayor parte del Sprint.
- El uso de Flow en lugar de LiveData simplificó la reactividad de la UI ante cambios en la base de datos.
- Las convenciones de commits aplicadas en este Sprint (feat, fix, test, docs) mejoraron notablemente la trazabilidad del historial de cambios respecto al Sprint anterior.

### Qué no funcionó

- La estimación de T1.5 (migración de ViewModels a Room) y T3.2 (registro con Firebase) fue optimista; ambas tareas consumieron más tiempo del previsto.
- Algunos tests unitarios de DAOs fallaron inicialmente por diferencias entre el comportamiento de Room en memoria y en disco, lo que requirió ajustes en la configuración de los tests.
- Los comentarios en el código no se añadieron de forma continua durante el desarrollo, acumulándose al final del Sprint.

### Qué mejoraremos en el próximo sprint

- Descomponer las tareas de mayor riesgo técnico en subtareas más pequeñas para mejorar la granularidad de las estimaciones.
- Establecer una sesión de revisión de código intermedia a mitad del Sprint para detectar problemas de integración antes de la fase final.
- Mantener la documentación y los comentarios de código actualizados de forma continua, no solo al cierre del Sprint.
- Dedicar tiempo explícito al inicio del Sprint para revisar la arquitectura conjuntamente antes de comenzar la implementación.

---

## 5. Autoevaluación del equipo

Consideramos que hemos cumplido todos los criterios del Definition of Done, incluyendo los bloques más complejos como Firebase y Room. La nota no es más alta porque las estimaciones volvieron a ser optimistas en las tareas de mayor complejidad técnica y porque la calidad del código (comentarios, commits intermedios) podría haber sido más consistente a lo largo del Sprint.