package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Service;

@Service
public class ServicioAprobacionPorNiveles implements ServicioAprobacion {

    private final ManejadorAprobacion cadena = CadenaAprobacion.construir();

    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        ResultadoAprobacion resultado = cadena.manejar(solicitud);
        solicitud.setEstado(resultado.isAprobada() ? "APROBADA" : "RECHAZADA");
        return resultado;
    }
}