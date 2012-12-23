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
import net.kokkeli.resources.ModelCollection;
import net.kokkeli.resources.Field;
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
    public final String process(String template, ViewModel model) throws RenderException {
        if (model == null){
            throw new RenderException("Model can't be null.");
        }
        
        Template temp;
        try {
            temp = cfg.getTemplate(template);
        } catch (IOException e) {
            throw new RenderException(String.format("Template with name: %s was not found", template));
        } catch (IllegalArgumentException e){
            throw new RenderException("Template name cant be null.");
        }
        
        Map<String,Object> map = createMap(model);
        Writer out = new StringWriter();
        try {
            temp.process(map, out);
        } catch (TemplateException | IOException e) {
            throw new RenderException("Template could be processed with given model:" + e.toString());
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
            throw new RenderException(String.format("Template with name: %s was not found", template));
        } catch (IllegalArgumentException e){
            throw new RenderException("Template name cant be null.");
        }
        Map<String,String> map = new HashMap<String, String>();
        
        Writer out = new StringWriter();
        try {
            temp.process(map, out);
        } catch (TemplateException | IOException e) {
            throw new RenderException("Template could be processed with given model:" + e.toString());
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
                }
            } catch (NullPointerException e){
                throw new RenderException("Method " + method.getName() + " can't be invoked with no arguments.");
            } catch (IllegalAccessException e) {
                throw new RenderException("Provided viewmodel contained Field-annotations with wrong access modifiers.");
            } catch (IllegalArgumentException e) {
                throw new RenderException("Provided viewmodel contained Field-annotations with arguments.");
            } catch (InvocationTargetException e) {
                throw new RenderException("Something went wrong while getting values from model.");
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
            throw new RenderException("Provided viewmodel contained ModelCollection-annotations with wrong access modifiers.");
        }
    }
}
