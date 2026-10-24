package com.universidad.compras.estado;

public class EstadoAprobada implements EstadoSolicitud {

    @Override
    public String nombre() { return "APROBADA"; }

    @Override
    public String ejecutar(ContextoSolicitud ctx) {
        ctx.transicionarA("EJECUTADA");
        return "Ejecutada";
    }

    @Override
    public String cancelar(ContextoSolicitud ctx) {
        ctx.transicionarA("CANCELADA");
        return "Cancelada";
    }
}