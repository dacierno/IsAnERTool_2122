package isadbtool.gui.popupmenus;

import isadbtool.blogic.conceptual.ConceptualEntity;
import isadbtool.blogic.conceptual.ConceptualGeneralization;
import isadbtool.blogic.conceptual.ConceptualRelationship;
import isadbtool.blogic.conceptual.ConceptualSchema;
import isadbtool.gui.IsAnERToolGUI;
import isadbtool.gui.drawobjs.DraggableObj;
import isadbtool.gui.MyException;
import isadbtool.gui.dlg.ObjectSelectorDLG;
import isadbtool.gui.drawobjs.DraggableRelLink;
import isadbtool.gui.drawobjs.DraggableRelationship;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

public class CRelationshipPUM extends CEntityRelationshipPUM {

  ConceptualRelationship theCR;

  public CRelationshipPUM(DraggableObj theObj, IsAnERToolGUI theGUI) {
    super(theObj, theGUI);
    this.theObj = theObj;
    theCR = (ConceptualRelationship) theObj.getBLObject();
    add(new JMenuItem(new AbstractAction("Connect to an entity") {
      @Override
      public void actionPerformed(ActionEvent ae) {
        try {
          connectToAnEntity();
        } catch (MyException ex) {
          theGUI.catchMyException(ex);
        }
      }
    }));
    add(new JMenuItem(new AbstractAction("Duplicate") {
      @Override
      public void actionPerformed(ActionEvent ae) {
        try {
          duplicateOBJ();
        } catch (MyException ex) {
          theGUI.catchMyException(ex);
        }
      }
    }));
    add(new JMenuItem(new AbstractAction("Remove") {
      @Override
      public void actionPerformed(ActionEvent ae) {
        try {
          removeOBJ();
        } catch (MyException ex) {
          theGUI.catchMyException(ex);
        }
      }
    }));
  }

  public void duplicateOBJ() throws MyException {
    ConceptualSchema theSchema = (ConceptualSchema) theGUI.getConceptualArea().getTheSchema();
    DraggableRelationship theGUIRelationship = new DraggableRelationship(theSchema
                    .newRelationship(theCR.getDisplayedName(), theCR.getDisplayedName()),
            new Point(10, 10),
            theGUI);
    duplicateOBJ((ConceptualRelationship) theGUIRelationship.getBLObject());
    theGUIRelationship.setContent();
    theGUI.getConceptualArea().addAnObject(theGUIRelationship);
  }

  public void connectToAnEntity() throws MyException {
    if (((ConceptualSchema) theGUI.getConceptualArea().getTheSchema())
            .getTheConceptualEntities()
            .isEmpty()) {
      theGUI.catchMyException(new MyException("No entities available"));
      return;
    }
    ObjectSelectorDLG s = new ObjectSelectorDLG(theGUI,
            ((ConceptualSchema) theGUI.getConceptualArea().getTheSchema())
                    .getTheConceptualEntities(),
            "Connect " + theObj.getBLObject().getDisplayedName());
    if (!s.isOkExitCode()) return;
    DraggableRelLink rl = new DraggableRelLink(
            new Point(10, 10),
            ((ConceptualSchema) theGUI.getConceptualArea().getTheSchema())
                    .newRelLink(s.getSelectedInternalName(),theObj.getBLObject().getInternalName()),
            theGUI);
    theGUI.getConceptualArea().addAnObject(rl);
  }

  public void removeOBJ() throws MyException {
    String toBeRemoved = theCR.getInternalName();
    for (ConceptualGeneralization g : 
            ((ConceptualSchema) theGUI.getConceptualArea().getTheSchema())
            .getTheConceptualGeneralizations()) {
      if (g.getFather().getInternalName().equals(toBeRemoved)) {
        throw new MyException("There are "
                + "generalizations involving " + theCR.getDisplayedName());
      }
      for (ConceptualEntity ce : g.getTheChilds()) {
        if (ce.getInternalName().equals(toBeRemoved)) {
          throw new MyException("There are "
                  + "generalizations involving " + theCR.getDisplayedName());
        }
      }
    }
    if (!theCR.getConnectedObjects().isEmpty()) {
      throw new MyException("There are "
              + "entities connected to " + theCR.getDisplayedName());
    }
    theGUI.getConceptualArea().removeAnObject(theObj);
  }

}
