package isadbtool.blogic.conceptual;

import isadbtool.blogic.AbsGeneralization;
import java.util.ArrayList;
import java.util.Collection;
import isadbtool.blogic.BLObject;
import isadbtool.blogic.BLUtils;
import isadbtool.gui.MyException;
import java.io.Serializable;
import org.w3c.dom.Node;

public class ConceptualGeneralization extends AbsGeneralization  
        implements Serializable {
  private static final long serialVersionUID = 1L;  
    
  private ConceptualEntity father;
  private ArrayList<ConceptualEntity> theChilds;
  
  
  ConceptualGeneralization(String displayedName, String internalName) throws MyException {
    super(displayedName, internalName, null);
    theChilds = new ArrayList();
    total = false;
    exclusive = false;    
  }
  
  public ConceptualGeneralization(String displayedName, String internalName, ConceptualEntity father) throws MyException {
    this(displayedName, internalName);
    this.father = father;
  }
  
  public ConceptualGeneralization(Node theNode, ConceptualEntity theFather, 
          boolean overlappedGeneralizations) throws MyException {    
    super(theNode);
    this.father = theFather;
    theChilds = new ArrayList();
    total = false;
    exclusive = false;      
    if (BLUtils.readXMLElement(theNode,"total").equals("true")) {
      total = true;
    }
    if (BLUtils.readXMLElement(theNode,"exclusive").equals("true")) {
      exclusive = true;
    }
    if (!overlappedGeneralizations) {
      exclusive = true;
    }    
  }  
  
  public void addAChild(ConceptualEntity aChild) {
    theChilds.add(aChild);   
  }
  
  public void removeAChild(String toBeRemoved) {
    int index =-1;
    for (int i=0; i<theChilds.size();i++) {
      if(theChilds.get(i).getInternalName().equals(toBeRemoved)) {
        index = i;
      }
    }
    theChilds.remove(index);   
    if (theChilds.size() == 1) {
      total = false;
      exclusive = false;
    }
  }
  
  @Override
  public void toHTMLStringBuilder() {
    result.setLength(0);
    result.append("");
  }
  
  @Override
  public String toXMLString(String startTab, String baseTab) {
    result.setLength(0);
    result.append(startTab);
    result.append("<ConceptualGeneralization internalName=\"")
            .append(internalName).append("\" ");
    result.append("displayedName=\"")
            .append(displayedName).append("\" ");
    if (total) result.append("total=\"true\" ");
    else result.append("total=\"false\" ");
    if (exclusive) result.append("exclusive=\"true\" ");
    else result.append("exclusive=\"false\" ");
    result.append("father=\"").append(father.getInternalName()).append("\"");
    if (!theChilds.isEmpty()) {
      result.append(" childs=\"");
      for(ConceptualEntity e : theChilds) {
        result.append(e.getInternalName()).append(", ");      
      }
      result.setLength(result.length()-2);
      result.append("\"");
    }
    result.append("/>");
    return result.toString();
  }  
  

  public ConceptualEntity getFather() {
    return father;
  }

  public ArrayList<ConceptualEntity> getTheChilds() {
    return theChilds;
  }

  public Collection<String> getChildNames() {
    Collection<String> result = new ArrayList();
    for (ConceptualEntity e : getTheChilds()) {
      result.add(e.getInternalName());
    }
    return result;
  }
  
}
