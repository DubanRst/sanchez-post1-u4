package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class DirectorFinanciero extends ManejadorAprobacion {

    @Override
    protected String nombre() { return "Director Financiero"; }

    @Override
    protected boolean dentroDeAutoridad(Solicitud solicitud) {
        return true;
    }
}