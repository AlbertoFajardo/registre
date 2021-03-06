package es.caib.regweb3.webapp.controller;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.ObjetoBasico;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.validation.FieldError;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
public class BaseController {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/PermisoLibroUsuarioEJB/local")
    public PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

    @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb3/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb3/OficinaEJB/local")
    public OficinaLocal oficinaEjb;

    @EJB(mappedName = "regweb3/InteresadoEJB/local")
    public InteresadoLocal interesadoEjb;

    /**
     * Retorna el mensaje traducido según el idioma del usuario
     * @param key
     * @return
     */
    protected String getMessage(String key){
      return I18NUtils.tradueix(key);
    }

    /**
     * Retorna el usuario autenticado en la sesión
     * @param request
     * @return
     */
    protected Usuario getUsuarioAutenticado(HttpServletRequest request){

        HttpSession session = request.getSession();

        return (Usuario)session.getAttribute(RegwebConstantes.SESSION_USUARIO);
    }

    /**
     * Retorna el UsuarioEntidad activo, a partir del UsuarioAutenticado y la EntidadActiva.
     * @param request
     * @return
     */
    protected UsuarioEntidad getUsuarioEntidadActivo(HttpServletRequest request) throws Exception{
        //return usuarioEntidadEjb.findByUsuarioEntidad(getUsuarioAutenticado(request).getId(), getEntidadActiva(request).getId());
        HttpSession session = request.getSession();
        return (UsuarioEntidad)session.getAttribute(RegwebConstantes.SESSION_USUARIO_ENTIDAD);
    }

    /**
     * Retorna los Libros Administrados del UsuarioEntidad Avtico
     * @param request
     * @return
     */
    protected List<Libro> getLibrosAdministrados(HttpServletRequest request) throws Exception{

        HttpSession session = request.getSession();
        return (List<Libro>) session.getAttribute(RegwebConstantes.SESSION_LIBROSADMINISTRADOS);

    }

    /**
     * Retorna el Rol activo del usuario autenticado
     * @param request
     * @return
     */
    protected Rol getRolActivo(HttpServletRequest request){

        HttpSession session = request.getSession();

        return (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);
    }

    /**
     * Devuelve true o false en función de si el ROl Activo es ADMIN
     * @param request
     * @return
     */
    protected Boolean isAdminEntidad(HttpServletRequest request){

        HttpSession session = request.getSession();
        Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);

