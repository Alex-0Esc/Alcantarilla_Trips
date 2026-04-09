# Sprint 03 - Planning Document

## 1. Sprint Goal

- Implementar la persistencia de datos con Room (SQLite), el sistema de autenticación de usuarios mediante Firebase (login, registro y recuperación de contraseña), la persistencia de información de usuario y viajes multi-usuario, y las pruebas y validaciones correspondientes, cumpliendo con el Definition of Done del proyecto.

---

## 2. Sprint Backlog

| ID     | Tarea                                                                 | Responsable    | Estimación (h) | Prioridad |
|--------|-----------------------------------------------------------------------|----------------|----------------|-----------|
| T1.0   | Implement SQLite Persistence using Room                               | Alex, Samuel   | 5 pts          | Alta      |
| T1.1   | Create Room Database class                                            | Samuel         | 1              | Alta      |
| T1.2   | Define Entities for Trip and ItineraryItem (mín. un campo datetime, text e integer) | Alex | 2 | Alta |
| T1.3   | Create DAOs for database operations                                   | Samuel         | 2              | Alta      |
| T1.4   | Implement CRUD operations using DAO for trips and itineraries         | Alex           | 2              | Alta      |
| T1.5   | Modify ViewModels to use Room Database instead of in-memory storage   | Samuel         | 2              | Alta      |
| T1.6   | Ensure UI updates when database changes                               | Alex           | 1              | Media     |
| T2.0   | Login and Logout                                                      | Alex, Samuel   | 3 pts          | Alta      |
| T2.1   | Connect app to Firebase                                               | Samuel         | 1              | Alta      |
| T2.2   | Design android login screen (login form)                              | Alex           | 2              | Alta      |
| T2.3   | Implement login actions using Firebase (auth & password)              | Samuel         | 2              | Alta      |
| T2.4   | Create logout action in the app                                       | Alex           | 1              | Alta      |
| T2.5   | Use Logcat to track all operations and errors                         | Alex, Samuel   | 1              | Media     |
| T3.0   | Register and Recover Password                                         | Alex, Samuel   | 4 pts          | Alta      |
| T3.1   | Design android register screen (register form)                        | Alex           | 2              | Alta      |
| T3.2   | Implement registration using Firebase (auth & password) con patrón repositorio e email verification | Samuel | 3 | Alta |
| T3.3   | Implement password recovery view and actions                          | Alex           | 2              | Media     |
| T4.0   | Persist User Information and Trip                                     | Alex, Samuel   | 3 pts          | Alta      |
| T4.1   | Persist user info in local DB: login, username, birthdate, address, country, phone, accept_emails; verificar username único | Samuel | 3 | Alta |
| T4.2   | Modify trip table to support multiple users; show only trips for logged-in user | Alex | 2 | Alta |
| T4.3   | Update documentation with database schema and usage at design.md      | Samuel         | 1              | Media     |
| T4.4   | Persist app access log (tabla con userid y datetime de cada login/logout) | Alex       | 2              | Media     |
| T5.0   | Testing and Debugging                                                 | Alex, Samuel   | 2 pts          | Alta      |
| T5.1   | Write unit tests for DAOs and database interactions                   | Alex           | 2              | Alta      |
| T5.2   | Implement data validation (prevent duplicate trip names, check valid dates) | Samuel   | 2              | Alta      |
| T5.3   | Use Logcat to track database operations and errors                    | Alex, Samuel   | 1              | Media     |
| T5.4   | Update documentation with database schema and usage at design.md      | Samuel         | 1              | Media     |
 
---

## 3. Definition of Done (DoD)

Un incremento del producto se considera **Done** cuando se cumplen todos los siguientes criterios:

### Código y ejecución
- El código está **completado**, integrado en la rama principal y **sin errores de compilación**.
- El proyecto se **ejecuta correctamente en Android Studio**, ya sea en emulador o dispositivo físico.
- Se han aplicado los **estándares de codificación** acordados por el equipo.
- La arquitectura **MVVM** está correctamente implementada: UI → ViewModel → Repository → Data Source.

### Persistencia con Room
- La base de datos Room está creada y funcional con las entidades Trip, ItineraryItem, User y AccessLog.
- Cada entidad contiene al menos un campo datetime, un campo de texto y un campo entero.
- Los DAOs exponen operaciones CRUD completas para todas las entidades.
- Los ViewModels utilizan Room en lugar de almacenamiento en memoria.
- La UI se actualiza dinámicamente ante cambios en la base de datos (LiveData/Flow).

### Autenticación Firebase
- Al iniciar la app se comprueba si el usuario está autenticado; en caso contrario se muestra la pantalla de login.
- El login con email y contraseña redirige correctamente a la vista principal.
- El registro de nuevo usuario implementa el patrón repositorio e incluye verificación de email.
- La recuperación de contraseña está implementada (pantalla o diálogo dedicado).
- El logout cierra la sesión y redirige a la pantalla de login.

### Datos de usuario y viajes
- La tabla de usuario persiste: login, username, birthdate, address, country, phone y accept_emails.
- El username se verifica como único antes de registrar.
- La tabla de viajes está asociada al usuario; solo se muestran los viajes del usuario autenticado.
- Cada inicio y cierre de sesión queda registrado en la tabla de accesos (userid + datetime).
- El archivo design.md está actualizado con el esquema de base de datos completo.

### Validación y testing
- Existen tests unitarios para los DAOs y las operaciones de base de datos.
- Se valida la no duplicidad de nombres de viaje y la coherencia de fechas.
- Los logs de Logcat cubren operaciones de base de datos, autenticación y errores.
- La documentación refleja los resultados de los tests y los arreglos aplicados.

### Repositorio y control de versiones
- El código ha sido **subido y versionado correctamente**.
- El Sprint finaliza con una **release publicada** (`v0.3.0`).
- El incremento es **potencialmente entregable**.
- No existen **bloqueos críticos** que impidan continuar con el siguiente Sprint.

---

## 4. Riesgos identificados

- Curva de aprendizaje con Firebase Authentication y la integración con el patrón repositorio.
- Posibles conflictos entre la sesión Firebase (remota) y los datos de usuario persistidos en Room (local).
- La migración del almacenamiento en memoria a Room puede requerir refactorización significativa de los ViewModels del Sprint 02.
- La implementación de verificación de email puede añadir complejidad en el flujo de registro y en las pruebas.
- La gestión de múltiples usuarios en la tabla de viajes puede generar bugs en el filtrado si no se gestiona correctamente el estado de sesión.
- El tiempo estimado para los tests de DAOs puede ser insuficiente si la implementación de Room se retrasa.
 
