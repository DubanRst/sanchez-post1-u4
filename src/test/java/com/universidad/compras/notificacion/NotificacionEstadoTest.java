package com.universidad.compras.notificacion;

import com.universidad.compras.aprobacion.ServicioAprobacionPorNiveles;
import com.universidad.compras.ejecucion.EjecutorSolicitud;
import com.universidad.compras.ejecucion.OrdenCompraService;
import com.universidad.compras.ejecucion.PresupuestoService;
import com.universidad.compras.modelo.Solicitud;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificacionEstadoTest {

    private PublicadorCambioEstado publicadorConTresReacciones() {
        return new PublicadorCambioEstado(
            List.of(new ObservadorCorreo(), new ObservadorContabilidad(), new ObservadorAuditoria()));
    }

    @Test
    void cambiarEstadoDisparaLasTresReaccionesSinLanzarExcepcion() {
        Solicitud s = new Solicitud("S-020", "ana@udes.edu.co", 2500000, "SOFTWARE", "CC-100");
        PublicadorCambioEstado publicador = publicadorConTresReacciones();

        assertDoesNotThrow(() -> publicador.cambiarEstado(s, "APROBADA"));
        assertEquals("APROBADA", s.getEstado());
    }

    @Test
    void agregarUnCuartoSuscriptorDePruebaNoRequiereModificarElMecanismo() {
        Solicitud s = new Solicitud("S-020", "ana@udes.edu.co", 2500000, "SOFTWARE", "CC-100");
        PublicadorCambioEstado publicador = publicadorConTresReacciones();
        List<String> recibidos = new ArrayList<>();
        publicador.suscribir((solicitud, anterior) -> recibidos.add(anterior + "->" + solicitud.getEstado()));

        publicador.cambiarEstado(s, "APROBADA");

        assertEquals(List.of("PENDIENTE->APROBADA"), recibidos);
    }

    @Test
    void laEvaluacionDeAprobacionDisparaLaNotificacion() {
        List<String> recibidos = new ArrayList<>();
        PublicadorCambioEstado publicador = new PublicadorCambioEstado(List.of());
        publicador.suscribir((solicitud, anterior) -> recibidos.add(solicitud.getEstado()));
        ServicioAprobacionPorNiveles servicio = new ServicioAprobacionPorNiveles(publicador);

        servicio.evaluar(new Solicitud("S-021", "ana@udes.edu.co", 1500000, "MATERIAL_OFICINA", "CC-100"));

        assertEquals(List.of("APROBADA"), recibidos);
    }

    @Test
    void laEjecucionDisparaLaNotificacionDeEstadoEjecutada() {
        List<String> recibidos = new ArrayList<>();
        PublicadorCambioEstado publicador = new PublicadorCambioEstado(List.of());
        publicador.suscribir((solicitud, anterior) -> recibidos.add(solicitud.getEstado()));
        Solicitud s = new Solicitud("S-022", "ana@udes.edu.co", 3000000, "SOFTWARE", "CC-100");
        s.setEstado("APROBADA");
        EjecutorSolicitud ejecutor =
            new EjecutorSolicitud(s, new PresupuestoService(), new OrdenCompraService(), publicador);

        ejecutor.ejecutarSolicitud("Proveedor Andino");

        assertEquals(List.of("EJECUTADA"), recibidos);
    }
}