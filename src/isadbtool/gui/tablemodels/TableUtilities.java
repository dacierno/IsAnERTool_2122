package isadbtool.gui.tablemodels;

import java.util.Collection;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TableUtilities {
  
  public static void hideColumn(JTable theTable, int columNumer) {
    theTable.getColumnModel().getColumn(columNumer).setMinWidth(0);
    theTable.getColumnModel().getColumn(columNumer).setMaxWidth(0);
    theTable.getColumnModel().getColumn(columNumer).setWidth(0);
  }  
  
  public static void downRow(JTable[] theTables, JTable theTable) {
    TableUtilities.stopEditing(theTables);
    if (theTable.getSelectedRow() == -1) return;
    if (theTable.getSelectedRow() == (theTable.getRowCount() - 1)) return;
    int selRow = theTable.getSelectedRow();
    ((DefaultTableModel) theTable.getModel()).moveRow(selRow, selRow, selRow + 1);
    theTable.setRowSelectionInterval(selRow + 1, selRow + 1);
  }
  
  
  public static void upRow(JTable[] theTables, JTable theTable) {
    TableUtilities.stopEditing(theTables);
    if (theTable.getSelectedRow() == -1) return;
    if (theTable.getSelectedRow() == 0) return;
    int selRow = theTable.getSelectedRow();
    ((DefaultTableModel) theTable.getModel()).moveRow(selRow, selRow, selRow - 1);
    theTable.setRowSelectionInterval(selRow - 1, selRow - 1);
  }
  
  
  public static void stopEditing(JTable[] theTables) {
    if (theTables == null) return;
    for (JTable t : theTables) {
      if (t.isEditing()) t.getCellEditor().stopCellEditing();
    }
  }
  
}
