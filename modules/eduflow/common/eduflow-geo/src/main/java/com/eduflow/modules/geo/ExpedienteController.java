package com.eduflow.modules.geo;

import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.eduflow.expedientes.common.db.Expediente;
import com.eduflow.expedientes.common.db.TipoExpediente;

import java.lang.reflect.Method;

public class ExpedienteController {

    public void initialEvent(ActionRequest request, ActionResponse response) {
        try {
            TipoExpediente tipoExpediente=request.getContext().asType(TipoExpediente.class);

            if (tipoExpediente == null) {
                throw new RuntimeException("No existe el tipo del expediente a crear.");
            }

            EventManager eventManager=(EventManager)Class.forName(tipoExpediente.getFqcnEventManager()).getDeclaredConstructor().newInstance();



            eventManager.initialEvent(request,response);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void event(ActionRequest request, ActionResponse response) {
        try {
            Expediente expediente=request.getContext().asType(Expediente.class);

            String eventName=(String)request.getContext().get("_signal");

            String fqcnEventManager = expediente.getTipoExpediente().getFqcnEventManager();
            if (fqcnEventManager == null || fqcnEventManager.isEmpty()) {
                throw new RuntimeException("No existe el EventManager para el tipo de expediente: " + expediente.getTipoExpediente().getName());
            }

            Class<EventManager> eventManagerClass;

            eventManagerClass=(Class<EventManager>)Class.forName(fqcnEventManager);


            EventManager eventManager=(EventManager)eventManagerClass.getDeclaredConstructor().newInstance();

            Class<?>[] parameterTypes = new Class<?>[]{ActionRequest.class, ActionResponse.class};
            Method method = eventManagerClass.getMethod(eventName, parameterTypes);

            method.invoke(eventManager, request, response);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
