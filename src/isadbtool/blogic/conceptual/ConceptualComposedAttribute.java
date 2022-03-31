package isadbtool.blogic.conceptual;

import isadbtool.blogic.BLAttribute;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import isadbtool.blogic.BLObject;
import isadbtool.blogic.BLUtils;
import isadbtool.gui.MyException;
import java.io.Serializable;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConceptualComposedAttribute extends BLAttribute
        implements Serializable {
  private static final long serialVersionUID = 1L;

  private Map<String, ConceptualSimpleAttribute> theSimpleAttributes;
  private boolean idColumn;

  public ConceptualComposedAttribute(String displayedName,
          String internalName,
          String minCard, String maxCard) throws MyException {
    super(displayedName, internalName, null);
    setMincard(minCard);
    setMaxcard(maxCard);
    theSimpleAttributes = new LinkedHashMap();
  }

  public ConceptualComposedAttribute(String internalName, ConceptualComposedAttribute toBeCopied) throws MyException {
    this(toBeCopied.displayedName, internalName,
            toBeCopied.getMincard(), toBeCopied.getMincard());
  }

  public ConceptualComposedAttribute(Node theNode) throws MyException {
    this(BLUtils.readXMLElement(theNode, "internalName"),
            BLUtils.readXMLElement(theNode, "displayedName"),
            BLUtils.readXMLElement(theNode, "minCard"),
            BLUtils.readXMLElement(theNode, "maxCard"));
    theSimpleAttributes = new LinkedHashMap();
    NodeList childsList = theNode.getChildNodes();
    for (int i = 0; i < childsList.getLength(); i++) {
      if (childsList.item(i).getNodeName().equals("Attribute")
              || childsList.item(i).getNodeName().equals("ConceptualSimpleAttribute")) {
        ConceptualSimpleAttribute a = new ConceptualSimpleAttribute(childsList.item(i));
        theSimpleAttributes.put(a.getInternalName(), a);
      }
    }
  }

  public ConceptualSimpleAttribute getASimpleAttribute(String internalName) {
    return theSimpleAttributes.get(internalName);
  }

  public Collection<ConceptualSimpleAttribute> getTheSimpleAttributes() {
    return theSimpleAttributes.values();
  }

  public void addSimpleAttributes(ConceptualSimpleAttribute a) {
    theSimpleAttributes.put(a.getInternalName(), a);
  }

  public void removeSimpleAttributes(String attrName) {
    theSimpleAttributes.remove(attrName);
  }

  public boolean attributeInternalNameExists(String attrName) {
    return theSimpleAttributes.containsKey(attrName);
  }
  
  @Override
  public String toXMLString(String startTab, String baseTab) {
    result.setLength(0);
    result.append(startTab);
    result.append("<ConceptualComposedAttribute internalName=\"").append(internalName).append("\"");
    result.append(" displayedName=\"").append(displayedName).append("\"");
    result.append(" minCard=\"").append(getMincard()).append("\"");
    result.append(" maxCard=\"").append(getMaxcard()).append("\">\n");
    for (ConceptualSimpleAttribute a : theSimpleAttributes.values()) {
      result.append(a.toXMLString(startTab + baseTab, baseTab)).append("\n");
    }
    result.append(startTab).append("</ConceptualComposedAttribute>");
    return result.toString();
  }

  public String toHTMLString(boolean idColumn) {
    this.idColumn = idColumn;
    toHTMLStringBuilder();
    return result.toString();
  }
  
  public void toHTMLStringBuilder() {
    result.setLength(0);
    result.append("<tr style=\"border-top: solid 1px;\">");
    result.append("<td id=\"first\"><b>")
            //.append(internalName + " - ")
            .append(displayedName).append("</b></td>");
    result.append("<td>&nbsp;</td>");
    result.append("<td>(").append(getMincard()).append(",").append(getMaxcard()).append(")</td>");
    if (idColumn) {
      result.append("<td>&nbsp;</td>");
    }
    result.append("</tr>");
    for (ConceptualSimpleAttribute a : theSimpleAttributes.values()) {
     a.appendHTMLToStringBuilder(result);
    }
  }

  public DefaultMutableTreeNode asTreeNode() {
    DefaultMutableTreeNode resultNode = new DefaultMutableTreeNode(getInternalName());
    for (ConceptualSimpleAttribute sa : theSimpleAttributes.values()) {
      resultNode.add(new DefaultMutableTreeNode(sa.toString()));
    }
    return resultNode;
  }





}
