package isadbtool.blogic;

import isadbtool.blogic.conceptual.ConceptualRelLink;
import isadbtool.gui.MyException;
import java.io.Serializable;
import org.w3c.dom.Node;

public abstract class AbsRelLink extends BLObject implements Serializable {
  private static final long serialVersionUID = 1L;
  private String minCard;
  private String maxCard;
  private boolean identifying;
  private String idName;
  private String role;
  
  public AbsRelLink(String displayedName, 
          String internalName, 
          ConceptualRelLink anotherLink) throws MyException {
    super(displayedName, anotherLink.getInternalName(), anotherLink.getInternalName());
    minCard = anotherLink.getMinCard();
    maxCard = anotherLink.getMaxCard();
    identifying = anotherLink.isIdentifying();
    idName = anotherLink.getIdName();
    role = anotherLink.getRole();
  }
  
  public AbsRelLink(String displayedName, 
          String internalName, 
          String sourceObjInternalName,
          String minCard, String maxCard, 
          String idName, String role) throws MyException {
    super(displayedName, internalName, sourceObjInternalName);
    this.minCard = minCard;
    this.maxCard = maxCard;
    this.idName = idName;
    if (idName == null) identifying = false;
    else identifying = true;
    this.role = role;
  }
  
  public AbsRelLink(Node theNode) throws MyException {
    super(theNode);
    minCard = BLUtils.readXMLElement(theNode,"minCard");
    maxCard = BLUtils.readXMLElement(theNode,"maxCard");    
    role = BLUtils.readXMLElement(theNode,"role");
    idName = BLUtils.readXMLElement(theNode,"idName");    
    if (idName == null) identifying = false;
    else identifying = true;    
  }
  
  protected void toXMLString(StringBuilder buffer) {
    buffer.append(" displayedName=\"").append(displayedName).append("\"");
    buffer.append(" internalName=\"").append(internalName).append("\"");
    buffer.append(" minCard=\"").append(minCard).append("\"");
    buffer.append(" maxCard=\"").append(maxCard).append("\"");
    if ((role != null) && (role.length() > 0)) {
      buffer.append(" role=\"").append(role).append("\"");
    }
    if ((idName != null) && (idName.length() > 0)) {
      buffer.append(" idName=\"").append(idName).append("\"");
    }
    buffer.append("/>");    
  }
  
  public static long getSerialVersionUID() {
    return serialVersionUID;
  }
  public String getMinCard() {
    return minCard;
  }
  public String getMaxCard() {
    return maxCard;
  }
  public boolean isIdentifying() {
    return identifying;
  }
  public String getIdName() {
    return idName;
  }
  public String getRole() {
    return role;
  }
  public void setMinCard(String minCard) {
    this.minCard = minCard;
  }
  public void setMaxCard(String maxCard) {
    this.maxCard = maxCard;
  }
  public void setIdentifying(boolean identifying) {
    this.identifying = identifying;
  }
  public void setIdName(String idName) {
    this.idName = idName;
  }
  public void setRole(String role) {
    this.role = role;
  }
  
  
  
}
