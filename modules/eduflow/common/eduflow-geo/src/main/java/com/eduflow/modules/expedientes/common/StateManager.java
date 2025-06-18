package com.eduflow.modules.expedientes.common;

public class StateManager<T extends Enum> {

    public static T getState(Expediente expediente) {
        return T.valueOf(expediente.getStateCode());
    }


    public static void setState(Expediente expediente, T newState,EventManager eventManager) {
        expediente.setStateCode(newState.name());
        String methodName = getMethodName(newState);
        Method method = eventManager.getClass().getMethod(methodName, Expediente.class);

        method.invoke(eventManager, expediente);
    }


    private String getMethodName(T state) {
        String enumName = state.name();
        StringBuilder methodNameBuilder = new StringBuilder("do");

        String[] parts = enumName.split("_");

        for (String part : parts) {
            if (!part.isEmpty()) {
                methodNameBuilder.append(capitalizeFirstLetter(part));
            }
        }
        return methodNameBuilder.toString();
    }


    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

}
