package es.caib.regweb3.webapp.controller.repro;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.Repro;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.ReproJson;
import es.caib.regweb3.persistence.ejb.BaseEjbJPA;
import es.caib.regweb3.persistence.ejb.OrganismoLocal;
import es.caib.regweb3.persistence.ejb.ReproLocal;
import es.caib.regweb3.persistence.utils.Dir3CaibUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created 16/07/14 12:52
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb3.model.Repro}
 * @author jpernia
 */
@Controller
@RequestMapping(value = "/repro")
@SessionAttributes(types = Repro.class)
public class ReproController extends BaseController {

    //protected final Logger log = Logger.getLogger(getClass());
    
    @EJB(mappedName = "regweb3/ReproEJB/local")
    public ReproLocal reproEjb;

    @EJB(mappedName = "regweb3/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;

    /**
     * Listado de todas las Repros de un Usuario Entidad
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listadoRepro() {
        return "redirect:/repro/list/1";
    }

    /**
     * Listado de Repros
     * @param pageNumber
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView listRepro(@PathVariable Integer pageNumber, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("repro/reproList");

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        List<Repro> listado = reproEjb.getPaginationUsuario((pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION, usuarioEntidad.getId());
        Long total = reproEjb.getTotalbyUsuario(usuarioEntidad.getId());

        Paginacion paginacion = new Paginacion(total.intValue(), pageNumber);

        mav.addObject("paginacion", paginacion);
        mav.addObject("listado", listado);

        return mav;
    }


    /**
     * Crea una Repro
     * @param reproJson
     * @param request
     * @return
     */
    @RequestMapping(value="/new/{nombre}/{tipoRegistro}", method= RequestMethod.POST)
    @ResponseBody
    public Long nuevaRepro(@PathVariable String nombre,@PathVariable Long tipoRegistro, @RequestBody ReproJson reproJson, HttpServletRequest request) throws Exception{

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        Repro repro = new Repro();
        repro.setNombre(nombre);
        repro.setTipoRegistro(tipoRegistro);
        repro.setUsuario(usuarioEntidad);

        switch (tipoRegistro.intValue()){

            case 1: //RegistroEntrada
                log.info("Repro entrada");
                Organismo organismoDestino = organismoEjb.findByCodigoVigente(reproJson.getDestinoCodigo());

                if(organismoDestino != null) { // es interno
                    log.info("Destino: " +reproJson.getDestinoDenominacion() + " Interno");
                    reproJson.setDestinoExterno(false);

                }else{ // es externo
                    reproJson.setDestinoExterno(true);
                    log.info("Destino: " +reproJson.getDestinoDenominacion() + " Externo");
                }

            break;

            case 2: //RegistroSalida
                log.info("Repro salida");
                Organismo organismoOrigen = organismoEjb.findByCodigoVigente(reproJson.getOrigenCodigo());

                if(organismoOrigen != null) { // es interno
                    log.info("Origen: " + reproJson.getOrigenDenominacion() + " Interno");
                    reproJson.setOrigenExterno(false);

                }else{ // es externo
                    reproJson.setOrigenExterno(true);
                    log.info("Origen: " +reproJson.getOrigenDenominacion() + " Externo");
                }

            break;
        }

        Oficina oficina = oficinaEjb.findByCodigo(reproJson.getOficinaCodigo());

        if(oficina != null){ // es interna
            log.info("Oficina: " +reproJson.getOficinaDenominacion() + " Interno");
            reproJson.setOficinaExterna(false);
        }else{ // es externa
            log.info("Oficina: " +reproJson.getOficinaDenominacion() + " Externa");
            reproJson.setOficinaExterna(true);
        }


        repro.setRepro(RegistroUtils.serilizarXml(reproJson));

        int orden = 0;
        List<Repro> repros = reproEjb.getAllbyUsuario(usuarioEntidad.getId());
        if(repros.size() > 0){
            orden = reproEjb.maxOrdenRepro(usuarioEntidad.getId());
        }

        repro.setOrden(orden+1);

        try {
            repro = reproEjb.persist(repro);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return repro.getId();
    }


    /**
     * Cambia estado de una {@link es.caib.regweb3.model.Repro}
     * @param idRepro
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idRepro}/cambiarEstado", method = RequestMethod.GET)
    public String cambiarEstadoRepro(@PathVariable Long idRepro, HttpServletRequest request)throws Exception {

        String redirect = "redirect:/repro/list";

        reproEjb.cambiarEstado(idRepro);

        return redirect;
    }


    /**
     * Sube el orden de una {@link es.caib.regweb3.model.Repro}
     * @param idRepro
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idRepro}/subir", method = RequestMethod.GET)
    public String subirRepro(@PathVariable Long idRepro, HttpServletRequest request)throws Exception {

        String redirect = "redirect:/repro/list";

        reproEjb.subirOrden(idRepro);

        return redirect;
    }


    /**
     * Baja el orden de una {@link es.caib.regweb3.model.Repro}
     * @param idRepro
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idRepro}/bajar", method = RequestMethod.GET)
    public String bajarRepro(@PathVariable Long idRepro, HttpServletRequest request)throws Exception {

        String redirect = "redirect:/repro/list";

        reproEjb.bajarOrden(idRepro);

        return redirect;
    }


    /**
     * Eliminar una {@link es.caib.regweb3.model.Repro}
     */
    @RequestMapping(value = "/{reproId}/delete")
    public String eliminarRepro(@PathVariable Long reproId, HttpServletRequest request) {

        try {

            Repro repro = reproEjb.findById(reproId);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            if(!repro.getUsuario().getId().equals(usuarioEntidad.getId())){
                Mensaje.saveMessageError(request, getMessage("error.autorizacion"));
                return "redirect:/repro/list";
            }

            List<Repro> repros = reproEjb.getAllbyUsuario(usuarioEntidad.getId());
            int ordenBorrado = repro.getOrden();

            reproEjb.remove(repro);

            for(int i=ordenBorrado; i<repros.size(); i++) {
                Repro reproCambiar = repros.get(i);
                reproEjb.modificarOrden(reproCambiar.getId(),reproCambiar.getOrden()-1);
            }


            Mensaje.saveMessageInfo(request, getMessage("regweb.eliminar.registro"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.relaciones.registro"));
            e.printStackTrace();
        }

        return "redirect:/repro/list";
    }

    /**
     * Envia una {@link es.caib.regweb3.model.Repro} a un {@link es.caib.regweb3.model.UsuarioEntidad}
     */
    @RequestMapping(value = "/enviar/{reproId}/{usuarioId}")
    public String enviarRepro(@PathVariable Long reproId, @PathVariable Long usuarioId, HttpServletRequest request) {

        try {
            Repro reproEnviada = reproEjb.findById(reproId);
            UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByUsuarioEntidad(usuarioId,getEntidadActiva(request).getId());
            Repro repro = new Repro();
            repro.setNombre(reproEnviada.getNombre());
            repro.setUsuario(usuarioEntidad);
            repro.setTipoRegistro(reproEnviada.getTipoRegistro());
            repro.setRepro(reproEnviada.getRepro());

            int orden = 0;
            List<Repro> repros = reproEjb.getAllbyUsuario(usuarioId);
            if(repros.size() > 0){
                orden = reproEjb.maxOrdenRepro(usuarioEntidad.getId());
            }
            orden = orden + 1;
            repro.setOrden(orden);

            reproEjb.persist(repro);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/repro/list";
    }


    @ModelAttribute("usuariosEntidadList")
    public List<UsuarioEntidad> usuariosEntidadList(HttpServletRequest request) throws Exception {

        return usuarioEntidadEjb.findByEntidadSinActivo(getEntidadActiva(request).getId(), getUsuarioAutenticado(request).getId());

    }


    /**
     * Obtiene las {@link es.caib.regweb3.model.Repro} de un {@link es.caib.regweb3.model.UsuarioEntidad}
     */
    @RequestMapping(value = "/obtenerRepros", method = RequestMethod.GET)
    public @ResponseBody
    List<Repro> obtenerRepros(@RequestParam Long idUsuario, @RequestParam Long tipoRegistro, HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByUsuarioEntidad(idUsuario, getEntidadActiva(request).getId());

        return reproEjb.getActivasbyUsuario(usuarioEntidad.getId(),tipoRegistro);
    }

    /**
     * Obtiene la {@link es.caib.regweb3.model.Repro} según su identificador.
     *
     */
    @RequestMapping(value = "/obtenerRepro", method = RequestMethod.GET)
    public @ResponseBody
    ReproJson obtenerRepro(@RequestParam Long idRepro, HttpServletRequest request) throws Exception {
        //todo: Mejorar las Repro sustituyendo los organismos extinguidos por sus sustitutos
        Repro repro = reproEjb.findById(idRepro);
        ReproJson reproJson = RegistroUtils.desSerilizarReproXml(repro.getRepro());

        switch (repro.getTipoRegistro().intValue()){

            case 1: //RegistroEntrada

                // Comprobamos la unidad destino
                if(reproJson.getDestinoCodigo()!= null && reproJson.isDestinoExterno()){ // Preguntamos a DIR3 si está Vigente
                    Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService();
                    UnidadTF unidad = unidadesService.obtenerUnidad(reproJson.getDestinoCodigo(), null, null);

                    if(!unidad.getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)){// Ya no es vigente
                        reproJson.setDestinoExterno(null);
                        reproJson.setDestinoCodigo(null);
                        reproJson.setDestinoDenominacion(null);
                        repro.setRepro(RegistroUtils.serilizarXml(reproJson));
                        reproEjb.merge(repro);
                    }

                }else{ // Comprobamos en REGWEB3 si está vigente
                    Organismo organismoDestino = organismoEjb.findByCodigoVigente(reproJson.getDestinoCodigo());

                    if(organismoDestino == null){ // Ya no es vigente
                        reproJson.setDestinoExterno(null);
                        reproJson.setDestinoCodigo(null);
                        reproJson.setDestinoDenominacion(null);
                        repro.setRepro(RegistroUtils.serilizarXml(reproJson));
                        reproEjb.merge(repro);
                    }
                }
            break;

            case 2: //RegistroSalida
                log.info("Repro salida");

                // Comprobamos la unidad origen
                if(reproJson.getOrigenCodigo()!= null && reproJson.isOrigenExterno()){ // Preguntamos a DIR3 si está Vigente
                    Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService();
                    UnidadTF unidad = unidadesService.obtenerUnidad(reproJson.getOrigenCodigo(), null, null);

                    if(!unidad.getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)){// Ya no es vigente
                        reproJson.setOrigenExterno(null);
                        reproJson.setOrigenCodigo(null);
                        reproJson.setOrigenDenominacion(null);
                        repro.setRepro(RegistroUtils.serilizarXml(reproJson));
                        reproEjb.merge(repro);
                    }

                }else{ // Comprobamos en REGWEB3 si está vigente
                    Organismo organismoOrigen = organismoEjb.findByCodigoVigente(reproJson.getOrigenCodigo());
                    if(organismoOrigen == null){ // Ya no es vigente
                        reproJson.setOrigenExterno(null);
                        reproJson.setOrigenCodigo(null);
                        reproJson.setOrigenDenominacion(null);
                        repro.setRepro(RegistroUtils.serilizarXml(reproJson));
                        reproEjb.merge(repro);
                    }
                }

            break;


        }

        // Oficina Origen
        if(reproJson.getOficinaCodigo()!= null  && !reproJson.getOficinaCodigo().equals("-1") && reproJson.isOficinaExterna()){// Preguntamos a DIR3 si está Vigente
            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService();
            OficinaTF oficina = oficinasService.obtenerOficina(reproJson.getOficinaCodigo(),null,null);

            if(!oficina.getEstado().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)){// Ya no es vigente
                reproJson.setOficinaCodigo(null);
                reproJson.setOficinaDenominacion(null);
                reproJson.setOficinaExterna(null);
                repro.setRepro(RegistroUtils.serilizarXml(reproJson));
                reproEjb.merge(repro);
            }

        }else{// Comprobamos en REGWEB3 si está vigente
            Oficina oficinaOrigen = oficinaEjb.findByCodigoVigente(reproJson.getOficinaCodigo());
            if(oficinaOrigen == null){
                reproJson.setOficinaCodigo(null);
                reproJson.setOficinaDenominacion(null);
                reproJson.setOficinaExterna(null);
                repro.setRepro(RegistroUtils.serilizarXml(reproJson));
                reproEjb.merge(repro);
            }
        }


        return reproJson;
    }




    @InitBinder("repro")
    public void initBinder(WebDataBinder binder) {

        binder.setDisallowedFields("id");
    }
}
