package es.caib.regweb3.sir.ws.api.manager;

import es.caib.regweb3.sir.core.utils.FicheroIntercambio;

/**
 * Created by earrivi on 19/01/2016.
 */
public interface FicheroIntercambioManager {

    /**
     * Envía el fichero de datos de intercambio al nodo distribuido asociado.
     *
     * @param ficheroIntercambio Información del asiento registral.
     */
    public void enviarFicheroIntercambio(FicheroIntercambio ficheroIntercambio);

    /**
     * Reenvía el fichero de datos de intercambio al nodo distribuido asociado.
     *
     * @param ficheroIntercambio Información del asiento registral.
     */
    public void reenviarFicheroIntercambio(FicheroIntercambio ficheroIntercambio);

    /**
     * Rechaza el fichero de datos de intercambio.
     *
     * @param ficheroIntercambio Información del asiento registral.
     */
    public void rechazarFicheroIntercambio(FicheroIntercambio ficheroIntercambio);

}
