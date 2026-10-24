package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Service
public class PublicadorCambioEstado {

    private final List<ObservadorEstado> observadores;

    public PublicadorCambioEstado(List<ObservadorEstado> observadores) {
        this.observadores = new CopyOnWriteArrayList<>(observadores);
    }

    public void suscribir(ObservadorEstado observador) {
        observadores.add(observador);
    }

    public void cambiarEstado(Solicitud solicitud, String nuevoEstado) {
        String estadoAnterior = solicitud.getEstado();
        solicitud.setEstado(nuevoEstado);
        observadores.forEach(o -> o.alCambiarEstado(solicitud, estadoAnterior));
    }
}