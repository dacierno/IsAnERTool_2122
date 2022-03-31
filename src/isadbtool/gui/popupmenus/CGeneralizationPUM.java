package isadbtool.gui.popupmenus;

import isadbtool.blogic.conceptual.ConceptualEntity;
import isadbtool.blogic.conceptual.ConceptualGeneralization;
import isadbtool.blogic.conceptual.ConceptualSchema;
import isadbtool.gui.IsAnERToolGUI;
import isadbtool.gui.dlg.ObjectSelectorDLG;
import isadbtool.gui.drawobjs.DraggableObj;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class CGeneralizationPUM extends JPopupMenu implements PopupMenuListener {

  JMenuItem changeGeneralizationName, totalPartial;
  JMenuItem exclusiveOverlapped;
  JMenuItem addChildEntity, removeChildEntity;
  JMenuItem removeTheGeneralization;
  ConceptualGeneralization theCG;
  IsAnERToolGUI theGUI;  

  public CGeneralizationPUM(DraggableObj theObj, IsAnERToolGUI theGUI) {
    theCG = (ConceptualGeneralization) theObj.getBLObject();
    this.theGUI = theGUI;
    changeGeneralizationName = add(new AbstractAction("Rename "
            + theCG.getDisplayedName()) {
      @Override
      public void actionPerformed(ActionEvent e) {
        String oldName = theCG.getDisplayedName();
        String m = JOptionPane.showInputDialog(theGUI,
                theCG.getDisplayedName(), "New name");
        if ((m != null) && (m.trim().length() > 0)) {
          if (((ConceptualSchema) theGUI.getConceptualArea().getTheSchema())
                  .objectDisplayedNameExists(m)) {
            JOptionPane.showConfirmDialog(theGUI,
                    "An object named " + m + " already exists", "alert", JOptionPane.CLOSED_OPTION);
            return;
          }
          theCG.setDisplayedName(m);
          theGUI.getConceptualArea().updateANode(oldName, theObj);
        } else {
          JOptionPane.showConfirmDialog(theGUI, "Generalization name cannot be empty", "alert", JOptionPane.CLOSED_OPTION);
          return;
        }
      }
    });
    totalPartial = add(new AbstractAction("Make total-partial") {
      @Override
      public void actionPerformed(ActionEvent e) {
        theCG.setTotal(!theCG.isTotal());
        theObj.setContent();
      }
    });
    exclusiveOverlapped = add(new AbstractAction("Make exclusive-overlapped") {
      @Override
      public void actionPerformed(ActionEvent e) {
       theCG.setExclusive(!theCG.isExclusive());
       theObj.setContent();
      }
    });
    addChildEntity = add(new AbstractAction("Add specialized entity") {
      @Override
      public void actionPerformed(ActionEvent e) {        
        Map<String,ConceptualEntity> tempCE = new TreeMap();
        for (ConceptualEntity c : 
                ((ConceptualSchema) theGUI.getConceptualArea().getTheSchema()).getTheConceptualEntities()) {
          tempCE.put(c.getInternalName(), c);
        }
        tempCE.remove(theCG.getFather().getInternalName());
        for (ConceptualEntity c : theCG.getTheChilds()) {
          tempCE.remove(c.getInternalName());
        }
        if (tempCE.isEmpty()) return;
        ObjectSelectorDLG s = new ObjectSelectorDLG(theGUI, tempCE.values(), 
                "Add specialized entity");
        if (!s.isOkExitCode()) return;
        if (s.getSelectedInternalName() == null) return;
        theCG.addAChild(tempCE.get(s.getSelectedInternalName()));
        theGUI.getConceptualArea().getDrawPanel().repaint();
      }
    });
    removeChildEntity = add(new AbstractAction("Remove specialized entity") {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (theCG.getTheChilds().isEmpty()) {
          return;
        }
        ObjectSelectorDLG s = new ObjectSelectorDLG(theGUI,
                theCG.getTheChilds(), "Remove specialized entity");
        if (!s.isOkExitCode()) {
          return;
        }
        if (s.getSelectedInternalName() == null) {
          return;
        }
        theCG.removeAChild(s.getSelectedInternalName());
        theObj.setContent();
        theGUI.getConceptualArea().getDrawPanel().repaint();
      }
    });
    removeTheGeneralization = add(new AbstractAction("Remove this generalization") {
      @Override
      public void actionPerformed(ActionEvent e) {
        theGUI.getConceptualArea().removeAnObject(theObj);
      }
    });
    this.addPopupMenuListener(this);
  }

  @Override
  public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
    removeAll();
    changeGeneralizationName.setText("Rename " + theCG.getDisplayedName());
    add(changeGeneralizationName);
    if (theCG.isTotal()) {
      totalPartial.setText("Make partial");
      add(totalPartial);
    } else if (theCG.getTheChilds().size() > 1) {
      totalPartial.setText("Make total");
      add(totalPartial);
    }
    if (theGUI.theBL.getOverlappedGeneralization()
            && (theCG.getTheChilds().size() > 1)) {
      if (theCG.isExclusive()) {
        exclusiveOverlapped.setText("Make overlapped");
      } else {
        exclusiveOverlapped.setText("Make exclusive");
      }
      add(exclusiveOverlapped);
    }
    add(addChildEntity);
    if (!theCG.getTheChilds().isEmpty()) {
      add(removeChildEntity);
    }
    add(removeTheGeneralization);
  }

  @Override
  public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
  }

  @Override
  public void popupMenuCanceled(PopupMenuEvent e) {
  }
}
