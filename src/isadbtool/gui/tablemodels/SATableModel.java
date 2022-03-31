package isadbtool.gui.tablemodels;

import isadbtool.blogic.conceptual.ConceptualEntity;
import isadbtool.blogic.conceptual.ConceptualRelationship;
import isadbtool.blogic.conceptual.ConceptualSimpleAttribute;
import isadbtool.gui.IsAnERToolGUI;
import isadbtool.gui.MyException;
import isadbtool.gui.RunTimeParams;
import isadbtool.gui.dlg.CATableDLG;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class SATableModel extends DefaultTableModel {
  JTable theTable;
  CATableDLG theParent;

  public SATableModel(CATableDLG theParent, JTable theTable) {
    super(new Object[][]{}, new String[]{"IN", "Name",
      "Type", "Length", "Precision", "Scale", "Cardinality", "IDs"});
    this.theParent = theParent;
    this.theTable = theTable;
    JComboBox comboBoxType = new JComboBox();
    for (String s : RunTimeParams.supportedTypes()) {
      comboBoxType.addItem(s);
    }
    theTable.setModel(this);
    theTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboBoxType));
    java.awt.Font theFont = new java.awt.Font("\"Lucida Grande", java.awt.Font.BOLD, 12);
    theTable.getTableHeader().setFont(theFont);
    theTable.setFont(theFont.deriveFont(java.awt.Font.PLAIN));
    int h = theParent.getGraphics().getFontMetrics(theFont).getHeight();
    theTable.setRowHeight(h + h / 4);
  }

  public SATableModel(CATableDLG parent, ConceptualRelationship theCR, JTable theTable) {
    this(parent, theTable);
    JComboBox comboBoxCard = new JComboBox();
    comboBoxCard.addItem("(0,1)");
    comboBoxCard.addItem("(1,1)");
    if (theCR instanceof ConceptualEntity) {
      comboBoxCard.addItem("(0,N)");
      comboBoxCard.addItem("(1,N)");
    }
    theTable.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(comboBoxCard));
    java.awt.Font theFont = new java.awt.Font("\"Lucida Grande", java.awt.Font.BOLD, 12);
    theTable.getTableHeader().setFont(theFont);
    theTable.setFont(theFont.deriveFont(java.awt.Font.PLAIN));
    int h = parent.getGraphics().getFontMetrics(theFont).getHeight();
    theTable.setRowHeight(h + h / 4);
    Map<String, TreeSet<String>> theIDs = new TreeMap();
    if (theCR instanceof ConceptualEntity) {
      ConceptualEntity theCE = (ConceptualEntity) theCR;
      for (String id : theCE.getTheIdentifiers().keySet()) {
        for (String a : theCE.getTheIdentifiers().get(id)) {
          if (!theIDs.containsKey(a)) {
            theIDs.put(a, new TreeSet());
          }
          theIDs.get(a).add(id);
        }
      }
    }
    for (ConceptualSimpleAttribute sa : theCR.getConceptualSimpleAttributes()) {
      if (!theIDs.containsKey(sa.getInternalName())) {
        theIDs.put(sa.getInternalName(), new TreeSet());
      }
    }
    for (ConceptualSimpleAttribute sa : theCR.getConceptualSimpleAttributes()) {
      addRow(new Object[]{
        sa.getInternalName(),
        sa.getDisplayedName(),
        sa.getType(),
        sa.getLength(),
        sa.getPrecision(),
        sa.getScale(),
        "(" + sa.getMincard() + "," + sa.getMaxcard() + ")",
        theIDs.get(sa.getInternalName()).toString().replace("[", "").replace("]", "")});
    }
  }

  public ConceptualSimpleAttribute getSA(int row, IsAnERToolGUI theGUI) throws MyException {
    if (getValueAt(row, 0) != null) {
      return new ConceptualSimpleAttribute(
              (String) getValueAt(row, 1),
              (String) getValueAt(row, 0),
              ((String) getValueAt(row, 6)).substring(1, 2),
              ((String) getValueAt(row, 6)).substring(3, 4),
              (String) getValueAt(row, 2),
              (String) getValueAt(row, 3),
              (String) getValueAt(row, 4),
              (String) getValueAt(row, 5));
    } else if (theGUI == null) {
      return new ConceptualSimpleAttribute(
              (String) getValueAt(row, 1),
              "A",
              ((String) getValueAt(row, 6)).substring(1, 2),
              ((String) getValueAt(row, 6)).substring(3, 4),
              (String) getValueAt(row, 2),
              (String) getValueAt(row, 3),
              (String) getValueAt(row, 4),
              (String) getValueAt(row, 5));
    } else {
      return new ConceptualSimpleAttribute(
              (String) getValueAt(row, 1),
              theGUI.getConceptualArea().getTheSchema().getNewObjectInternalName(),
              ((String) getValueAt(row, 6)).substring(1, 2),
              ((String) getValueAt(row, 6)).substring(3, 4),
              (String) getValueAt(row, 2),
              (String) getValueAt(row, 3),
              (String) getValueAt(row, 4),
              (String) getValueAt(row, 5));
    }
  }

  public void setTheSAs(IsAnERToolGUI theGUI, ConceptualRelationship theCR) throws MyException {
    LinkedHashMap<String, ConceptualSimpleAttribute> theSAs = new LinkedHashMap();
    Map<String, Set<String>> theIdentifiers = new TreeMap();    
    for (int i=0; i<getRowCount(); i++) {
      ConceptualSimpleAttribute sa = getSA(i, theGUI);
      theSAs.put(sa.getInternalName(), sa);
      if (theCR instanceof ConceptualEntity) {
        if (getValueAt(i,7) == null) continue;
        String[] theIDs = ((String) getValueAt(i,7)).split(",");
        for (String s : theIDs) {
          if (!theIdentifiers.containsKey(s.trim())) theIdentifiers.put(s.trim(), new LinkedHashSet());
          theIdentifiers.get(s.trim()).add(sa.getInternalName());
        }        
      }
    }    
    theCR.setTheSimpleAttributes(theSAs);
    if (theCR instanceof ConceptualEntity)
      ((ConceptualEntity) theCR).setTheIdentifiers(theIdentifiers);
  }

  @Override
  public void removeRow(int a) {
    theParent.removeAttributeName((String) getValueAt(a,1));
    super.removeRow(a);
    if (theTable.getRowCount() > 0) {
      if (a > (theTable.getRowCount() - 1)) {
        a = theTable.getRowCount() - 1;
      }
      theTable.setRowSelectionInterval(a, a);
    }
  }
  
  @Override
  public void addRow(Object[] data) {
    super.addRow(data);
    theParent.addAttributeName((String) data[1]);
    theTable.setRowSelectionInterval(theTable.getRowCount() - 1, theTable.getRowCount() - 1);
  }
  
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Object res = super.getValueAt(rowIndex, columnIndex);
    if (res == null) {
      return null;
    }
    if (res instanceof String) {
      if (((String) res).trim().length() == 0) {
        return null;
      }
      return ((String) res).trim();
    }
    if (res instanceof Integer) {
      return ((Integer) res).toString();
    }
    return res;
  }
  @Override
  public boolean isCellEditable(int row, int column) {
    if (column == 0) {
      return false;
    }
    return true;
  }
}
