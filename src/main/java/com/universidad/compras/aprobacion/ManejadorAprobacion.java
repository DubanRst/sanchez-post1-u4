package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

/**
 * Chain of Responsibility: cada nivel decide si la solicitud esta dentro de su
 * autoridad (y la resuelve) o la remite al siguiente nivel de la cadena.
 */
public abstract class ManejadorAprobacion {

    private ManejadorAprobacion siguiente;

    public ManejadorAprobacion enlazar(ManejadorAprobacion siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    public final ResultadoAprobacion manejar(Solicitud solicitud) {
        if (dentroDeAutoridad(solicitud)) {
            solicitud.setNivelResolutor(nombre());
            boolean aprobada = solicitud.getMonto() > 0;
            String detalle = aprobada
                ? "Aprobada por " + nombre()
                : "Rechazada por " + nombre() + ": el monto debe ser mayor que cero";
            return new ResultadoAprobacion(aprobada, nombre(), detalle);
        }
        if (siguiente != null) {
            return siguiente.manejar(solicitud);
        }
        return new ResultadoAprobacion(false, "Ninguno", "Ningun nivel tiene autoridad para esta solicitud");
    }

    protected abstract String nombre();

    protected abstract boolean dentroDeAutoridad(Solicitud solicitud);
}