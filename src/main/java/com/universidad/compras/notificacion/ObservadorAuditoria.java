package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Component;

@Component
public class ObservadorAuditoria implements ObservadorEstado {

    @Override
    public void alCambiarEstado(Solicitud solicitud, String estadoAnterior) {
        ClientesNotificacion.registrarAuditoria(
            solicitud.getId(), solicitud.getEstado(),
            "estado anterior: " + estadoAnterior + ", nivel resolutor: " + solicitud.getNivelResolutor());
    }
}