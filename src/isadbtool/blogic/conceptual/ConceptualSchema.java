package isadbtool.blogic.conceptual;

import isadbtool.blogic.BLObject;
import isadbtool.blogic.BLUtils;
import isadbtool.gui.MyException;
import isadbtool.blogic.TheSchema;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import org.w3c.dom.Node;

public class ConceptualSchema extends TheSchema implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public static String getConceptualDTD() {
    return "<!ELEMENT ConceptualSchema (ConceptualEntities?, ConceptualRelationships?, ConceptualGeneralizations?,ConceptualRelLinks?,ConceptualNotes?)>\n"
            + "<!ATTLIST ConceptualSchema longOID CDATA #REQUIRED>\n"
            + "<!ELEMENT ConceptualEntities (ConceptualEntity*)>\n"
            + "<!ELEMENT ConceptualRelationships (ConceptualRelationship*)>\n"
            + "<!ELEMENT ConceptualGeneralizations (ConceptualGeneralization*)>\n"
            + "<!ELEMENT ConceptualRelLinks (ConceptualRelLink*)>\n"
            + "<!ELEMENT ConceptualEntity (ConceptualSimpleAttribute*, ConceptualComposedAttribute*, ConceptualIdentifier*)>\n"
            + "<!ATTLIST ConceptualEntity internalName CDATA #REQUIRED>\n"
            + "<!ATTLIST ConceptualEntity displayedName CDATA #REQUIRED>\n"
            + "<!ELEMENT ConceptualRelationship (ConceptualSimpleAttribute*, ConceptualComposedAttribute*)>\n"
            + "<!ATTLIST ConceptualRelationship internalName CDATA #REQUIRED>\n"
            + "<!ATTLIST ConceptualRelationship displayedName CDATA #REQUIRED>\n"
            + "<!ELEMENT ConceptualGeneralization EMPTY>\n"
            + "<!ATTLIST ConceptualGeneralization internalName CDATA #REQUIRED>\n"
            + "<!ATTLIST ConceptualGeneralization displayedName CDATA #REQUIRED>\n"
            + "<!ATTLIST ConceptualGeneralization total CDATA #REQUIRED>\n"
            + "<!ATTLIST ConceptualGeneralization exclusive CDATA #REQUIRED>\n"
            + "<!ATTLIST ConceptualGeneralization father CDATA #REQUIRED>\n"
            + "<!ATTLIST ConceptualGeneralization childs CDATA #IMPLIED>\n"
            + "<!ELEMENT ConceptualRelLink EMPTY>\n"
            + "<!ATTLIST ConceptualRelLink internalName CDATA #REQUIRED>\n"
            + "<!ATTLIST ConceptualRelLink displayedName CDATA #REQUIRED>\n"
            + "<!ATTLIST ConceptualRelLink minCard (0|1) #REQUIRED>\n"
            + "<!ATTLIST ConceptualRelLink maxCard (1|N) #REQUIRED>\n"
            + "<!ATTLIST ConceptualRelLink entity CDATA #REQUIRED>\n"
            + "<!ATTLIST ConceptualRelLink relationship CDATA #REQUIRED>\n"
            + "<!ATTLIST ConceptualRelLink role CDATA #IMPLIED>\n"
            + "<!ATTLIST ConceptualRelLink idName CDATA #IMPLIED>\n"
            + "<!ELEMENT ConceptualNotes (ConceptualNotesLine*)>\n"
            + "<!ELEMENT ConceptualNotesLine EMPTY>\n"
            + "<!ATTLIST ConceptualNotesLine value CDATA #REQUIRED>"
            + "<!ELEMENT ConceptualSimpleAttribute EMPTY>\n"
            + "<!ATTLIST ConceptualSimpleAttribute internalName CDATA #REQUIRED>"
            + "<!ATTLIST ConceptualSimpleAttribute displayedName CDATA #REQUIRED>"
            + "<!ATTLIST ConceptualSimpleAttribute type CDATA #REQUIRED>"
            + "<!ATTLIST ConceptualSimpleAttribute minCard CDATA #REQUIRED>"
            + "<!ATTLIST ConceptualSimpleAttribute maxCard CDATA #REQUIRED>"
            + "<!ATTLIST ConceptualSimpleAttribute length CDATA #IMPLIED>"
            + "<!ATTLIST ConceptualSimpleAttribute precision CDATA #IMPLIED>"
            + "<!ATTLIST ConceptualSimpleAttribute scale CDATA #IMPLIED>"
            + "<!ELEMENT ConceptualComposedAttribute (ConceptualSimpleAttribute+)>\n"
            + "<!ATTLIST ConceptualComposedAttribute internalName CDATA #REQUIRED>"
            + "<!ATTLIST ConceptualComposedAttribute displayedName CDATA #REQUIRED>"
            + "<!ATTLIST ConceptualComposedAttribute minCard (0|1) #REQUIRED>"
            + "<!ATTLIST ConceptualComposedAttribute maxCard (1|N) #REQUIRED>"
            + "<!ELEMENT ConceptualIdentifier EMPTY>\n"
            + "<!ATTLIST ConceptualIdentifier idName CDATA #REQUIRED>"
            + "<!ATTLIST ConceptualIdentifier attributes CDATA #REQUIRED>"
            + "\n";
  }
  
  
  

  public ConceptualSchema() {
    super();
  }

  public ConceptualSchema(Node theNode, boolean allowOverlappedGen) throws MyException {
    this();
    Node aNode = BLUtils.gettingNodeByName(theNode, "ConceptualEntities");
    if (aNode != null)
      for (int i = 0; i < aNode.getChildNodes().getLength(); i++) {
        if (aNode.getChildNodes().item(i).getNodeName().equals("ConceptualEntity"))
          addABLObject(new ConceptualEntity(aNode.getChildNodes().item(i)));
      }
    aNode = BLUtils.gettingNodeByName(theNode, "ConceptualRelationships");
    if (aNode != null)
      for (int i = 0; i < aNode.getChildNodes().getLength(); i++) {
        if (aNode.getChildNodes().item(i).getNodeName().equals("ConceptualRelationship"))
          addABLObject(new ConceptualRelationship(aNode.getChildNodes().item(i)));
      }
    aNode = BLUtils.gettingNodeByName(theNode, "ConceptualGeneralizations");
    if (aNode != null)
      for (int i = 0; i < aNode.getChildNodes().getLength(); i++) {
        if (aNode.getChildNodes().item(i).getNodeName().equals("ConceptualGeneralization")) {
          String fatherName = BLUtils.readXMLElement(aNode.getChildNodes().item(i), "father");
          String childs = BLUtils.readXMLElement(aNode.getChildNodes().item(i), "childs");
          ConceptualGeneralization cg = new ConceptualGeneralization(
                  aNode.getChildNodes().item(i),
                  (ConceptualEntity) getABLObject(fatherName),
                  allowOverlappedGen);
          if ((childs != null) && (childs.trim().length() > 0)) {
            String[] temp = childs.split(",");
            for (int c = 0; c < temp.length; c++) {
              cg.addAChild((ConceptualEntity) getABLObject(temp[c].trim()));
            }
          }
          addABLObject(cg);
        }
      }
    aNode = BLUtils.gettingNodeByName(theNode, "ConceptualRelLinks");
    if (aNode != null)
      for (int i = 0; i < aNode.getChildNodes().getLength(); i++) {
        if (aNode.getChildNodes().item(i).getNodeName().equals("ConceptualRelLink")) {
          String entName = BLUtils.readXMLElement(aNode.getChildNodes().item(i), "entity");
          String relName = BLUtils.readXMLElement(aNode.getChildNodes().item(i), "relationship");
          ConceptualRelLink rl = new ConceptualRelLink(aNode.getChildNodes().item(i),
                  (ConceptualEntity) getABLObject(entName),
                  (ConceptualRelationship) getABLObject(relName));
          ((ConceptualEntity) getABLObject(entName)).addARelLink(rl);
          ((ConceptualRelationship) getABLObject(relName)).addARelLink(rl);
        }
      }
    aNode = BLUtils.gettingNodeByName(theNode, "ConceptualNotes");
    notes.clear();
    for (int i = 0; i < aNode.getChildNodes().getLength(); i++) {
      if (aNode.getChildNodes().item(i).getNodeName().equals("ConceptualNotesLine"))
        if ((BLUtils.readXMLElement(aNode.getChildNodes().item(i), "value") != null)
                && BLUtils.readXMLElement(aNode.getChildNodes().item(i), "value").length() > 0)
          notes.add(BLUtils.readXMLElement(aNode.getChildNodes().item(i), "value"));
        else
          notes.add("");
    }
  }

  public ConceptualEntity newEntity(String prefix, String defPrefix) throws MyException {
    ConceptualEntity ce = new ConceptualEntity(
            getNewObjectDisplayedName(prefix, defPrefix),
            getNewObjectInternalName());
    addABLObject(ce);
    return ce;
  }

  public ConceptualRelationship newRelationship(String prefix, String defPrefix) throws MyException {
    ConceptualRelationship cr = new ConceptualRelationship(
            getNewObjectDisplayedName(prefix, defPrefix),
            getNewObjectInternalName());
    addABLObject(cr);
    return cr;
  }

  public ConceptualGeneralization newGeneralization(String genName, String fatherName) throws MyException {
    ConceptualGeneralization cg = new ConceptualGeneralization(
            genName,
            getNewObjectInternalName(),
            (ConceptualEntity) getABLObject(fatherName));
    addABLObject(cg);
    return cg;
  }

  public ConceptualRelLink newRelLink(String entName, String relName) throws MyException {
    ConceptualRelLink crl = new ConceptualRelLink(
            getNewObjectInternalName(),
            (ConceptualEntity) getABLObject(entName),
            (ConceptualRelationship) getABLObject(relName),
            "0", "N", null, null);
    ((ConceptualEntity) getABLObject(entName)).addARelLink(crl);
    ((ConceptualRelationship) getABLObject(relName)).addARelLink(crl);
    return crl;
  }

  @Override
  public String toXMLString(String startTab, String baseTab) {
    result.setLength(0);
    BLObjectsToXML(getTheConceptualEntities(), startTab, baseTab, "ConceptualEntities");
    BLObjectsToXML(getTheConceptualRelationships(), startTab, baseTab, "ConceptualRelationships");
    BLObjectsToXML(getTheConceptualGeneralizations(), startTab, baseTab, "ConceptualGeneralizations");
    Map<String, ConceptualRelLink> temp = new TreeMap();
    for (ConceptualRelationship theCR : getTheConceptualRelationships()) {
      for (String e : theCR.getConnectedObjects()) {
        for (ConceptualRelLink theCRL : theCR.getTheRelLinks(e)) {
          temp.put(theCRL.getInternalName(), theCRL);
        }
      }
    }
    BLObjectsToXML(temp.values(), startTab, baseTab, "ConceptualRelLinks");
    result.append(startTab + baseTab).append("<ConceptualNotes>\n");
    for (String s : notes) {
      result.append(startTab + baseTab + baseTab).append("<ConceptualNotesLine value=\"").append(s).append("\"/>\n");
    }
    result.append(startTab + baseTab).append("</ConceptualNotes>\n");
    return result.toString();
  }

  public String getNewObjectDisplayedName(String prefix, String defPrefix) {
    if (!prefix.equals(defPrefix)
            && !objectDisplayedNameExists(prefix))
      return prefix;
    int i = 0;
    while (objectDisplayedNameExists(prefix + i)) {
      i++;
    };
    return prefix + i;
  }

  public boolean objectDisplayedNameExists(String displayedName) {
    for (ConceptualEntity c : getTheConceptualEntities()) {
      if (c.getDisplayedName().equals(displayedName))
        return true;
    }
    for (ConceptualRelationship c : getTheConceptualRelationships()) {
      if (c.getDisplayedName().equals(displayedName))
        return true;
    }
    for (ConceptualGeneralization c : getTheConceptualGeneralizations()) {
      if (c.getDisplayedName().equals(displayedName))
        return true;
    }
    for (ConceptualRelationship theCR : getTheConceptualRelationships()) {
      for (String e : theCR.getConnectedObjects()) {
        for (ConceptualRelLink theCRL : theCR.getTheRelLinks(e)) {
          if (theCRL.getDisplayedName().equals(displayedName))
            return true;
        }
      }
    }
    return false;
  }

  public Collection<ConceptualEntity> getTheConceptualEntities() {
    Map<String, ConceptualEntity> theConceptualEntities = new LinkedHashMap();
    for (BLObject o : getTheBLObjects()) {
      if (o instanceof ConceptualEntity)
        theConceptualEntities.put(o.getInternalName(), (ConceptualEntity) o);
    }
    return theConceptualEntities.values();
  }
  
  public Collection<ConceptualRelationship> getTheConceptualRelationships() {
    Map<String,ConceptualRelationship> theConceptualRelationships = new LinkedHashMap();
    for (BLObject o : getTheBLObjects()) {
      if (o.getClass().getSimpleName().equals("ConceptualRelationship")) 
        theConceptualRelationships.put(o.getInternalName(), (ConceptualRelationship) o);
    }
    return theConceptualRelationships.values();
  }
  
  public Collection<ConceptualGeneralization> getTheConceptualGeneralizations() {
    Map<String,ConceptualGeneralization> theConceptualGeneralizations = new LinkedHashMap();
    for (BLObject o : getTheBLObjects()) {
      if (o.getClass().getSimpleName().equals("ConceptualGeneralization")) 
        theConceptualGeneralizations.put(o.getInternalName(), (ConceptualGeneralization) o);
    }
    return theConceptualGeneralizations.values();
  }

  @Override
  public String toString() {
    result.setLength(0);
    result.append("Conceptual Schema\n");
    result.append(" Entities\n");
    for (ConceptualEntity theCE : getTheConceptualEntities()) {
      result.append("  " + theCE.toString());
    }
    result.append(" Relationships\n");
    for (ConceptualRelationship theCR : getTheConceptualRelationships()) {
      result.append("  " + theCR.toString());      
    }
    /*BLObjectsToXML(getTheConceptualEntities(), startTab, baseTab, "ConceptualEntities");
    BLObjectsToXML(getTheConceptualRelationships(), startTab, baseTab, "ConceptualRelationships");
    BLObjectsToXML(getTheConceptualGeneralizations(), startTab, baseTab, "ConceptualGeneralizations");
    Map<String, ConceptualRelLink> temp = new TreeMap();
    for (ConceptualRelationship theCR : getTheConceptualRelationships()) {
      for (String e : theCR.getConnectedObjects()) {
        for (ConceptualRelLink theCRL : theCR.getTheRelLinks(e)) {
          temp.put(theCRL.getInternalName(), theCRL);
        }
      }
    }
    BLObjectsToXML(temp.values(), startTab, baseTab, "ConceptualRelLinks");
    result.append(startTab + baseTab).append("<ConceptualNotes>\n");
    for (String s : notes) {
      result.append(startTab + baseTab + baseTab).append("<ConceptualNotesLine value=\"").append(s).append("\"/>\n");
    }
    result.append(startTab + baseTab).append("</ConceptualNotes>\n");*/
    return result.toString();
  }
  
  

}
