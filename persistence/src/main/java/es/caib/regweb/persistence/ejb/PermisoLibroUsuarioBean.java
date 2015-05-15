package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.*;
import es.caib.regweb.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "PermisoLibroUsuarioEJB")
@SecurityDomain("seycon")
public class PermisoLibroUsuarioBean extends BaseEjbJPA<PermisoLibroUsuario, Long> 
   implements PermisoLibroUsuarioLocal, RegwebConstantes {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb")
    private EntityManager em;

    @EJB(mappedName = "regweb/CatEstadoEntidadEJB/local")
    public CatEstadoEntidadLocal catEstadoEntidadEjb;

    @EJB(mappedName = "regweb/UsuarioEntidadEJB/local")
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb/LibroEJB/local")
    public LibroLocal libroEjb;

    @EJB(mappedName = "regweb/PermisoLibroUsuarioEJB/local")
    public PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

    @EJB(mappedName = "regweb/EntidadEJB/local")
    public EntidadLocal entidadEjb;

    @Override
    public PermisoLibroUsuario findById(Long id) throws Exception {

        return em.find(PermisoLibroUsuario.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PermisoLibroUsuario> getAll() throws Exception {

        return  em.createQuery("Select permisoLibroUsuario from PermisoLibroUsuario as permisoLibroUsuario order by permisoLibroUsuario.libro.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(permisoLibroUsuario.id) from PermisoLibroUsuario as permisoLibroUsuario");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<PermisoLibroUsuario> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select permisoLibroUsuario from PermisoLibroUsuario as permisoLibroUsuario order by permisoLibroUsuario.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


    @Override
    public List<PermisoLibroUsuario> findByUsuario(Long idUsuarioEntidad) throws Exception {

        Query q = em.createQuery("Select plu from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad order by plu.libro.id, plu.permiso");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);

        return q.getResultList();
    }

    @Override
    public List<PermisoLibroUsuario> findByUsuarioLibro(Long idUsuarioEntidad, Long idLibro) throws Exception{

        Query q = em.createQuery("Select plu from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.id = :idLibro order by plu.permiso");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("idLibro",idLibro);

        return q.getResultList();
    }

    @Override
    public List<PermisoLibroUsuario> findByLibro(Long idLibro) throws Exception {

        Query q = em.createQuery("Select plu from PermisoLibroUsuario as plu where plu.libro.id = :idLibro");

        q.setParameter("idLibro",idLibro);

        return q.getResultList();
    }

    @Override
    public List<UsuarioEntidad> getUsuariosEntidadByLibro(Long idLibro) throws Exception {

        Query q = em.createQuery("Select distinct plu.usuario from PermisoLibroUsuario as plu where " +
                " plu.libro.id = :idLibro");

        q.setParameter("idLibro",idLibro);

        return q.getResultList();
    }

    @Override
    public List<Libro> getLibrosRegistro(Long idUsuarioEntidad) throws Exception {

        CatEstadoEntidad vigente = catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        Query q = em.createQuery("Select distinct plu.libro from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.organismo.estado.id = :vigente and " +
                "plu.libro.activo = true and plu.activo = true");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("vigente",vigente.getId());

        return q.getResultList();
    }

    @Override
    public List<Libro> getLibrosAdministrados(Long idUsuarioEntidad) throws Exception {

        CatEstadoEntidad vigente = catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        Query q = em.createQuery("Select distinct plu.libro from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.organismo.estado.id = :vigente and " +
                "plu.libro.activo = true and " +
                "(plu.permiso = " + PERMISO_ADMINISTRACION_LIBRO + " and plu.activo = true)");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("vigente",vigente.getId());

        return q.getResultList();
    }

    @Override
    public List<Libro> getLibrosPermiso(Long idUsuarioEntidad, Long idPermiso) throws Exception{

        Query q = em.createQuery("Select distinct plu.libro from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.activo = true and (plu.permiso = :idPermiso and plu.activo = true)");

        q.setParameter("idPermiso",idPermiso);
        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);

        return q.getResultList();
    }

    @Override
    public List<Libro> getLibrosRegistroOficina(Set<Organismo> organismos, UsuarioEntidad usuario) throws Exception{

        Query q = em.createQuery("Select distinct plu.libro from PermisoLibroUsuario as plu where " +
                "plu.libro.organismo in (:organismos) and plu.usuario.id = :idUsuarioEntidad and " +
                "plu.libro.activo = true and " +
                "(plu.permiso = " +PERMISO_REGISTRO_ENTRADA + " or plu.permiso = " + PERMISO_REGISTRO_SALIDA +" and plu.activo = true)");

        q.setParameter("organismos",organismos);
        q.setParameter("idUsuarioEntidad",usuario.getId());

        return q.getResultList();
    }

    @Override
    public List<Libro> getLibrosOrganismoPermiso(Set<Organismo> organismos, UsuarioEntidad usuario, Long idPermiso) throws Exception{

        Query q = em.createQuery("Select distinct plu.libro from PermisoLibroUsuario as plu where " +
                "plu.libro.organismo in (:organismos) and plu.usuario.id = :idUsuarioEntidad and " +
                "plu.libro.activo = true and " +
                "(plu.permiso = :idPermiso and plu.activo = true)");

        q.setParameter("organismos",organismos);
        q.setParameter("idUsuarioEntidad",usuario.getId());
        q.setParameter("idPermiso",idPermiso);

        return q.getResultList();
    }

    @Override
    public List<Libro> getLibrosEntidadPermiso(Long idEntidad, UsuarioEntidad usuario, Long idPermiso) throws Exception {

        Query q = em.createQuery("Select distinct plu.libro from PermisoLibroUsuario as plu where " +
                "plu.libro.organismo.entidad.id = :idEntidad and plu.usuario.id = :idUsuarioEntidad and " +
                "plu.libro.activo = true and " +
                "(plu.permiso = :idPermiso and plu.activo = true)");

        q.setParameter("idEntidad",idEntidad);
        q.setParameter("idUsuarioEntidad",usuario.getId());
        q.setParameter("idPermiso",idPermiso);

        return q.getResultList();
    }

    @Override
    public Boolean isAdministradorLibro(Long idUsuarioEntidad, Long idLibro) throws Exception {

        CatEstadoEntidad vigente = catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        Query q = em.createQuery("Select plu from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.id= :idLibro and plu.libro.organismo.estado.id = :vigente and " +
                "plu.libro.activo = true and " +
                "(plu.permiso = " + PERMISO_ADMINISTRACION_LIBRO + " and plu.activo = true)");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("idLibro",idLibro);
        q.setParameter("vigente",vigente.getId());

        List<PermisoLibroUsuario> permisos = q.getResultList();

        return permisos.size() == 1;
    }

    @Override
    public Boolean tienePermiso(Long idUsuarioEntidad, Long idLibro, Long idPermiso) throws Exception {

        Query q = em.createQuery("Select plu from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.id = :idLibro and " +
                "plu.libro.activo = true and " +
                "(plu.permiso = :idPermiso and plu.activo = true)");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("idLibro",idLibro);
        q.setParameter("idPermiso",idPermiso);

        List<PermisoLibroUsuario> permisos = q.getResultList();

        return permisos.size() == 1;
    }

    @Override
    public List<UsuarioEntidad> getUsuariosEntidadEnLibros(List<Libro> libros) throws Exception{

        Query q = em.createQuery("Select distinct permisoLibroUsuario.usuario from PermisoLibroUsuario as permisoLibroUsuario where " +
                "permisoLibroUsuario.libro in (:libros)");

        q.setParameter("libros",libros);

        return q.getResultList();
    }

    @Override
    public void actualizarPermiso(Long idPermisoLibroUsuario, Boolean esActivo) throws Exception {

        Query q = em.createQuery("UPDATE PermisoLibroUsuario SET activo = :esActivo WHERE " +
                "id = :idPermisoLibroUsuario");

        q.setParameter("esActivo",esActivo);
        q.setParameter("idPermisoLibroUsuario",idPermisoLibroUsuario);

        q.executeUpdate();
    }

    @Override
    public void crearPermisosUsuarioNuevo(UsuarioEntidad usuarioEntidad, Long idEntidad) throws Exception {

        List<Libro> librosEntidad = libroEjb.getTodosLibrosEntidad(idEntidad);
        for (Libro libroEntidad : librosEntidad) {
            for (int column = 0; column < RegwebConstantes.PERMISOS.length; column++) {
                PermisoLibroUsuario plu = new PermisoLibroUsuario();
                plu.setLibro(libroEntidad);
                plu.setPermiso(RegwebConstantes.PERMISOS[column]);
                plu.setUsuario(usuarioEntidad);
                permisoLibroUsuarioEjb.persist(plu);
            }
        }

    }

    @Override
    public void crearPermisosLibroNuevo(Libro libro, Long idEntidad) throws Exception {

        List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findOperadoresByEntidad(idEntidad);
        for (UsuarioEntidad usuario : usuariosEntidad) {
            for ( int column = 0; column < RegwebConstantes.PERMISOS.length; column++ ){
                PermisoLibroUsuario plu = new PermisoLibroUsuario();
                plu.setLibro(libro);
                plu.setPermiso(RegwebConstantes.PERMISOS[column]);
                plu.setUsuario(usuario);
                permisoLibroUsuarioEjb.persist(plu);
            }
        }

    }

    @Override
    public void crearPermisosNoExistentes() throws Exception {

        List<Entidad> entidades = entidadEjb.getAll();

        for (Entidad entidad : entidades) {
            log.info("ENTITAT: " + entidad.getDescripcion());

            List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findOperadoresByEntidad(entidad.getId());
            List<Libro> libros = libroEjb.getTodosLibrosEntidad(entidad.getId());

            for (UsuarioEntidad usuario : usuariosEntidad) {
                for (Libro libro : libros) {
                    for (int column = 0; column < RegwebConstantes.PERMISOS.length; column++) {

                        if (!permisoLibroUsuarioEjb.existePermiso(usuario.getId(),libro.getId(),Long.valueOf(column + 1))) {
                            log.info("CREAT_PERMIS (LLIBRE/USUARI/PERMIS): " + libro.getCodigo() + " / " + usuario.getNombreCompleto() + " / " + RegwebConstantes.PERMISOS[column]);
                            PermisoLibroUsuario plu = new PermisoLibroUsuario();
                            plu.setLibro(libro);
                            plu.setPermiso(RegwebConstantes.PERMISOS[column]);
                            plu.setUsuario(usuario);
                            permisoLibroUsuarioEjb.persist(plu);
                        }

                    }
                }
            }

        }
    }

    @Override
    public Boolean existePermiso(Long idUsuarioEntidad, Long idLibro, Long idPermiso) throws Exception{

        Query q = em.createQuery("Select plu from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.id = :idLibro and " +
                "plu.permiso = :idPermiso");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("idLibro",idLibro);
        q.setParameter("idPermiso",idPermiso);

        List<PermisoLibroUsuario> permisos = q.getResultList();

        return permisos.size() == 1;
    }

}
