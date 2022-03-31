package isadbtool.gui.dlg;

import isadbtool.blogic.BLObject;
import isadbtool.gui.IsAnERToolGUI;
import isadbtool.gui.drawobjs.DraggableObj;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JOptionPane;

public class GeneralizationDLG extends javax.swing.JDialog {
  boolean okExitCode;
  IsAnERToolGUI theGUI;
  ArrayList<String> generalizingEntityInternalNames;
  String displayedName;


  public GeneralizationDLG(IsAnERToolGUI theGUI,
          String displayedName,
          Collection<? extends BLObject> theEntities) {
    super(theGUI, true);
    this.displayedName = displayedName;
    this.theGUI = theGUI;
    setTitle("New Generalization");
    initComponents();
    this.setLocationRelativeTo(theGUI);
    generalizingEntityInternalNames = new ArrayList();
    for (BLObject o : theEntities) {
      generalizingEntityDisplayedName.addItem(o.getDisplayedName());
      generalizingEntityInternalNames.add(o.getInternalName());
    }
    generalizationName.setText(displayedName);
    generalizationName.setEditable(false);
    okExitCode = false;
  }

  public boolean isOkExitCode() {
    return okExitCode;
  }
  
  public String getGeneralizationName() {
    return generalizationName.getText().trim();
  }

  public String getGeneralizingEntityInternalName() {
    return generalizingEntityInternalNames.get(generalizingEntityDisplayedName.getSelectedIndex());
  }
  
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    generalizationName = new javax.swing.JTextField();
    generalizingEntityDisplayedName = new javax.swing.JComboBox<>();
    okButton = new javax.swing.JButton();
    cancelButton = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

    generalizationName.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
    generalizationName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    generalizationName.setText("jTextField1");

    generalizingEntityDisplayedName.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N

    okButton.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
    okButton.setText("OK");
    okButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        okButtonActionPerformed(evt);
      }
    });

    cancelButton.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelButtonActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(generalizationName)
          .addComponent(generalizingEntityDisplayedName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
      .addGroup(layout.createSequentialGroup()
        .addGap(0, 28, Short.MAX_VALUE)
        .addComponent(okButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(cancelButton))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(12, 12, 12)
        .addComponent(generalizationName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(generalizingEntityDisplayedName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(okButton)
          .addComponent(cancelButton)))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
    // TODO add your handling code here:
    okExitCode = false;
    if (getGeneralizationName().length() == 0) {
      JOptionPane.showConfirmDialog(this, "Generalization name cannot be empty", "alert", JOptionPane.CLOSED_OPTION);      
      return;
    } 
    okExitCode = true;
    setVisible(false);
  }//GEN-LAST:event_okButtonActionPerformed

  private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
    // TODO add your handling code here:
    okExitCode = false;
    setVisible(false);
  }//GEN-LAST:event_cancelButtonActionPerformed


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton cancelButton;
  private javax.swing.JTextField generalizationName;
  private javax.swing.JComboBox<String> generalizingEntityDisplayedName;
  private javax.swing.JButton okButton;
  // End of variables declaration//GEN-END:variables
}
