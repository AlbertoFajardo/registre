package es.caib.regweb3.webapp.editor;

import es.caib.regweb3.model.Usuario;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.PropertyEditorSupport;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 11/02/14
 */
public class UsuarioEditor extends PropertyEditorSupport {

    protected final Log log = LogFactory.getLog(getClass());

    public UsuarioEditor(){
        super();
    }

    public void setAsText(String text) throws IllegalArgumentException {

        Usuario usuario = new Usuario();

        if(text != null && text.length() > 0){
            usuario.setId(Long.parseLong(text));
        }

        setValue(usuario);
    }
}
