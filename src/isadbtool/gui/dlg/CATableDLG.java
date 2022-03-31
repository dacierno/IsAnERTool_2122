package isadbtool.gui.dlg;

import isadbtool.gui.tablemodels.CATableModel;
import isadbtool.gui.tablemodels.SATableModel;
import isadbtool.blogic.conceptual.ConceptualComposedAttribute;
import isadbtool.blogic.conceptual.ConceptualEntity;
import isadbtool.blogic.conceptual.ConceptualRelationship;
import isadbtool.blogic.conceptual.ConceptualSimpleAttribute;
import isadbtool.gui.IsAnERToolGUI;
import isadbtool.gui.MyException;
import isadbtool.gui.tablemodels.TableCellListener;
import isadbtool.gui.tablemodels.TableUtilities;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

public class CATableDLG extends javax.swing.JDialog {
  ConceptualRelationship theCR;
  IsAnERToolGUI theGUI;
  boolean okExitCode;
  SATableModel saModel;
  CATableModel caModel;
  Map<String, SATableModel> theModels;
  Set<String> attributesNames;

  public CATableDLG(IsAnERToolGUI theGUI, ConceptualRelationship theCR) {
    super(theGUI, true);
    initComponents();
    this.theGUI = theGUI;
    okExitCode = false;
    this.theCR = theCR;
    theModels = new LinkedHashMap();
    attributesNames = new TreeSet();    
    
    saModel = new SATableModel(this, theCR, saTable);
    caModel = new CATableModel(this, theCR, saTable, caTable, saINcaTable, theModels);
    
    if (saTable.getRowCount() > 0) saTable.setRowSelectionInterval(0, 0);
    if (caTable.getRowCount() > 0) {
      caTable.setRowSelectionInterval(0,0);
      saINcaTable.setModel(theModels.get((String) caModel.getValueAt(0, 1)));
    } else {
      saINcaTable.setModel(new SATableModel(this, saINcaTable));      
    }
    
    
    TableUtilities.hideColumn(saTable, 0);
    if (!(theCR instanceof ConceptualEntity)) TableUtilities.hideColumn(saTable, 7);

    TableUtilities.hideColumn(caTable, 0);
    TableUtilities.hideColumn(saINcaTable, 0);
    TableUtilities.hideColumn(saINcaTable, 6);
    TableUtilities.hideColumn(saINcaTable, 7);
    
    TableCellListener tcl_saTable = new TableCellListener(saTable, new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        TableCellListener tcl = (TableCellListener) e.getSource();
        if (tcl.getColumn() == 1) {
          if (attributesNames.contains((String) tcl.getNewValue())) {            
            theGUI.catchMyException(new MyException(tcl.getNewValue()+ " already exists"));
            tcl.getTable().setValueAt(tcl.getOldValue(), tcl.getRow(), tcl.getColumn());
          } else {
            attributesNames.remove((String) tcl.getOldValue());
            attributesNames.add((String) tcl.getNewValue());
          }
        }
      }
    });
    TableCellListener tcl_caTable = new TableCellListener(caTable,new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        TableCellListener tcl = (TableCellListener) e.getSource();
        if (tcl.getColumn() == 1) {
          if (attributesNames.contains((String) tcl.getNewValue())) {            
            theGUI.catchMyException(new MyException(tcl.getNewValue()+ " already exists"));
            tcl.getTable().setValueAt(tcl.getOldValue(), tcl.getRow(), tcl.getColumn());
          } else {
            attributesNames.remove((String) tcl.getOldValue());
            attributesNames.add((String) tcl.getNewValue());
            theModels.put((String) tcl.getNewValue(), theModels.get((String) tcl.getOldValue()));
            theModels.remove((String) tcl.getOldValue());
          }
        }
      }
    });
    TableCellListener tcl_saINcaTable = new TableCellListener(saINcaTable,new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        TableCellListener tcl = (TableCellListener) e.getSource();
        if (tcl.getColumn() == 1) {
          if (attributesNames.contains((String) tcl.getNewValue())) {            
            theGUI.catchMyException(new MyException(tcl.getNewValue()+ " already exists"));
            tcl.getTable().setValueAt(tcl.getOldValue(), tcl.getRow(), tcl.getColumn());
          } else {
            attributesNames.remove((String) tcl.getOldValue());
            attributesNames.add((String) tcl.getNewValue());
          }
        }
      }
    });
  }
  
  public void addAttributeName(String newName) {
    attributesNames.add(newName);
  }

  public void removeAttributeName(String newName) {
    attributesNames.remove(newName);
  }
  
  public boolean isOkExitCode() {
    return okExitCode;
  }

  private void check() throws MyException {
    Set<String> temp = new TreeSet();
    for (int i = 0; i < saTable.getRowCount(); i++) {
      saModel.getSA(i, null);
      if (temp.contains((String) saTable.getValueAt(i, 1))) {
        throw new MyException("Two simple attributes with the same name");
      }
      temp.add((String) saTable.getValueAt(i, 1));
    }
    for (int i = 0; i < caTable.getRowCount(); i++) {
      caModel.getCA(i, null);
      if (temp.contains((String) caTable.getValueAt(i, 1))) {
        throw new MyException("Two composed attributes with the same name");
      }
      temp.add((String) caTable.getValueAt(i, 1));
    }
    for (String s : theModels.keySet()) {
      SATableModel saINcaModel = theModels.get(s);
      if (saINcaModel.getRowCount() < 2) {
        throw new MyException(s+ " has less than 2 simple attributes");
      }
      for (int i = 0; i < saINcaModel.getRowCount(); i++) {
        saINcaModel.getSA(i, null);
        if (temp.contains((String) saINcaModel.getValueAt(i, 1))) {
          throw new MyException("Two simple attributes with the same name");
        }
        temp.add((String) saINcaModel.getValueAt(i, 1));
      }
    }
  }

  private String getNewAttributeDisplayedName() {
    int i = 0;
    while (attributesNames.contains("Attr" + i)) {
      i++;
    }
    return "Attr" + i;
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    saPanel1 = new javax.swing.JPanel();
    jScrollPane4 = new javax.swing.JScrollPane();
    saTable1 = new javax.swing.JTable();
    jButton9 = new javax.swing.JButton();
    jButton10 = new javax.swing.JButton();
    jButton11 = new javax.swing.JButton();
    jButton12 = new javax.swing.JButton();
    southPanel = new javax.swing.JPanel();
    okButton = new javax.swing.JButton();
    cancelButton = new javax.swing.JButton();
    jSplitPane1 = new javax.swing.JSplitPane();
    saPanel = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    saTable = new javax.swing.JTable();
    addSAButton = new javax.swing.JButton();
    upSAButton = new javax.swing.JButton();
    downSAButton = new javax.swing.JButton();
    deleteSAButton = new javax.swing.JButton();
    caPanel = new javax.swing.JPanel();
    jPanel1 = new javax.swing.JPanel();
    addCAButton = new javax.swing.JButton();
    deleteCAButton = new javax.swing.JButton();
    upCAButton = new javax.swing.JButton();
    downCAButton = new javax.swing.JButton();
    caScrollPane = new javax.swing.JScrollPane();
    caTable = new javax.swing.JTable();
    jPanel2 = new javax.swing.JPanel();
    saINcaScrollPane = new javax.swing.JScrollPane();
    saINcaTable = new javax.swing.JTable();
    addSAinCAButton = new javax.swing.JButton();
    deleteSAinCAButton = new javax.swing.JButton();
    upSAinCAButton = new javax.swing.JButton();
    downSAinCAButton = new javax.swing.JButton();

    saPanel1.setLayout(new java.awt.GridBagLayout());

    saTable1.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {
        {null, null, null, null, null, null},
        {null, null, null, null, null, null},
        {null, null, null, null, null, null},
        {null, null, null, null, null, null}
      },
      new String [] {
        "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6"
      }
    ));
    jScrollPane4.setViewportView(saTable1);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 9;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
    saPanel1.add(jScrollPane4, gridBagConstraints);

    jButton9.setText("jButton1");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    saPanel1.add(jButton9, gridBagConstraints);

    jButton10.setText("jButton2");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 4;
    saPanel1.add(jButton10, gridBagConstraints);

    jButton11.setText("jButton2");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 6;
    saPanel1.add(jButton11, gridBagConstraints);

    jButton12.setText("jButton2");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 2;
    saPanel1.add(jButton12, gridBagConstraints);

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    getContentPane().setLayout(new java.awt.GridBagLayout());

    southPanel.setLayout(new java.awt.GridBagLayout());

    okButton.setText("OK");
    okButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        okButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 2);
    southPanel.add(okButton, gridBagConstraints);

    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 2);
    southPanel.add(cancelButton, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    getContentPane().add(southPanel, gridBagConstraints);

    jSplitPane1.setDividerLocation(300);
    jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

    saPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Simple Attributes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 3, 14))); // NOI18N
    saPanel.setLayout(new java.awt.GridBagLayout());

    saTable.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {
        {null, null, null, null, null, null},
        {null, null, null, null, null, null},
        {null, null, null, null, null, null},
        {null, null, null, null, null, null}
      },
      new String [] {
        "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6"
      }
    ));
    jScrollPane1.setViewportView(saTable);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
    saPanel.add(jScrollPane1, gridBagConstraints);

    addSAButton.setText("Add");
    addSAButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        addSAButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
    saPanel.add(addSAButton, gridBagConstraints);

    upSAButton.setText("Up");
    upSAButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        upSAButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
    saPanel.add(upSAButton, gridBagConstraints);

    downSAButton.setText("Down");
    downSAButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        downSAButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
    saPanel.add(downSAButton, gridBagConstraints);

    deleteSAButton.setText("Delete");
    deleteSAButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        deleteSAButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
    saPanel.add(deleteSAButton, gridBagConstraints);

    jSplitPane1.setLeftComponent(saPanel);

    java.awt.GridBagLayout caPanelLayout1 = new java.awt.GridBagLayout();
    caPanelLayout1.columnWidths = new int[] {0, 16, 0};
    caPanelLayout1.rowHeights = new int[] {0};
    caPanel.setLayout(caPanelLayout1);

    jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Composed Attributes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 3, 14))); // NOI18N
    java.awt.GridBagLayout jPanel1Layout = new java.awt.GridBagLayout();
    jPanel1Layout.columnWidths = new int[] {0, 16, 0};
    jPanel1Layout.rowHeights = new int[] {0, 10, 0, 10, 0, 10, 0, 10, 0};
    jPanel1.setLayout(jPanel1Layout);

    addCAButton.setText("Add");
    addCAButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        addCAButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
    jPanel1.add(addCAButton, gridBagConstraints);

    deleteCAButton.setText("Delete");
    deleteCAButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        deleteCAButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    jPanel1.add(deleteCAButton, gridBagConstraints);

    upCAButton.setText("Up");
    upCAButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        upCAButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    jPanel1.add(upCAButton, gridBagConstraints);

    downCAButton.setText("Down");
    downCAButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        downCAButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    jPanel1.add(downCAButton, gridBagConstraints);

    caTable.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {
        {null, null, null, null},
        {null, null, null, null},
        {null, null, null, null},
        {null, null, null, null}
      },
      new String [] {
        "Title 1", "Title 2", "Title 3", "Title 4"
      }
    ));
    caScrollPane.setViewportView(caTable);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 9;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 0.4;
    gridBagConstraints.weighty = 1.0;
    jPanel1.add(caScrollPane, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 0.3;
    gridBagConstraints.weighty = 1.0;
    caPanel.add(jPanel1, gridBagConstraints);

    jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Simple in Composed", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 3, 14))); // NOI18N
    java.awt.GridBagLayout jPanel2Layout = new java.awt.GridBagLayout();
    jPanel2Layout.columnWidths = new int[] {0, 16, 0};
    jPanel2Layout.rowHeights = new int[] {0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0};
    jPanel2.setLayout(jPanel2Layout);

    saINcaTable.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {
        "Title 1", "Title 2", "Title 3", "Title 4"
      }
    ));
    saINcaScrollPane.setViewportView(saINcaTable);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 15;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.ipadx = 52;
    gridBagConstraints.ipady = 48;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 0.6;
    gridBagConstraints.weighty = 1.0;
    jPanel2.add(saINcaScrollPane, gridBagConstraints);

    addSAinCAButton.setText("Add");
    addSAinCAButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        addSAinCAButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    jPanel2.add(addSAinCAButton, gridBagConstraints);

    deleteSAinCAButton.setText("Delete");
    deleteSAinCAButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        deleteSAinCAButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    jPanel2.add(deleteSAinCAButton, gridBagConstraints);

    upSAinCAButton.setText("Up");
    upSAinCAButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        upSAinCAButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    jPanel2.add(upSAinCAButton, gridBagConstraints);

    downSAinCAButton.setText("Down");
    downSAinCAButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        downSAinCAButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    jPanel2.add(downSAinCAButton, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 0.7;
    gridBagConstraints.weighty = 1.0;
    caPanel.add(jPanel2, gridBagConstraints);

    jSplitPane1.setRightComponent(caPanel);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    getContentPane().add(jSplitPane1, gridBagConstraints);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void addSAButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSAButtonActionPerformed
    // TODO add your handling code here:
    TableUtilities.stopEditing(new JTable[] {saTable,caTable,saINcaTable});
    String newName = getNewAttributeDisplayedName();
    saModel.addRow(new Object[]{"",
      newName,
      theGUI.theBL.getDefaultType(),
      theGUI.theBL.getDefaultLength(),
      "", "", "(1,1)"});
    attributesNames.add(newName);
  }//GEN-LAST:event_addSAButtonActionPerformed

  private void deleteSAButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSAButtonActionPerformed
    // TODO add your handling code here:
    TableUtilities.stopEditing(new JTable[] {saTable,caTable,saINcaTable});
    if (saTable.getSelectedRow() == -1) return;
    saModel.removeRow(saTable.getSelectedRow());
  }//GEN-LAST:event_deleteSAButtonActionPerformed

  private void upSAButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upSAButtonActionPerformed
    // TODO add your handling code here:
    TableUtilities.upRow(new JTable[] {saTable,caTable,saINcaTable}, saTable);
  }//GEN-LAST:event_upSAButtonActionPerformed

  private void downSAButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downSAButtonActionPerformed
    // TODO add your handling code here:
    TableUtilities.downRow(new JTable[] {saTable,caTable,saINcaTable}, saTable);
  }//GEN-LAST:event_downSAButtonActionPerformed

  private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
    // TODO add your handling code here:
    TableUtilities.stopEditing(new JTable[] {saTable,caTable,saINcaTable});
    try {
      check();
      saModel.setTheSAs(theGUI, theCR);
      LinkedHashMap<String, ConceptualComposedAttribute> theCAs = new LinkedHashMap();
      for (int i = 0; i < caTable.getRowCount(); i++) {
        ConceptualComposedAttribute ca = caModel.getCA(i, theGUI);
        for (int j = 0; j < theModels.get(ca.getDisplayedName()).getRowCount(); j++) {
          ConceptualSimpleAttribute sa = theModels.get(ca.getDisplayedName()).getSA(j, theGUI);
          ca.addSimpleAttributes(sa);
        }
        theCAs.put(ca.getInternalName(), ca);
      }
      theCR.setTheComposedAttributes(theCAs);
      okExitCode = true;
      setVisible(false);
    } catch (MyException ex) {
      theGUI.catchMyException(ex);
    }
  }//GEN-LAST:event_okButtonActionPerformed

  private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
    // TODO add your handling code here:
    okExitCode = false;
    setVisible(false);
  }//GEN-LAST:event_cancelButtonActionPerformed

  private void addCAButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCAButtonActionPerformed
    // TODO add your handling code here:
    TableUtilities.stopEditing(new JTable[] {saTable,caTable,saINcaTable});
    caModel.addRow(new Object[]{"", getNewAttributeDisplayedName(), "(0,1)"});
  }//GEN-LAST:event_addCAButtonActionPerformed

  private void addSAinCAButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSAinCAButtonActionPerformed
    // TODO add your handling code here:
    TableUtilities.stopEditing(new JTable[] {saTable,caTable,saINcaTable});
    if (caTable.getSelectedRow() == -1) return;
    SATableModel t = theModels.get(caModel.getValueAt(caTable.getSelectedRow(), 1));
    t.addRow(new Object[]{"",
      getNewAttributeDisplayedName(),
      theGUI.theBL.getDefaultType(), theGUI.theBL.getDefaultLength(),
      "", "", "(1,1)"});
    saINcaTable.setRowSelectionInterval(saINcaTable.getRowCount() - 1, saINcaTable.getRowCount() - 1);
    attributesNames.add((String) t.getValueAt(saINcaTable.getRowCount() - 1, 1));
  }//GEN-LAST:event_addSAinCAButtonActionPerformed

  private void deleteCAButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteCAButtonActionPerformed
    // TODO add your handling code here:
    TableUtilities.stopEditing(new JTable[] {saTable,caTable,saINcaTable});
    if (caTable.getSelectedRow() == -1) return;
    int a = caTable.getSelectedRow();    
    attributesNames.remove((String) caTable.getValueAt(caTable.getSelectedRow(), 1));
    theModels.remove((String) caTable.getValueAt(caTable.getSelectedRow(), 1));
    caModel.removeRow(caTable.getSelectedRow());
    if (caModel.getRowCount() > 0) {
      if (a > (caTable.getRowCount()-1)) a = caTable.getRowCount()-1;
      caTable.setRowSelectionInterval(a, a);
    } else {
      saINcaTable.setModel(new SATableModel(this,saINcaTable));
    }
  }//GEN-LAST:event_deleteCAButtonActionPerformed

  private void upCAButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upCAButtonActionPerformed
    // TODO add your handling code here:
    TableUtilities.upRow(new JTable[] {saTable,caTable,saINcaTable}, caTable);
  }//GEN-LAST:event_upCAButtonActionPerformed

  private void downCAButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downCAButtonActionPerformed
    // TODO add your handling code here:
    TableUtilities.downRow(new JTable[] {saTable,caTable,saINcaTable}, caTable);
  }//GEN-LAST:event_downCAButtonActionPerformed

  private void deleteSAinCAButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSAinCAButtonActionPerformed
    // TODO add your handling code here:
    TableUtilities.stopEditing(new JTable[] {saTable,caTable,saINcaTable});
    if (caTable.getSelectedRow() == -1) return;
    if (saINcaTable.getSelectedRow() == -1) return;
    int a = saINcaTable.getSelectedRow();
    SATableModel t = theModels.get(caModel.getValueAt(caTable.getSelectedRow(), 1));
    System.out.println(t.getValueAt(a, 1));
    attributesNames.remove((String) t.getValueAt(a, 1));
    t.removeRow(saINcaTable.getSelectedRow());
    if (saINcaTable.getRowCount() > 0) {
      if (a > (saINcaTable.getRowCount() - 1)) {
        a = saINcaTable.getRowCount() - 1;
      }
      saINcaTable.setRowSelectionInterval(a, a);
    }
  }//GEN-LAST:event_deleteSAinCAButtonActionPerformed

  private void upSAinCAButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upSAinCAButtonActionPerformed
    // TODO add your handling code here:
    TableUtilities.upRow(new JTable[] {saTable,caTable,saINcaTable}, saINcaTable);
  }//GEN-LAST:event_upSAinCAButtonActionPerformed

  private void downSAinCAButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downSAinCAButtonActionPerformed
    // TODO add your handling code here:
    TableUtilities.downRow(new JTable[] {saTable,caTable,saINcaTable}, saINcaTable);
  }//GEN-LAST:event_downSAinCAButtonActionPerformed

  /**
   * @param args the command line arguments
   */

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton addCAButton;
  private javax.swing.JButton addSAButton;
  private javax.swing.JButton addSAinCAButton;
  private javax.swing.JPanel caPanel;
  private javax.swing.JScrollPane caScrollPane;
  private javax.swing.JTable caTable;
  private javax.swing.JButton cancelButton;
  private javax.swing.JButton deleteCAButton;
  private javax.swing.JButton deleteSAButton;
  private javax.swing.JButton deleteSAinCAButton;
  private javax.swing.JButton downCAButton;
  private javax.swing.JButton downSAButton;
  private javax.swing.JButton downSAinCAButton;
  private javax.swing.JButton jButton10;
  private javax.swing.JButton jButton11;
  private javax.swing.JButton jButton12;
  private javax.swing.JButton jButton9;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane4;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JButton okButton;
  private javax.swing.JScrollPane saINcaScrollPane;
  private javax.swing.JTable saINcaTable;
  private javax.swing.JPanel saPanel;
  private javax.swing.JPanel saPanel1;
  private javax.swing.JTable saTable;
  private javax.swing.JTable saTable1;
  private javax.swing.JPanel southPanel;
  private javax.swing.JButton upCAButton;
  private javax.swing.JButton upSAButton;
  private javax.swing.JButton upSAinCAButton;
  // End of variables declaration//GEN-END:variables
}
