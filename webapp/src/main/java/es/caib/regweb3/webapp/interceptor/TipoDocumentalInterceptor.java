package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Rol;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Created by Fundació BIT.
 *
 * Interceptor para la gestión de Tipo Documental
 *
 * @author jpernia
 * Date: 31/12/15
 */
public class TipoDocumentalInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {

            HttpSession session = request.getSession();
            Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);

            // Cualquier accion con Tipo Documental
            if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_ADMIN)){
                log.info("Error de rol");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            return true;
        } finally {
            //log.info("Interceptor TipoDocumental: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        }

    }

}