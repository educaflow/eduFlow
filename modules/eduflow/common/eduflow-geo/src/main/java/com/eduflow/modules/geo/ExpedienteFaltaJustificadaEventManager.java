package com.eduflow.modules.geo;

import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.eduflow.expedientes.common.db.repo.ExpedienteRepository;
import com.eduflow.expedientes.profesores.db.ExpedienteFaltaJustificada;
import com.eduflow.expedientes.profesores.db.repo.ExpedienteFaltaJustificadaRepository;
import com.google.inject.Inject;


import java.util.Map;

public class ExpedienteFaltaJustificadaEventManager implements EventManager<ExpedienteFaltaJustificada> {
    private final ExpedienteFaltaJustificadaRepository expedienteFaltaJustificadaRepository;

    @Inject
    public ExpedienteFaltaJustificadaEventManager(ExpedienteFaltaJustificadaRepository expedienteFaltaJustificadaRepository) {
        this.expedienteFaltaJustificadaRepository = expedienteFaltaJustificadaRepository;
    }

    @ExpedienteEvent
    public void eventPresentarDatos(ActionRequest request, ActionResponse response) {
        ExpedienteFaltaJustificada expedienteFaltaJustificada=getRequestEntity(request);

        expedienteFaltaJustificada.setCodeState(Estado.PENDIENTE_APROBACION);

        expedienteFaltaJustificada=expedienteFaltaJustificadaRepository.save(expedienteFaltaJustificada);

        returnView(response,expedienteFaltaJustificada,"form-expediente-falta-justificada-pendiente-aprobacion");
    }

    public void eventRechazarDatos(ActionRequest request, ActionResponse response) {
        response.setInfo("Expediente rechazado datos correctamente.");
    }


    public void eventAceptarFaltaJustificada(ActionRequest request, ActionResponse response) {
        response.setInfo("Expediente aceptado falta justificada correctamente.");
    }

    public void eventRechazarFaltaJustificada(ActionRequest request, ActionResponse response) {
        response.setInfo("Expediente rechazado correctamente.");
    }

    public void eventPresentarJustificante(ActionRequest request, ActionResponse response) {
        response.setInfo("Expediente anulado correctamente.");
    }

    public void eventAceptarJustificante(ActionRequest request, ActionResponse response) {
        response.setInfo("Justificante aceptado correctamente.");
    }

    public void eventRechazarJustificante(ActionRequest request, ActionResponse response) {
        response.setInfo("Justificante rechazado correctamente.");
    }

    @Override
    public void initialEvent(ActionRequest request, ActionResponse response) {
        ExpedienteFaltaJustificada expedienteFaltaJustificada=new ExpedienteFaltaJustificada();
        returnView(response,expedienteFaltaJustificada,"form-expediente-falta-justificada-datos-iniciales");
    }




    public static class Estado {
        public static final String DATOS_INICIALES="DATOS_INICIALES";
        public static final String PENDIENTE_APROBACION="PENDIENTE_APROBACION";
        public static final String PENDIENTE_JUSTIFICANTE="PENDIENTE_JUSTIFICANTE";
        public static final String PENDIENTE_CERRAR="PENDIENTE_CERRAR";
        public static final String CERRADO="CERRADO";


        private static final Map<String, String> NAMES = Map.of(
                DATOS_INICIALES, "Datos iniciales del expediente",
                PENDIENTE_APROBACION, "Esperando aprobación por parte del supervisor",
                PENDIENTE_JUSTIFICANTE, "Pendiente de recibir justificante del interesado",
                PENDIENTE_CERRAR, "Listo para ser cerrado",
                CERRADO, "Expediente cerrado"
        );

        private Estado() {
        }

        public static boolean isValid(String estado) {
            return NAMES.containsKey(estado);
        }

        public static String getName(String estado) {
            String name = NAMES.get(estado);
            if (name == null) {
                throw new IllegalArgumentException("Estado no válido: " + estado);
            }
            return name;
        }
    }

}

