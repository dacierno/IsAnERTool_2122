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
import java.util.Map;
import java.util.TreeMap;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

public class CEntityPUM extends CEntityRelationshipPUM {
  ConceptualEntity theCE;
  
  public CEntityPUM(DraggableObj theObj, IsAnERToolGUI theGUI) {
    super(theObj, theGUI);
    theCE = (ConceptualEntity) theObj.getBLObject();
    add(new JMenuItem(new AbstractAction("Connect to a relationship") {
      @Override
      public void actionPerformed(ActionEvent ae) {
        try {
          connectToARelationship();
        } catch (MyException ex) {
          theGUI.catchMyException(ex);
        }
      }
    }));
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
    DraggableObj theGUIEntity = new DraggableObj(
            ((ConceptualSchema) theGUI.getConceptualArea().getTheSchema())
                    .newEntity(theCE.getDisplayedName(), theCE.getDisplayedName()),
            new Point(10, 10),
            theGUI);
    duplicateOBJ((ConceptualRelationship) theGUIEntity.getBLObject());
    theGUIEntity.setContent();
    theGUI.getConceptualArea().addAnObject(theGUIEntity);
  }

  public void connectToAnEntity() throws MyException {
    if (((ConceptualSchema) theGUI.getConceptualArea()
            .getTheSchema()).getTheConceptualEntities().isEmpty()) {
      theGUI.catchMyException(new MyException("No entities available"));
      return;
    }
    Map<String, ConceptualEntity> temp = new TreeMap();
    for (ConceptualEntity e : ((ConceptualSchema) theGUI.getConceptualArea()
            .getTheSchema()).getTheConceptualEntities()) {
      temp.put(e.getInternalName(), e);
    }
    ObjectSelectorDLG s = new ObjectSelectorDLG(theGUI,
            temp.values(),
            "Connect " + theObj.getBLObject().getDisplayedName());
    if (!s.isOkExitCode()) {
      return;
    }
    DraggableRelationship addedRel;
    ConceptualEntity theOtherOne;
    if (!s.getSelectedInternalName().equals(theObj.getBLObject().getInternalName())) {
      int x = (getX() + theGUI.getConceptualArea().getDrawPanel().getComponent(s.getSelectedInternalName()).getX()) / 2;
      int y = (getY() + theGUI.getConceptualArea().getDrawPanel().getComponent(s.getSelectedInternalName()).getY()) / 2;
      addedRel = new DraggableRelationship(((ConceptualSchema) theGUI.getConceptualArea().getTheSchema()).newRelationship(theGUI.theBL.getRelDefPrefix(), theGUI.theBL.getRelDefPrefix()),
              new Point(10, 10), theGUI);
      theOtherOne = temp.get(s.getSelectedInternalName());
    } else {
      addedRel = new DraggableRelationship(((ConceptualSchema) theGUI.getConceptualArea().getTheSchema()).newRelationship(theGUI.theBL.getRelDefPrefix(), theGUI.theBL.getRelDefPrefix()),
              new Point(10, 10), theGUI);
      theOtherOne = theCE;
    }
    DraggableRelLink rl1 = new DraggableRelLink(new Point(0, 0),
            ((ConceptualSchema) theGUI.getConceptualArea().getTheSchema()).newRelLink(theCE.getInternalName(), addedRel.getBLObject().getInternalName()),
            theGUI);
    DraggableRelLink rl2 = new DraggableRelLink(new Point(0, 0),
            ((ConceptualSchema) theGUI.getConceptualArea().getTheSchema()).newRelLink(theOtherOne.getInternalName(), addedRel.getBLObject().getInternalName()),
            theGUI);
    theGUI.setToBeSaved(true);
    addedRel.setContent();
    theGUI.getConceptualArea().addAnObject(addedRel);
    theGUI.getConceptualArea().addAnObject(rl1);
    theGUI.getConceptualArea().addAnObject(rl2);
  }

  public void connectToARelationship() throws MyException {
    if (((ConceptualSchema) theGUI.getConceptualArea().getTheSchema())
            .getTheConceptualRelationships().isEmpty()) {
      theGUI.catchMyException(new MyException("No relationships available"));
      return;
    }
    ObjectSelectorDLG s = new ObjectSelectorDLG(theGUI,
            ((ConceptualSchema) theGUI.getConceptualArea().getTheSchema())
                    .getTheConceptualRelationships(),
            "Connect " + theObj.getBLObject().getDisplayedName());
    if (!s.isOkExitCode()) {
      return;
    }
    DraggableRelLink rl = new DraggableRelLink(
            new Point(10, 10),
            ((ConceptualSchema) theGUI.getConceptualArea().getTheSchema())
                    .newRelLink(theObj.getBLObject().getInternalName(), s.getSelectedInternalName()),
            theGUI);
    theGUI.setToBeSaved(true);
    theGUI.getConceptualArea().addAnObject(rl);
  }

  public void removeOBJ() throws MyException {
    String toBeRemoved = theCE.getInternalName();
    for (ConceptualGeneralization g
            : ((ConceptualSchema) theGUI.getConceptualArea().getTheSchema())
                    .getTheConceptualGeneralizations()) {
      if (g.getFather().getInternalName().equals(toBeRemoved)) {
        throw new MyException("There are "
                + "generalizations involving " + theCE.getDisplayedName());
      }
      for (ConceptualEntity ce : g.getTheChilds()) {
        if (ce.getInternalName().equals(toBeRemoved)) {
          throw new MyException("There are "
                  + "generalizations involving " + theCE.getDisplayedName());
        }
      }
    }
    if (!theCE.getConnectedObjects().isEmpty()) {
      throw new MyException("There are "
              + "entities connected to " + theCE.getDisplayedName());
    }
    theGUI.getConceptualArea().removeAnObject(theObj);
  }

}
