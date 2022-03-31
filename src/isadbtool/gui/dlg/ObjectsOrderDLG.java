package isadbtool.gui.dlg;

import isadbtool.blogic.BLObject;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ObjectsOrderDLG extends javax.swing.JDialog {

  boolean okExitCode;
  Collection<String> attributesDisplayedNames;
  Map<String,String> attributesInternalNames;
  LinkedHashSet<String> result;    

  public ObjectsOrderDLG(java.awt.Frame parent, Collection<BLObject> objects) {
    super(parent, true);
    initComponents();
    attributesDisplayedNames = new LinkedHashSet();
    attributesInternalNames = new LinkedHashMap();
    
    for(BLObject o : objects) {
      attributesInternalNames.put(o.getDisplayedName(), o.getInternalName());
      attributesDisplayedNames.add(o.getDisplayedName());
    }
    
    
    okExitCode = false;
    result = new LinkedHashSet();

    String[] columnNames = new String[1];
    columnNames[0] = "Attribute";
    Object[][] data = new Object[attributesDisplayedNames.size()][columnNames.length];
    int i = 0;
    for (String s : attributesDisplayedNames) {
      data[i][0] = s;
      i++;
    }
    ((DefaultTableModel) jTable1.getModel()).setColumnIdentifiers(columnNames);
    jTable1.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    for (Object[] rowData : data) {
      ((DefaultTableModel) jTable1.getModel()).addRow(rowData);
    }
  }

  public boolean isOkExitCode() {
    return okExitCode;
  }

  public JTable getjTable1() {
    return jTable1;
  }

  public LinkedHashSet<String> getResult() {
    return result;
  }
  
  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    okButton = new javax.swing.JButton();
    abortButton = new javax.swing.JButton();
    upButton = new javax.swing.JButton();
    downButton = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    jTable1 = new javax.swing.JTable();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

    okButton.setText("OK");
    okButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        okButtonActionPerformed(evt);
      }
    });

    abortButton.setText("ABORT");
    abortButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        abortButtonActionPerformed(evt);
      }
    });

    upButton.setText("UP");
    upButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        upButtonActionPerformed(evt);
      }
    });

    downButton.setText("DOWN");
    downButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        downButtonActionPerformed(evt);
      }
    });

    jTable1.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
    jTable1.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {
        "Title 1"
      }
    ) {
      Class[] types = new Class [] {
        java.lang.String.class
      };
      boolean[] canEdit = new boolean [] {
        false
      };

      public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    jScrollPane1.setViewportView(jTable1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(90, 90, 90)
        .addComponent(downButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
      .addComponent(upButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
      .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
      .addGroup(layout.createSequentialGroup()
        .addComponent(okButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(abortButton))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(30, 30, 30)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(downButton)
          .addComponent(upButton))
        .addGap(11, 11, 11)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(okButton)
          .addComponent(abortButton)))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
    // TODO add your handling code here:result
    result.clear();
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    for (int r = 0; r < model.getRowCount(); r++) {
      result.add(attributesInternalNames.get(model.getValueAt(r, 0).toString()));
    }
    okExitCode = true;
    setVisible(false);
  }//GEN-LAST:event_okButtonActionPerformed

  private void abortButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abortButtonActionPerformed
    // TODO add your handling code here:
    okExitCode = false;
    setVisible(false);
  }//GEN-LAST:event_abortButtonActionPerformed

  private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
    // TODO add your handling code here:
    int index = jTable1.getSelectedRow();
    int rowCount = ((DefaultTableModel) jTable1.getModel()).getRowCount();
    if ((index >= 0) && (index < (rowCount - 1))) {
      ((DefaultTableModel) jTable1.getModel()).moveRow(index, index, index + 1);
      jTable1.setRowSelectionInterval(index + 1, index + 1);
    }
  }//GEN-LAST:event_downButtonActionPerformed

  private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
    // TODO add your handling code here:
    int index = jTable1.getSelectedRow();
    if (index >= 1) {
      ((DefaultTableModel) jTable1.getModel()).moveRow(index, index, index - 1);
      jTable1.setRowSelectionInterval(index - 1, index - 1);
    }
  }//GEN-LAST:event_upButtonActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton abortButton;
  private javax.swing.JButton downButton;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JTable jTable1;
  private javax.swing.JButton okButton;
  private javax.swing.JButton upButton;
  // End of variables declaration//GEN-END:variables
}
