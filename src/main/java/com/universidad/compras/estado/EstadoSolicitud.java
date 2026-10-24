package com.universidad.compras.estado;

public interface EstadoSolicitud {

    String nombre();

    default String aprobar(ContextoSolicitud ctx) { return noPermitida("aprobar"); }

    default String rechazar(ContextoSolicitud ctx) { return noPermitida("rechazar"); }

    default String ejecutar(ContextoSolicitud ctx) { return noPermitida("ejecutar"); }

    default String cancelar(ContextoSolicitud ctx) { return noPermitida("cancelar"); }

    private String noPermitida(String operacion) {
        return "Error: no se puede " + operacion + " una solicitud " + nombre();
    }
}