package isadbtool.blogic.conceptual;

import isadbtool.blogic.AbsRelLink;
import isadbtool.blogic.BLObject;
import isadbtool.blogic.BLUtils;
import isadbtool.gui.MyException;
import java.io.Serializable;
import org.w3c.dom.Node;

public class ConceptualRelLink extends AbsRelLink implements Serializable {
  private static final long serialVersionUID = 1L;
  private ConceptualEntity theEntity;
  private ConceptualRelationship theRelationship;  

  public ConceptualRelLink(String relLinkInternalName,
          ConceptualEntity theEREntity,
          ConceptualRelationship theRelationship,
          String minCard, String maxCard, 
          String idName, String role) throws MyException {
    super(relLinkInternalName, relLinkInternalName, null,
          minCard, maxCard, idName, role);    
    this.theEntity = theEREntity;
    this.theRelationship = theRelationship;
  }

  public ConceptualRelLink(Node theNode, ConceptualEntity theEREntity,
          ConceptualRelationship theRelationship) throws MyException {
    super(theNode);
    this.theEntity = theEREntity;
    this.theRelationship = theRelationship;
  }

  @Override
  public String toXMLString(String startTab, String baseTab) {
    result.setLength(0);
    result.append(startTab).append("<ConceptualRelLink");
    result.append(" relationship=\"").append(theRelationship.getInternalName()).append("\"");
    result.append(" entity=\"").append(theEntity.getInternalName()).append("\"");
    super.toXMLString(result);
    return result.toString();
  }
  
  public void toHTMLStringBuilder() {
    result.setLength(0);
    result.append("<table class=\"grid\">");
    result.append("<tr><td id=\"first\">");
    //result.append(internalName).append(" ");
    if ((getRole() != null) && (getRole().length() > 0)) 
      result.append(getRole()).append(" ");
    result.append("(").append(getMinCard()).append(",").append(getMaxCard());
    if ((getIdName() != null) && (getIdName().length() > 0)) {
      result.append("," + getIdName() + ")");
    } else {
      result.append(")");
    }
    result.append("</td></tr></body></table>");
  }
  

  public ConceptualEntity getTheEntity() {
    return theEntity;
  }

  public ConceptualRelationship getTheRelationship() {
    return theRelationship;
  }

}
