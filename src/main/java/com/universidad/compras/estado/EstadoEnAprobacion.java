package com.universidad.compras.estado;

public class EstadoEnAprobacion implements EstadoSolicitud {

    @Override
    public String nombre() { return "EN_APROBACION"; }

    @Override
    public String aprobar(ContextoSolicitud ctx) {
        ctx.transicionarA("APROBADA");
        return "Aprobada";
    }

    @Override
    public String rechazar(ContextoSolicitud ctx) {
        ctx.transicionarA("RECHAZADA");
        return "Rechazada";
    }

    @Override
    public String cancelar(ContextoSolicitud ctx) {
        ctx.transicionarA("CANCELADA");
        return "Cancelada";
    }

    @Override
    public String ejecutar(ContextoSolicitud ctx) {
        return "Error: debe estar aprobada antes de ejecutarse";
    }
}