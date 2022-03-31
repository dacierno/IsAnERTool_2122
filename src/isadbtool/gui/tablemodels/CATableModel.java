package isadbtool.gui.tablemodels;

import isadbtool.blogic.conceptual.ConceptualComposedAttribute;
import isadbtool.blogic.conceptual.ConceptualEntity;
import isadbtool.blogic.conceptual.ConceptualRelationship;
import isadbtool.blogic.conceptual.ConceptualSimpleAttribute;
import isadbtool.gui.IsAnERToolGUI;
import isadbtool.gui.MyException;
import isadbtool.gui.RunTimeParams;
import isadbtool.gui.dlg.CATableDLG;
import java.util.Map;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class CATableModel extends DefaultTableModel {
  CATableDLG theParent;
  Map<String,SATableModel> theModels;
  JTable saTable;
  JTable caTable;
  JTable saINcaTable;

  public CATableModel(CATableDLG theParent, ConceptualRelationship theCR, 
          JTable saTable, JTable caTable, JTable saINcaTable, 
          Map<String,SATableModel> theModels) {
    super(new Object[][]{}, new String[]{"IN", "Name", "Cardinality"});
    this.saTable = saTable;
    this.caTable = caTable;
    this.saINcaTable = saINcaTable;
    this.theParent = theParent;
    this.theModels = theModels;
    
    JComboBox comboBoxCard = new JComboBox();
    comboBoxCard.addItem("(0,1)");
    if (theCR instanceof ConceptualEntity) {
      comboBoxCard.addItem("(0,N)");
      comboBoxCard.addItem("(1,N)");
    }
    caTable.setModel(this);
    caTable.getColumnModel()
            .getColumn(2)
            .setCellEditor(new DefaultCellEditor(comboBoxCard));
    
    JComboBox comboBoxType = new JComboBox();
    for (String s : RunTimeParams.supportedTypes()) {
      comboBoxType.addItem(s);
    }
    java.awt.Font theFont = new java.awt.Font("\"Lucida Grande",  java.awt.Font.BOLD, 12);
    int h = theParent.getGraphics().getFontMetrics(theFont).getHeight();
    caTable.setRowHeight(h+h/4);
    caTable.getTableHeader().setFont(theFont);
    caTable.setFont(theFont.deriveFont(java.awt.Font.PLAIN));
    
    
    for (ConceptualComposedAttribute ca : theCR.getConceptualComposedAttributes()) {
      addRow(new Object[] {
        ca.getInternalName(),
        ca.getDisplayedName(),
        "("+ca.getMincard()+","+ca.getMaxcard()+")"});
      for (ConceptualSimpleAttribute sa : ca.getTheSimpleAttributes()) {
        theModels.get(ca.getDisplayedName()).addRow(new Object[] {
          sa.getInternalName(),
          sa.getDisplayedName(),
          sa.getType(),
          sa.getLength(),
          sa.getPrecision(),
          sa.getScale(),
          "("+sa.getMincard()+","+sa.getMaxcard()+")",""});
      }
    }
    
    caTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && caTable.getSelectedRow() != -1) {
          System.out.println("Hola");
          TableUtilities.stopEditing(new JTable[] {saTable,caTable,saINcaTable});
          saINcaTable.setModel(theModels.get((String) caTable.getValueAt(caTable.getSelectedRow(), 1)));
          saINcaTable.getColumnModel().getColumn(2)
                  .setCellEditor(new DefaultCellEditor(comboBoxType));
          TableUtilities.hideColumn(saINcaTable, 0);
          TableUtilities.hideColumn(saINcaTable, 6);
          TableUtilities.hideColumn(saINcaTable, 7);
         }
      }
    });
    
  }
  
  public ConceptualComposedAttribute getCA (int row, IsAnERToolGUI theGUI) throws MyException {
    if (getValueAt(row, 0) != null) {
      return new ConceptualComposedAttribute(
              (String) getValueAt(row, 1),
              (String) getValueAt(row, 0),
              ((String) getValueAt(row, 2)).substring(1, 2),
              ((String)getValueAt(row, 2)).substring(3, 4));          
    } else if (theGUI == null) {
      return new ConceptualComposedAttribute(
              (String) getValueAt(row, 1),
              "A",
              ((String) getValueAt(row, 2)).substring(1, 2),
              ((String) getValueAt(row, 2)).substring(3, 4));          
    } else {
      return new ConceptualComposedAttribute(
              (String) getValueAt(row, 1),
              theGUI.getConceptualArea().getTheSchema().getNewObjectInternalName(),
              ((String) getValueAt(row, 2)).substring(1, 2),
              ((String) getValueAt(row, 2)).substring(3, 4));          
    }    
  }
  
  @Override
  public void addRow(Object[] data) {
    super.addRow(data);
    theModels.put((String) data[1], new SATableModel(theParent, saINcaTable));
    theParent.addAttributeName((String) data[1]);
    caTable.setRowSelectionInterval(getRowCount() - 1, getRowCount() - 1);
  }
  

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Object res = super.getValueAt(rowIndex, columnIndex);
    if (res == null) return null;
    if (res instanceof String) {
      if (((String) res).trim().length() == 0) 
        return null;
      return ((String) res).trim();
    }
    if (res instanceof Integer) return ((Integer) res).toString();
    return res;
  }
  
  @Override
  public boolean isCellEditable(int row, int column) {
    if (column == 0) return false;
    //if (column == 1) return false;
    return true;
  }
  
}
