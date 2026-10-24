package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class SupervisorArea extends ManejadorAprobacion {

    @Override
    protected String nombre() { return "Supervisor de Área"; }

    @Override
    protected boolean dentroDeAutoridad(Solicitud solicitud) {
        return solicitud.getMonto() <= 2_000_000;
    }
}