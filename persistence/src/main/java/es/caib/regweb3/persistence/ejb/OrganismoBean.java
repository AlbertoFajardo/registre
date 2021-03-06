package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal
 * Date: 16/01/14
 */

@Stateless(name = "OrganismoEJB")
@SecurityDomain("seycon")
public class OrganismoBean extends BaseEjbJPA<Organismo, Long> implements OrganismoLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB(mappedName = "regweb3/CatEstadoEntidadEJB/local")
    public CatEstadoEntidadLocal catEstadoEntidadEjb;

    @EJB(mappedName = "regweb3/OficinaEJB/local")
    public OficinaLocal oficinaEjb;

    @EJB(name = "LibroEJB")
    public LibroLocal libroEjb;

    @Override
    public Organismo findById(Long id) throws Exception {

        Organismo organismo = em.find(Organismo.class, id);
        Hibernate.initialize(organismo.getLibros());
        Hibernate.initialize(organismo.getHistoricoUO());

        return organismo;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> getAll() throws Exception {

        return em.createQuery("Select organismo from Organismo as organismo order by organismo.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(organismo.id) from Organismo as organismo");

        return (Long) q.getSingleResult();
    }

    @Override
    public Long getTotalByEntidad(Long entidad) throws Exception {

        Query q = em.createQuery("Select count(organismo.id) from Organismo as organismo where " +
                "organismo.entidad.id = :entidad");

        q.setParameter("entidad", entidad);

        return (Long) q.getSingleResult();
    }

    @Override
    public List<Organismo> getAllByEntidad(Long entidad) throws Exception {

        Query q = em.createQuery("Select organismo from Organismo as organismo where " +
                "organismo.entidad.id = :entidad");

        q.setParameter("entidad", entidad);

        return q.getResultList();
    }


    @Override
    public List<Organismo> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select organismo from Organismo as organismo order by organismo.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public List<Organismo> getPaginationByEntidad(int inicio, Long entidad) throws Exception {

        Query q = em.createQuery("Select organismo from Organismo as organismo where " +
                "organismo.entidad.id = :entidad");

        q.setParameter("entidad", entidad);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public Organismo findByCodigo(String codigo) throws Exception {

        Query q = em.createQuery("Select organismo from Organismo as organismo where " +
                "organismo.codigo = :codigo");

        q.setParameter("codigo", codigo);

        List<Organismo> organismo = q.getResultList();
        if (organismo.size() == 1) {
            return organismo.get(0);
        } else {
            return null;
        }

    }

    public Organismo findByCodigoLigero(String codigo) throws Exception {
        Query q = em.createQuery("Select organismo.id, organismo.codigo, organismo.denominacion, organismo.codAmbComunidad.id, organismo.estado.id from Organismo as organismo where " +
                "organismo.codigo = :codigo");

        q.setParameter("codigo", codigo);

        List<Object[]> result = q.getResultList();
        if (result.size() == 1) {
            Organismo organismo = new Organismo((Long) result.get(0)[0], (String) result.get(0)[1], (String) result.get(0)[2]);
            organismo.setCodAmbComunidad(new CatComunidadAutonoma((Long) result.get(0)[3]));
            organismo.setEstado(catEstadoEntidadEjb.findById((Long) result.get(0)[4]));
            return organismo;
        } else {
            return null;
        }
    }

    @Override
    public Organismo findByCodigoEntidad(String codigo, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select organismo.id,organismo.codigo, organismo.denominacion from Organismo as organismo where " +
                "organismo.codigo = :codigo and organismo.entidad.id = :idEntidad and " +
                "organismo.estado.codigoEstadoEntidad=:vigente");

        q.setParameter("codigo", codigo);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        List<Object[]> result = q.getResultList();
        if (result.size() == 1) {
            return new Organismo((Long) result.get(0)[0], (String) result.get(0)[1], (String) result.get(0)[2]);
        } else {
            return null;
        }

    }

    @Override
    public List<Organismo> findByEntidadLibros(Long entidad) throws Exception {

        //Obtenemos aquellos organismos de la entidad que tienen libros. Esto devuelve los organismos sin los libros
        Query q = em.createQuery("Select distinct(organismo.id), organismo.codigo, organismo.denominacion, organismo.estado.id from Organismo as organismo" +
                " inner join  organismo.libros as libros " +
                " where organismo.entidad.id = :entidad ");


        q.setParameter("entidad", entidad);

        // Montamos los organismos con los campos obtenidos de la query anterior, pero faltan los libros.
        List<Object[]> result = q.getResultList();
        List<Organismo> organismos = new ArrayList<Organismo>();
        for (Object[] object : result) {
            Organismo org = new Organismo((Long) object[0], (String) object[1], (String) object[2]);
            organismos.add(org);
        }

        // Realizamos una segunda query para obtener los libros de los organismos, ya que en la query inicial ha sido
        // imposible
        for (Organismo org : organismos) {
            List<Libro> libros = libroEjb.getLibrosOrganismoLigero(org.getId());
            org.setLibros(libros);
        }

        return organismos;

    }

    @Override
    public List<Organismo> findByEntidadEstadoLibros(Long entidad, String estado) throws Exception {

        Query q = em.createQuery("Select distinct(organismo.id), organismo.estado from Organismo as organismo" +
                " inner join  organismo.libros as libros " +
                " where organismo.entidad.id = :entidad and organismo.estado.codigoEstadoEntidad= :estado");

        q.setParameter("entidad", entidad);
        q.setParameter("estado", estado);


        List<Object[]> result = q.getResultList();
        List<Organismo> organismos = new ArrayList<Organismo>();
        for (Object[] object : result) {
            Organismo org = new Organismo((Long) object[0]);
            org.setEstado((CatEstadoEntidad) object[1]);
            organismos.add(org);
        }

        // Realizamos una segunda query para obtener los libros de los organismos, ya que en la query inicial ha sido
        // imposible
        for (Organismo org : organismos) {
            List<Libro> libros = libroEjb.getLibrosOrganismoLigero(org.getId());
            org.setLibros(libros);
        }

        return organismos;

    }


    @Override
    public List<Organismo> findByEntidadReduce(Long entidad) throws Exception {

        Query q = em.createQuery("Select organismo.id, organismo.denominacion from Organismo as organismo where " +
                "organismo.entidad.id = :entidad");

        q.setParameter("entidad", entidad);

        List<Object[]> result = q.getResultList();
        List<Organismo> organismos = new ArrayList<Organismo>();
        for (Object[] object : result) {
            Organismo org = new Organismo((Long) object[0], (String) object[1]);
            organismos.add(org);
        }

        return organismos;

    }

    @Override
    public List<Organismo> findByEntidadByEstado(Long entidad, String estado) throws Exception {

        Query q = em.createQuery("Select organismo.id, organismo.codigo, organismo.denominacion from Organismo as organismo where " +
                "organismo.entidad.id = :entidad and organismo.estado.codigoEstadoEntidad = :codigoEstado");

        q.setParameter("entidad", entidad);
        q.setParameter("codigoEstado", estado);

        List<Object[]> result = q.getResultList();
        List<Organismo> organismos = new ArrayList<Organismo>();
        for (Object[] object : result) {
            Organismo org = new Organismo((Long) object[0], (String) object[1], (String) object[2]);
            organismos.add(org);
        }

        return organismos;

    }

    @Override
    public List<Organismo> findByEntidadEstadoConOficinas(Long entidad, String codigoEstado) throws Exception {


        Query q = em.createQuery("Select distinct(organismo.id) , organismo.denominacion from Organismo as organismo, Oficina as oficina " +
                "inner join oficina.organismoResponsable as orgrespon " +
                "where orgrespon.id=organismo.id and oficina.estado.codigoEstadoEntidad=:codigoEstado and " +
                "organismo.entidad.id =:entidad and organismo.estado.codigoEstadoEntidad =:codigoEstado order by organismo.denominacion");

        q.setParameter("entidad", entidad);
        q.setParameter("codigoEstado", codigoEstado);


        List<Object[]> result = q.getResultList();
        Set<Organismo> organismosConOficinas = new HashSet<Organismo>();
        for (Object[] object : result) {
            Organismo org = new Organismo((Long) object[0], (String) object[1]);
            organismosConOficinas.add(org);
        }


        Query q2 = em.createQuery("Select distinct(organismo.id) , organismo.denominacion from Organismo as organismo, RelacionOrganizativaOfi as relorgOfi " +
                "inner join relorgOfi.organismo as orgfuncional " +
                "where orgfuncional.id=organismo.id  and relorgOfi.estado.codigoEstadoEntidad=:codigoEstado and " +
                "organismo.entidad.id =:entidad and organismo.estado.codigoEstadoEntidad =:codigoEstado order by organismo.denominacion");

        q2.setParameter("entidad", entidad);
        q2.setParameter("codigoEstado", codigoEstado);


        List<Object[]> result2 = q2.getResultList();
        Set<Organismo> organismosConOficinasFuncionales = new HashSet<Organismo>();
        for (Object[] object2 : result2) {
            Organismo org = new Organismo((Long) object2[0], (String) object2[1]);
            organismosConOficinasFuncionales.add(org);
        }
        organismosConOficinas.addAll(organismosConOficinasFuncionales);

        return new ArrayList<Organismo>(organismosConOficinas);

    }

    @Override
    public List<Organismo> getOrganismosByNivel(Long nivel, Long idEntidad, String estado) throws Exception {

        Query q = em.createQuery("Select organismo.id,organismo.codigo, organismo.denominacion, organismo.organismoSuperior.id from Organismo as organismo where " +
                "organismo.nivelJerarquico = :nivel and organismo.entidad.id = :idEntidad and organismo.estado.codigoEstadoEntidad = :estado order by organismo.codigo");

        q.setParameter("nivel", nivel);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("estado", estado);

        List<Organismo> organismos = new ArrayList<Organismo>();
        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            Organismo organismo = new Organismo((Long) object[0], (String) object[1], (String) object[2], (Long) object[3]);

            organismos.add(organismo);
        }

        return organismos;
    }


    @Override
    public Paginacion busqueda(Integer pageNumber, Long idEntidad, String denominacion, Long idCatEstadoEntidad) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select organismo from Organismo as organismo ");

        if (denominacion != null && denominacion.length() > 0) {
            where.add(DataBaseUtils.like("organismo.denominacion", "denominacion", parametros, denominacion));
        }
        if (idCatEstadoEntidad != null && idCatEstadoEntidad > 0) {
            where.add(" organismo.estado.id = :idCatEstadoEntidad");
            parametros.put("idCatEstadoEntidad", idCatEstadoEntidad);
        }

        // Añadimos la Entidad
        where.add("organismo.entidad.id = :idEntidad ");
        parametros.put("idEntidad", idEntidad);


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
            q2 = em.createQuery(query.toString().replaceAll("Select organismo from Organismo as organismo ", "Select count(organismo.id) from Organismo as organismo "));
            query.append("order by organismo.denominacion");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            q2 = em.createQuery(query.toString().replaceAll("Select organismo from Organismo as organismo ", "Select count(organismo.id) from Organismo as organismo "));
            query.append("order by organismo.denominacion");
            q = em.createQuery(query.toString());
        }
        log.info("Query: " + query);

        Paginacion paginacion = null;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        } else {
            paginacion = new Paginacion(0, 0);
        }
        List<Organismo> organismos = q.getResultList();
        for (Organismo org : organismos) {
            Hibernate.initialize(org.getLibros());
        }
        paginacion.setListado(organismos);

        return paginacion;

    }

    /**
     * Método que obtiene los organismos vigentes y en los que puede registrar de la oficina activa
     *
     * @param oficinaActiva
     * @return List
     * @throws Exception
     */
    @Override
    public LinkedHashSet<Organismo> getByOficinaActiva(Oficina oficinaActiva) throws Exception {

        // Añadimos los organismos a los que da servicio la Oficina (Directos y Funcionales)
        LinkedHashSet<Organismo> organismos = oficinaActiva.getOrganismosFuncionales();

        // Añadimos todos los hijos de los Organismos obtenidos anteriormetne
        LinkedHashSet<Organismo> hijosTotales = new LinkedHashSet<Organismo>();
        obtenerHijosOrganismos(organismos, hijosTotales);
        organismos.addAll(hijosTotales);

        return organismos;

    }

    /**
     * Método que nos devuelve los códigos DIR3 de las oficinas SIR de un organismo
     *
     * @param idOrganismo identificador del organismo
     * @return
     * @throws Exception
     */
    public List<String> organismoSir(Long idOrganismo) throws Exception {
        List<String> oficinasSir = new ArrayList<String>();

        Query q = em.createQuery("Select relacionSirOfi from RelacionSirOfi as relacionSirOfi where " +
                "relacionSirOfi.organismo.id = :idOrganismo ");

        List<RelacionSirOfi> relSir = q.getResultList();
        if (!relSir.isEmpty()) {
            for (RelacionSirOfi rel : relSir) {
                oficinasSir.add(rel.getOficina().getCodigo());
            }
        }
        return oficinasSir;
    }


    /**
     * Metodo que recursivamente obtiene todos los hijos de una lista de organismos
     *
     * @param organismosPadres organismos de los que obtener sus hijos
     * @param totales          organismos totales obtenidos despues del proceso recursivo.
     * @throws Exception
     */
    private void obtenerHijosOrganismos(LinkedHashSet<Organismo> organismosPadres, LinkedHashSet<Organismo> totales) throws Exception {

        // recorremos para todos los organismos Padres
        for (Organismo org : organismosPadres) {

            Query q = em.createQuery("select organismo.id,organismo.codigo, organismo.denominacion from Organismo as organismo where organismo.organismoSuperior.id =:idOrganismoSuperior and organismo.estado.codigoEstadoEntidad =:vigente ");
            q.setParameter("idOrganismoSuperior", org.getId());
            q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

            LinkedHashSet<Organismo> hijos = new LinkedHashSet<Organismo>();


            List<Object[]> result = q.getResultList();

            for (Object[] object : result) {
                Organismo hijo = new Organismo((Long) object[0], (String) object[1], (String) object[2]);

                hijos.add(hijo);
            }
            totales.addAll(hijos);

            // Hijos de cada organismo
            obtenerHijosOrganismos(hijos, totales);

        }

    }

    /**
     * Obtiene todos los organismos(padres e hijos) a los que da servicio el libro
     *
     * @param idLibro identificador del libro
     * @return
     * @throws Exception
     */
    public List<Organismo> getByLibro(Long idLibro) throws Exception {
        // Obtenemos el organismo responsable del libro.
        Query q2 = em.createQuery("Select libro.organismo from Libro as libro where libro.id=:idLibro");
        q2.setParameter("idLibro", idLibro);
        LinkedHashSet<Organismo> organismosPadres = new LinkedHashSet<Organismo>(q2.getResultList());

        // aquí guardaremos todos los organismo, el padre y sus hijos.
        LinkedHashSet<Organismo> totales = new LinkedHashSet<Organismo>();
        totales.addAll(organismosPadres);
        // recursivamente obtenemos los hijos de los organismos padre.
        obtenerHijosOrganismos(organismosPadres, totales);

        return new ArrayList<Organismo>(totales);
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception {

        List<?> organismos = em.createQuery("Select distinct(id) from Organismo where entidad.id =:idEntidad").setParameter("idEntidad", idEntidad).getResultList();
        Integer total = organismos.size();

        if (organismos.size() > 0) {

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (organismos.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = organismos.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                log.info("Historico UO eliminados: " + em.createNativeQuery("delete from RWE_HISTORICOUO WHERE CODULTIMA in(:organismos)").setParameter("organismos", subList).executeUpdate());
                em.createQuery("delete from Organismo where id in (:organismos) ").setParameter("organismos", subList).executeUpdate();
                organismos.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            log.info("Historico UO eliminados: " + em.createNativeQuery("delete from RWE_HISTORICOUO WHERE CODULTIMA in(:organismos)").setParameter("organismos", organismos).executeUpdate());
            em.createQuery("delete from Organismo where id in (:organismos) ").setParameter("organismos", organismos).executeUpdate();
        }

        return total;

    }

    public Boolean tieneOficinasServicio(Long idOrganismo) throws Exception {

        Boolean oficinas = oficinaEjb.tieneOficinasOrganismo(idOrganismo);
        if (!oficinas) {
            // Si no tiene Oficinas que cuelguen de el, buscamos en su padre
            Organismo organismo = findById(idOrganismo);
            if (organismo.getOrganismoSuperior() != null && (!organismo.getOrganismoSuperior().getId().equals(organismo.getOrganismoRaiz().getId()))) {

                return tieneOficinasServicio(organismo.getOrganismoSuperior().getId());
            }
            return false;
        }
        return true;
    }


    public void obtenerHistoricosFinales(Long id, Set<Organismo> historicosFinales) throws Exception {
        Organismo org = em.find(Organismo.class, id);
        Hibernate.initialize(org.getHistoricoUO());
        Set<Organismo> parciales = org.getHistoricoUO();
        for (Organismo parcial : parciales) {
            Organismo par = em.find(Organismo.class, parcial.getId());
            Hibernate.initialize(par.getHistoricoUO());
            if (par.getHistoricoUO().size() == 0) {
                historicosFinales.add(par);
            } else {
                obtenerHistoricosFinales(par.getId(), historicosFinales);
            }
        }

    }



}
