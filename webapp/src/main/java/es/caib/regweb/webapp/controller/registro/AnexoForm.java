package es.caib.regweb.webapp.controller.registro;


import org.springframework.web.multipart.commons.CommonsMultipartFile;

import es.caib.regweb.model.Anexo;
import es.caib.regweb.persistence.ejb.AnexoFull;

/**
 * 
 * @author anadal
 *
 */
public class AnexoForm extends AnexoFull {


  // Only Form
  //String returnURL;
  
  private CommonsMultipartFile documentoFile;
  
  private CommonsMultipartFile firmaFile;
  
  
  String tipoRegistro;
  
  Long registroID;
  
  public AnexoForm() {
    super();
  }
  
  
  public AnexoForm(Anexo anexo) {
    super(anexo);
  }
  
  
  public AnexoForm(AnexoFull anexoFull) {
    super(anexoFull);
  }
  /*
  public void setReturnURL(String returnURL) {
    this.returnURL = returnURL;
  }
  
  public String getReturnURL() {
    return returnURL;
  }

  */
  public void setDocumentoFile(CommonsMultipartFile documentoFile) {
    this.documentoFile = documentoFile;
  }
  
  public CommonsMultipartFile getDocumentoFile() {
    return documentoFile;
  }

  
  public CommonsMultipartFile getFirmaFile() {
    return firmaFile;
  }

  public void setFirmaFile(CommonsMultipartFile firmaFile) {
    this.firmaFile = firmaFile;
  }
  
  

  public String getTipoRegistro() {
    return tipoRegistro;
  }


  public void setTipoRegistro(String tipoRegistro) {
    this.tipoRegistro = tipoRegistro;
  }


  public Long getRegistroID() {
    return registroID;
  }


  public void setRegistroID(Long registroID) {
    this.registroID = registroID;
  }
  
  

}