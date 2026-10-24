package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class RevisorCumplimiento extends ManejadorAprobacion {

    @Override
    protected String nombre() { return "Revisor de Cumplimiento Normativo"; }

    @Override
    protected boolean dentroDeAutoridad(Solicitud solicitud) {
        return "INTERNACIONAL".equals(solicitud.getCategoria());
    }
}