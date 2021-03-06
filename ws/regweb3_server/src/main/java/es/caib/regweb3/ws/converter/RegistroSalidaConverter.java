package es.caib.regweb3.ws.converter;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.AnexoLocal;
import es.caib.regweb3.persistence.ejb.CodigoAsuntoLocal;
import es.caib.regweb3.persistence.ejb.TipoAsuntoLocal;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.ws.model.AnexoWs;
import es.caib.regweb3.ws.model.InteresadoWs;
import es.caib.regweb3.ws.model.RegistroSalidaResponseWs;
import es.caib.regweb3.ws.model.RegistroSalidaWs;
import es.caib.regweb3.ws.v3.impl.CommonConverter;
import org.fundaciobit.genapp.common.i18n.I18NException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Fundació BIT.
 * Conversor entre las clases {@link es.caib.regweb3.ws.model.RegistroSalidaWs} y {@link es.caib.regweb3.model.RegistroSalida}
 * @author earrivi
 */
public class RegistroSalidaConverter extends CommonConverter {

    /**
     * Convierte un {@link es.caib.regweb3.ws.model.RegistroSalidaWs} en un {@link es.caib.regweb3.model.RegistroSalida}
     * @param registroSalidaWs
     * @return
     * @throws Exception
     * @throws org.fundaciobit.genapp.common.i18n.I18NException
     */
    public static RegistroSalida getRegistroSalida(RegistroSalidaWs registroSalidaWs,
        UsuarioEntidad usuario,Libro libro, Oficina oficina,
        Organismo organismo,  CodigoAsuntoLocal codigoAsuntoEjb,
        TipoAsuntoLocal tipoAsuntoEjb) throws Exception, I18NException {

        if (registroSalidaWs == null){
            return  null;
        }

        RegistroSalida registroSalida = new RegistroSalida();
        RegistroDetalle registroDetalle = new RegistroDetalle();

        registroSalida.setOrigen(organismo);
        registroSalida.setOficina(oficina);
        registroSalida.setFecha(new Date());
        registroSalida.setUsuario(usuario);
        registroSalida.setEstado(RegwebConstantes.ESTADO_VALIDO);
        registroSalida.setLibro(libro);

        registroDetalle.setTipoAsunto(getTipoAsunto(registroSalidaWs.getTipoAsunto(),usuario.getEntidad().getId(), tipoAsuntoEjb));
        registroDetalle.setCodigoAsunto(getCodigoAsunto(registroSalidaWs.getCodigoAsunto(), codigoAsuntoEjb));
        registroDetalle.setTipoDocumentacionFisica(registroSalidaWs.getDocFisica());
        registroDetalle.setTransporte(RegwebConstantes.TRANSPORTE_BY_CODIGO_SICRES.get(registroSalidaWs.getTipoTransporte()));
        registroDetalle.setIdioma(getIdiomaRegistro(registroSalidaWs.getIdioma()));
        registroDetalle.setExtracto(registroSalidaWs.getExtracto());
        registroDetalle.setAplicacion(registroSalidaWs.getAplicacion());
        registroDetalle.setVersion(registroSalidaWs.getVersion());
        registroDetalle.setReferenciaExterna(registroSalidaWs.getRefExterna());
        registroDetalle.setExpediente(registroSalidaWs.getNumExpediente());
        registroDetalle.setNumeroTransporte(registroSalidaWs.getNumTransporte());
        registroDetalle.setObservaciones(registroSalidaWs.getObservaciones());
        registroDetalle.setExpone(registroSalidaWs.getExpone());
        registroDetalle.setSolicita(registroSalidaWs.getSolicita());

        registroSalida.setRegistroDetalle(registroDetalle);

        return registroSalida;
    }

