package net.kokkeli.server;

import net.kokkeli.resources.models.ViewModel;

/**
 * Interface for templateservice.
 * @author Hekku2
 *
 */
public interface ITemplateService {
    /**
     * Processes template with given model.
     * @param template Name of template
     * @param model Model used with template
     * @return Template processed with model.
     * @throws RenderException Thrown if there is exception with rendering.
     */
    String process(String template, ViewModel model) throws RenderException;
    
    /**
     * Processes template with no model.
     * @param template Name of the template
     * @return Processed template.
     * @throws RenderException Thrown if there is exception with rendering.
     */
    String process(String template) throws RenderException;
}
