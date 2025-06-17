package com.eduflow.modules.geo;

import com.axelor.auth.db.AuditableModel;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface EventManager<T extends AuditableModel> {

    public void initialEvent(ActionRequest request, ActionResponse response);

    @SuppressWarnings("unchecked")
    default T getRequestEntity(ActionRequest request) {
        Class<T> clazz=null;
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }

        if (clazz == null) {
            throw new IllegalStateException("Cannot determine entity type from EventManager implementation: " + getClass().getName());
        }

        return (T) request.getContext().asType(clazz);
    };

    default void returnView (ActionResponse response,AuditableModel entity,String viewName) {
        response.setView(
                ActionView.define(entity.getClass().getSimpleName())
                        .model(entity.getClass().getName())
                        .add("form", viewName)
                        .param("forceEdit", "true")
                        .context("_showRecord", entity.getId())
                        .map());
    }
}
