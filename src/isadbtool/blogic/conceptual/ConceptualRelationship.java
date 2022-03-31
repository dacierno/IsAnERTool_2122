package isadbtool.blogic.conceptual;

import isadbtool.blogic.BLObject;
import isadbtool.blogic.BLUtils;
import isadbtool.gui.MyException;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConceptualRelationship extends BLObject implements Serializable {

  private static final long serialVersionUID = 1L;
  protected Map<String, ConceptualSimpleAttribute> theSimpleAttributes;
  protected Map<String, ConceptualComposedAttribute> theComposedAttributes;
  protected final Map<String, Map<String, ConceptualRelLink>> theRelLinks;

  public ConceptualRelationship(String displayedName, String internalName) throws MyException {
    super(displayedName, internalName, null);
    theSimpleAttributes = new LinkedHashMap();
    theComposedAttributes = new LinkedHashMap();
    theRelLinks = new LinkedHashMap();
  }

  public ConceptualRelationship(Node theNode) throws MyException {
    this(BLUtils.readXMLElement(theNode, "displayedName"),
            BLUtils.readXMLElement(theNode, "internalName"));
    NodeList childsList = theNode.getChildNodes();
    for (int i = 0; i < childsList.getLength(); i++) {
      if (childsList.item(i).getNodeName().equals("ConceptualSimpleAttribute")) {
        ConceptualSimpleAttribute a = new ConceptualSimpleAttribute(childsList.item(i));
        this.addConceptualSimpleAttribute(a);
      } else if (childsList.item(i).getNodeName().equals("ConceptualComposedAttribute")) {
        ConceptualComposedAttribute a = new ConceptualComposedAttribute(childsList.item(i));
        addConceptualComposedAttribute(a);
      }
    }
  }
  
  @Override
  public String toXMLString(String startTab, String baseTab) {
    if (theSimpleAttributes.isEmpty()
            && theComposedAttributes.isEmpty()) {
      return startTab + "<ConceptualRelationship "
              + "displayedName=\"" + displayedName + "\" "
              + "internalName=\"" + internalName + "\"/>";
    }
    result.setLength(0);
    result.append(startTab)
            .append("<ConceptualRelationship displayedName=\"")
            .append(displayedName).append("\" ")
            .append("internalName=\"").append(internalName).append("\">\n");
    for (ConceptualSimpleAttribute a : theSimpleAttributes.values()) {
      result.append(a.toXMLString(startTab + baseTab, baseTab)).append("\n");
    }
    for (ConceptualComposedAttribute a : theComposedAttributes.values()) {
      result.append(a.toXMLString(startTab + baseTab, baseTab)).append("\n");
    }
    result.append(startTab).append("</ConceptualRelationship>");
    return result.toString();
  }
  
  @Override
  public void toHTMLStringBuilder() {
    super.HtmlTableHead(3);
    for (ConceptualSimpleAttribute a : theSimpleAttributes.values()) {
      a.appendHTMLToStringBuilder(result);
    }
    for (ConceptualComposedAttribute ca : theComposedAttributes.values()) {
      result.append(ca.toHTMLString(false));
    }
  }

  public final void addConceptualSimpleAttribute(ConceptualSimpleAttribute toBeAdded) throws MyException {
    for (ConceptualSimpleAttribute a1 : theSimpleAttributes.values()) {
      if (a1.getDisplayedName().equals(toBeAdded.getDisplayedName())) {
        throw new MyException("1 " + toBeAdded.getDisplayedName()
                + " (" + toBeAdded.getDisplayedName() + ") "
                + " already exists in  "
                + displayedName + " (" + internalName + ")");
      }
    }
    if (theSimpleAttributes.containsKey(toBeAdded.getInternalName())) {
      throw new MyException("2 " + toBeAdded.getDisplayedName()
              + " (" + toBeAdded.getInternalName() + ") "
              + " already exists in  "
              + displayedName + " (" + internalName + ")");
    } else if (!toBeAdded.getMincard().equals("0") && !toBeAdded.getMincard().equals("1")) {
      throw new MyException("Wrong min cardinality for " + toBeAdded.getDisplayedName());
    } else if (!toBeAdded.getMaxcard().equals("1") && !toBeAdded.getMaxcard().equals("N")) {
      throw new MyException("Wrong max cardinality for " + toBeAdded.getDisplayedName());
    }
    theSimpleAttributes.put(toBeAdded.getInternalName(), toBeAdded);
  }
  
  public final void addConceptualComposedAttribute(ConceptualComposedAttribute a) throws MyException {
    if (theComposedAttributes.containsKey(a.getInternalName())) {
      throw new MyException(a.getDisplayedName()
              + " (" + a.getInternalName() + ") "
              + " already exists in  "
              + displayedName + " (" + internalName + ")");
    } else if (!a.getMincard().equals("0") && !a.getMincard().equals("1")) {
      throw new MyException("Wrong min cardinality for " + a.getDisplayedName());
    } else if (!a.getMaxcard().equals("1") && !a.getMaxcard().equals("N")) {
      throw new MyException("Wrong max cardinality for " + a.getDisplayedName());
    }
    theComposedAttributes.put(a.getInternalName(), a);
  }  

  protected void addARelLink(ConceptualRelLink l, String connObjName) {
    if (!theRelLinks.containsKey(connObjName)) {
      theRelLinks.put(connObjName, new LinkedHashMap());
    }
    theRelLinks.get(connObjName).put(l.getInternalName(), l);
  }

  public void addARelLink(ConceptualRelLink l) {
    addARelLink(l, l.getTheEntity().getInternalName());
  }
  
  protected void removeARelLink(ConceptualRelLink l, String connObjName) {
    theRelLinks.get(connObjName).remove(l.getInternalName());
    if (theRelLinks.get(connObjName).isEmpty()) {
      theRelLinks.remove(connObjName);
    }
  }  

  public void removeARelLink(ConceptualRelLink l) {
    removeARelLink(l, l.getTheEntity().getInternalName());
  }
  
 public Collection<ConceptualSimpleAttribute> getConceptualSimpleAttributes() {
   return theSimpleAttributes.values();
 }
  
 public ConceptualComposedAttribute getAConceptualComposedAttribute(String internalName) {
   return theComposedAttributes.get(internalName);
 }
 
 public ConceptualSimpleAttribute getAConceptualSimpleAttribute(String internalName) {
   return theSimpleAttributes.get(internalName);
 }
 
 public Collection<ConceptualComposedAttribute> getConceptualComposedAttributes() {
   return theComposedAttributes.values();
 }
  
 public Collection<String> getConnectedObjects() {
   return theRelLinks.keySet();
 }  

  public Collection<ConceptualRelLink> getTheRelLinks(String objName) {
    if (theRelLinks.get(objName) == null) return null;
    return theRelLinks.get(objName).values();
  }
  
  @Override
  public String toString() {
    result.setLength(0);
    result.append(displayedName).append("(");
    for (ConceptualSimpleAttribute a : theSimpleAttributes.values()) {
      result.append(a.toString()).append(",");
    }
    result.setLength(result.length()-1);
    result.append(")\n");    
    return result.toString();
  }    
  
  public void setTheSimpleAttributes(Map<String, ConceptualSimpleAttribute> theSimpleAttributes) {
    this.theSimpleAttributes = theSimpleAttributes;
  }

  public void setTheComposedAttributes(Map<String, ConceptualComposedAttribute> theComposedAttributes) {
    this.theComposedAttributes = theComposedAttributes;
  }
  
  

}
