# Sprint 04 - Planning Document

## 1. Sprint Goal

Implementar la integración con servicios externos mediante **Retrofit** para la búsqueda y reserva de hoteles en Londres, París y Barcelona. Expandir la persistencia local con Room para gestionar reservas e imágenes, permitiendo la visualización de galerías multimedia y la gestión completa del ciclo de vida de una reserva (búsqueda, creación, listado y cancelación).

---

## 2. Sprint Backlog

| ID     | Tarea                                                                 | Responsable    | Estimación (pts/h) | Prioridad |
|--------|-----------------------------------------------------------------------|----------------|--------------------|-----------|
| **T1.0** | **Retrofit Configuration**                                 | **Alex, Samuel** | **3 pts**          | **Alta**  |
| T1.1   | Configurar dependencias de Retrofit y cliente HTTP          | Samuel         | 1h                 | Alta      |
| T1.2   | Crear modelos de datos y interfaces API para hoteles (MVVM) | Alex           | 2h                 | Alta      |
| T1.3   | Crear capa de repositorio para abstraer el uso de la API    | Samuel         | 2h                 | Alta      |
| T1.4   | Crear unit tests simulando la conexión remota (Mocking)     | Alex           | 2h                 | Media     |
| **T2.0** | **Search and Booking Screens**                             | **Alex, Samuel** | **5 pts**          | **Alta**  |
| T2.1   | Pantalla de búsqueda de hoteles (London, Paris, Barcelona)  | Alex           | 3h                 | Alta      |
| T2.2   | Mostrar datos de hoteles y habitaciones (3 por hotel)       | Samuel         | 2h                 | Alta      |
| T2.3   | Lógica de reserva y guardado local en la tabla de viajes/reservas| Samuel     | 3h                 | Alta      |
| T2.4   | Mostrar todas las imágenes del hotel y habitaciones         | Alex           | 2h                 | Media     |
| **T3.0** | **Add Images / Gallery to Trip**                           | **Alex, Samuel** | **4 pts**          | **Media** |
| T3.1   | Permitir al usuario adjuntar múltiples imágenes a un viaje  | Alex           | 3h                 | Media     |
| T3.2   | Guardar imágenes localmente en la base de datos o almacenamiento| Samuel     | 2h                 | Media     |
| T3.3   | Mostrar galerías específicas en la pantalla de detalles del viaje| Alex       | 2h                 | Media     |
| **T4.0** | **List and Cancel Reservations**                           | **Alex, Samuel** | **3 pts**          | **Alta**  |
| T4.1   | Pantalla para listar todas las reservas locales vinculadas  | Alex           | 2h                 | Alta      |
| T4.2   | Funcionalidad para eliminar una reserva localmente y vía API| Samuel         | 2h                 | Alta      |
| T4.3   | Mostrar imágenes asociadas en el listado de reservas        | Alex           | 2h                 | Media     |
| T4.4   | Actualizar "My Trips" con detalles e indicadores de reserva | Samuel         | 2h                 | Media     |

---

## 3. Definition of Done (DoD)

Un incremento del producto se considera **Done** cuando se cumplen todos los siguientes criterios:

### Código e Integración API
- El cliente **Retrofit** está configurado y los modelos de datos (POJOs) mapean correctamente la respuesta de la API.
- Se utiliza el patrón **MVVM**: UI → ViewModel → Repository → Remote Data Source.
- El código está integrado en la rama principal y no presenta errores de compilación.

### Búsqueda y Reservas
- La búsqueda funciona para Londres, París o Barcelona con fechas de inicio y fin.
- La información de la reserva (ID, habitación, hotel, precio) se guarda en la base de datos local.
- Se muestran todas las imágenes del hotel y las habitaciones durante el proceso de reserva.

### Galería de Imágenes
- El usuario puede añadir múltiples imágenes a un viaje específico.
- Las imágenes se visualizan correctamente en una galería dentro del detalle del viaje.
- Las rutas o archivos de imagen persisten localmente en el dispositivo.

### Gestión de Reservas
- Existe una pantalla funcional para listar y eliminar reservas.
- La pantalla "My Trips" indica visualmente si un viaje incluye una reserva de hotel y muestra sus detalles.
- El archivo `design.md` ha sido actualizado con el nuevo esquema de base de datos.

---

## 4. Riesgos identificados

- **Conectividad y API:** Problemas de latencia o cambios inesperados en el formato de respuesta de la API de hoteles.
- **Gestión de Imágenes:** Posible degradación del rendimiento al cargar múltiples imágenes de alta resolución en las galerías.
- **Sincronización de Datos:** Riesgo de inconsistencia entre la información de la API y los datos guardados localmente en Room tras una cancelación.
- **Manejo de Almacenamiento:** Permisos de lectura/escritura en Android para guardar imágenes localmente de forma persistente.
