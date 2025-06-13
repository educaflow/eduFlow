package com.eduflow.modules.geo;

import com.axelor.i18n.I18n;
import com.axelor.inject.Beans;
import com.axelor.meta.loader.XMLViews;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.meta.schema.views.AbstractView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.eduflow.common.geo.db.Provincia;
import com.axelor.meta.service.MetaService;
import com.axelor.rpc.Response;

public class FlowAction {

    public void abrirFormularioDinamico(ActionRequest request, ActionResponse response) {
        Provincia registroActual = request.getContext().asType(Provincia.class);

        if (registroActual == null) {
            response.setInfo("No se pudo obtener el registro.");
            return;
        }

        System.out.println("Registro actual: " + registroActual.getDescripcion());

        String vistaFormularioADetalle;
        String tipoRegistro = registroActual.getEstado();

        if ("A".equals(tipoRegistro)) {
            vistaFormularioADetalle = "grid-provincias-A";
        } else if ("B".equals(tipoRegistro)) {
            vistaFormularioADetalle = "grid-provincias-B";
        } else {
            vistaFormularioADetalle = "grid-provincias";
        }

        response.setView(
                ActionView.define("Hola")
                        .model(Provincia.class.getName())
                        .add("grid", vistaFormularioADetalle)
                        .map());

    }

}
