package isadbtool.gui.trees;

import isadbtool.blogic.conceptual.ConceptualEntity;
import isadbtool.blogic.conceptual.ConceptualGeneralization;
import isadbtool.blogic.conceptual.ConceptualRelationship;
import isadbtool.gui.IsAnERToolGUI;
import isadbtool.gui.drawobjs.DraggableObj;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

public class ElementsTree extends JTree implements MouseListener {

  protected final DefaultMutableTreeNode root;
  protected final Map<String,DefaultMutableTreeNode> firstLevelNodes;
  
  protected final JPopupMenu popup;
  protected IsAnERToolGUI theGUI;

  public ElementsTree(IsAnERToolGUI theGUI) {
    super(new DefaultMutableTreeNode("The project", true));
    this.theGUI = theGUI;
    getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    root = (DefaultMutableTreeNode) getModel().getRoot();
    addMouseListener(this);
    popup = new JPopupMenu();
    firstLevelNodes = new LinkedHashMap();
  }

  public void addAnObject(DraggableObj theOBJ) {
    if (theOBJ.getBLObject() instanceof ConceptualEntity) {
      addANode(firstLevelNodes.get("entities"), theOBJ.asTreeNode());
    } else if (theOBJ.getBLObject() instanceof ConceptualRelationship) {
      addANode(firstLevelNodes.get("relationships"), theOBJ.asTreeNode());
    } else if (theOBJ.getBLObject() instanceof ConceptualGeneralization) {
      addANode(firstLevelNodes.get("generalizations"), theOBJ.asTreeNode());
    } 
  }

  private void addANode(DefaultMutableTreeNode father, DefaultMutableTreeNode child) {
    if (father != null) {
      father.add(child);
      sortANode(father);
      ((DefaultTreeModel) this.getModel()).nodeStructureChanged((TreeNode) father);
    }
  }

  public void removeAnObject(DraggableObj theOBJ) {
    DefaultMutableTreeNode aNode = null;
    if (theOBJ.getBLObject() instanceof ConceptualEntity) {
      aNode = firstLevelNodes.get("entities");
    } else if (theOBJ.getBLObject() instanceof ConceptualRelationship) {
      aNode = firstLevelNodes.get("relationships");
    } else if (theOBJ.getBLObject() instanceof ConceptualGeneralization) {
      aNode = firstLevelNodes.get("generalizations");
    }
    if (aNode != null) {
      for (int i = 0; i < aNode.getChildCount(); i++) {
        if (aNode.getChildAt(i).toString().equals(theOBJ.getBLObject().getDisplayedName())) {
          aNode.remove(i);
        }
      }
      ((DefaultTreeModel) this.getModel()).nodeStructureChanged((TreeNode) aNode);
    }
  }

  public void updateANode(String oldName, DraggableObj theNewObject) {
    if (theNewObject.getBLObject() instanceof ConceptualEntity) {
      updateANode(firstLevelNodes.get("entities"), oldName, theNewObject);
      sortANode(firstLevelNodes.get("entities"));
    } else if (theNewObject.getBLObject() instanceof ConceptualRelationship) {
      updateANode(firstLevelNodes.get("relationships"), oldName, theNewObject);
      sortANode(firstLevelNodes.get("relationships"));
    } else if (theNewObject.getBLObject() instanceof ConceptualGeneralization) {
      updateANode(firstLevelNodes.get("generalizations"), oldName, theNewObject);
      sortANode(firstLevelNodes.get("generalizations"));
    }
  }

  private void updateANode(DefaultMutableTreeNode aNode, String oldName, DraggableObj theNewObject) {
    int toBeRemoved = -1;
    for (int i = 0; i < aNode.getChildCount(); i++) {
      if (aNode.getChildAt(i).toString().equals(oldName)) {
        toBeRemoved = i;
      }
    }
    if (toBeRemoved != -1) {
      aNode.remove(toBeRemoved);
      aNode.add(theNewObject.asTreeNode());
    }
  }

  private void sortANode(DefaultMutableTreeNode dmtn) {
    Map<String, DefaultMutableTreeNode> temp = new TreeMap();
    for (int i = 0; i < dmtn.getChildCount(); i++) {
      temp.put(dmtn.getChildAt(i).toString(), (DefaultMutableTreeNode) dmtn.getChildAt(i));
    }
    dmtn.removeAllChildren();
    for (String s : temp.keySet()) {
      dmtn.add(temp.get(s));
    }
    ((DefaultTreeModel) this.getModel()).nodeStructureChanged((TreeNode) dmtn);
  }

  public void clearTree() {
    for (int i = 0; i < root.getChildCount(); i++) {
      ((DefaultMutableTreeNode) root.getChildAt(i)).removeAllChildren();
      ((DefaultTreeModel) this.getModel()).nodeStructureChanged(root.getChildAt(i));
    }
  }

  public void expandAll() {
    for (int i = 0; i < getRowCount(); i++) {
      collapseRow(i);
    }
    expandRow(0);
    for (int i = 0; i < getRowCount(); i++) {
      if (getPathForRow(i).getLastPathComponent().toString().equals("Entities")) {
        expandRow(i);
      }
      if (getPathForRow(i).getLastPathComponent().toString().equals("Relationships")) {
        expandRow(i);
      }
      if (getPathForRow(i).getLastPathComponent().toString().equals("Generalizations")) {
        expandRow(i);
      }
    }
  }

  public void setRootLabel(String rootName) {
    root.setUserObject(rootName);
    ((DefaultTreeModel) this.getModel()).nodeStructureChanged(root);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }
}
