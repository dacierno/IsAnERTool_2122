package isadbtool.blogic;

import isadbtool.gui.MyException;
import java.io.Serializable;
import org.w3c.dom.Node;

public abstract class AbsGeneralization extends BLObject implements Serializable {
  private static final long serialVersionUID = 1L;
  protected boolean total, exclusive;
  
  public AbsGeneralization(String displayedName, 
          String internalName,
          String sourceObjInternalName) throws MyException {
    super(displayedName, internalName, sourceObjInternalName);
  }
  
  public AbsGeneralization(Node theNode) throws MyException {
    super(theNode);
  }
  
  public boolean isTotal() {
    return total;
  }

  public boolean isExclusive() {
    return exclusive;
  }

  public void setTotal(boolean total) {
    this.total = total;
  }

  public void setExclusive(boolean exclusive) {
    this.exclusive = exclusive;
  }
  
  
  
  
}
