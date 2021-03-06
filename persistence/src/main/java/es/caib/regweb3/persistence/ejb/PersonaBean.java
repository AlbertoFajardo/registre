package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Persona;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "PersonaEJB")
@SecurityDomain("seycon")
public class PersonaBean extends BaseEjbJPA<Persona, Long> implements PersonaLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public Persona findById(Long id) throws Exception {

        return em.find(Persona.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Persona> getAll() throws Exception {

        return  em.createQuery("Select persona from Persona as persona order by persona.id").getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Persona> getAllbyEntidadTipo(Long idEntidad, Long tipoPersona) throws Exception {

        StringBuffer query = new StringBuffer("Select persona from Persona as persona  " +
                "where persona.entidad.id = :idEntidad ");
        
        if (tipoPersona != null) {
          query.append("and persona.tipo = :tipoPersona ");
        }
      
        query.append("order by persona.apellido1");
        
        Query q = em.createQuery(query.toString());

        q.setParameter("idEntidad",idEntidad);
        if (tipoPersona != null) {
          q.setParameter("tipoPersona",tipoPersona);
        }

        return  q.getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(persona.id) from Persona as persona");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<Persona> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select persona from Persona as persona order by persona.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public Boolean existeDocumentoNew(String documento, Long idEntidad) throws Exception{

        Query q = em.createQuery("Select persona.id from Persona as persona where " +
                "persona.documento = :documento and persona.entidad.id = :idEntidad");

        q.setParameter("documento",documento);
        q.setParameter("idEntidad",idEntidad);

        return q.getResultList().size() > 0;
    }

    @Override
    public Boolean existeDocumentoEdit(String documento, Long idPersona, Long idEntidad) throws Exception{
        Query q = em.createQuery("Select persona.id from Persona as persona where " +
                "persona.id != :idPersona and persona.documento = :documento and persona.entidad.id = :idEntidad");

        q.setParameter("documento",documento);
        q.setParameter("idPersona",idPersona);
        q.setParameter("idEntidad",idEntidad);

        return q.getResultList().size() > 0;
    }

    @Override
    public Paginacion busqueda(Integer pageNumber, Long idEntidad, String nombre, String apellido1, String apellido2, String documento) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select persona from Persona as persona ");

        if(nombre!= null && nombre.length() > 0){
            where.add(
               "( (" + DataBaseUtils.like("persona.nombre","nombre",parametros,nombre)
               + " ) OR ( "  
               + DataBaseUtils.like("persona.razonSocial","nombre",parametros,nombre)
               + " ) ) ");
        }
        if(apellido1!= null && apellido1.length() > 0){where.add(DataBaseUtils.like("persona.apellido1","apellido1",parametros,apellido1));}
        if(apellido2!= null && apellido2.length() > 0){where.add(DataBaseUtils.like("persona.apellido2","apellido2",parametros,apellido2));}
        if(documento!= null && documento.length() > 0){where.add(" upper(persona.documento) like upper(:documento) "); parametros.put("documento","%"+documento.toLowerCase()+"%");}
        where.add("persona.entidad.id = :idEntidad "); parametros.put("idEntidad",idEntidad);


        if (parametros.size() != 0) {
            query.append("where ");
            int count = 0;
            for (String w : where) {
                if (count != 0) {
                    query.append(" and ");
                }
                query.append(w);
                count++;
            }
            q2 = em.createQuery(query.toString().replaceAll("Select persona from Persona as persona ", "Select count(persona.id) from Persona as persona "));
            query.append("order by persona.id");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        }else{
            q2 = em.createQuery(query.toString().replaceAll("Select persona from Persona as persona ", "Select count(persona.id) from Persona as persona "));
            query.append("order by persona.id");
            q = em.createQuery(query.toString());
        }
        log.info("Query: " + query);

        Paginacion paginacion = null;

        if(pageNumber != null){ // Comprobamos si es una busqueda paginada o no
            Long total = (Long)q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        }else{
            paginacion = new Paginacion(0, 0);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;
        
    }

    @Override
    public List<Persona> busquedaFisicas(Long idEntidad, String nombre, String apellido1, String apellido2, String documento, Long idTipoPersona) throws Exception {

        Query q;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select persona from Persona as persona ");

        if(nombre!= null && nombre.length() > 0){where.add( DataBaseUtils.like("persona.nombre","nombre",parametros,nombre));}
        if(apellido1!= null && apellido1.length() > 0){where.add(DataBaseUtils.like("persona.apellido1","apellido1",parametros,apellido1));}
        if(apellido2!= null && apellido2.length() > 0){where.add(DataBaseUtils.like("persona.apellido2","apellido2",parametros,apellido2));}
        if(documento!= null && documento.length() > 0){where.add(" upper(persona.documento) like upper(:documento) "); parametros.put("documento","%"+documento.toLowerCase()+"%");}
        where.add("persona.entidad.id = :idEntidad "); parametros.put("idEntidad",idEntidad);
        where.add("persona.tipo = :idTipoPersona "); parametros.put("idTipoPersona",idTipoPersona);

        // Añadimos los parametros de búsqueda
        if (parametros.size() != 0) {
            query.append("where ");
            int count = 0;
            for (String w : where) {
                if (count != 0) {
                    query.append(" and ");
                }
                query.append(w);
                count++;
            }

            query.append("order by persona.apellido1");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());

            }

        }else{

            query.append("order by persona.apellido1");
            q = em.createQuery(query.toString());
        }


        return q.getResultList();

    }

