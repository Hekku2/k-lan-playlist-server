package net.kokkeli.resources.models;

import java.lang.reflect.Method;

import javax.ws.rs.core.MultivaluedMap;

import net.kokkeli.data.dto.Role;
import net.kokkeli.data.services.ServiceException;

public class ModelBuilder<T extends ViewModel> {
    private Class<T> classOfModel;
    private final String setPrefix = "set";
    private final Method[] methods;
    
    public ModelBuilder(Class<T> classOfModel){
        this.classOfModel = classOfModel;
        methods = classOfModel.getMethods();
    }
    
    /**
     * Crates model from form params.
     * @param formParams
     * @return
     * @throws ServiceException 
     */
    public T createModelFrom(MultivaluedMap<String, String> formParams) throws ServiceException {
        try {
            T model = classOfModel.newInstance();
            
            for (Method method : methods) {
                String methodName = method.getName();
                
                if (containsParameter(methodName, formParams)){
                    String parameterName = parseParameterName(methodName);
                    setField(formParams.getFirst(parameterName), method, model);
                }
            }       
            return model;
        } catch (InstantiationException e) {
            throw new ServiceException("Parsing model from form failed.", e);
        } catch (IllegalAccessException e) {
            throw new ServiceException("Parsing model from form failed.", e);
        }

    }
    
    /**
     * Parses parameter name from methods name. (removes the set prefix)
     * @param methodName Method name
     * @return Parsed name of parameters
     */
    private String parseParameterName(String methodName){
        if (methodName.length() <= setPrefix.length())
            return null;
        return methodName.substring(setPrefix.length()).toLowerCase();
    }
    
    private boolean containsParameter(String methodName, MultivaluedMap<String, String> formParams){
        String parameterName = parseParameterName(methodName);
        return parameterName != null && formParams.containsKey(parameterName);
    }
    
    private void setField(String value, Method method, T model){
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 1){
            Class<?> type = parameterTypes[0];
            if (type == java.lang.Long.TYPE){
                handleLongType(method, value, model);
            }
            if (type == String.class){
                handleStringType(method, value, model);
            }
            if (type == java.lang.Boolean.TYPE){
                handleBooleanType(method, value, model);
            }
            if (type == Role.class){
                handleRoleType(method, value, model);
            }
        }
    }
    
    /**
     * Handles long type. Parses it and adds it to model.
     * @param method Method that is used for setting.
     * @param value Value that is parsed and set.
     * @param model Model that is modified
     */
    private void handleLongType(Method method, String value, T model){
        try {
            Long parsed = Long.parseLong(value.trim());
            method.invoke(model, parsed);
        } catch (Exception e) {
            // Never throw exception if model can't be built.
        } 
    }
    
    private void handleBooleanType(Method method, String value, T model){
        try {
            Boolean parsed = Boolean.valueOf(value);
            method.invoke(model, parsed);
        } catch (Exception e) {
            // Never throw exception if model can't be built.
        }
    }
    
    private void handleStringType(Method method, String value, T model){
        try {
            method.invoke(model, value);
        } catch (Exception e) {
            // Never throw exception if model can't be built.
        }
    }
    
    private void handleRoleType(Method method, String value, T model){
        try {
            int parsed = Integer.parseInt(value.trim());
            Role role = Role.getRole(parsed);
            
            method.invoke(model, role);
        } catch (Exception e) {
            // Never throw exception if model can't be built.
        }
    }
}
