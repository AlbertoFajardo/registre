package es.caib.regweb.webapp.form;

import es.caib.regweb.model.Organismo;
import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 7/05/14
 */
public class OrganismoBusquedaForm implements Serializable {

    private Organismo organismo;
    private Integer pageNumber;

    public OrganismoBusquedaForm() {
    }

    public OrganismoBusquedaForm(Organismo organismo, Integer pageNumber) {
        this.organismo = organismo;
        this.pageNumber = pageNumber;
    }

    public Organismo getOrganismo() {
        return organismo;
    }

    public void setOrganismo(Organismo organismo) {
        this.organismo = organismo;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