    public List<Persona> busquedaJuridicas(Long idEntidad, String razonSocial, String documento, Long idTipoPersona) throws Exception{
        Query q;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select persona from Persona as persona ");

        if(razonSocial!= null && razonSocial.length() > 0){where.add( DataBaseUtils.like("persona.razonSocial","razonSocial",parametros,razonSocial));}
        if(documento!= null && documento.length() > 0){where.add(" upper(persona.documento) like upper(:documento) "); parametros.put("documento","%"+documento.toLowerCase()+"%");}
        where.add("persona.entidad.id = :idEntidad "); parametros.put("idEntidad",idEntidad);
        where.add("persona.tipo = :idTipoPersona "); parametros.put("idTipoPersona",idTipoPersona);

        // Añadimos los parametros de búsqueda
        if (parametros.size() != 0) {
            query.append("where ");
            int count = 0;
            for (String w : where) {
                if (count != 0) {
                    query.append(" and ");
                }
                query.append(w);
                count++;
            }

            query.append("order by persona.razonSocial");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());

            }

        }else{

            query.append("order by persona.razonSocial");
            q = em.createQuery(query.toString());
        }


        return q.getResultList();
    }

    public List<Persona> busquedaPersonas(Long idEntidad, String nombre, String apellido1, String apellido2, String documento, String razonSocial) throws Exception{

        Query q;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select persona from Persona as persona ");

        
        if(nombre!= null && nombre.length() > 0){where.add( DataBaseUtils.like("persona.nombre","nombre",parametros,nombre));}
        if(apellido1!= null && apellido1.length() > 0){where.add( DataBaseUtils.like("persona.apellido1","apellido1",parametros,apellido1));}
        if(apellido2!= null && apellido2.length() > 0){where.add( DataBaseUtils.like("persona.apellido2","apellido2",parametros,apellido2));}
        if(documento!= null && documento.length() > 0){where.add(" upper(persona.documento) like upper(:documento) "); parametros.put("documento","%"+documento.toLowerCase()+"%");}
        if(razonSocial!= null && razonSocial.length() > 0){where.add(" upper(persona.razonSocial) like upper(:razonSocial) "); parametros.put("razonSocial","%"+razonSocial.toLowerCase()+"%");}
        where.add("persona.entidad.id = :idEntidad "); parametros.put("idEntidad",idEntidad);

        // Añadimos los parametros de búsqueda
        if (parametros.size() != 0) {
            query.append("where ");
            int count = 0;
            for (String w : where) {
                if (count != 0) {
                    query.append(" and ");
                }
                query.append(w);
                count++;
            }

            query.append("order by persona.apellido1");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());

            }

        }else{

            query.append("order by persona.apellido1");
            q = em.createQuery(query.toString());
        }


        return q.getResultList();
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        Query query = em.createQuery("delete from Persona where entidad.id = :idEntidad");
        return query.setParameter("idEntidad", idEntidad).executeUpdate();
    }
}
