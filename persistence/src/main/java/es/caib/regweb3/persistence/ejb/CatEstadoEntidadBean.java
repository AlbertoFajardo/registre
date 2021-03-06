package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatEstadoEntidad;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "CatEstadoEntidadEJB")
@SecurityDomain("seycon")
public class CatEstadoEntidadBean extends BaseEjbJPA<CatEstadoEntidad, Long> implements CatEstadoEntidadLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public CatEstadoEntidad findById(Long id) throws Exception {

        return em.find(CatEstadoEntidad.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatEstadoEntidad> getAll() throws Exception {

        return  em.createQuery("Select catEstadoEntidad from CatEstadoEntidad as catEstadoEntidad order by catEstadoEntidad.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(catEstadoEntidad.id) from CatEstadoEntidad as catEstadoEntidad");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<CatEstadoEntidad> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select catEstadoEntidad from CatEstadoEntidad as catEstadoEntidad order by catEstadoEntidad.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public CatEstadoEntidad findByCodigo(String codigo) throws Exception {
        Query q = em.createQuery("Select catEstadoEntidad from CatEstadoEntidad as catEstadoEntidad where catEstadoEntidad.codigoEstadoEntidad = :codigo");

        q.setParameter("codigo",codigo);

        List<CatEstadoEntidad> catEstadoEntidad = q.getResultList();
        if(catEstadoEntidad.size() == 1){
            return catEstadoEntidad.get(0);
        }else{
            return  null;
        }

    }
}
