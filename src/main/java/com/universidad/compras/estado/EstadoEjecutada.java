package com.universidad.compras.estado;

public class EstadoEjecutada implements EstadoSolicitud {

    @Override
    public String nombre() { return "EJECUTADA"; }

    @Override
    public String ejecutar(ContextoSolicitud ctx) {
        return "Error: ya fue ejecutada";
    }
}