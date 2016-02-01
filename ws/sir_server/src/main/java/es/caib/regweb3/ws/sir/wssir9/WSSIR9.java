package es.caib.regweb3.ws.sir.wssir9;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.2.12-patch-04
 * Thu Dec 04 12:18:32 CET 2014
 * Generated source version: 2.2.12-patch-04
 * 
 */
 
@WebService(targetNamespace = "http://impl.manager.cct.map.es", name = "WS_SIR9")
@XmlSeeAlso({ObjectFactory.class})
public interface WSSIR9 {

    @WebResult(name = "envioMensajeDatosControlAAplicacionReturn", targetNamespace = "http://impl.manager.cct.map.es")
    @RequestWrapper(localName = "envioMensajeDatosControlAAplicacion", targetNamespace = "http://impl.manager.cct.map.es", className = "es.caib.regweb3.ws.sir.wssir9.EnvioMensajeDatosControlAAplicacion")
    @WebMethod
    @ResponseWrapper(localName = "envioMensajeDatosControlAAplicacionResponse", targetNamespace = "http://impl.manager.cct.map.es", className = "es.caib.regweb3.ws.sir.wssir9.EnvioMensajeDatosControlAAplicacionResponse")
    public es.caib.regweb3.ws.sir.wssir9.RespuestaWS envioMensajeDatosControlAAplicacion(
        @WebParam(name = "mensaje", targetNamespace = "http://impl.manager.cct.map.es")
        java.lang.String mensaje,
        @WebParam(name = "firma", targetNamespace = "http://impl.manager.cct.map.es")
        java.lang.String firma
    );
}