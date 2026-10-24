package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.PublicadorCambioEstado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioAprobacionPorNiveles implements ServicioAprobacion {

    private final ManejadorAprobacion cadena = CadenaAprobacion.construir();
    private final PublicadorCambioEstado publicador;

    public ServicioAprobacionPorNiveles() {
        this(new PublicadorCambioEstado(List.of()));
    }

    @Autowired
    public ServicioAprobacionPorNiveles(PublicadorCambioEstado publicador) {
        this.publicador = publicador;
    }

    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        ResultadoAprobacion resultado = cadena.manejar(solicitud);
        publicador.cambiarEstado(solicitud, resultado.isAprobada() ? "APROBADA" : "RECHAZADA");
        return resultado;
    }
}