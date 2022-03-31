package isadbtool.blogic;

import isadbtool.blogic.conceptual.ConceptualSimpleAttribute;
import isadbtool.gui.MyException;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public abstract class BLAttribute extends BLObject implements Serializable {
  private static final long serialVersionUID = 1L;
  private String mincard;
  private String maxcard;

  private boolean idColumn;
  private boolean cardColumn;
  private String pID;
  private Map<String, Set<String>> ids;
  protected String sqlType;
  protected Collection<String> splittedIdNames;
  private boolean solved;
  private Set<String> addedObjects;


  public BLAttribute(String displayedName, String internalName, String sourceObjInternalName) throws MyException {
    super(displayedName, internalName, sourceObjInternalName);
    mincard = null;
    maxcard = null;
    sqlType = null;
    splittedIdNames = new TreeSet();
    solved = false;
    addedObjects = new LinkedHashSet();
  }

  @Override
  public void toHTMLStringBuilder() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void appendHTMLToStringBuilder(StringBuilder result) {
    result.append("<tr><td id=\"first\">");
    result.append(displayedName).append("</td>");
    result.append("<td>").append(getSqlType()).append("</td>");
    if (this instanceof ConceptualSimpleAttribute) {
      isConceptualSimpleAttribute(result);
    } 
  }

  public String getSqlType() {
    return sqlType;
  }
  public void setSqlType(String sqlType) {
    this.sqlType = sqlType;
  }

  protected final void setSqlType(String type, String length,
          String precision, String scale) throws MyException {
    if (type.equalsIgnoreCase("varchar")) {
      try {
        sqlType = type + "(" + length + ")";
      } catch (NumberFormatException ex) {
        throw new MyException("Length is invalid for " + displayedName);
      }
    } else if (type.equalsIgnoreCase("numeric") || type.equalsIgnoreCase("decimal")) {
      try {
        sqlType = type + "(" + precision + "," + scale + ")";
      } catch (NumberFormatException ex) {
        throw new MyException("Precision and or Scale are invalid for " + displayedName);
      }
    } else if (type.equalsIgnoreCase("char")) {
      if ((length == null) || (length.length() == 0)) {
        sqlType = type;
      } else {
        try {
          if (Integer.parseInt(length) > 1) {
            sqlType = type + "(" + Integer.parseInt(length) + ")";
          } else {
            sqlType = type;
          }
        } catch (NumberFormatException ex) {
          throw new MyException("Length is invalid for " + displayedName);
        }
      }
    }
    if (sqlType == null) {
      sqlType = type;
    }
  }
  public String getMaxcard() {
    return maxcard;
  }

  public void setMaxcard(String maxcard) {
    this.maxcard = maxcard;
  }

  public String getMincard() {
    return mincard;
  }
  public void setMincard(String mincard) {
    this.mincard = mincard;
  }
  public void setIdColumn(boolean idColumn) {
    this.idColumn = idColumn;
  }
  
  public void setIds(Map<String, Set<String>> ids) {
    this.ids = ids;
  }
  public Map<String, Set<String>> getIds() {
    return ids;
  }

  public void setCardColumn(boolean cardColumn) {
    this.cardColumn = cardColumn;
  }

  public void setpID(String pID) {
    this.pID = pID;
  }
  
  public Collection<String> getSplittedIdNames() {
    return splittedIdNames;
  }

  private void isLogicalColumn(StringBuilder result) {
    if (getMincard().equals("1")) {
      result.append("<td>M</td>");
    } else {
      result.append("<td>&nbsp</td>");
    }    
  }
  
  private void isRestructuredSimpleAttribute(StringBuilder result) {
    if (cardColumn) {
      if (getMaxcard().equals("1")) {
        if (getMincard().equals("1")) {
          result.append("<td>M</td>");
        } else {
          result.append("<td>&nbsp</td>");
        }
      } else {
        result.append("<td>(").append(getMincard()).append(",").append(getMaxcard()).append(")</td>");
      }
    }
    if (idColumn) {
      if (splittedIdNames.isEmpty()) {
        result.append("<td>&nbsp;</td>");
      } else {
        result.append("<td>");
        for (String s : splittedIdNames) {
          if ((pID != null) && pID.equals(s)) {
            result.append("<b>" + s + "</b>, ");
          } else {
            result.append(s + ", ");
          }
        }
        result.setLength(result.length() - 2);
        result.append("</td>");
      }
    }
  }

  private void isConceptualSimpleAttribute(StringBuilder result) {
    result.append("<td>(").append(getMincard()).append(",").append(getMaxcard()).append(")</td>");
    Set<String> newIdNames = new TreeSet();
    if (getIds() != null) {
      for (String id : getIds().keySet()) {
        if (getIds().get(id).contains(internalName)) {
          newIdNames.add(id);
        }
      }
    }
    if (!newIdNames.isEmpty()) {
      result.append("<td>")
              .append(newIdNames.toString().replace("[", "").replace("]", ""))
              .append("</td>");
    } else if (idColumn) {
      result.append("<td>&nbsp;</td>");
    }
    result.append("</tr>");
  }
  
  public boolean isSolved() {
    return solved;
  }

  public void setSolved(boolean solved) {
    this.solved = solved;
  }
  
  public Set<String> getAddedObjects() {
    return addedObjects;
  }

}
