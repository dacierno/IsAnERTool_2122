package isadbtool.gui.popupmenus;

import isadbtool.blogic.conceptual.ConceptualComposedAttribute;
import isadbtool.blogic.conceptual.ConceptualRelationship;
import isadbtool.blogic.conceptual.ConceptualSchema;
import isadbtool.blogic.conceptual.ConceptualSimpleAttribute;
import isadbtool.gui.IsAnERToolGUI;
import isadbtool.gui.drawobjs.DraggableObj;
import isadbtool.gui.MyException;
import isadbtool.gui.dlg.CATableDLG;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

public class CEntityRelationshipPUM extends JPopupMenu {
  protected DraggableObj theObj;
  protected IsAnERToolGUI theGUI;

  public CEntityRelationshipPUM(DraggableObj theObj, IsAnERToolGUI theGUI) {
    this.theObj = theObj;
    this.theGUI = theGUI;
    
    add(new JMenuItem(new AbstractAction("Rename") {
      @Override
      public void actionPerformed(ActionEvent ae) {
        changeObjectName();
      }
    }));
    
    add(new JMenuItem(new AbstractAction("Attributes") {
      @Override
      public void actionPerformed(ActionEvent ae) {
        CATableDLG caTable = new CATableDLG(theGUI, (ConceptualRelationship) theObj.getBLObject());
        caTable.setPreferredSize(new Dimension(200,400));
        caTable.setSize(new Dimension(800,600));
        caTable.setLocationRelativeTo(theGUI);
        caTable.setVisible(true);
        if (caTable.isOkExitCode()) {
          theObj.setContent();
          theGUI.getConceptualArea().repaint();
        }        
      }
    }));        
  }
  
  public void changeObjectName() {
    String oldName = theObj.getBLObject().getDisplayedName();
    String m = JOptionPane.showInputDialog(theGUI,
            theObj.getBLObject().getDisplayedName(), oldName);
    if (m == null)return;
    String newName = m.trim();
    if (newName.length() == 0) return;
    if (oldName.equals(m.trim())) return;
    if ((((ConceptualSchema) theGUI.getConceptualArea().getTheSchema())
            .objectDisplayedNameExists(newName))) {
      JOptionPane.showConfirmDialog(theGUI,
              "An object named " + newName + " already exists", "alert", JOptionPane.CLOSED_OPTION);
      return;
    }
    theObj.getBLObject().setDisplayedName(newName);
    theGUI.getConceptualArea().updateANode(oldName, theObj);
    theObj.setContent();        
    theGUI.getConceptualArea().repaint();
  }

  public void duplicateOBJ(ConceptualRelationship ce) throws MyException {
    for (ConceptualSimpleAttribute a : ((ConceptualRelationship) theObj.getBLObject()).getConceptualSimpleAttributes()) {
      ConceptualSimpleAttribute sa = new ConceptualSimpleAttribute(
              theGUI.getConceptualArea().getTheSchema().getNewObjectInternalName(), a);
      ce.addConceptualSimpleAttribute(sa);
    }
    for (ConceptualComposedAttribute caOLD : ((ConceptualRelationship) theObj.getBLObject()).getConceptualComposedAttributes()) {
      ConceptualComposedAttribute caNEW = new ConceptualComposedAttribute(
              theGUI.getConceptualArea().getTheSchema().getNewObjectInternalName(), caOLD);
      ce.addConceptualComposedAttribute(caNEW);
      for (ConceptualSimpleAttribute a : caOLD.getTheSimpleAttributes()) {
        ConceptualSimpleAttribute sa = new ConceptualSimpleAttribute(
                theGUI.getConceptualArea().getTheSchema().getNewObjectInternalName(), a);
        caNEW.addSimpleAttributes(sa);
      }
    }
  }

}
