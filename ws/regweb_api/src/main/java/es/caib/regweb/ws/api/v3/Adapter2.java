
package es.caib.regweb.ws.api.v3;

import java.sql.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter2
    extends XmlAdapter<String, Date>
{


    public Date unmarshal(String value) {
        return (org.fundaciobit.genapp.common.ws.WsSqlDateAdapter.parseDate(value));
    }

    public String marshal(Date value) {
        return (org.fundaciobit.genapp.common.ws.WsSqlDateAdapter.printDate(value));
    }

}