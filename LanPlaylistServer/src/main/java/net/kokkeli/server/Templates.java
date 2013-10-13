package net.kokkeli.server;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import net.kokkeli.ISettings;
import net.kokkeli.resources.Model;
import net.kokkeli.resources.ModelCollection;
import net.kokkeli.resources.Field;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.resources.models.ViewModel;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Class for template rendering
 * 
 * Must be initialized before first use.
 * 
 * @author Hekku2
 *
 */
public class Templates implements ITemplateService {
    private static Configuration cfg;
    
    /**
     * Creates template service
     * @param location
     * @throws IOException
     */
    @Inject
    public Templates(ISettings settings) throws IOException{
        cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(new File(settings.getTemplatesLocation()));
        cfg.setObjectWrapper(new DefaultObjectWrapper());
    }

    /**
     * Processes template with given model.
     * @param template Name of template
     * @param model Model used with template
     * @return Template processed with model.
     * @throws RenderException Thrown if there is exception with rendering.
     */
    public final String process(String template, BaseModel model) throws RenderException {
        if (model == null){
            throw new RenderException("Model can't be null.");
        }
        
        Template temp;
        try {
            temp = cfg.getTemplate(template);
        } catch (IOException e) {
            throw new RenderException(String.format("Template with name: %s was not found", template), e);
        } catch (IllegalArgumentException e){
            throw new RenderException("Template name cant be null.", e);
        }
        
        Map<String,Object> map = createMap(model);
        Writer out = new StringWriter();
        try {
            temp.process(map, out);
        } catch (TemplateException | IOException e) {
            throw new RenderException("Template could be processed with given model:" + e.toString(), e);
        }
        return out.toString();
    }
    
    /**
     * Processes template with no model.
     * @param template Name of the template
     * @return Processed template.
     * @throws RenderException Thrown if there is exception with rendering.
     */
    public final String process(String template) throws RenderException{
        
        Template temp;
        try {
            temp = cfg.getTemplate(template);
        } catch (IOException e) {
            throw new RenderException(String.format("Template with name: %s was not found", template), e);
        } catch (IllegalArgumentException e){
            throw new RenderException("Template name cant be null.", e);
        }
        Map<String,String> map = new HashMap<String, String>();
        
        Writer out = new StringWriter();
        try {
            temp.process(map, out);
        } catch (TemplateException | IOException e) {
            throw new RenderException("Template could be processed with given model:" + e.toString(), e);
        }
        return out.toString();
    }
    
    /**
     * Creates map from model.
     * @param model Used model
     * @return Map created from model.
     * @throws RenderException Thrown if there is problem with model.
     */
    private final Map<String,Object> createMap(ViewModel model) throws RenderException{
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        //Checks if method contain annotation used for building map.
        Method[] methods = model.getClass().getMethods();
        for (Method method : methods) {
            try {
                if (method.isAnnotationPresent(Field.class)){
                    map.put(method.getName(), method.invoke(model));
                } else if (method.isAnnotationPresent(ModelCollection.class)){
                    map.put(method.getName(), createMapFromModelCollection(model, method));
                } else if (method.isAnnotationPresent(Model.class)){
                    if ((ViewModel)method.invoke(model) != null){
                        map.put(method.getName(), createMap((ViewModel)method.invoke(model))); 
                    }
                }
            } catch (NullPointerException e){
                throw new RenderException("Method " + method.getName() + " can't be invoked with no arguments.", e);
            } catch (IllegalAccessException e) {
                throw new RenderException("Provided viewmodel contained Field-annotations with wrong access modifiers.", e);
            } catch (IllegalArgumentException e) {
                throw new RenderException("Provided viewmodel contained Field-annotations with arguments.", e);
            } catch (InvocationTargetException e) {
                throw new RenderException("Something went wrong while getting values from model. " + e.toString(), e);
            }
        }
        return map;
    }
    
    /**
     * Creates List of maps from method that is ModelCollection.
     * 
     * Method must not have arguments, and method must return object castable to List<ViewModel>.
     * @param method
     * @return List of maps
     * @throws RenderException Thrown if return value of method can't be casted to List<ViewModel>
     * @throws InvocationTargetException Thrown if called method throws exception
     * @throws IllegalArgumentException Thrown if method can't be called with zero arguments
     * @throws IllegalAccessException Thrown if method can't be called.
     */
    private final List<Map<String,Object>> createMapFromModelCollection(ViewModel model, Method method) throws RenderException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        try {
            @SuppressWarnings("unchecked")
            List<ViewModel> items = (List<ViewModel>)method.invoke(model);
            List<Map<String, Object>> transformed = new ArrayList<Map<String, Object>>();
            for (ViewModel item : items) {
                transformed.add(createMap(item));
            }
            return transformed;
        } catch (ClassCastException e) {
            throw new RenderException("Provided viewmodel contained ModelCollection-annotations with wrong access modifiers.", e);
        }
    }
}
