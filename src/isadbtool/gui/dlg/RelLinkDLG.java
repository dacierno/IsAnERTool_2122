package isadbtool.gui.dlg;

import java.awt.Frame;
import java.util.Set;
import javax.swing.JOptionPane;
import isadbtool.blogic.conceptual.ConceptualRelLink;

public class RelLinkDLG extends javax.swing.JDialog {
  boolean OKExitCode;

  private RelLinkDLG(Frame theFrame) {
    super(theFrame, true);
    initComponents();
    cardinalityComboBox.removeAllItems();
    cardinalityComboBox.addItem("(0,1)");
    cardinalityComboBox.addItem("(0,N)");
    cardinalityComboBox.addItem("(1,1)");
    cardinalityComboBox.addItem("(1,N)");
    OKExitCode = false;
  }
  
  public RelLinkDLG(Frame theFrame, ConceptualRelLink theRelLink) {
    this(theFrame);
    cardinalityComboBox.setSelectedItem("("+theRelLink.getMinCard()+","+theRelLink.getMaxCard()+")");
    identifierCheckBox.setSelected(theRelLink.isIdentifying());
    this.identifierCheckBoxActionPerformed(null);
    idNameTextField.setText(theRelLink.getIdName());
    if ((theRelLink.getRole() != null)&& (theRelLink.getRole().trim().length()>0)) {
      roleTextField.setText(theRelLink.getRole());
    }
    this.setTitle("Modify the Link");
  }  
  
  public String getRole() {
    if (roleTextField.getText().length() == 0)
      return null;
    return roleTextField.getText();
  }
  
  public String getMinCard() {
    return ((String) cardinalityComboBox.getSelectedItem()).split(",")[0].substring(1);
  }

  public String getMaxCard() {
    return ((String) cardinalityComboBox.getSelectedItem()).split(",")[1].substring(0, 1);
  }
  
  public boolean isOKExitCode() {
    return OKExitCode;
  }
  
  public boolean isIdentifier() {    
    return this.identifierCheckBox.isSelected();
  }

  public String getIdentifierName() {
    if (!isIdentifier()) return null;
    return this.idNameTextField.getText();
  }
  
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    okButton = new javax.swing.JButton();
    dismiss = new javax.swing.JButton();
    cardinalityComboBox = new javax.swing.JComboBox<>();
    identifierCheckBox = new javax.swing.JCheckBox();
    idNameTextField = new javax.swing.JTextField();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    roleTextField = new javax.swing.JTextField();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

    okButton.setText("OK");
    okButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        okButtonActionPerformed(evt);
      }
    });

    dismiss.setText("Dismiss");
    dismiss.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        dismissActionPerformed(evt);
      }
    });

    cardinalityComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cardinalityComboBoxActionPerformed(evt);
      }
    });

    identifierCheckBox.setText("Identifier");
    identifierCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        identifierCheckBoxActionPerformed(evt);
      }
    });

    jLabel1.setText("Id Name");

    jLabel2.setText("Role");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap(23, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel1)
          .addComponent(jLabel2))
        .addGap(28, 28, 28)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(cardinalityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(identifierCheckBox)
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(roleTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                .addComponent(idNameTextField, javax.swing.GroupLayout.Alignment.LEADING)))
            .addGap(69, 69, 69))
          .addGroup(layout.createSequentialGroup()
            .addComponent(okButton)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
            .addComponent(dismiss)
            .addContainerGap())))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(cardinalityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(identifierCheckBox)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(idNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel1))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(roleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel2))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(okButton)
          .addComponent(dismiss))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
    // TODO add your handling code here:
    if (isIdentifier() && (getIdentifierName().length() == 0)) {
        JOptionPane.showMessageDialog(this, "Please, select a name for the identifier",
                "Error",JOptionPane.ERROR_MESSAGE);        
        return;
    }
    OKExitCode = true;
    setVisible(false);
  }//GEN-LAST:event_okButtonActionPerformed

  private void dismissActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dismissActionPerformed
    // TODO add your handling code here:
    OKExitCode = false;
    setVisible(false);    
  }//GEN-LAST:event_dismissActionPerformed

  private void cardinalityComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cardinalityComboBoxActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cardinalityComboBoxActionPerformed

  private void identifierCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_identifierCheckBoxActionPerformed
    // TODO add your handling code here:
    String temp = (String) cardinalityComboBox.getSelectedItem();
    if (identifierCheckBox.isSelected()) {
      cardinalityComboBox.removeAllItems();
      cardinalityComboBox.addItem("(0,1)");
      cardinalityComboBox.addItem("(1,1)");
      //idName.setEditable(true);
      //idName.setEnabled(true);
    } else {
      cardinalityComboBox.removeAllItems();
      cardinalityComboBox.addItem("(0,1)");
      cardinalityComboBox.addItem("(0,N)");
      cardinalityComboBox.addItem("(1,1)");
      cardinalityComboBox.addItem("(1,N)");      
      //idName.setEditable(false);
      //idName.setEnabled(false);
    }    
    cardinalityComboBox.setSelectedItem(temp);
  }//GEN-LAST:event_identifierCheckBoxActionPerformed



  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox<String> cardinalityComboBox;
  private javax.swing.JButton dismiss;
  private javax.swing.JTextField idNameTextField;
  private javax.swing.JCheckBox identifierCheckBox;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JButton okButton;
  private javax.swing.JTextField roleTextField;
  // End of variables declaration//GEN-END:variables
}
