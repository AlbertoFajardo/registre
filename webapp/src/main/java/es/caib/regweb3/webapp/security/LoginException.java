package es.caib.regweb3.webapp.security;
/**
 * 
 * @author anadal
 *
 */
public class LoginException extends IllegalArgumentException {

  /**
   * @param message
   * @param cause
   */
  public LoginException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param s
   */
  public LoginException(String s) {
    super(s);
  }
  
  

}
