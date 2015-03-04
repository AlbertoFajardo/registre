package es.caib.regweb.persistence.ejb;


import es.caib.regweb.model.*;
import es.caib.regweb.model.utils.OficioPendienteLlegada;
import es.caib.regweb.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal (Convertir en EJB)
 * Date: 16/01/14
 */
@Stateless(name = "OficioRemisionUtilsEJB")
@SecurityDomain("seycon")
public class OficioRemisionUtilsBean implements OficioRemisionUtilsLocal {

  public final Logger log = Logger.getLogger(getClass());

  @EJB(mappedName = "regweb/RegistroEntradaEJB/local")
  public RegistroEntradaLocal registroEntradaEjb;

  @EJB(mappedName = "regweb/RegistroSalidaEJB/local")
  public RegistroSalidaLocal registroSalidaEjb;

  @EJB(mappedName = "regweb/OficinaEJB/local")
  public OficinaLocal oficinaEjb;

  @EJB(mappedName = "regweb/OficioRemisionEJB/local")
  public OficioRemisionLocal oficioRemisionEjb;

  @EJB(mappedName = "regweb/OrganismoEJB/local")
  public OrganismoLocal organismoEjb;

  @EJB(mappedName = "regweb/LibroEJB/local")
  public LibroLocal libroEjb;

  @EJB(mappedName = "regweb/TrazabilidadEJB/local")
  public TrazabilidadLocal trazabilidadEjb;

  @EJB(mappedName = "regweb/HistoricoRegistroEntradaEJB/local")
  public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;



  /**
   * Crea un OficioRemision con todos los ResgistroEntrada seleccionados
   * Crea un RegistroSalida por cada uno de los RegistroEntrada que contenga el OficioRemision
   * Crea la trazabilidad para los RegistroEntrada y RegistroSalida
   * @param registrosEntrada Listado de RegistrosEntrada que forman parte del Oficio de remisión
   * @param oficinaActiva Oficia en la cual se realiza el OficioRemision
   * @param usuarioEntidad Usuario que realiza el OficioRemision
   * @param idOrganismo
   * @param idLibro
   * @return
   * @throws Exception
   */

  public OficioRemision crearOficioRemisionInterno(List<RegistroEntrada> registrosEntrada, Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, Long idOrganismo, Long idLibro) throws Exception{

      Organismo organismoDestino = organismoEjb.findById(idOrganismo);

      OficioRemision oficioRemision = new OficioRemision();
      oficioRemision.setOficina(oficinaActiva);
      oficioRemision.setFecha(new Date());
      oficioRemision.setRegistrosEntrada(registrosEntrada);
      oficioRemision.setUsuarioResponsable(usuarioEntidad);
      oficioRemision.setLibro(new Libro(idLibro));
      oficioRemision.setOrganismoDestinatario(organismoDestino);

      synchronized (this){
          oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision, RegwebConstantes.ESTADO_OFICIO_INTERNO);
      }

      return oficioRemision;

  }

  /**
   * Crea un OficioRemision con todos los ResgistroEntrada seleccionados
   * @param registrosEntrada Listado de RegistrosEntrada que forman parte del Oficio de remisión
   * @param oficinaActiva Oficia en la cual se realiza el OficioRemision
   * @param usuarioEntidad Usuario que realiza el OficioRemision
   * @param organismoExterno
   * @param idLibro
   * @return
   * @throws Exception
   */

  public OficioRemision crearOficioRemisionExterno(List<RegistroEntrada> registrosEntrada,
      Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, String organismoExterno,
      String organismoExternoDenominacion, Long idLibro, String identificadorIntercambioSir) throws Exception{

      //Organismo organismoDestino = organismoEjb.findById(idOrganismo);

      OficioRemision oficioRemision = new OficioRemision();
      oficioRemision.setIdentificadorIntercambioSir(identificadorIntercambioSir);
      
      if (identificadorIntercambioSir == null) {
        oficioRemision.setEstado(RegwebConstantes.OFICIO_REMISION_ESTADO_NO_PROCESADO);
        oficioRemision.setFechaEstado(null);
      } else {
        oficioRemision.setEstado(RegwebConstantes.OFICIO_REMISION_ESTADO_ENVIADO);
        oficioRemision.setFechaEstado(new Date());
      }
      
      oficioRemision.setOficina(oficinaActiva);
      oficioRemision.setFecha(new Date());
      oficioRemision.setRegistrosEntrada(registrosEntrada);
      oficioRemision.setUsuarioResponsable(usuarioEntidad);
      oficioRemision.setLibro(new Libro(idLibro));
      Organismo organismoExt = organismoEjb.findByCodigo(organismoExterno);
      if (organismoExt == null) {
        oficioRemision.setDestinoExternoCodigo(organismoExterno);
        oficioRemision.setDestinoExternoDenominacion(organismoExternoDenominacion);
      } else {
        oficioRemision.setOrganismoDestinatario(organismoExt);
      }

      oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision, RegwebConstantes.ESTADO_OFICIO_EXTERNO);

      return oficioRemision;

  }

  /**
   * Procesa un OficioRemision pendiente de llegada, creando tantos Registros de Entrada,
   *  como contenga el Oficio.
   * @param oficioRemision
   * @throws Exception
   */
  public List<RegistroEntrada> procesarOficioRemision(OficioRemision oficioRemision, UsuarioEntidad usuario, Oficina oficinaActiva, List<OficioPendienteLlegada> oficios) throws Exception{

      List<RegistroEntrada> registros = new ArrayList<RegistroEntrada>();

      // Recorremos los RegistroEntrada del Oficio y Libro de registro seleccionado
      for (int i = 0; i < oficios.size(); i++) {

          OficioPendienteLlegada oficio = oficios.get(i);

          RegistroEntrada registroEntrada = registroEntradaEjb.findById(oficio.getIdRegistroEntrada());
          Libro libro = libroEjb.findById(oficio.getIdLibro());

          RegistroEntrada nuevoRE = new RegistroEntrada();

          nuevoRE.setUsuario(usuario);

          if(registroEntrada.getDestino() != null){
              nuevoRE.setDestino(registroEntrada.getDestino());
          }else{
              nuevoRE.setDestinoExternoCodigo(registroEntrada.getDestinoExternoCodigo());
              nuevoRE.setDestinoExternoDenominacion(registroEntrada.getDestinoExternoDenominacion());
          }

          nuevoRE.setOficina(oficinaActiva);
          nuevoRE.setEstado(RegwebConstantes.ESTADO_VALIDO);
          nuevoRE.setLibro(libro);

          nuevoRE.setRegistroDetalle(registroEntrada.getRegistroDetalle());

          synchronized (this){
              nuevoRE = registroEntradaEjb.registrarEntrada(nuevoRE);
          }

          //Guardamos el HistorioRegistroEntrada
          historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(nuevoRE, usuario, RegwebConstantes.TIPO_MODIF_ALTA,false);
          registros.add(nuevoRE);

          // ACTUALIZAMOS LA TRAZABILIDAD
          Trazabilidad trazabilidad = trazabilidadEjb.getByOficioRegistroEntrada(oficioRemision.getId(),registroEntrada.getId());
          trazabilidad.setRegistroEntradaDestino(nuevoRE);

          trazabilidadEjb.merge(trazabilidad);

      }

      oficioRemision.setEstado(RegwebConstantes.OFICIO_REMISION_ESTADO_ACEPTADO);
      oficioRemision.setFechaEstado(new Date());

      // Actualizamos el oficio de remisión
      oficioRemision = oficioRemisionEjb.merge(oficioRemision);

      return registros;

  }


}