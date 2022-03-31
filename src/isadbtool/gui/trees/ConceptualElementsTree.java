package isadbtool.gui.trees;

import isadbtool.blogic.conceptual.ConceptualSchema;
import isadbtool.gui.IsAnERToolGUI;
import isadbtool.gui.drawobjs.DraggableObj;
import isadbtool.gui.MyException;
import isadbtool.gui.dlg.GeneralizationDLG;
import isadbtool.gui.drawobjs.DraggableGeneralization;
import isadbtool.gui.drawobjs.DraggableRelationship;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;

class ConceptualTreeCellRenderer extends DefaultTreeCellRenderer {
  IsAnERToolGUI theGUI;
  public ConceptualTreeCellRenderer(IsAnERToolGUI theGUI) {
    this.theGUI = theGUI;
  }

  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value,
          boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
    super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);
    setFont(new Font("TimesRoman", Font.BOLD, 18));
    String node = (String) ((DefaultMutableTreeNode) value).getUserObject();
    setForeground(theGUI.rtp.getConceptualColor());
    return this;
  }
}

public class ConceptualElementsTree extends ElementsTree {

  public ConceptualElementsTree(IsAnERToolGUI theGUI) {
    super(theGUI);
    firstLevelNodes.put("entities", new DefaultMutableTreeNode("Entities", true));
    firstLevelNodes.put("relationships", new DefaultMutableTreeNode("Relationships", true));
    firstLevelNodes.put("generalizations", new DefaultMutableTreeNode("Generalizations", true));
    for (DefaultMutableTreeNode n : firstLevelNodes.values()) {
      root.add(n);
    }
    this.setCellRenderer(new ConceptualTreeCellRenderer(theGUI));
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (!SwingUtilities.isRightMouseButton(e)) {
      return;
    }
    int row = getClosestRowForLocation(e.getX(), e.getY());
    setSelectionRow(row);
    popup.removeAll();
    TreeNode tmp = (TreeNode) getLastSelectedPathComponent();
    if (tmp == root) {
      popup.add(new AbstractAction("Change Project Name") {
        @Override
        public void actionPerformed(ActionEvent e) {
          theGUI.changeProjectName();
        }
      });
    } else if (tmp == firstLevelNodes.get("entities")) {
      popup.add(new AbstractAction("New Entity") {
        @Override
        public void actionPerformed(ActionEvent e) {
          try {
            String m = JOptionPane.showInputDialog(theGUI,
                    "New entity name",
                    ((ConceptualSchema)theGUI.getConceptualArea().getTheSchema())
                            .getNewObjectDisplayedName(theGUI.theBL.getEntDefPrefix(), theGUI.theBL.getEntDefPrefix()));
            if (m == null) return;
            if (m.trim().length() == 0) return;
            if (theGUI.getConceptualArea().getTheSchema().objDisplayedNameExists(m.trim())) {
              theGUI.catchMyException(new MyException("An objects named " + m.trim() +" already exists"));
              return;
            }
            
            DraggableObj theGUIEntity = new DraggableObj(
                    ((ConceptualSchema) theGUI.getConceptualArea().getTheSchema()).newEntity(m.trim(), theGUI.theBL.getEntDefPrefix()),
                    new Point(10, 10), theGUI);
            theGUIEntity.setContent();
            theGUI.getConceptualArea().addAnObject(theGUIEntity);
          } catch (MyException ex) {
            theGUI.catchMyException(ex);
          }
        }
      });
    } else if (tmp == firstLevelNodes.get("relationships")) {
      popup.add(new AbstractAction("New Relationship") {
        @Override
        public void actionPerformed(ActionEvent e) {
          try {
            String m = JOptionPane.showInputDialog(theGUI,
                    "New entity name",
                    ((ConceptualSchema) theGUI.getConceptualArea().getTheSchema())
                            .getNewObjectDisplayedName(theGUI.theBL.getRelDefPrefix(), theGUI.theBL.getRelDefPrefix()));
            if (m == null) return;
            if (m.trim().length() == 0) return;
            if (theGUI.getConceptualArea().getTheSchema().objDisplayedNameExists(m.trim())) {
              theGUI.catchMyException(new MyException("An objects named " + m.trim() +" already exists"));
              return;
            }
            
            DraggableRelationship theGUIRelationship = new DraggableRelationship(
                    ((ConceptualSchema) theGUI.getConceptualArea().getTheSchema()).newRelationship(m.trim(), theGUI.theBL.getRelDefPrefix()),
                    new Point(10, 10), theGUI);
            theGUIRelationship.setContent();
            theGUI.getConceptualArea().addAnObject(theGUIRelationship);
          } catch (MyException ex) {
            theGUI.catchMyException(ex);
          }
        }
      });
    } else if (tmp == firstLevelNodes.get("generalizations")) {
      popup.add(new AbstractAction("New Generalization") {
        @Override
        public void actionPerformed(ActionEvent e) {
          GeneralizationDLG g = new GeneralizationDLG(theGUI,
                  ((ConceptualSchema) theGUI.getConceptualArea().getTheSchema())
                          .getNewObjectDisplayedName(theGUI.theBL.getGenDefPrefix(), theGUI.theBL.getGenDefPrefix()),
                  ((ConceptualSchema) theGUI.getConceptualArea().getTheSchema())
                          .getTheConceptualEntities());
          g.setVisible(true);
          if (!g.isOkExitCode()) {
            return;
          }
          try {
            DraggableGeneralization theGUIGeneralization = new DraggableGeneralization(
                    new Point(0, 0),
                    ((ConceptualSchema) theGUI.getConceptualArea().getTheSchema()).newGeneralization(
                            g.getGeneralizationName(), g.getGeneralizingEntityInternalName()),
                      theGUI);
            theGUIGeneralization.setContent();
            theGUI.getConceptualArea().addAnObject(theGUIGeneralization);
          } catch (MyException ex) {
            theGUI.catchMyException(ex);
          }
        }
      });
    } else if (tmp.getParent().getParent() == root) {
      popup.add(new AbstractAction("Search") {
        @Override
        public void actionPerformed(ActionEvent e) {
          theGUI.getConceptualArea().search(tmp.toString());
        }
      });
    }
    popup.show(e.getComponent(), e.getX(), e.getY());
  }

}
