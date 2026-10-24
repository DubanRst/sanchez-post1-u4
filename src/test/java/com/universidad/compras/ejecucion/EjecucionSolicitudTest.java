package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EjecucionSolicitudTest {

    @Test
    void ejecutarReservaPresupuestoYGeneraOrden() {
        Solicitud s = new Solicitud("S-010", "ana@udes.edu.co", 3000000, "SOFTWARE", "CC-100");
        s.setEstado("APROBADA");
        EjecutorSolicitud ejecutor = new EjecutorSolicitud(s, new PresupuestoService(), new OrdenCompraService());

        ejecutor.ejecutarSolicitud("Proveedor Andino");

        assertEquals("EJECUTADA", s.getEstado());
    }

    @Test
    void deshacerSoloLaUltimaOperacionNoAfectaLaAnterior() {
        List<String> llamadas = new ArrayList<>();
        PresupuestoService presupuesto = new PresupuestoService() {
            @Override
            public boolean reservar(String centroCosto, double monto) {
                llamadas.add("reservar");
                return super.reservar(centroCosto, monto);
            }

            @Override
            public void liberar(String centroCosto, double monto) {
                llamadas.add("liberar");
                super.liberar(centroCosto, monto);
            }
        };
        OrdenCompraService ordenes = new OrdenCompraService() {
            @Override
            public String generar(String solicitudId, String proveedor) {
                llamadas.add("generar");
                return super.generar(solicitudId, proveedor);
            }

            @Override
            public void cancelar(String numeroOrden) {
                llamadas.add("cancelar");
                super.cancelar(numeroOrden);
            }
        };
        Solicitud s = new Solicitud("S-011", "luis@udes.edu.co", 4000000, "MATERIAL_OFICINA", "CC-200");
        EjecutorSolicitud ejecutor = new EjecutorSolicitud(s, presupuesto, ordenes);

        ejecutor.reservarPresupuesto();
        ejecutor.generarOrdenCompra("Proveedor Andino");
        ejecutor.deshacerUltima();

        assertEquals(List.of("reservar", "generar", "cancelar"), llamadas);
    }

    @Test
    void elHistorialConservaTodasLasOperacionesNoSoloLaUltima() {
        Solicitud s = new Solicitud("S-012", "luis@udes.edu.co", 4000000, "MATERIAL_OFICINA", "CC-200");
        EjecutorSolicitud ejecutor = new EjecutorSolicitud(s, new PresupuestoService(), new OrdenCompraService());

        ejecutor.reservarPresupuesto();
        ejecutor.generarOrdenCompra("Proveedor Andino");

        assertEquals(2, ejecutor.getHistorial().size());
    }
}