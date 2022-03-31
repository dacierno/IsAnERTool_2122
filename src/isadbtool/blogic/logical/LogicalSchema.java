package isadbtool.blogic.logical;

import isadbtool.blogic.TheSchema;
import java.io.Serializable;

public abstract class LogicalSchema extends TheSchema implements Serializable {
  private static final long serialVersionUID = 1L;

  public static String getTableDTD() {
    return "<!ELEMENT LogicalTable (LogicalColumn*)>\n"
            + "<!ATTLIST LogicalTable internalName CDATA #REQUIRED>\n"
            + "<!ATTLIST LogicalTable displayedName CDATA #REQUIRED>\n"
            + "<!ATTLIST LogicalTable sourceObjInternalName CDATA #REQUIRED>\n"
            + "<!ELEMENT LogicalColumn EMPTY>\n"
            + "<!ATTLIST LogicalColumn internalName CDATA #REQUIRED>\n"
            + "<!ATTLIST LogicalColumn displayedName CDATA #REQUIRED>\n"
            + "<!ATTLIST LogicalColumn sourceObjInternalName CDATA #REQUIRED>\n"
            + "<!ATTLIST LogicalColumn sqlType CDATA #REQUIRED>\n"
            + "<!ATTLIST LogicalColumn isNotNull (true|false) #REQUIRED>\n";
  }
  
  public static String getLogicalDTD() {
    return "<!ELEMENT LogicalSchema (LogicalTables?)>\n"
            + "<!ATTLIST LogicalSchema longOID CDATA #REQUIRED>\n"
            + "<!ELEMENT LogicalTables (LogicalTable*)>\n"
            + getTableDTD()
            + "\n";
  }



}
