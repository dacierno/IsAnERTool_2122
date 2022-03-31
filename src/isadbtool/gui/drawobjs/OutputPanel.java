package isadbtool.gui.drawobjs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.rtf.RTFEditorKit;

public class OutputPanel extends JPanel {

  private JTextPane textPane;
  private JScrollPane scrollPane;
  private String newline = "\n";
  
  public OutputPanel() {
    this.setLayout(new BorderLayout());
    textPane = new JTextPane();
    textPane.setCaretPosition(0);
    textPane.setMargin(new Insets(5, 5, 5, 5));
    scrollPane = new JScrollPane(textPane);
    textPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);    

    textPane.setFont(new Font("TimesRoman", Font.BOLD, 18));
    add(scrollPane, BorderLayout.CENTER);
    JPopupMenu menu = new JPopupMenu();
    menu.add(new AbstractAction("Copy Selected") {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        textPane.copy();
      }
    });
    menu.add(new AbstractAction("Copy All") {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        textPane.selectAll(); 
        textPane.copy();
      }
    });
    textPane.setComponentPopupMenu(menu);
    textPane.setCaretPosition(0);    
  }  

  public void setCaretPosition(int i) {
    textPane.setCaretPosition(i);
  }
  
  public JTextPane getTextPane() {
    return textPane;
  }
  
  public void SetFromString(String theString) throws BadLocationException  {
    textPane.getStyledDocument().remove(0, textPane.getStyledDocument().getLength());
    textPane.getStyledDocument().insertString(0,theString, null);
    textPane.setCaretPosition(0);
  }
  
  public void append(String text) {
    try {
      textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
              text, null);
    } catch (BadLocationException ex) {
    }
  }
}
