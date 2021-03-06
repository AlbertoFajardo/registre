/**
 * RegistroEntradaWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class RegistroEntradaWs  extends es.caib.regweb3.ws.v3.apiaxis.RegistroWs  implements java.io.Serializable {
    private java.lang.String destino;

    public RegistroEntradaWs() {
    }

    public RegistroEntradaWs(
           es.caib.regweb3.ws.v3.apiaxis.AnexoWs[] anexos,
           java.lang.String aplicacion,
           java.lang.String codigoAsunto,
           java.lang.String codigoUsuario,
           java.lang.String contactoUsuario,
           java.lang.Long docFisica,
           java.lang.String expone,
           java.lang.String extracto,
           java.util.Calendar fecha,
           java.lang.String idioma,
           es.caib.regweb3.ws.v3.apiaxis.InteresadoWs[] interesados,
           java.lang.String libro,
           java.lang.String numExpediente,
           java.lang.String numTransporte,
           int numero,
           java.lang.String numeroRegistroFormateado,
           java.lang.String observaciones,
           java.lang.String oficina,
           java.lang.String refExterna,
           java.lang.String solicita,
           java.lang.String tipoAsunto,
           java.lang.String tipoTransporte,
           java.lang.String version,
           java.lang.String destino) {
        super(
            anexos,
            aplicacion,
            codigoAsunto,
            codigoUsuario,
            contactoUsuario,
            docFisica,
            expone,
            extracto,
            fecha,
            idioma,
            interesados,
            libro,
            numExpediente,
            numTransporte,
            numero,
            numeroRegistroFormateado,
            observaciones,
            oficina,
            refExterna,
            solicita,
            tipoAsunto,
            tipoTransporte,
            version);
        this.destino = destino;
    }


    /**
     * Gets the destino value for this RegistroEntradaWs.
     * 
     * @return destino
     */
    public java.lang.String getDestino() {
        return destino;
    }


    /**
     * Sets the destino value for this RegistroEntradaWs.
     * 
     * @param destino
     */
    public void setDestino(java.lang.String destino) {
        this.destino = destino;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RegistroEntradaWs)) return false;
        RegistroEntradaWs other = (RegistroEntradaWs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.destino==null && other.getDestino()==null) || 
             (this.destino!=null &&
              this.destino.equals(other.getDestino())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getDestino() != null) {
            _hashCode += getDestino().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RegistroEntradaWs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "registroEntradaWs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destino");
        elemField.setXmlName(new javax.xml.namespace.QName("", "destino"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
