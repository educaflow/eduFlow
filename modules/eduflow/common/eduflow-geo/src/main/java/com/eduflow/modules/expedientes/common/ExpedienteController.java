package com.eduflow.modules.expedientes.common;

import com.axelor.db.Repository;
import com.axelor.meta.CallMethod;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.eduflow.expedientes.common.db.Expediente;
import com.eduflow.expedientes.common.db.TipoExpediente;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.lang.reflect.Method;

public class ExpedienteController {

    private final Injector injector;

    @Inject
    public ExpedienteController(Injector injector) {
        this.injector = injector;
    }

    @CallMethod
    public void initialEvent(ActionRequest request, ActionResponse response) {
        try {
            TipoExpediente tipoExpediente=request.getContext().asType(TipoExpediente.class);
            EventManager eventManager=getEventManager(tipoExpediente);

            eventManager.initialEvent(request,response);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @CallMethod
    public void event(ActionRequest request, ActionResponse response) {
        try {
            Expediente expediente=request.getContext().asType(Expediente.class);
            TipoExpediente tipoExpediente=expediente.getTipoExpediente();
            EventManager eventManager=getEventManager(tipoExpediente);

            String eventName=(String)request.getContext().get("_signal");

            Method eventMethod = getEventMethod(eventManager, eventName);

            method.invoke(eventManager, request, response);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }




    /********************************************/
    /************ Funciones Privadas ************/
    /********************************************/


    private EventManager getEventManager(TipoExpediente tipoExpediente) {
        if (tipoExpediente == null) {
            throw new RuntimeException("No existe el tipo del expediente a crear.");
        }
        String fqcnEventManager = tipoExpediente.getFqcnEventManager();
        if (fqcnEventManager == null || fqcnEventManager.isEmpty()) {
            throw new RuntimeException("No existe el EventManager para el tipo de expediente: " + tipoExpediente.getName());
        }
        Class<EventManager> eventManagerClass=(Class<EventManager>)Class.forName(tipoExpediente.getFqcnEventManager());

        EventManager eventManager=(EventManager) injector.getInstance(eventManagerClass);

        return eventManager;
    }

    private Method getEventMethod(EventManager eventManager, String eventName) {
        try {
            Class<EventManage> eventManagerClass = eventManager.getClass();
            Class<?>[] parameterTypes = new Class<?>[]{ActionRequest.class, ActionResponse.class};
            Method method = eventManagerClass.getMethod(eventName, parameterTypes);

            return method;
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo encontrar el método del evento: " + eventName + " en la clase: " + eventManager, ex);
        }
    }

}
