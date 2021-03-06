package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.HistoricoRegistroSalidaLocal;
import es.caib.regweb3.persistence.ejb.RegistroSalidaLocal;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.RegistroSalidaWebValidator;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 * Controller para gestionar los Registros de Salida
 * @author earrivi
 * Date: 31/03/14
 */
@Controller
@RequestMapping(value = "/registroSalida")
@SessionAttributes({"registroSalida"})
public class RegistroSalidaFormController extends AbstractRegistroCommonFormController {

    @Autowired
    private RegistroSalidaWebValidator registroSalidaValidator;


    @EJB(mappedName = "regweb3/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;

    @EJB(mappedName = "regweb3/HistoricoRegistroSalidaEJB/local")
    public HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;


    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String nuevoRegistroSalida(Model model, HttpServletRequest request) throws Exception {

        Oficina oficina = getOficinaActiva(request);

        RegistroSalida registroSalida = new RegistroSalida();
        registroSalida.setRegistroDetalle(new RegistroDetalle());
        registroSalida.setOficina(oficina);

        //Eliminamos los posibles interesados de la Sesion
        eliminarVariableSesion(request, RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        model.addAttribute(getEntidadActiva(request));
        model.addAttribute(getUsuarioAutenticado(request));
        model.addAttribute(oficina);
        model.addAttribute("registroSalida",registroSalida);
        model.addAttribute("libros", getLibrosRegistroSalida(request));
        model.addAttribute("organismosOficinaActiva", getOrganismosOficinaActiva(request));
        model.addAttribute("oficinasOrigen",  getOficinasOrigen(request));

        return "registroSalida/registroSalidaForm";
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String nuevoRegistroSalida(@ModelAttribute("registroSalida") RegistroSalida registroSalida,
        BindingResult result, Model model, SessionStatus status,
        HttpServletRequest request) throws Exception, I18NException, I18NValidationException {

        HttpSession session = request.getSession();
        Entidad entidad = getEntidadActiva(request);

        registroSalidaValidator.validate(registroSalida, result);

        // Comprobamos si el usuario ha añadido algún interesado
        List<Interesado> interesadosSesion = (List<Interesado>) session.getAttribute(RegwebConstantes.SESSION_INTERESADOS_SALIDA);
        Boolean errorInteresado = true;
        if(interesadosSesion != null && interesadosSesion.size() > 0){
            errorInteresado = false;
        }

        if (result.hasErrors() || errorInteresado) { // Si hay errores volvemos a la vista del formulario

            // Si no hay ningún interesado, generamos un error.
            if(errorInteresado){
                model.addAttribute("errorInteresado", errorInteresado);
            }

            model.addAttribute(entidad);
            model.addAttribute(getUsuarioAutenticado(request));
            model.addAttribute(getOficinaActiva(request));
            model.addAttribute("oficinasOrigen",  getOficinasOrigen(request));
            model.addAttribute("libros", getLibrosRegistroSalida(request));

            // Organismo origen: Select
            log.info("Origen: " + registroSalida.getOrigen().getCodigo());
            Set<Organismo> organismosOficinaActiva = getOrganismosOficinaActiva(request);

            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);

            // Si la Oficina Origen es Externa, la añadimos al listado.
            Set<Oficina> oficinasOrigen = getOficinasOrigen(request);
            if (!registroSalida.getRegistroDetalle().getOficinaOrigen().getCodigo().equals("-1")) {// Han indicado oficina de origen

                Oficina oficinaOrigen = oficinaEjb.findByCodigoEntidad(registroSalida.getRegistroDetalle().getOficinaOrigen().getCodigo(), entidad.getId());

                if (oficinaOrigen == null) { // Es externa

                    oficinasOrigen.add(new Oficina(null, registroSalida.getRegistroDetalle().getOficinaOrigen().getCodigo(), registroSalida.getRegistroDetalle().getOficinaOrigen().getDenominacion()));

                } else { // Es interna, la añadimos a la lista por si acaso no está
                    oficinasOrigen.add(oficinaOrigen);
                }
            }
            model.addAttribute("oficinasOrigen", oficinasOrigen);

            return "registroSalida/registroSalidaForm";
        }else{ // Si no hay errores guardamos el registroSalida

            try {

                UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

                registroSalida.setOficina(getOficinaActiva(request));
                registroSalida.setUsuario(usuarioEntidad);

                // Estado RegistroSalida
                registroSalida.setEstado(RegwebConstantes.ESTADO_VALIDO);

                // Procesamos las opciones comunes del RegistroSalida
                registroSalida = procesarRegistroSalida(registroSalida, entidad);

                // Procesamos lo Interesados de la session
                List<Interesado> interesados = procesarInteresados(interesadosSesion, null);

                registroSalida.getRegistroDetalle().setInteresados(interesados);

                //Guardamos el RegistroSalida
                registroSalida = registroSalidaEjb.registrarSalida(registroSalida);

                //Guardamos el HistorioRegistroSalida
                historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalida, usuarioEntidad, RegwebConstantes.TIPO_MODIF_ALTA,false);


            }catch (Exception e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
            }finally {
                status.setComplete();
                //Eliminamos los posibles interesados de la Sesion
                try {
                    eliminarVariableSesion(request, RegwebConstantes.SESSION_INTERESADOS_SALIDA);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return "redirect:/registroSalida/"+registroSalida.getId()+"/detalle";
        }
    }


    /**
     * Carga el formulario para modificar un {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/{idRegistro}/edit", method = RequestMethod.GET)
    public String editarRegistroSalida(@PathVariable("idRegistro") Long idRegistro,  Model model, HttpServletRequest request) throws Exception{

        if(!isOperador(request)){
            Mensaje.saveMessageError(request, getMessage("error.rol.operador"));
            return "redirect:/inici";
        }

        //Eliminamos los posibles interesados de la Sesion
        eliminarVariableSesion(request, RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        RegistroSalida registroSalida = null;

        Oficina oficina = getOficinaActiva(request);
        Usuario usuario = getUsuarioAutenticado(request);
        Entidad entidad = getEntidadActiva(request);

        try {
            registroSalida = registroSalidaEjb.findById(idRegistro);

            // Organismo origen: Select
            Set<Organismo> organismosOficinaActiva = getOrganismosOficinaActiva(request);
            // Si el Organismo Origen no está en al lista lo añadimos
            if (!organismosOficinaActiva.contains(registroSalida.getOrigen())) {
                organismosOficinaActiva.add(registroSalida.getOrigen());
            }

            // Oficina Origen: Select
            Set<Oficina> oficinasOrigen = getOficinasOrigen(request);

            // Si la Oficina Origen es Externa, la añadimos al listado.
            Oficina oficinaOrigen = registroSalida.getRegistroDetalle().getOficinaOrigen();
            if (oficinaOrigen == null) {// Si  Externa, la añadimos al listado.

                oficinasOrigen.add(new Oficina(null, registroSalida.getRegistroDetalle().getOficinaOrigenExternoCodigo(), registroSalida.getRegistroDetalle().getOficinaOrigenExternoDenominacion()));

            } else if (!oficinasOrigen.contains(oficinaOrigen)) {// Si es Interna, pero no esta relacionado con la Oficina Activa
                oficinasOrigen.add(oficinaOrigen);
            }

            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);
            model.addAttribute("oficinasOrigen", oficinasOrigen);


        }catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute(entidad);
        model.addAttribute(usuario);
        model.addAttribute(oficina);
        model.addAttribute("registroSalida",registroSalida);

        return "registroSalida/registroSalidaForm";
    }

    /**
     * Editar un {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/{idRegistro}/edit", method = RequestMethod.POST)
    public String editarRegistroSalida(@ModelAttribute("registroSalida") RegistroSalida registroSalida, BindingResult result,
                                        Model model, SessionStatus status,HttpServletRequest request) throws Exception{


        registroSalidaValidator.validate(registroSalida, result);
        Entidad entidad = getEntidadActiva(request);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            model.addAttribute("usuario", getUsuarioAutenticado(request));
            model.addAttribute("oficina", getOficinaActiva(request));
            model.addAttribute(entidad);

            // Organismo origen: Select
            Set<Organismo> organismosOficinaActiva = getOrganismosOficinaActiva(request);
            // Si el Organismo Origen no está en al lista lo añadimos
            if (!organismosOficinaActiva.contains(registroSalida.getOrigen())) {
                organismosOficinaActiva.add(registroSalida.getOrigen());
            }

            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);

            // Oficina Origen: Select
            Set<Oficina> oficinasOrigen = getOficinasOrigen(request);

            if (!registroSalida.getRegistroDetalle().getOficinaOrigen().getCodigo().equals("-1")) { // Si han indicado OficinaOrigen
                Oficina oficinaOrigen = oficinaEjb.findByCodigoEntidad(registroSalida.getRegistroDetalle().getOficinaOrigen().getCodigo(), entidad.getId());

                if (oficinaOrigen == null) { // Es externa
                    log.info("Es oficina externa: " + registroSalida.getRegistroDetalle().getOficinaOrigenExternoDenominacion());
                    oficinasOrigen.add(new Oficina(null, registroSalida.getRegistroDetalle().getOficinaOrigen().getCodigo(), registroSalida.getRegistroDetalle().getOficinaOrigenExternoDenominacion()));

                } else { // Es interna, la añadimos a la lista por si acaso no está
                    log.info("Es oficina interna: " + oficinaOrigen.getDenominacion());
                    oficinasOrigen.add(oficinaOrigen);
                }
            }
            model.addAttribute("oficinasOrigen", oficinasOrigen);

            return "registroSalida/registroSalidaForm";

        }else { // Si no hay errores actualizamos el registroSalida

            try {

                UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

                // Procesamos las opciones comunes del RegistroEnrtrada
                registroSalida = procesarRegistroSalida(registroSalida, entidad);

                // Calculamos los días transcurridos desde que se Registró para asignarle un Estado
                Long dias = RegistroUtils.obtenerDiasRegistro(registroSalida.getFecha());

                if(dias >= 1){ // Si ha pasado 1 día o mas
                    registroSalida.setEstado(RegwebConstantes.ESTADO_PENDIENTE_VISAR);
                }

                // Obtenemos el RS antes de guardarlos, para crear el histórico
                RegistroSalida registroSalidaAntiguo = registroSalidaEjb.findById(registroSalida.getId());

                // Actualizamos el RegistroSalida
                registroSalida = registroSalidaEjb.merge(registroSalida);

                // Creamos el Historico RegistroSalida
                historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalidaAntiguo, usuarioEntidad, RegwebConstantes.TIPO_MODIF_DATOS, true);

                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));

            }catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                return "redirect:/inici";

            }finally {
                status.setComplete();
            }

            return "redirect:/registroSalida/"+registroSalida.getId()+"/detalle";
        }
    }




    /**
     * Procesa las opciones de comunes de un RegistroSalida, lo utilizamos en la creación y modificación.
     * @param registroSalida
     * @return
     * @throws Exception
     */
    private RegistroSalida procesarRegistroSalida(RegistroSalida registroSalida, Entidad entidad) throws Exception{

        Organismo organismoDestino = registroSalida.getOrigen();

        // Gestionamos el Organismo, determinando si es Interno o Externo
        Organismo orgDestino = organismoEjb.findByCodigoEntidad(organismoDestino.getCodigo(), entidad.getId());
        if(orgDestino != null){ // es interno

            registroSalida.setOrigen(orgDestino);
            registroSalida.setOrigenExternoCodigo(null);
            registroSalida.setOrigenExternoDenominacion(null);
        }

        // Oficina origen, determinando si es Interno o Externo
        Oficina oficinaOrigen = registroSalida.getRegistroDetalle().getOficinaOrigen();

        if (oficinaOrigen.getCodigo().equals("-1")) { // No han indicado oficina de origen

            // Asignamos la Oficina donde se realiza el registro
            registroSalida.getRegistroDetalle().setOficinaOrigen(registroSalida.getOficina());
            registroSalida.getRegistroDetalle().setOficinaOrigenExternoCodigo(null);
            registroSalida.getRegistroDetalle().setOficinaOrigenExternoDenominacion(null);

        } else { // Han indicado oficina origen

            Oficina ofiOrigen = oficinaEjb.findByCodigoEntidad(oficinaOrigen.getCodigo(), entidad.getId());
            if (ofiOrigen != null) { // Es interna

                registroSalida.getRegistroDetalle().setOficinaOrigen(ofiOrigen);
                registroSalida.getRegistroDetalle().setOficinaOrigenExternoCodigo(null);
                registroSalida.getRegistroDetalle().setOficinaOrigenExternoDenominacion(null);

            } else {  // es externa
                registroSalida.getRegistroDetalle().setOficinaOrigenExternoCodigo(registroSalida.getRegistroDetalle().getOficinaOrigen().getCodigo());
                if (registroSalida.getId() != null) {//es una modificación
                    registroSalida.getRegistroDetalle().setOficinaOrigenExternoDenominacion(registroSalida.getRegistroDetalle().getOficinaOrigenExternoDenominacion());
                } else {
                    registroSalida.getRegistroDetalle().setOficinaOrigenExternoDenominacion(registroSalida.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                }

                registroSalida.getRegistroDetalle().setOficinaOrigen(null);
            }
        }


        // Solo se comprueba si es una modificación de RegistroSalida
        if(registroSalida.getId() != null){
            // Si no ha introducido ninguna fecha de Origen, se establece la fecha actual
            if(registroSalida.getRegistroDetalle().getFechaOrigen() == null){
                registroSalida.getRegistroDetalle().setFechaOrigen(new Date());
            }

            // Si no ha introducido ningún número de registroSalida de Origen, le ponemos el actual.
            if(registroSalida.getRegistroDetalle().getNumeroRegistroOrigen() == null || registroSalida.getRegistroDetalle().getNumeroRegistroOrigen().length() == 0){
                registroSalida.getRegistroDetalle().setNumeroRegistroOrigen(registroSalida.getNumeroRegistroFormateado());
            }
        }

        // No han especificado Codigo Asunto
        if( registroSalida.getRegistroDetalle().getCodigoAsunto().getId() == null || registroSalida.getRegistroDetalle().getCodigoAsunto().getId() == -1){
            registroSalida.getRegistroDetalle().setCodigoAsunto(null);
        }

        // No han especificadoTransporte
        if( registroSalida.getRegistroDetalle().getTransporte() == -1){
            registroSalida.getRegistroDetalle().setTransporte(null);
        }

        return registroSalida;
    }

    /**
     * Obtiene los {@link es.caib.regweb3.model.Organismo} a partir del llibre seleccionat
     */
   /* @RequestMapping(value = "/obtenerOrganismoLibro", method = RequestMethod.GET)
    public @ResponseBody
    List<Organismo> obtenerOrganismoLibro(@RequestParam Long id) throws Exception {

        return organismoEjb.getByLibro(id);
    }*/


    @InitBinder("registroSalida")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");
        binder.setDisallowedFields("registroDetalle.id");
        binder.setDisallowedFields("tipoInteresado");
        binder.setDisallowedFields("organismoInteresado");
        binder.setDisallowedFields("fecha");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);

        binder.setValidator(this.registroSalidaValidator);
    }

}