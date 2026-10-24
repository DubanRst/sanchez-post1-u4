package com.universidad.compras.aprobacion;

public final class CadenaAprobacion {

    private CadenaAprobacion() {
    }

    public static ManejadorAprobacion construir() {
        ManejadorAprobacion cumplimiento = new RevisorCumplimiento();
        cumplimiento.enlazar(new SupervisorArea())
                    .enlazar(new GerenteArea())
                    .enlazar(new DirectorFinanciero());
        return cumplimiento;
    }
}