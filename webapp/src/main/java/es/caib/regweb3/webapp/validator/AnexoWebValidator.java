package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.persistence.validator.AnexoValidator;
import es.caib.regweb3.webapp.controller.registro.AnexoForm;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.validation.WebValidationResult;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 
 * @author anadal
 */
@Component
public class AnexoWebValidator implements Validator {

  protected final Logger log = Logger.getLogger(getClass());

  protected AnexoValidator<Object> validator = new AnexoValidator<Object>();


  public AnexoWebValidator() {
    super();    
  }
  
  @Override
  public boolean supports(Class<?> clazz) {
    return Anexo.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {

    WebValidationResult<Object> wvr;
    wvr = new WebValidationResult<Object>(errors, "anexo");

    final boolean isNou = true;
    /*{
      Boolean nou = (Boolean)errors.getFieldValue("nou");
      boolean isNou =  nou != null && nou.booleanValue();
    } */
    
    
    

    validate(target, errors, wvr, isNou);
  }


  public void validate(Object target, Errors errors,
    WebValidationResult<Object> wvr, boolean isNou) {
    
    log.info("============= TARGET VALIDATOR ==== " + target);
    log.info("============= TARGET VALIDATOR CLASS ==== " + target.getClass());

    if (target instanceof AnexoForm) {
      target = ((AnexoForm)target).getAnexo();
    }
    
    
    log.info("============= POST TARGET VALIDATOR ==== " + target);
    log.info("============= POST TARGET VALIDATOR CLASS ==== " + target.getClass());
    
    validator.validate(wvr, target, isNou);

  } // Final de metode


  public AnexoValidator<Object> getValidator() {
    return validator;
  }

  public void setValidator(AnexoValidator<Object> validator) {
    this.validator = validator;
  }

}