        return rolActivo.getNombre().equals(RegwebConstantes.ROL_ADMIN);
    }

    /**
     * Devuelve true o false en función de si el ROl Activo es SUPERADMIN
     * @param request
     * @return
     */
    protected Boolean isSuperAdmin(HttpServletRequest request){

        HttpSession session = request.getSession();
        Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);

        return rolActivo.getNombre().equals(RegwebConstantes.ROL_SUPERADMIN);
    }

    /**
     * Devuelve true o false en función de si el ROl Activo es OPERADOR
     * @param request
     * @return
     */
    protected Boolean isOperador(HttpServletRequest request){

        HttpSession session = request.getSession();
        Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);

        return rolActivo.getNombre().equals(RegwebConstantes.ROL_USUARI);
    }


    /**
     * Retorna los Roles que tiene asociados el usuario autenticado
     * @param request
     * @return
     */
    protected List<Rol> getRolesAutenticado(HttpServletRequest request){

        HttpSession session = request.getSession();

        return (List<Rol>) session.getAttribute(RegwebConstantes.SESSION_ROLES);

    }

    /**
     * Retorna la Entidad activa del usuario autenticado
     * @param request
     * @return
     */
    protected Entidad getEntidadActiva(HttpServletRequest request){

        HttpSession session = request.getSession();

        return (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);

    }

    /**
     * Actualiza la Entidad activa en la sesion
     * @param request
     * @return
     */
    protected void setEntidadActiva(Entidad entidad, HttpServletRequest request){

        HttpSession session = request.getSession();
        session.setAttribute(RegwebConstantes.SESSION_ENTIDAD,entidad);

    }

    /**
     * Retorna las Entidades que tiene asociadas el usuario autenticado
     * @param request
     * @return
     */
    protected List<Entidad> getEntidadesAutenticado(HttpServletRequest request){

        HttpSession session = request.getSession();

        return (List<Entidad>) session.getAttribute(RegwebConstantes.SESSION_ENTIDADES);
    }

    /**
     * Retorna la Oficina activa del usuario autenticado
     * @param request
     * @return
     */
    protected Oficina getOficinaActiva(HttpServletRequest request){

        HttpSession session = request.getSession();

        return (Oficina) session.getAttribute(RegwebConstantes.SESSION_OFICINA);
    }

    /**
     * Retorna las Oficinas que tiene asociadas el usuario autenticado
     * @param request
     * @return
     */
    protected Set<ObjetoBasico> getOficinasAutenticado(HttpServletRequest request){

        HttpSession session = request.getSession();

        return (Set<ObjetoBasico>) session.getAttribute(RegwebConstantes.SESSION_OFICINAS);

    }
    
    
    /**
     * Obtenemos los Libros de la EntidadActiva en los que el UsuarioEntidad actual tiene permisos para consultar registros de entrada
     * @param request
     * @return
     * @throws Exception
     */
    protected List<Libro> getLibrosConsultaEntradas(HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Entidad entidadActiva = getEntidadActiva(request);

        return permisoLibroUsuarioEjb.getLibrosEntidadPermiso(entidadActiva.getId(), usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_ENTRADA);
    }

    /**
     * Obtenemos los Libros de los Organismos a los que la OficinaActiva da servicio
     * y en los que el UsuarioEntidad actual tiene permisos para registrar entradas
     * @param request
     * @return
     * @throws Exception
     */
    protected List<Libro> getLibrosRegistroEntrada(HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Oficina oficinaActiva = getOficinaActiva(request);

        // Obtenemos los Organismos a los que da servicio una Oficina
        Set<Long> organismos = oficinaActiva.getOrganismosFuncionalesId();

        return permisoLibroUsuarioEjb.getLibrosOrganismoPermiso(organismos, usuarioEntidad.getId(), RegwebConstantes.PERMISO_REGISTRO_ENTRADA);
    }
    

    /**
     * Obtenemos los Libros de los Organismos a los que la OficinaActiva da servicio
     * y en los que el UsuarioEntidad actual tiene permisos para registrar salidas
     * @param request
     * @return
     * @throws Exception
     */
    protected List<Libro> getLibrosRegistroSalida(HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Oficina oficinaActiva = getOficinaActiva(request);

        // Obtenemos los Organismos a los que da servicio una Oficina
        Set<Long> organismos = oficinaActiva.getOrganismosFuncionalesId();

        return permisoLibroUsuarioEjb.getLibrosOrganismoPermiso(organismos, usuarioEntidad.getId(), RegwebConstantes.PERMISO_REGISTRO_SALIDA);
    }



    /**
     * Obtenemos los Libros de la Entidad Activa en los que el UsuarioEntidad actual tiene permisos para consultar registros de salida
     * @param request
     * @return
     * @throws Exception
     */
    protected List<Libro> getLibrosConsultaSalidas(HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Entidad entidadActiva = getEntidadActiva(request);


        return permisoLibroUsuarioEjb.getLibrosEntidadPermiso(entidadActiva.getId(), usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_SALIDA);
    }

    /**
     * Obtiene los Organismos de la OficinaActiva a los que da servicio
     * @param request
     * @return
     * @throws Exception
     */
    public Set<Organismo> getOrganismosOficinaActiva(HttpServletRequest request) throws Exception {
        return organismoEjb.getByOficinaActiva(getOficinaActiva(request));
    }

    /**
     * Obtiene los Id de los Organismos de la OficinaActiva a los que da servicio
     *
     * @param request
     * @return
     * @throws Exception
     */
    public Set<Long> getOrganismosIdOficinaActiva(HttpServletRequest request) throws Exception {

        // Obtenemos los Organismos
        Set<Organismo> organismos = getOrganismosOficinaActiva(request);

        // Creamos un Set solo con los identificadores
        Set<Long> organismosId = new HashSet<Long>();

        for (Organismo organismo : organismos) {
            organismosId.add(organismo.getId());
        }
        return organismosId;
    }


    /**
     *  Obtiene todas las oficinas de la entidad activa vigentes
     * @param request
     * @return
     * @throws Exception
     */
    public Set<Oficina> getOficinasOrigen(HttpServletRequest request ) throws  Exception {
        Set<Oficina> oficinasOrigen = new HashSet<Oficina>();
        oficinasOrigen.addAll(oficinaEjb.findByEntidadByEstado(getEntidadActiva(request).getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
        return oficinasOrigen;
    }

    /**
     * Retorna los años para un select de búsqueda
     *
     * @return
     */
    protected List<Integer> getAnys() {
        List<Integer> anys = new ArrayList<Integer>();

        Date hoy = new Date();
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");

        for (int i = Integer.valueOf(formatYear.format(hoy)); i >= RegwebConstantes.ANY; i--) {
            anys.add(i);
        }

        return anys;

    }

    /**
     * Obtenemos y procesamos los Interesados almacenados en la Session
     * @param interesadosSesion
     * @return
     * @throws Exception
     */
    public List<Interesado> procesarInteresados(List<Interesado> interesadosSesion, Long idRegistroDetalle) throws Exception{

        List<Interesado> interesados  = new ArrayList<Interesado>();

        RegistroDetalle registroDetalle = null;

        if(idRegistroDetalle != null){
            registroDetalle = new RegistroDetalle(idRegistroDetalle);
        }

        if(interesadosSesion != null){

            for(Interesado interesado:interesadosSesion){

                if(!interesado.getIsRepresentante()){ // No es representante

                    // Comprobamos si tiene un representante
                    if(interesado.getRepresentante() != null){
                        try{

                            log.info(interesado.getNombre() + " tiene representante");

                            Interesado representante = interesadosSesion.get(interesadosSesion.indexOf(interesado.getRepresentante()));

                            //Guardamos el Interesado
                            interesado.setId(null); // ponemos su id a null
                            interesado.setRegistroDetalle(registroDetalle);
                            interesado.setRepresentante(null);
                            interesado = interesadoEjb.persist(interesado);

                            // Guardamos el Representante
                            representante.setId(null);
                            representante.setRegistroDetalle(registroDetalle);
                            representante.setRepresentado(interesado);
                            representante = interesadoEjb.persist(representante);

                            // Le asignamos su representado y actualizamos
                            //representante.setRepresentado(interesado);
                            //representante = interesadoEjb.merge(representante);

                            // Lo asigamos al interesado y actualizamos
                            interesado.setRepresentante(representante);
                            log.info("id representante : " + representante.getId());
                            interesado = interesadoEjb.merge(interesado);

                            // Lo añadimos al Array
                            interesados.add(interesado);
                            interesados.add(representante);

                        }catch (Exception e){                 // TODO NO!!!!!!!!!!!!!!!!!
                            e.printStackTrace();
                        }
                    }else{
                        // Guardamos el nuevo Interesado
                        interesado.setId(null); // ponemos su id a null
                        interesado.setRegistroDetalle(registroDetalle);
                        interesado = interesadoEjb.persist(interesado);

                        // Lo añadimos al Array
                        interesados.add(interesado);

                    }

                }
            }
        }

        return interesados;

    }

    /**
     * Eliminamos la variable especificada de la Sesion
     * @param request
     * @throws Exception
     */
    public void eliminarVariableSesion(HttpServletRequest request, String variable) throws Exception{
        HttpSession session = request.getSession();

        session.setAttribute(variable, null);
    }
    
    
    public List<FieldError> setDefaultMessageToErrors(List<FieldError> errores, String bean) {

      if (errores == null) {
        log.warn("Variable errores val null !!!!" , new Exception());
      }

      
      List<FieldError> nousErrors = new ArrayList<FieldError>(errores.size());
      
      for (FieldError error : errores) {
        if (error.getDefaultMessage() == null) {
          
          final String code = error.getCode();
          Object[] args = error.getArguments(); 
          String defMsg;
          if (args == null || args.length == 0) {
            defMsg = I18NUtils.tradueix(code);
          } else {
          
            String[] stringArray = Arrays.copyOf(args, args.length, String[].class);
            defMsg = I18NUtils.tradueix(code,  stringArray);
          }
          
          FieldError fe = new FieldError(error.getObjectName(),error.getField(),
              error.getRejectedValue(), false, error.getCodes(), error.getArguments(),
              defMsg);
          nousErrors.add(fe);
        } else {
          nousErrors.add(error);
        }
      }
      
      // TODO Passar a DEBUG
      log.info("====== Hi ha errors en " + bean + " ==========");
      for(FieldError error:nousErrors){
          log.info("+ ObjectName: " + error.getField());
          log.info("    - Code: " + error.getCode());
          log.info("    - DefaultMessage: " + error.getDefaultMessage());
      }

      return nousErrors;
      
    }

}
