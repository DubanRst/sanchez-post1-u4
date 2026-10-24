# Post-contenido — Unidad 4: Patrones de Comportamiento en ComprasUDES

## Descripción

Repositorio del post-contenido de la Unidad 4 de Patrones de Diseño de Software. Un único proyecto Spring Boot (`compras-comportamiento`) que resuelve cuatro necesidades reales del backend de ComprasUDES, el sistema interno de solicitudes de compra corporativas: aprobación por niveles jerárquicos, ejecución reversible de solicitudes aprobadas, notificaciones ante cambios de estado y reglas de transición según el estado actual de la solicitud.

### Estructura del proyecto

- `modelo/`: entidad Solicitud (dada).
- `aprobacion/`: cadena de niveles de aprobación (Necesidad 1).
- `ejecucion/`: comandos reversibles con historial (Necesidad 2).
- `notificacion/`: publicador y observadores de cambio de estado (Necesidad 3).
- `estado/`: estados de la solicitud y su contexto (Necesidad 4).

## Cómo ejecutar

```powershell
mvn clean package
mvn spring-boot:run
mvn test
```

## Decisiones de diseño

### Necesidad 1 — Aprobación por niveles jerárquicos

Patrón aplicado: Chain of Responsibility

Justificación: el problema central es que una solicitud debe recorrer una secuencia de decisores independientes (Revisor de Cumplimiento, Supervisor de Área, Gerente de Área y Director Financiero), donde cada uno la resuelve si está dentro de su autoridad o la remite al siguiente. Esa secuencia debe poder crecer o reordenarse sin tocar el código que dispara la evaluación. Con la cadena, ControladorSolicitudes solo conoce ServicioAprobacion, y agregar, quitar o reordenar un nivel implica modificar únicamente CadenaAprobacion, sin tocar el controlador ni los demás niveles. El nivel de Cumplimiento se evalúa primero y solo aplica a la categoría INTERNACIONAL, así que el código cliente no necesita saber cuántos niveles existen ni si una categoría requiere uno adicional. Command se descartó porque modela una operación encapsulada que alguien ejecuta y puede deshacer, con historial consultable; aquí no hay nada que deshacer, y usarlo obligaría al código cliente a elegir qué nivel invocar según el monto, es decir, a recrear los if/else que se querían evitar.

### Necesidad 2 — Ejecución reversible de solicitudes aprobadas

Patrón aplicado: Command

Justificación: reservar presupuesto y generar la orden de compra son operaciones discretas que un mismo actor (el equipo de Compras) decide ejecutar y, eventualmente, deshacer de forma independiente. Cada una se encapsula como un objeto (ReservarPresupuestoComando y GenerarOrdenCompraComando) con ejecutar() y deshacer(), y EjecutorSolicitud conserva el historial completo de todas las operaciones, no solo la última, y las deshace en orden inverso. PresupuestoService y OrdenCompraService no se modificaron: los comandos solo los invocan. Chain of Responsibility se descartó porque aquí no hay ningún decisor evaluando condiciones para decidir si resuelve o delega una petición entrante, y además la cadena no ofrece deshacer ni historial de operaciones ejecutadas.

### Necesidad 3 — Notificaciones ante cambio de estado

Patrón aplicado: Observer

Justificación: cuando una solicitud cambia de estado, tres módulos completamente ajenos a ella (correo al solicitante, tablero de contabilidad y log de auditoría) deben enterarse y reaccionar. PublicadorCambioEstado actúa como sujeto: cambia el estado y notifica a los ObservadorEstado suscritos, de modo que ni la solicitud ni el código que la modifica conocen a los tres módulos. Agregar una cuarta reacción solo requiere un nuevo observador, sin modificar el mecanismo central, y la prueba lo verifica con un suscriptor de prueba. El mecanismo se conectó a los puntos de cambio de estado de las Necesidades 1 (evaluación de aprobación) y 2 (ejecución y deshacer). State se descartó porque en esta necesidad el comportamiento que cambia no es el de la propia solicitud (qué operaciones se le permiten según su estado), sino el de otros objetos que reaccionan después de que la solicitud ya cambió; usar State obligaría a cada estado a conocer y llamar al correo, a contabilidad y a auditoría, acoplando la solicitud con esos módulos.

### Necesidad 4 — Reglas de transición según el estado

Patrón aplicado: State

Justificación: las reglas sobre qué operaciones son válidas (aprobar, rechazar, ejecutar, cancelar) estaban dispersas como if/else repetidos que consultaban getEstado(). Con State, cada estado es una clase (EstadoPendiente, EstadoAprobada, EstadoEjecutada, entre otras) que define qué operaciones permite y a qué estado transiciona; ContextoSolicitud delega cada operación en el estado actual. Una operación inválida devuelve un error sin cambiar el estado, y agregar un estado nuevo, como EN_ESPERA_PROVEEDOR, solo requiere una clase nueva registrada en Estados, sin revisar varios métodos. Strategy se descartó a pesar del parecido estructural: en Strategy un cliente externo elige e inyecta explícitamente el comportamiento intercambiable en cada llamada, mientras que aquí es la propia solicitud quien decide qué es válido según el estado en que se encuentre, y además debe transicionar de un estado a otro como parte de resolver la operación, algo que un conjunto de estrategias independientes entre sí no hace por su cuenta.

### Reflexión — otros tres patrones (opcional)

1. Recorrer secuencialmente las solicitudes de un centro de costo sin exponer si se guardan en una lista, un mapa u otra estructura encajaría con **Iterator**.
2. Los tres tipos de comprobante comparten el esqueleto de impresión (encabezado, cuerpo y pie) y difieren solo en cómo llenan el cuerpo, lo que encajaría con **Template Method**.
3. Guardar y restaurar instantáneas completas del estado de una solicitud sin que quien las guarda conozca sus detalles internos se acercaría a **Memento**. Se diferencia del deshacer de la Necesidad 2 en que Command revierte una operación puntual ejecutando su acción inversa (liberar presupuesto, cancelar orden), mientras que Memento guarda una copia del estado completo y lo restaura tal cual en cualquier punto de su historia.

## Herramientas utilizadas

- Java 21, Spring Boot 3.2, Apache Maven, JUnit 5
- VS Code, Git, GitHub

## Conclusiones

Este post-contenido mostró que los patrones de comportamiento se distinguen por el problema que resuelven y no por su forma: Chain of Responsibility y Command pueden parecer cercanos porque ambos encapsulan decisiones, pero uno hace avanzar una petición hasta que alguien la resuelve y el otro registra operaciones que se pueden deshacer. Lo mismo ocurrió con Observer frente a State, donde la clave fue preguntarse quién cambia de comportamiento (módulos externos o la propia solicitud), y con State frente a Strategy, donde la clave fue quién decide el comportamiento activo. Formular esa pregunta antes de escribir código fue más útil que reconocer la estructura de cada patrón.