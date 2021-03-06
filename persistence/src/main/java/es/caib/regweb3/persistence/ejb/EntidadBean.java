package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.UsuarioEntidad;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "EntidadEJB")
@SecurityDomain("seycon")
public class EntidadBean extends BaseEjbJPA<Entidad, Long> implements EntidadLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    @EJB public RegistroDetalleLocal registroDetalleEjb;
    @EJB public LibroLocal libroEjb;
    @EJB public CodigoAsuntoLocal codigoAsuntoEjb;
    @EJB public OficioRemisionLocal oficioRemisionEjb;
    @EJB public TipoDocumentalLocal tipoDocumentalEjb;
    @EJB public TipoAsuntoLocal tipoAsuntoEjb;
    @EJB public RegistroEntradaLocal registroEntradaEjb;
    @EJB public RegistroSalidaLocal registroSalidaEjb;
    @EJB public OrganismoLocal organismoEjb;
    @EJB public TrazabilidadLocal trazabilidadEjb;
    @EJB public PersonaLocal personaEjb;
    @EJB public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;
    @EJB public HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;
    @EJB public ReproLocal reproEjb;
    @EJB public LopdLocal lopdEjb;
    @EJB public AnexoLocal anexoEjb;
    @EJB public PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;
    @EJB public RelacionOrganizativaOfiLocal relacionOrganizativaOfiEjb;
    @EJB public RelacionSirOfiLocal relacionSirOfiEjb;
    @EJB public OficinaLocal oficinaEjb;
    @EJB public UsuarioEntidadLocal usuarioEntidadEjb;
    @EJB public DescargaLocal descargaEjb;
    @EJB public ModeloOficioRemisionLocal modeloOficioRemisionEjb;
    @EJB public ModeloReciboLocal modeloReciboEjb;
    @EJB public RegistroLopdMigradoLocal registroMigradoLopdEjb;
    @EJB public RegistroMigradoLocal registroMigradoEjb;



    @Override
    public Entidad findById(Long id) throws Exception {

        return em.find(Entidad.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Entidad> getAll() throws Exception {

        return  em.createQuery("Select entidad from Entidad as entidad order by entidad.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(entidad.id) from Entidad as entidad");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<Entidad> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select entidad from Entidad as entidad order by entidad.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public Entidad findByCodigoDir3(String codigo) throws Exception {

        Query q = em.createQuery("Select entidad from Entidad as entidad where entidad.codigoDir3 = :codigo ");

        q.setParameter("codigo", codigo);

        List<Entidad> entidad =  q.getResultList();

        if(entidad.size() == 1){
            return entidad.get(0);
        }else{
            return  null;
        }

    }

    @Override
    public Boolean tieneOrganismos(Long idEntidad) throws Exception{
        Query q = em.createQuery("Select organismo.id from Organismo as organismo where organismo.entidad.id = :idEntidad");

        q.setParameter("idEntidad",idEntidad);

        List<Long> organismos = q.getResultList();

        return organismos.size() > 0;
    }

    @Override
    public List<Entidad> getEntidadesAdministrador(Long idUsuario) throws Exception{

        Query q = em.createQuery("Select entidad.id, entidad.nombre from Entidad as entidad, UsuarioEntidad as usuarioEntidad where usuarioEntidad in elements(entidad.administradores) " +
                "and usuarioEntidad.usuario.id = :idUsuario and entidad.activo = true");

        q.setParameter("idUsuario",idUsuario);

        List<Entidad> entidades =  new ArrayList<Entidad>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result){
            Entidad entidad = new Entidad((Long)object[0],(String)object[1]);

            entidades.add(entidad);
        }

        return entidades;
    }

    @Override
    public List<Entidad> getEntidadesPropietario(Long idUsuario) throws Exception {

        Query q = em.createQuery("Select entidad.id, entidad.nombre from Entidad as entidad where entidad.propietario.id = :idUsuario " +
                "and entidad.activo = true");

        q.setParameter("idUsuario",idUsuario);

        List<Entidad> entidades =  new ArrayList<Entidad>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result){
            Entidad entidad = new Entidad((Long)object[0],(String)object[1]);

            entidades.add(entidad);
        }

        return entidades;
    }

    @Override
    public Boolean existeCodigoDir3Edit(String codigo, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select entidad.id from Entidad as entidad where " +
                "entidad.id != :idEntidad and entidad.codigoDir3 = :codigo");

        q.setParameter("codigo",codigo);
        q.setParameter("idEntidad",idEntidad);

        return q.getResultList().size() > 0;
    }

    @Override
    public Boolean esAdministrador(Long idEntidad, UsuarioEntidad usuarioEntidad) throws Exception {

        Query q = em.createQuery("Select entidad.id from Entidad as entidad where entidad.id=:idEntidad and entidad.activo = true and :usuarioEntidad in elements(entidad.administradores) ");

        q.setParameter("idEntidad",idEntidad);
        q.setParameter("usuarioEntidad", usuarioEntidad);

        return q.getResultList().size() > 0;

    }

    @Override
    public Boolean esAutorizado(Long idEntidad, Long idUsuario) throws Exception {

        Query q = em.createQuery("Select entidad.id from Entidad as entidad, UsuarioEntidad as usuarioEntidad where (entidad.propietario.id = :idUsuario or usuarioEntidad in elements(entidad.administradores) ) and entidad.id=:idEntidad");

        q.setParameter("idUsuario",idUsuario);
        q.setParameter("idEntidad",idEntidad);

        return q.getResultList().size() > 0;

    }


    @Override
    public void eliminarRegistros(Long idEntidad) throws Exception{

        log.info("Dentro eliminar Registros Entidad");

        /********* TRAZABILIDAD *********/
        log.info("Trazabilidades eliminadas: " +trazabilidadEjb.eliminarByEntidad(idEntidad));

        /********* OFICIOS REMISIÓN *********/
        log.info("OficiosRemision eliminados: " +oficioRemisionEjb.eliminarByEntidad(idEntidad));

        /********* PERSONAS *********/
        log.info("Personas eliminadas: " + personaEjb.eliminarByEntidad(idEntidad));


        /********* REGISTROS ENTRADA *********/

        // HistoricoRegistroEntrada
        log.info("HistoricosEntrada eliminados: " + historicoRegistroEntradaEjb.eliminarByEntidad(idEntidad));

        // RegistroEntrada
        Set<Long> registrosDetalle =  registroDetalleEjb.getRegistrosDetalle(idEntidad);

        log.info("RegistrosEntrada eliminados: " +registroEntradaEjb.eliminarByEntidad(idEntidad));

        /********* REGISTROS SALIDA *********/

        // HistoricoRegistroSalida
        log.info("HistoricosSalida eliminados: " + historicoRegistroSalidaEjb.eliminarByEntidad(idEntidad));

        // RegistroSalida
        log.info("RegistrosSalida eliminados: " +registroSalidaEjb.eliminarByEntidad(idEntidad));


        /********* REGISTROS DETALLE *********/

        log.info("RegistrosDetalle eliminados: " +registroDetalleEjb.eliminar(registrosDetalle));

        /********* REPRO *********/
        log.info("Repros eliminados: " +reproEjb.eliminarByEntidad(idEntidad));

        /********* LOPD *********/
        log.info("Lopds eliminados: " + lopdEjb.eliminarByEntidad(idEntidad));

        /********* LIBROS: PONER CONTADORES A 0  *********/
        List<Libro> libros = libroEjb.getLibrosEntidad(idEntidad);
        for (Libro libro : libros) {
           libroEjb.reiniciarContadores(libro.getId());
        }
        log.info("Libros reiniciados: " +libros.size());
        em.flush();

    }

    @Override
    public void eliminarEntidad(Long idEntidad)throws Exception{
        //todo: Añadir pre-registros cuando se active SIR

        log.info("Dentro eliminar Entidad");

        //Eliminamos todos los datos relacionados con los RegistrosEntradad y RegistrosSalida
        eliminarRegistros(idEntidad);

        /********* PERMISO LIBRO USUARIO *********/
        log.info("PermisoLibroUsuarios eliminados: " + permisoLibroUsuarioEjb.eliminarByEntidad(idEntidad));

        /********* LIBROS *********/
        log.info("Libros eliminados: " + libroEjb.eliminarByEntidad(idEntidad));

        /********* RelacionOrganizativaOfi *********/
        log.info("RelacionOrganizativaOfi eliminadas: " + relacionOrganizativaOfiEjb.eliminarByEntidad(idEntidad));

        /********* RelacionSirOfi *********/
        log.info("RelacionOrganizativaOfi eliminadas: " + relacionSirOfiEjb.eliminarByEntidad(idEntidad));

        /********* USUARIOS ENTIDAD *********/
        log.info("UsuariosEntidad eliminados: " + usuarioEntidadEjb.eliminarByEntidad(idEntidad));

        /********* OFICINAS *********/
        log.info("Oficinas eliminadas: " + oficinaEjb.eliminarByEntidad(idEntidad));

        /********* ORGANISMOS *********/
        log.info("Organismos eliminados: " + organismoEjb.eliminarByEntidad(idEntidad));

        /********* TIPOS ASUNTO Y CODIGOS ASUNTO *********/
        log.info("TipoAsuntos: " + tipoAsuntoEjb.eliminarByEntidad(idEntidad));

        /********* TIPOS DOCUMENTAL *********/
        log.info("TiposDocumental: " + tipoDocumentalEjb.eliminarByEntidad(idEntidad));

        /********* Modelo OficioRemision *********/
        log.info("Modelo OficioRemision: " + modeloOficioRemisionEjb.eliminarByEntidad(idEntidad));

        /********* Modelo Recibo *********/
        log.info("Modelo Recibo: " + modeloReciboEjb.eliminarByEntidad(idEntidad));

        /********* DESCARGAS *********/
        log.info("Descargas: " + descargaEjb.eliminarByEntidad(idEntidad));

        /********* REGISTROS MIGRADOS *********/
        log.info("RegistrosMigradosLopd: " + registroMigradoLopdEjb.eliminarByEntidad(idEntidad));
        log.info("RegistrosMigrados: " + registroMigradoEjb.eliminarByEntidad(idEntidad));

        /********* ENTIDAD *********/
        em.flush();

        //em.createQuery("delete from Entidad where id = :idEntidad ").setParameter("idEntidad", idEntidad).executeUpdate();

    }


}
