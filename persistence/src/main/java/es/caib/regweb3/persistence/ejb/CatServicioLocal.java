package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatServicio;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface CatServicioLocal extends BaseEjb<CatServicio, Long> {

  public CatServicio findByCodigo(Long codigo) throws Exception;

}