    public static RegistroSalidaWs getRegistroSalidaWs(RegistroSalida registroSalida,
        String idioma, AnexoLocal anexoEjb) throws Exception, I18NException {

        if (registroSalida == null) {
            return null;
        }

       

        // Creamos los datos comunes mediante RegistroWs
        RegistroSalidaWs registroWs = new RegistroSalidaWs();
        RegistroDetalle registroDetalle = registroSalida.getRegistroDetalle();

        registroWs.setFecha(registroSalida.getFecha());
        registroWs.setNumero(registroSalida.getNumeroRegistro());
        registroWs.setNumeroRegistroFormateado(registroSalida.getNumeroRegistroFormateado());

        registroWs.setOficina(registroSalida.getOficina().getCodigo());
        registroWs.setLibro(registroSalida.getLibro().getNombreCompleto());

        registroWs.setExtracto(registroDetalle.getExtracto());
        registroWs.setDocFisica(registroDetalle.getTipoDocumentacionFisica());
        registroWs.setIdioma(I18NLogicUtils.tradueix(new Locale(idioma), "idioma." + registroDetalle.getIdioma()));

        TraduccionTipoAsunto traduccionTipoAsunto = (TraduccionTipoAsunto) registroDetalle.getTipoAsunto().getTraduccion(idioma);
        registroWs.setTipoAsunto(traduccionTipoAsunto.getNombre());

        registroWs.setAplicacion(registroDetalle.getAplicacion());
        registroWs.setVersion(registroDetalle.getVersion());

        registroWs.setCodigoUsuario(registroSalida.getUsuario().getUsuario().getIdentificador());

        registroWs.setContactoUsuario("");

        registroWs.setNumExpediente(registroDetalle.getExpediente());
        registroWs.setNumTransporte(registroDetalle.getNumeroTransporte());
        registroWs.setObservaciones(registroDetalle.getObservaciones());

        registroWs.setRefExterna(registroDetalle.getReferenciaExterna());

        if(registroDetalle.getCodigoAsunto() != null){
            TraduccionCodigoAsunto traduccionCodigoAsunto = (TraduccionCodigoAsunto) registroDetalle.getCodigoAsunto().getTraduccion(idioma);
            registroWs.setCodigoAsunto(traduccionCodigoAsunto.getNombre());
        }else{
            registroWs.setCodigoAsunto(null);
        }

        if(registroDetalle.getTransporte() != null){
            registroWs.setTipoTransporte(I18NLogicUtils.tradueix(new Locale(idioma), "transporte." + registroDetalle.getTransporte()));
        }else{
            registroWs.setTipoTransporte(null);
        }


        registroWs.setExpone(registroDetalle.getExpone());
        registroWs.setSolicita(registroDetalle.getSolicita());


        //Interesados
        if(registroDetalle.getInteresados() != null){
            List<InteresadoWs> interesadosWs = procesarInteresadosWs(registroDetalle.getInteresados());

            registroWs.setInteresados(interesadosWs);
        }

         //Interesados
        if(registroDetalle.getAnexos() != null){
            List<AnexoWs> anexosWs = procesarAnexosWs(registroDetalle.getAnexos(), anexoEjb);

            registroWs.setAnexos(anexosWs);
        }
        
        // Campos únicos de RegistroSalida
        if(registroSalida.getOrigen() != null){
            registroWs.setOrigen(registroSalida.getOrigen().getDenominacion());
        }else{
            registroWs.setOrigen(registroSalida.getOrigenExternoDenominacion());
        }

        return registroWs;

    }

