package com.universidad.compras.estado;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Estados {

    private static final Map<String, EstadoSolicitud> POR_NOMBRE = Stream.<EstadoSolicitud>of(
            new EstadoPendiente(),
            new EstadoEnAprobacion(),
            new EstadoAprobada(),
            new EstadoRechazada(),
            new EstadoEjecutada(),
            new EstadoCancelada())
        .collect(Collectors.toMap(EstadoSolicitud::nombre, Function.identity()));

    private Estados() {
    }

    public static EstadoSolicitud de(String nombre) {
        EstadoSolicitud estado = POR_NOMBRE.get(nombre);
        if (estado == null) {
            throw new IllegalArgumentException("Estado desconocido: " + nombre);
        }
        return estado;
    }
}