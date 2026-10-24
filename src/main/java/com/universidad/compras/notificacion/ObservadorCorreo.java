package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Component;

@Component
public class ObservadorCorreo implements ObservadorEstado {

    @Override
    public void alCambiarEstado(Solicitud solicitud, String estadoAnterior) {
        ClientesNotificacion.enviarCorreo(
            solicitud.getSolicitanteEmail(),
            "Solicitud " + solicitud.getId() + " cambio de estado",
            estadoAnterior + " -> " + solicitud.getEstado());
    }
}