package com.universidad.compras.estado;

public class EstadoCancelada implements EstadoSolicitud {

    @Override
    public String nombre() { return "CANCELADA"; }
}