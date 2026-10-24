package com.universidad.compras.estado;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.PublicadorCambioEstado;

import java.util.List;

/**
 * State (contexto): delega cada operacion en el objeto del estado actual,
 * que decide si es valida y, si lo es, transiciona al estado siguiente.
 */
public class ContextoSolicitud {

    private final Solicitud solicitud;
    private final PublicadorCambioEstado publicador;
    private EstadoSolicitud estado;

    public ContextoSolicitud(Solicitud solicitud) {
        this(solicitud, new PublicadorCambioEstado(List.of()));
    }

    public ContextoSolicitud(Solicitud solicitud, PublicadorCambioEstado publicador) {
        this.solicitud = solicitud;
        this.publicador = publicador;
        this.estado = Estados.de(solicitud.getEstado());
    }

    public String aprobar() { return estado.aprobar(this); }

    public String rechazar() { return estado.rechazar(this); }

    public String ejecutar() { return estado.ejecutar(this); }

    public String cancelar() { return estado.cancelar(this); }

    public String getEstadoActual() { return estado.nombre(); }

    void transicionarA(String nombreEstado) {
        this.estado = Estados.de(nombreEstado);
        publicador.cambiarEstado(solicitud, nombreEstado);
    }
}