    public static RegistroSalidaResponseWs getRegistroSalidaResponseWs(RegistroSalida registroSalida,
                                                                         String idioma, AnexoLocal anexoEjb) throws Exception, I18NException {

        if (registroSalida == null) {
            return null;
        }

        // Creamos los datos comunes mediante RegistroWs
        RegistroSalidaResponseWs registroWs = new RegistroSalidaResponseWs();
        RegistroDetalle registroDetalle = registroSalida.getRegistroDetalle();

        registroWs.setEntidadCodigo(registroSalida.getOficina().getOrganismoResponsable().getEntidad().getCodigoDir3());
        registroWs.setEntidadDenominacion(registroSalida.getOficina().getOrganismoResponsable().getEntidad().getNombre());

        registroWs.setNumeroRegistro(registroSalida.getNumeroRegistro());
        registroWs.setNumeroRegistroFormateado(registroSalida.getNumeroRegistroFormateado());
        registroWs.setFechaRegistro(registroSalida.getFecha());

        registroWs.setCodigoUsuario(registroSalida.getUsuario().getUsuario().getIdentificador());
        registroWs.setNombreUsuario(registroSalida.getUsuario().getNombreCompleto());
        registroWs.setContactoUsuario(registroSalida.getUsuario().getUsuario().getEmail());

        registroWs.setOficinaCodigo(registroSalida.getOficina().getCodigo());
        registroWs.setOficinaDenominacion(registroSalida.getOficina().getDenominacion());
        registroWs.setLibroCodigo(registroSalida.getLibro().getCodigo());
        registroWs.setLibroDescripcion(registroSalida.getLibro().getNombre());

        registroWs.setExtracto(registroDetalle.getExtracto());
        registroWs.setDocFisicaCodigo(registroDetalle.getTipoDocumentacionFisica().toString());
        registroWs.setDocFisicaDescripcion(I18NLogicUtils.tradueix(new Locale(idioma), "tipoDocumentacionFisica." + registroDetalle.getTipoDocumentacionFisica()));

        TraduccionTipoAsunto traduccionTipoAsunto = (TraduccionTipoAsunto) registroDetalle.getTipoAsunto().getTraduccion(idioma);
        registroWs.setTipoAsuntoCodigo(registroDetalle.getTipoAsunto().getCodigo());
        registroWs.setTipoAsuntoDescripcion(traduccionTipoAsunto.getNombre());

        registroWs.setIdiomaCodigo(RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(registroDetalle.getIdioma()));
        registroWs.setIdiomaDescripcion(I18NLogicUtils.tradueix(new Locale(idioma), "idioma." + registroDetalle.getIdioma()));

        if(registroDetalle.getCodigoAsunto() != null){
            TraduccionCodigoAsunto traduccionCodigoAsunto = (TraduccionCodigoAsunto) registroDetalle.getCodigoAsunto().getTraduccion(idioma);
            registroWs.setCodigoAsuntoCodigo(registroDetalle.getCodigoAsunto().getCodigo());
            registroWs.setCodigoAsuntoDescripcion(traduccionCodigoAsunto.getNombre());
        }else{
            registroWs.setCodigoAsuntoCodigo(null);
            registroWs.setCodigoAsuntoDescripcion(null);
        }

        registroWs.setRefExterna(registroDetalle.getReferenciaExterna());
        registroWs.setNumExpediente(registroDetalle.getExpediente());

        if(registroDetalle.getTransporte() != null){
            registroWs.setTipoTransporteCodigo(RegwebConstantes.CODIGO_SICRES_BY_TRANSPORTE.get(registroDetalle.getTransporte()));
            registroWs.setTipoTransporteDescripcion(I18NLogicUtils.tradueix(new Locale(idioma), "transporte." + registroDetalle.getTransporte()));
        }else{
            registroWs.setTipoTransporteCodigo(null);
            registroWs.setTipoTransporteDescripcion(null);
        }

        registroWs.setNumTransporte(registroDetalle.getNumeroTransporte());
        registroWs.setObservaciones(registroDetalle.getObservaciones());
        registroWs.setFechaOrigen(registroDetalle.getFechaOrigen());
        registroWs.setAplicacion(registroDetalle.getAplicacion());
        registroWs.setVersion(registroDetalle.getVersion());

        registroWs.setExpone(registroDetalle.getExpone());
        registroWs.setSolicita(registroDetalle.getSolicita());

        //Interesados
        if(registroDetalle.getInteresados() != null){
            List<InteresadoWs> interesadosWs = procesarInteresadosWs(registroDetalle.getInteresados());

            registroWs.setInteresados(interesadosWs);
        }

        if(registroDetalle.getAnexos() != null){
            List<AnexoWs> anexosWs = procesarAnexosWs(registroDetalle.getAnexos(), anexoEjb);

            registroWs.setAnexos(anexosWs);
        }

        // Campos únicos de RegistroEntrada
        if(registroSalida.getOrigen() != null ){
            registroWs.setOrigenCodigo(registroSalida.getOrigen().getCodigo());
            registroWs.setOrigenDenominacion(registroSalida.getOrigen().getDenominacion());
        }else{
            registroWs.setOrigenCodigo(registroSalida.getOrigenExternoCodigo());
            registroWs.setOrigenDenominacion(registroSalida.getOrigenExternoDenominacion());
        }

        return registroWs;

    }

    /**
     *
     * @param interesados
     * @return
     * @throws Exception
     */
    private static List<InteresadoWs> procesarInteresadosWs(List<Interesado> interesados) throws Exception{

        List<InteresadoWs> interesadosWs = new ArrayList<InteresadoWs>();

        for (Interesado interesado : interesados) {

            InteresadoWs interesadoWs =  DatosInteresadoConverter.getInteresadoWs(interesado);

            interesadosWs.add(interesadoWs);
        }

        return interesadosWs;
    }
}
