package es.caib.regweb3.webapp.login;

import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.plugins.userinformation.IUserInformationPlugin;
import org.fundaciobit.plugins.utils.PluginsManager;


/**
 * 
 * @author anadal
 *
 */
public class RegwebLoginPluginManager {
  
  public static IUserInformationPlugin plugin = null;
  
  
  public static IUserInformationPlugin getInstance() throws Exception {
       
    if (plugin == null) {
      // Valor de la Clau
      final String propertyName = RegwebConstantes.REGWEB3_PROPERTY_BASE + "userinformationplugin";
      String className = System.getProperty(propertyName);
      if (className == null || className.trim().length()<=0) {
        throw new Exception("No hi ha cap propietat " + propertyName 
            + " definint la classe que gestiona el plugin de login");
      }
      // Carregant la classe
      Object obj;
      obj = PluginsManager.instancePluginByClassName(className, RegwebConstantes.REGWEB3_PROPERTY_BASE);
      plugin = (IUserInformationPlugin)obj;

    }      
    
    return plugin; 
  }
  
  
  
}
