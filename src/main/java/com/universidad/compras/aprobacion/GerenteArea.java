package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class GerenteArea extends ManejadorAprobacion {

    @Override
    protected String nombre() { return "Gerente de Área"; }

    @Override
    protected boolean dentroDeAutoridad(Solicitud solicitud) {
        return solicitud.getMonto() <= 10_000_000;
    }
}