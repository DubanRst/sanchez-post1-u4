package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.PublicadorCambioEstado;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Command (invoker): ejecuta las operaciones de una solicitud aprobada,
 * conserva el historial completo y permite deshacerlas una por una.
 */
public class EjecutorSolicitud {

    private final Solicitud solicitud;
    private final PresupuestoService presupuesto;
    private final OrdenCompraService ordenes;
    private final PublicadorCambioEstado publicador;
    private final Deque<Comando> pendientesDeDeshacer = new ArrayDeque<>();
    private final List<String> historial = new ArrayList<>();

    public EjecutorSolicitud(Solicitud solicitud, PresupuestoService presupuesto, OrdenCompraService ordenes) {
        this(solicitud, presupuesto, ordenes, new PublicadorCambioEstado(List.of()));
    }

    public EjecutorSolicitud(Solicitud solicitud, PresupuestoService presupuesto,
                             OrdenCompraService ordenes, PublicadorCambioEstado publicador) {
        this.solicitud = solicitud;
        this.presupuesto = presupuesto;
        this.ordenes = ordenes;
        this.publicador = publicador;
    }

    public void reservarPresupuesto() {
        ejecutar(new ReservarPresupuestoComando(presupuesto, solicitud.getCentroCosto(), solicitud.getMonto()));
    }

    public void generarOrdenCompra(String proveedor) {
        ejecutar(new GenerarOrdenCompraComando(ordenes, solicitud.getId(), proveedor));
    }

    public void ejecutarSolicitud(String proveedor) {
        reservarPresupuesto();
        generarOrdenCompra(proveedor);
        cambiarEstado("EJECUTADA");
    }

    public boolean deshacerUltima() {
        if (pendientesDeDeshacer.isEmpty()) {
            return false;
        }
        Comando comando = pendientesDeDeshacer.pop();
        comando.deshacer();
        historial.add("DESHECHO: " + comando.descripcion());
        if ("EJECUTADA".equals(solicitud.getEstado())) {
            cambiarEstado("APROBADA");
        }
        return true;
    }

    public List<String> getHistorial() {
        return List.copyOf(historial);
    }

    private void ejecutar(Comando comando) {
        comando.ejecutar();
        pendientesDeDeshacer.push(comando);
        historial.add("EJECUTADO: " + comando.descripcion());
    }

    private void cambiarEstado(String nuevoEstado) {
        publicador.cambiarEstado(solicitud, nuevoEstado);
    }
}