package es.caib.regweb.webapp.editor;

import es.caib.regweb.model.UsuarioEntidad;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.PropertyEditorSupport;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 11/02/14
 */
public class UsuarioEntidadEditor extends PropertyEditorSupport {

    protected final Log log = LogFactory.getLog(getClass());

    public UsuarioEntidadEditor(){
        super();
    }

    public void setAsText(String text) throws IllegalArgumentException {

        UsuarioEntidad usuarioEntidad = new UsuarioEntidad();

        if(text != null && text.length() > 0){
            usuarioEntidad.setId(Long.parseLong(text));
        }

        setValue(usuarioEntidad);
    }
}
