package isadbtool.blogic.conceptual;

import isadbtool.blogic.BLAttribute;
import javax.swing.tree.DefaultMutableTreeNode;
import isadbtool.blogic.BLUtils;
import isadbtool.gui.MyException;
import java.io.Serializable;
import org.w3c.dom.Node;

public class ConceptualSimpleAttribute extends BLAttribute implements Serializable {
  private static final long serialVersionUID = 1L;

  private String type;
  private Integer length;
  private Integer precision;
  private Integer scale;

  public ConceptualSimpleAttribute(String displayedName,
          String internalName,
          String sourceObjInternalName) throws MyException {
    super(displayedName, internalName, sourceObjInternalName);
    type = null;
    length = null;
    precision = null;
    scale = null;
  }

  public ConceptualSimpleAttribute(String displayedName, String internalName,
          String mincard, String maxcard, String type,
          String length, String precision, String scale)
          throws MyException {
    this(displayedName, internalName, null);
    setMincard(mincard.trim());
    setMaxcard(maxcard.trim());
    this.type = type.trim();
    if (type.equalsIgnoreCase("varchar")) {
      if (length == null) throw new MyException("Length is invalid for " + displayedName);
      try {
        this.length = Integer.parseInt(length.trim());
      } catch (NumberFormatException ex) {
        throw new MyException("Length is invalid for " + displayedName);
      }
    } else if (type.equalsIgnoreCase("numeric") || type.equalsIgnoreCase("decimal")) {
      if (precision == null) throw new MyException("Precision is invalid for " + displayedName);
      if (scale == null) throw new MyException("Scale is invalid for " + displayedName);
      try {
        this.precision = Integer.parseInt(precision.trim());
        this.scale = Integer.parseInt(scale.trim());
      } catch (NumberFormatException ex) {
        throw new MyException("Precision and or Scale are invalid for " + displayedName);
      }
    } else if (type.equalsIgnoreCase("char")) {
      if ((length != null) && (length.trim().length() > 0)) {
        try {
          this.length = Integer.parseInt(length.trim());
        } catch (NumberFormatException ex) {
          throw new MyException("Length is invalid for " + displayedName);
        }
      }
    }
    setSqlType(type, length, precision, scale);
  }

  public ConceptualSimpleAttribute(String internalName, ConceptualSimpleAttribute theAttribute) throws MyException {
    super(theAttribute.displayedName, internalName, null);
    setMincard(theAttribute.getMincard());
    setMaxcard(theAttribute.getMaxcard());
    type = theAttribute.type;
    length = theAttribute.length;
    scale = theAttribute.scale;
    setSqlType(theAttribute.getSqlType());
  }

  public ConceptualSimpleAttribute(Node theNode) throws MyException {
    this(BLUtils.readXMLElement(theNode, "displayedName"),
            BLUtils.readXMLElement(theNode, "internalName"),
            BLUtils.readXMLElement(theNode, "minCard"),
            BLUtils.readXMLElement(theNode, "maxCard"),
            BLUtils.readXMLElement(theNode, "type"),
            BLUtils.readXMLElement(theNode, "length"),
            BLUtils.readXMLElement(theNode, "precision"),
            BLUtils.readXMLElement(theNode, "scale"));
  }

  public void modify(ConceptualSimpleAttribute sa) {
    this.displayedName = sa.displayedName;
    setMincard(sa.getMincard());
    setMaxcard(sa.getMaxcard());
    this.type = sa.type;
    this.length = sa.length;
    this.precision = sa.precision;
    this.scale = sa.scale;
    setSqlType(sa.getSqlType());
  }

  @Override
  public String toXMLString(String startTab, String baseTab) {
    result.setLength(0);
    result.append(startTab);
    result.append("<ConceptualSimpleAttribute internalName=\"").append(internalName).append("\"");
    result.append(" displayedName=\"").append(displayedName).append("\"");
    result.append(" type=\"").append(type).append("\"");
    result.append(" minCard=\"").append(getMincard()).append("\"");
    result.append(" maxCard=\"").append(getMaxcard()).append("\"");
    if (length != null) {
      result.append(" length=\"").append(length).append("\"");
    }
    if (precision != null) {
      result.append(" precision=\"").append(precision).append("\"");
    }
    if (scale != null) {
      result.append(" scale=\"").append(scale).append("\"");
    }
    result.append("/>");
    return result.toString();
  }  

  public DefaultMutableTreeNode asTreeNode() {
    DefaultMutableTreeNode resultNode = new DefaultMutableTreeNode(this.toString());
    return resultNode;
  }

  public String getType() {
    return type;
  }

  public Integer getPrecision() {
    return precision;
  }

  public void setPrecision(Integer precision) {
    this.precision = precision;
  }

  public void setScale(Integer scale) {
    this.scale = scale;
  }

  public Integer getLength() {
    return length;
  }

  public Integer getScale() {
    return scale;
  }
  
  @Override
  public String toString() {
    result.setLength(0);
    result.append(displayedName);
    return result.toString();
  }  
  

}
