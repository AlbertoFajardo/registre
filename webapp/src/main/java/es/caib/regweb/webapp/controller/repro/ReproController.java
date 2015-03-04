package es.caib.regweb.webapp.controller.repro;

import es.caib.regweb.model.Repro;
import es.caib.regweb.model.UsuarioEntidad;
import es.caib.regweb.model.utils.ReproJson;
import es.caib.regweb.persistence.ejb.ReproLocal;
import es.caib.regweb.persistence.utils.Paginacion;
import es.caib.regweb.persistence.utils.RegistroUtils;
import es.caib.regweb.webapp.controller.BaseController;
import es.caib.regweb.webapp.utils.Mensaje;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created 16/07/14 12:52
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb.model.Repro}
 * @author jpernia
 */
@Controller
@RequestMapping(value = "/repro")
@SessionAttributes(types = Repro.class)
public class ReproController extends BaseController {

    //protected final Logger log = Logger.getLogger(getClass());
    
    @EJB(mappedName = "regweb/ReproEJB/local")
    public ReproLocal reproEjb;

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

        List<Repro> listado = reproEjb.getAllbyUsuario(usuarioEntidad.getId());
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
     * Cambia estado de una {@link es.caib.regweb.model.Repro}
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
     * Sube el orden de una {@link es.caib.regweb.model.Repro}
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
     * Baja el orden de una {@link es.caib.regweb.model.Repro}
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
     * Eliminar una {@link es.caib.regweb.model.Repro}
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
     * Envia una {@link es.caib.regweb.model.Repro} a un {@link es.caib.regweb.model.UsuarioEntidad}
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
     * Obtiene las {@link es.caib.regweb.model.Repro} de un {@link es.caib.regweb.model.UsuarioEntidad}
     */
    @RequestMapping(value = "/obtenerRepros", method = RequestMethod.GET)
    public @ResponseBody
    List<Repro> obtenerRepros(@RequestParam Long idUsuario, @RequestParam Long tipoRegistro, HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByUsuarioEntidad(idUsuario, getEntidadActiva(request).getId());

        return reproEjb.getActivasbyUsuario(usuarioEntidad.getId(),tipoRegistro);
    }

    /**
     * Obtiene la {@link es.caib.regweb.model.Repro} según su identificador.
     *
     */
    @RequestMapping(value = "/obtenerRepro", method = RequestMethod.GET)
    public @ResponseBody
    ReproJson obtenerRepro(@RequestParam Long idRepro, HttpServletRequest request) throws Exception {

        Repro repro = reproEjb.findById(idRepro);

        return RegistroUtils.desSerilizarReproXml(repro.getRepro());
    }




    @InitBinder("repro")
    public void initBinder(WebDataBinder binder) {

        binder.setDisallowedFields("id");
    }
}
