package isadbtool.gui.drawobjs;

import isadbtool.gui.notes.NotesPanel;
import isadbtool.blogic.TheSchema;
import isadbtool.blogic.conceptual.ConceptualGeneralization;
import isadbtool.blogic.conceptual.ConceptualSchema;
import isadbtool.gui.IsAnERToolGUI;
import isadbtool.gui.MyException;
import isadbtool.gui.trees.ConceptualElementsTree;
import isadbtool.gui.trees.ElementsTree;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class AbstractArea extends JPanel {
  protected NotesPanel notesPanel;
  private ZoomPanel zoomPanel;
  protected ElementsTree elementsTree;
  protected IsAnERToolGUI theGUI;
  private String Type;
  public JSplitPane verticalSplitPane, horizontalSplitPane;
  
  protected final void initComponents() {
    this.setLayout(new BorderLayout());
    verticalSplitPane = new JSplitPane();
    horizontalSplitPane = new JSplitPane();
    verticalSplitPane.setBackground(new java.awt.Color(255, 255, 153));
    verticalSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
    horizontalSplitPane.setBackground(new java.awt.Color(255, 255, 153));
    JScrollPane qPane = new JScrollPane(elementsTree,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    horizontalSplitPane.setRightComponent(verticalSplitPane);
    horizontalSplitPane.setLeftComponent(qPane);
    verticalSplitPane.setTopComponent(zoomPanel);
    verticalSplitPane.setBottomComponent(notesPanel);
    add(horizontalSplitPane, BorderLayout.CENTER);
  }

  public AbstractArea(IsAnERToolGUI theGUI, String Type) {
    this.theGUI = theGUI;
    this.Type = Type;
    if (Type.equals("Conceptual")) {
      zoomPanel = new ZoomPanel(new DrawPanel("Conceptual",theGUI));
      elementsTree = new ConceptualElementsTree(theGUI);
    } 
    notesPanel = new NotesPanel(theGUI);
    initComponents();
    elementsTree.expandAll();
  }
  
  public void readFromSchema(TheSchema theSchema, 
          Map<String,Point> oldPositions,
          Map<String,Point> currentPositions) throws MyException {
    zoomPanel.drawPanel.removeAll();
    zoomPanel.drawPanel.readFromSchema(theSchema, oldPositions,currentPositions);
    zoomPanel.drawPanel.repaint();    
    StringBuilder temp = new StringBuilder();
    for (String s : theSchema.getNotes()) {
      temp.append(s).append("\n");
    }
    if (temp.length() > 0) temp.setLength(temp.length()-1);
    notesPanel.getTextPane().setText(temp.toString());
    elementsTree.clearTree();
    for (Component c : zoomPanel.drawPanel.getComponents()) {
      if (c instanceof DraggableObj) {
        elementsTree.addAnObject((DraggableObj) c);
      }
    }
    elementsTree.expandAll();    
  }
  
  public void updateConceptualgeneralizations() {
    for (ConceptualGeneralization o :  
            ((ConceptualSchema) zoomPanel.drawPanel.theSchema)
            .getTheConceptualGeneralizations()) {
      zoomPanel.drawPanel.getComponent(o.getInternalName()).setContent();
    }
  }
  
  public void addAnObject(DraggableObj theOBJ) {
    theGUI.setToBeSaved(true);
    zoomPanel.drawPanel.addAnObject(theOBJ);
    elementsTree.addAnObject(theOBJ);
    zoomPanel.drawPanel.repaint();
  }
  
  public void removeAnObject(DraggableObj theOBJ) {
    theGUI.setToBeSaved(true);
    elementsTree.removeAnObject(theOBJ);
    zoomPanel.drawPanel.remove(theOBJ);
    zoomPanel.drawPanel.theSchema.removeAnObject(theOBJ.BLObject);
    zoomPanel.drawPanel.repaint();
  }
  
  public void updateANode(String oldName, DraggableObj newObject) {
    theGUI.setToBeSaved(true);
    elementsTree.updateANode(oldName, newObject);
  }
  
  public Map<String, Point> getAllPositions() {
    Map<String, Point> result = new LinkedHashMap();
    for (Component c : zoomPanel.drawPanel.getComponents()) {
      if (c instanceof DraggableObj) {
        result.put(((DraggableObj) c).getBLObject().getInternalName(),
                c.getLocation());
      }
    }
    return result;
  }    

  public TheSchema getTheSchema() {
    return zoomPanel.drawPanel.theSchema;
  }

  public void setSlider(int value) {
    zoomPanel.zoomSlider.setValue(value);
  }
  
  public int getSlider() {
    return zoomPanel.zoomSlider.getValue();
  }

  public DrawPanel getDrawPanel() {
    return zoomPanel.drawPanel;
  }
  
  public void search(String displayedName) {
    zoomPanel.drawPanel.search(displayedName);  
  }  

  public NotesPanel getNotesPanel() {
    return notesPanel;
  }

  public void clearProject() {
    if (Type.equalsIgnoreCase("Conceptual"))
      zoomPanel.drawPanel.theSchema = new ConceptualSchema();
    else {
        theGUI.catchMyException(new MyException("Illegal Type in drawPanel"));
        return;
    }
    zoomPanel.drawPanel.removeAll();
    notesPanel.getTextPane().setText("");
    elementsTree.clearTree();
  }

  public void setRootLabel(String rootName) {
    elementsTree.setRootLabel(rootName);
  }
  
  public void exportProjectAsImage(String fileName) {
    zoomPanel.drawPanel.exportAsImage(fileName);
  }
  
  
}
