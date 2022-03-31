package isadbtool.blogic.restructured;

import isadbtool.blogic.TheSchema;
import java.io.Serializable;

public abstract class RestructuredSchema extends TheSchema implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public static String getRestructuredEntityDTD() {
    return "<!ELEMENT RestructuredEntity (PrimaryIdentifier?, RestructuredSimpleAttribute*)>\n"
            + "<!ATTLIST RestructuredEntity internalName CDATA #REQUIRED>\n"
            + "<!ATTLIST RestructuredEntity displayedName CDATA #REQUIRED>\n"
            + "<!ATTLIST RestructuredEntity sourceObjInternalName CDATA #REQUIRED>\n"
            + "<!ELEMENT PrimaryIdentifier EMPTY>\n"
            + "<!ATTLIST PrimaryIdentifier name CDATA #REQUIRED>\n"
            + "<!ELEMENT RestructuredSimpleAttribute EMPTY>\n"
            + "<!ATTLIST RestructuredSimpleAttribute internalName CDATA #REQUIRED>\n"
            + "<!ATTLIST RestructuredSimpleAttribute displayedName CDATA #REQUIRED>\n"
            + "<!ATTLIST RestructuredSimpleAttribute minCard (0|1) #REQUIRED>"
            + "<!ATTLIST RestructuredSimpleAttribute maxCard (1|N) #REQUIRED>"
            + "<!ATTLIST RestructuredSimpleAttribute sourceObjInternalName CDATA #REQUIRED>"
            + "<!ATTLIST RestructuredSimpleAttribute splittedIdNames CDATA #IMPLIED>";    
  }
  
  
  public static String getRestructuredDTD() {
    return "<!ELEMENT RestructuredSchema (RestructuredEntities?, RestructuredRelationships?, RestructuredGeneralizations?,RestructuredRelLinks?,RestructuredNotes?)>\n"
            + "<!ATTLIST RestructuredSchema longOID CDATA #REQUIRED>\n"
            + "<!ELEMENT RestructuredEntities (RestructuredEntity*)>\n"
            + getRestructuredEntityDTD()
            
            + "<!ELEMENT RestructuredRelationships (RestructuredRelationship*)>\n"            
            + "<!ELEMENT RestructuredRelationship (RestructuredSimpleAttribute*)>\n"
            + "<!ATTLIST RestructuredRelationship internalName CDATA #REQUIRED>\n"
            + "<!ATTLIST RestructuredRelationship displayedName CDATA #REQUIRED>\n"
            + "<!ATTLIST RestructuredRelationship sourceObjInternalName CDATA #REQUIRED>\n"
            
            + "<!ELEMENT RestructuredGeneralizations (RestructuredGeneralization*)>\n"
            + "<!ELEMENT RestructuredGeneralization EMPTY>\n"            
            + "<!ATTLIST RestructuredGeneralization internalName CDATA #REQUIRED>\n"
            + "<!ATTLIST RestructuredGeneralization displayedName CDATA #REQUIRED>\n"
            + "<!ATTLIST RestructuredGeneralization total (true|false) #REQUIRED>\n"
            + "<!ATTLIST RestructuredGeneralization exclusive (true|false) #REQUIRED>\n"
            + "<!ATTLIST RestructuredGeneralization father CDATA #REQUIRED>\n"
            + "<!ATTLIST RestructuredGeneralization childs CDATA #IMPLIED>\n"
            + "<!ATTLIST RestructuredGeneralization sourceObjInternalName CDATA #REQUIRED>\n"
            
            + "<!ELEMENT RestructuredRelLinks (RestructuredRelLink*)>\n"
            + "<!ELEMENT RestructuredRelLink EMPTY>\n"
            + "<!ATTLIST RestructuredRelLink internalName CDATA #REQUIRED>\n"
            + "<!ATTLIST RestructuredRelLink displayedName CDATA #REQUIRED>\n"
            + "<!ATTLIST RestructuredRelLink minCard (0|1) #REQUIRED>\n"
            + "<!ATTLIST RestructuredRelLink maxCard (1|N) #REQUIRED>\n"
            + "<!ATTLIST RestructuredRelLink entity CDATA #REQUIRED>\n"
            + "<!ATTLIST RestructuredRelLink relationship CDATA #REQUIRED>\n"
            + "<!ATTLIST RestructuredRelLink role CDATA #IMPLIED>\n"
            + "<!ATTLIST RestructuredRelLink idName CDATA #IMPLIED>\n"
            + "<!ATTLIST RestructuredRelLink sourceObjInternalName CDATA #REQUIRED>\n"
            + "\n";
  }  
  

  public RestructuredSchema() {
    super();
  }



}
