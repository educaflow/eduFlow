package com.eduflow.modules.expedientes;

import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.eduflow.expedientes.profesores.db.ExpedienteFaltaJustificada;
import com.eduflow.expedientes.profesores.db.repo.ExpedienteFaltaJustificadaRepository;
import com.eduflow.modules.expedientes.common.EventManager;
import com.eduflow.modules.expedientes.common.StateManager;
import com.eduflow.modules.expedientes.common.annotations.EventAction;
import com.google.inject.Inject;
import java.util.Map;
import static com.eduflow.modules.expedientes.common.ExpedienteUtil.returnView;

public class FaltaJustificadaEventManager implements EventManager<ExpedienteFaltaJustificada> {
    private final ExpedienteFaltaJustificadaRepository expedienteFaltaJustificadaRepository;

    @Inject
    public FaltaJustificadaEventManager(ExpedienteFaltaJustificadaRepository expedienteFaltaJustificadaRepository) {
        this.expedienteFaltaJustificadaRepository = expedienteFaltaJustificadaRepository;
    }

    @Override
    @EventAction
    public void initialEvent(ActionRequest request, ActionResponse response) {
        ExpedienteFaltaJustificada expedienteFaltaJustificada=new ExpedienteFaltaJustificada();
        returnView(response,expedienteFaltaJustificada,"form-expediente-falta-justificada-datos-iniciales");
    }

    @EventAction
    public void eventPresentarDatos(ActionRequest request, ActionResponse response) {
        ExpedienteFaltaJustificada expedienteFaltaJustificada=getRequestEntity(request);

        StateManager.setState(expedienteFaltaJustificada, Estado.PENDIENTE_PRESENTACION);

        expedienteFaltaJustificada=expedienteFaltaJustificadaRepository.save(expedienteFaltaJustificada);

        returnView(response,expedienteFaltaJustificada,"form-expediente-falta-justificada-pendiente-aprobacion");
    }

    @EventAction
    public void eventRechazarDatos(ActionRequest request, ActionResponse response) {
        response.setInfo("Expediente rechazado datos correctamente.");
    }

    @EventAction
    public void eventAceptarFaltaJustificada(ActionRequest request, ActionResponse response) {
        response.setInfo("Expediente aceptado falta justificada correctamente.");
    }

    @EventAction
    public void eventRechazarFaltaJustificada(ActionRequest request, ActionResponse response) {
        response.setInfo("Expediente rechazado correctamente.");
    }

    @EventAction
    public void eventPresentarJustificante(ActionRequest request, ActionResponse response) {
        response.setInfo("Expediente anulado correctamente.");
    }

    @EventAction
    public void eventAceptarJustificante(ActionRequest request, ActionResponse response) {
        response.setInfo("Justificante aceptado correctamente.");
    }

    @EventAction
    public void eventRechazarJustificante(ActionRequest request, ActionResponse response) {
        response.setInfo("Justificante rechazado correctamente.");
    }


    public enum Estado {
        // Cada constante del enum tiene una descripción asociada
        DATOS_INICIALES("Datos iniciales del expediente"),
        PENDIENTE_PRESENTACION("Esperando la presentación"),
        PENDIENTE_APROBACION("Esperando aprobación por parte del supervisor"),
        PENDIENTE_JUSTIFICANTE("Pendiente de recibir justificante del interesado"),
        PENDIENTE_CERRAR("Listo para ser cerrado"),
        CERRADO("Expediente cerrado");

        // Campo privado para almacenar la descripción de cada estado
        private final String description;

        // Constructor del enum: se ejecuta una vez por cada constante al cargar la clase
        Estado(String description) {
            this.description = description;
        }



        @Override
        public String toString() {
            return description;
        }


    }

}

