package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Component;

@Component
public class ObservadorContabilidad implements ObservadorEstado {

    @Override
    public void alCambiarEstado(Solicitud solicitud, String estadoAnterior) {
        ClientesNotificacion.actualizarDashboardContabilidad(
            solicitud.getId(), solicitud.getEstado(), solicitud.getMonto());
    }
}