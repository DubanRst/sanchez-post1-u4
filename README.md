# Post-contenido — Unidad 4: Patrones de Comportamiento en ComprasUDES

## Descripción

Repositorio del post-contenido de la Unidad 4 de Patrones de Diseño de Software. Un único proyecto Spring Boot (`compras-comportamiento`) que resuelve necesidades reales del backend de ComprasUDES, el sistema interno de solicitudes de compra corporativas.

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