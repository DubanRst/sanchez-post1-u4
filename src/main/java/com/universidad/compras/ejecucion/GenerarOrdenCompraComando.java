package com.universidad.compras.ejecucion;

public class GenerarOrdenCompraComando implements Comando {

    private final OrdenCompraService ordenes;
    private final String solicitudId;
    private final String proveedor;
    private String numeroOrden;

    public GenerarOrdenCompraComando(OrdenCompraService ordenes, String solicitudId, String proveedor) {
        this.ordenes = ordenes;
        this.solicitudId = solicitudId;
        this.proveedor = proveedor;
    }

    @Override
    public void ejecutar() {
        numeroOrden = ordenes.generar(solicitudId, proveedor);
    }

    @Override
    public void deshacer() {
        if (numeroOrden != null) {
            ordenes.cancelar(numeroOrden);
        }
    }

    @Override
    public String descripcion() {
        return "Generar orden de compra de " + solicitudId + " con " + proveedor;
    }
}