package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

@FunctionalInterface
public interface ObservadorEstado {
    void alCambiarEstado(Solicitud solicitud, String estadoAnterior);
}