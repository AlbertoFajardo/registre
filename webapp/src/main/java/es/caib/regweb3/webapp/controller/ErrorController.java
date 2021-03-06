package es.caib.regweb3.webapp.controller;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.utils.RegWebMaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal
 * Date: 16/01/14
 */
@ControllerAdvice
public class ErrorController {

    protected final Logger log = Logger.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(HttpServletRequest req, Exception exception) {

        log.info("Dentro de handleException: " + req.getRequestURL() + " raised " + exception);
        exception.printStackTrace(); // Mostramos el error por pantalla

        ModelAndView mav = new ModelAndView("error");

        mav.addObject("exception", exception.getMessage());
        mav.addObject("trazaError", getStackTraceComoString(exception));
        mav.addObject("url", req.getRequestURL());

        return mav;
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(HttpServletRequest req, Exception exception) {

        log.info("Dentro de handleRuntimeException: " + req.getRequestURL() + " raised " + exception);
        exception.printStackTrace(); // Mostramos el error por pantalla

        ModelAndView mav = new ModelAndView("error");

        mav.addObject("exception", exception.getMessage());
        mav.addObject("trazaError", getStackTraceComoString(exception));
        mav.addObject("url", req.getRequestURL());

        return mav;
    }

    public static String getStackTraceComoString ( Throwable e )
    {
        if (e == null){
            return "";
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try{
            PrintWriter writer = new PrintWriter(bytes, true);
            e.printStackTrace(writer);
        }
        catch (Exception ex){
        }

        return bytes.toString();
    }

    /*@RequestMapping("/error/{error}")
    public ModelAndView error(@PathVariable String error, HttpServletRequest request, HttpServletResponse response) {

        ModelAndView mav =  new ModelAndView("error");

        mav.addObject("error", error);

        log.info("Dentro de ErrorController");

        return mav;

    }*/
    
    
    // ERRORS DE PUJADA DE FITXERS
    
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleRuntimeMaxUploadSizeExceededException(HttpServletRequest request,
        Exception exception) {
      return resolveFileSizeException(request, exception);
    }

    
    @ExceptionHandler(RegWebMaxUploadSizeExceededException.class)
    public ModelAndView handleRuntimeRegWebMaxUploadSizeExceededException(HttpServletRequest request,
        Exception exception) {
      return resolveFileSizeException(request, exception);
    }
    
    
    @ExceptionHandler(SizeLimitExceededException.class)
    public ModelAndView handleRuntimeSizeLimitExceededException(HttpServletRequest request,
        Exception exception) {
      return resolveFileSizeException(request, exception);
    }
    
    
   
    private ModelAndView resolveFileSizeException(HttpServletRequest request, Exception ex) {

      
      if (ex instanceof MaxUploadSizeExceededException
          || ex instanceof RegWebMaxUploadSizeExceededException
          || ex instanceof SizeLimitExceededException) {

        /*
          log.info(" ++++ Scheme: " + request.getScheme());
         log.info(" ++++ PathInfo: " + request.getPathInfo());
         log.info(" ++++ PathTrans: " + request.getPathTranslated());
          log.info(" ++++ ContextPath: " + request.getContextPath());
          log.info(" ++++ ServletPath: " + request.getServletPath());
          log.info(" ++++ getRequestURI: " + request.getRequestURI());
          log.info(" ++++ getRequestURL: " + request.getRequestURL().toString());
          log.info(" ++++ getQueryString: " + request.getQueryString());
          */
         
        String maxUploadSize = "???";
        String currentSize = "???";
        String msgCode;
        if (ex instanceof MaxUploadSizeExceededException) {
          MaxUploadSizeExceededException musee = (MaxUploadSizeExceededException) ex;
          if (musee instanceof RegWebMaxUploadSizeExceededException) {
            msgCode = ((RegWebMaxUploadSizeExceededException) musee).getMsgCode();
          } else {
            msgCode = "tamanyfitxerpujatsuperat";
          }

          // log.error(" YYYYYYYYYYYY  CAUSE: " + musee.getCause());
          if (musee.getCause() instanceof SizeLimitExceededException) {
            SizeLimitExceededException slee = (SizeLimitExceededException) musee.getCause();
            maxUploadSize = String.valueOf(slee.getPermittedSize());
            currentSize = String.valueOf(slee.getActualSize());
          } else {
            maxUploadSize = String.valueOf(musee.getMaxUploadSize());
          }

        } else {
          SizeLimitExceededException slee = (SizeLimitExceededException) ex;
          maxUploadSize = String.valueOf(slee.getPermittedSize());
          currentSize = String.valueOf(slee.getActualSize());
          msgCode = "tamanyfitxerpujatsuperat";
        }

        Mensaje.saveMessageError(request, I18NUtils.tradueix(msgCode, currentSize, maxUploadSize));

        ModelAndView mav = new ModelAndView(new RedirectView(request.getServletPath(), true));
        return mav;
      }
      return null;
    }

    
    
}
