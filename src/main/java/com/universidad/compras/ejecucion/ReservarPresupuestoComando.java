package com.universidad.compras.ejecucion;

public class ReservarPresupuestoComando implements Comando {

    private final PresupuestoService presupuesto;
    private final String centroCosto;
    private final double monto;

    public ReservarPresupuestoComando(PresupuestoService presupuesto, String centroCosto, double monto) {
        this.presupuesto = presupuesto;
        this.centroCosto = centroCosto;
        this.monto = monto;
    }

    @Override
    public void ejecutar() {
        if (!presupuesto.reservar(centroCosto, monto)) {
            throw new IllegalStateException("No se pudo reservar el presupuesto en " + centroCosto);
        }
    }

    @Override
    public void deshacer() {
        presupuesto.liberar(centroCosto, monto);
    }

    @Override
    public String descripcion() {
        return "Reservar presupuesto de " + monto + " en " + centroCosto;
    }
}