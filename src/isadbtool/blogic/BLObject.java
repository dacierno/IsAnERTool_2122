package isadbtool.blogic;

import isadbtool.gui.MyException;
import isadbtool.gui.UtilsGUI;
import isadbtool.gui.UtilsHTML;
import java.io.Serializable;
import org.w3c.dom.Node;

public abstract class BLObject implements Serializable {
  private static final long serialVersionUID = 1L;
  protected StringBuilder result;
  protected String internalName;
  protected String displayedName;
  protected String sourceObjInternalName;

  public abstract String toXMLString(String startTab, String baseTab);
  public abstract void toHTMLStringBuilder();

  public BLObject(String newDisplayedName, String newInternalName,
          String sourceObjInternalName) throws MyException {
    if ((newDisplayedName == null) || (newDisplayedName.trim().length() == 0)) {
      throw new MyException("Invalid displayed object name");
    }
    if ((newInternalName == null) || (newInternalName.trim().length() == 0)) {
      throw new MyException("Invalid internal object name");
    }
    this.displayedName = newDisplayedName.trim();
    this.internalName = newInternalName.trim();
    this.sourceObjInternalName = null;
    if ((sourceObjInternalName != null) && (internalName.length() >= 0)) {
      this.sourceObjInternalName = sourceObjInternalName;
    }
    result = new StringBuilder();
  }

  protected final void HtmlTableHead(int colNumber) {
    result.setLength(0);
    UtilsHTML.getTableStyle(result);
    result.append("<table class=\"grid\" >");
    result.append("<th colspan=\"").append(colNumber).append("\" class=\"grid\">");
    //if ((sourceObjInternalName != null) && !sourceObjInternalName.equals(internalName))
    //  result.append("s"+sourceObjInternalName + " - ");
    //result.append("i"+internalName).append(" - ");
    result.append(displayedName);
    result.append("</th>");
  }
  
  public BLObject(Node theNode) throws MyException {
    this(BLUtils.readXMLElement(theNode, "displayedName"),
            BLUtils.readXMLElement(theNode, "internalName"),
            BLUtils.readXMLElement(theNode, "sourceObjInternalName"));
    result = new StringBuilder();
  }

  public final void setDisplayedName(String displayedName) {
    this.displayedName = displayedName;
  }

  public final String getDisplayedName() {
    return displayedName;
  }

  public String getInternalName() {
    return internalName;
  }
  public String getSourceObjInternalName() {
    return sourceObjInternalName;
  }
  
  public StringBuilder getResult() {
    return result;
  }

}
