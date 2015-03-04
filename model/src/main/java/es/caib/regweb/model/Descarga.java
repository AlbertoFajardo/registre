/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.caib.regweb.model;

import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Index;


/**
 *
 * @author mgonzalez
 * @author anadal (index)
 */
@Table(name = "RWE_DESCARGA")
@org.hibernate.annotations.Table(appliesTo = "RWE_DESCARGA", indexes = {
    @Index(name="RWE_DESCAR_ENTIDA_FK_I", columnNames = {"ENTIDAD"})
})
@Entity
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize=1)
public class Descarga implements Serializable {
  
  private Long id;
  private String fechaInicio;
  private String fechaFin;
  private String fechaImportacion;  
  private String tipo;
  private Entidad entidad;

  public Descarga() {
  }
  
  @Column(name = "ID", nullable = false, length = 3)
  @Id
  @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "generator")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  @Column(name = "FECHAINICIO")  
  public String getFechaInicio() {
    return fechaInicio;
  }

  public void setFechaInicio(String fechaInicio) {
    this.fechaInicio = fechaInicio;
  }

  @Column(name = "FECHAFIN")  
  public String getFechaFin() {
    return fechaFin;
  }

  public void setFechaFin(String fechaFin) {
    this.fechaFin = fechaFin;
  }
  
  @Column(name = "FECHAIMPORTACION")
  public String getFechaImportacion() {
    return fechaImportacion;
  }

  public void setFechaImportacion(String fechaImportacion) {
    this.fechaImportacion = fechaImportacion;
  }
  
  @Column(name = "TIPO")
  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name="ENTIDAD")
  @ForeignKey(name="RWE_DESCARGA_ENTIDAD_FK")
  public Entidad getEntidad() {
    return entidad;
  }

  public void setEntidad(Entidad entidad) {
    this.entidad = entidad;
  }



}