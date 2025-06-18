package com.eduflow.modules.expedientes.common;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.Model;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionResponse;

public class ExpedienteUtil {

    public static void  returnView (ActionResponse response, Model entity, String viewName) {
        response.setView(
                ActionView.define(entity.getClass().getSimpleName())
                        .model(entity.getClass().getName())
                        .add("form", viewName)
                        .param("forceEdit", "true")
                        .context("_showRecord", entity.getId())
                        .map());
    }

}
