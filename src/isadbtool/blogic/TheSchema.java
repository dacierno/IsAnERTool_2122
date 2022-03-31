package isadbtool.blogic;

import isadbtool.blogic.conceptual.*;
import isadbtool.blogic.logical.*;
import isadbtool.blogic.restructured.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class TheSchema implements Serializable {
  private static final long serialVersionUID = 1L;
  
  protected StringBuilder result;
  protected ArrayList<String> notes;
  protected final Map<String, BLObject> theBLObjects;
  private long longOID;

  public abstract String toXMLString(String startTab, String baseTab);

  public TheSchema() {
    result = new StringBuilder();
    notes = new ArrayList();
    theBLObjects = new LinkedHashMap();
    longOID = 0L;
  }

  public final String getNewObjectInternalName() {
    if (this instanceof ConceptualSchema) return "C" + ++longOID;
    return null;
  }    
  
  public boolean objDisplayedNameExists(String displayedName) {
    for (BLObject o : theBLObjects.values()) {
      if (o.getDisplayedName().equals(displayedName)) return true;
    }
    return false;
  }  

  public ArrayList<String> getNotes() {
    return notes;
  }

  public void addNotes(String theNotes) {
    String[] temp = theNotes.split("\n");
    notes.clear();
    for (int i = 0; i < temp.length; i++) {
      if (temp[i] == null) {
        notes.add("");
      } else {
        notes.add(temp[i]);
      }
    }
  }

  public void removeAnObject(BLObject o) {
    theBLObjects.remove(o.getInternalName());
    if (o instanceof ConceptualRelLink) {
      ((ConceptualRelLink) o).getTheEntity().removeARelLink((ConceptualRelLink) o);
      ((ConceptualRelLink) o).getTheRelationship().removeARelLink((ConceptualRelLink) o);
    }
  }
  
  public void addABLObject(BLObject o) {
    theBLObjects.put(o.getInternalName(), o);
  }
  
  public BLObject getABLObject(String internalName) {
    return theBLObjects.get(internalName);
  }

  protected void BLObjectsToXML(Collection<? extends BLObject> theObjects, String startTab, String baseTab, String nodeName) {
    if (!theObjects.isEmpty()) {
      result.append(startTab).append(startTab).append("<").append(nodeName).append(">\n");
      for (BLObject o : theObjects) {
        result.append(o.toXMLString(startTab + startTab + startTab, baseTab)).append("\n");
      }
      result.append(startTab).append(startTab).append("</").append(nodeName).append(">\n");
    }
  }

  public Collection<BLObject> getTheBLObjects() {
    return theBLObjects.values();
  }
  
  public long getLongOID() {
    return longOID;
  }
  
}
