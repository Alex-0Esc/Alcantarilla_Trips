# Sprint 04 – Execution & Review

## 1. Resultados obtenidos

Comparación con Sprint Goal:

El objetivo del Sprint era implementar la integración con servicios externos mediante **Retrofit** para la búsqueda y reserva de hoteles en Londres, París y Barcelona, expandir la persistencia local con **Room** para gestionar reservas e imágenes, y permitir la visualización de galerías multimedia y la gestión completa del ciclo de vida de una reserva. A continuación se detalla el grado de cumplimiento por bloque.

---

## 2. Tareas completadas

| ID | Tarea | Responsable | Completada | Comentarios |
|----|-------|-------------|------------|-------------|
| T1.0 | Retrofit Configuration | Alex, Samuel | Sí | |
| T1.1 | Configurar dependencias de Retrofit y cliente HTTP | Samuel | Sí | |
| T1.2 | Crear modelos de datos e interfaces API para hoteles (MVVM) | Alex | Sí | |
| T1.3 | Crear capa de repositorio para abstraer el uso de la API | Samuel | Sí | |
| T1.4 | Crear unit tests simulando la conexión remota (Mocking) | Alex | Sí | |
| T2.0 | Search and Booking Screens | Alex, Samuel | Sí | |
| T2.1 | Pantalla de búsqueda de hoteles (London, Paris, Barcelona) | Alex | Sí | |
| T2.2 | Mostrar datos de hoteles y habitaciones (3 por hotel) | Samuel | Sí | |
| T2.3 | Lógica de reserva y guardado local en la tabla de viajes/reservas | Samuel | Sí | |
| T2.4 | Mostrar todas las imágenes del hotel y habitaciones | Alex | Sí | |
| T3.0 | Add Images / Gallery to Trip | Alex, Samuel | Sí | |
| T3.1 | Permitir al usuario adjuntar múltiples imágenes a un viaje | Alex | Sí | |
| T3.2 | Guardar imágenes localmente en la base de datos o almacenamiento | Samuel | Sí | |
| T3.3 | Mostrar galerías específicas en la pantalla de detalles del viaje | Alex | Sí | |
| T4.0 | List and Cancel Reservations | Alex, Samuel | Sí | |
| T4.1 | Pantalla para listar todas las reservas locales vinculadas | Alex | Sí | |
| T4.2 | Funcionalidad para eliminar una reserva localmente y vía API | Samuel | Sí | |
| T4.3 | Mostrar imágenes asociadas en el listado de reservas | Alex | Sí | |
| T4.4 | Actualizar "My Trips" con detalles e indicadores de reserva | Samuel | Sí | |

---

## 3. Desviaciones

- La gestión de imágenes de alta resolución generó algunos problemas de rendimiento iniciales en las galerías, que se resolvieron implementando carga lazy y compresión previa al almacenamiento.
- La integración con la API de hoteles requirió ajustes en los modelos de datos (POJOs) debido a cambios menores en el formato de respuesta JSON no documentados previamente.
- Los permisos de lectura/escritura en Android para el almacenamiento local de imágenes añadieron complejidad extra, especialmente para dispositivos con Android 13+.

---

## 4. Retrospectiva

### Qué funcionó bien

- La arquitectura MVVM facilitó la separación de responsabilidades y simplificó la integración de Retrofit con la capa de repositorio.
- El uso de mocking en los unit tests permitió detectar errores de mapeo de datos antes de realizar llamadas reales a la API.
- La comunicación entre los miembros del equipo mejoró respecto a sprints anteriores, con revisiones de código más ágiles.

### Qué no funcionó

- La estimación de tiempo para las tareas de galería de imágenes fue demasiado optimista, sin contemplar la casuística de permisos en diferentes versiones de Android.
- La sincronización entre los datos remotos y la base de datos local Room presentó algún caso de inconsistencia tras la cancelación de reservas, que hubo que corregir manualmente.

### Qué mejoraremos en el próximo sprint

- Dedicar más tiempo al análisis de la API y sus posibles variaciones en el formato de respuesta antes de comenzar la implementación.
- Mejorar las estimaciones para tareas que impliquen permisos del sistema o funcionalidades específicas de Android.
- Establecer una estrategia clara de sincronización local/remota desde el inicio del sprint para evitar inconsistencias de datos.

---

## 5. Autoevaluación del equipo (0-10)

**Nota: 8,5**

**Justificación: Hemos completado satisfactoriamente todos los bloques del Sprint, incluyendo la integración con Retrofit, la gestión de reservas, las galerías de imágenes y el listado/cancelación de reservas. A pesar de las desviaciones menores relacionadas con la gestión de imágenes y la sincronización de datos, el equipo supo adaptarse y resolver los problemas sin comprometer la entrega. El patrón MVVM aplicado de forma consistente y la mejora en la coordinación del equipo son puntos positivos a destacar de este Sprint.**