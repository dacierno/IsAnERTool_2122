package isadbtool.blogic.conceptual;

import isadbtool.blogic.BLUtils;
import isadbtool.gui.MyException;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConceptualEntity extends ConceptualRelationship
        implements Serializable {
  
  private static final long serialVersionUID = 1L;  
  private Map<String, Set<String>> theIdentifiers;
  
  public ConceptualEntity(String displayedName, String internalName) throws MyException {
    super(displayedName, internalName);
    theIdentifiers = new TreeMap();
  }

  public ConceptualEntity(Node theNode) throws MyException {
    this(BLUtils.readXMLElement(theNode, "displayedName"),
            BLUtils.readXMLElement(theNode, "internalName"));
    NodeList childsList = theNode.getChildNodes();
    for (int i = 0; i < childsList.getLength(); i++) {
      if (childsList.item(i).getNodeName().equals("ConceptualSimpleAttribute")) {
        ConceptualSimpleAttribute a = new ConceptualSimpleAttribute(childsList.item(i));
        addConceptualSimpleAttribute(a);
      } else if (childsList.item(i).getNodeName().equals("ConceptualComposedAttribute")) {
        ConceptualComposedAttribute a = new ConceptualComposedAttribute(childsList.item(i));
        addConceptualComposedAttribute(a);
      } else if (childsList.item(i).getNodeName().equals("ConceptualIdentifier")) {
        String idName = BLUtils.readXMLElement(childsList.item(i),"idName");
        String attributes = BLUtils.readXMLElement(childsList.item(i),"attributes");
        theIdentifiers.put(idName, new LinkedHashSet());
        for (String s : attributes.split(",")) {
          theIdentifiers.get(idName).add(s.trim());
        }
      } 
    }
  }

  @Override
  public String toXMLString(String startTab, String baseTab) {
    result.setLength(0);
    if (theSimpleAttributes.isEmpty()
            && theComposedAttributes.isEmpty()
            && theIdentifiers.isEmpty()) {
      return startTab + "<ConceptualEntity "
              + "displayedName=\""
              + displayedName + "\" internalName =\""
              + internalName + "\"/>";
    }
    result.append(startTab).append("<ConceptualEntity displayedName=\"")
            .append(displayedName)
            .append("\" internalName =\"").append(internalName).append("\">\n");
    for (ConceptualSimpleAttribute a : theSimpleAttributes.values()) {
      result.append(a.toXMLString(startTab + baseTab, baseTab)).append("\n");
    }
    for (ConceptualComposedAttribute a : theComposedAttributes.values()) {
      result.append(a.toXMLString(startTab + baseTab, baseTab)).append("\n");
    }
    for (String id : theIdentifiers.keySet()) {
      result.append(startTab+baseTab+baseTab).append("<ConceptualIdentifier ");
      result.append("idName=\"").append(id).append("\" ");
      result.append("attributes=\"")
            .append(theIdentifiers.get(id).toString().replace("[", "").replace("]", ""))
            .append("\"/>\n");
    }
    result.append(startTab).append("</ConceptualEntity>");
    return result.toString();
  }
  
  @Override
  public void toHTMLStringBuilder() {
    int colNumber = 3;
    boolean idColumn = false;
    if ((theIdentifiers != null) && !theIdentifiers.isEmpty()) idColumn = true;
    if (idColumn) colNumber++;
    super.HtmlTableHead(colNumber);
    for (ConceptualSimpleAttribute a : theSimpleAttributes.values()) {
      a.setIdColumn(idColumn);
      a.setIds(theIdentifiers);
      a.appendHTMLToStringBuilder(result);
    }
    for (ConceptualComposedAttribute ca : theComposedAttributes.values()) {
      result.append(ca.toHTMLString(idColumn));
    }
    result.append("</table>");
  }  
  
  @Override
  public void addARelLink(ConceptualRelLink l) {
    addARelLink(l,l.getTheRelationship().getInternalName());
  }

  @Override
  public void removeARelLink(ConceptualRelLink l) {
    removeARelLink(l,l.getTheRelationship().getInternalName());
  }
  
  public Map<String, Set<String>> getTheIdentifiers() {
    return theIdentifiers;
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
  
  public void setTheIdentifiers(Map<String, Set<String>> theIdentifiers) {
    this.theIdentifiers = theIdentifiers;
  }

}
