package isadbtool.gui.notes;

import isadbtool.gui.IsAnERToolGUI;
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

public class NotesPanel extends JPanel {

  private JTextPane textPane;
  private JScrollPane scrollPane;
  private String newline = "\n";
  
  public NotesPanel(IsAnERToolGUI theGUI) {
    this.setLayout(new BorderLayout());
    textPane = new JTextPane();
    textPane.setCaretPosition(0);
    textPane.setMargin(new Insets(5, 5, 5, 5));
    scrollPane = new JScrollPane(textPane);
    textPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
    
    textPane.setFont(new Font("TimesRoman", Font.BOLD, 18));
    add(scrollPane, BorderLayout.CENTER);
    JPopupMenu menu = new JPopupMenu();
    
    menu.add(new StyledEditorKit.ForegroundAction("Red", Color.red));
    menu.add(new StyledEditorKit.ForegroundAction("Green", Color.green));
    menu.add(new StyledEditorKit.ForegroundAction("Black", Color.black));
    menu.add(new AbstractAction("Paste") {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        textPane.paste();
      }
    });
    menu.add(new AbstractAction("Copy") {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        textPane.copy();
      }
    });
    menu.add(new AbstractAction("Font") {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        MyFontChooser d = new MyFontChooser(theGUI,textPane.getFont().getSize());
        d.setLocationRelativeTo(theGUI);
        d.setVisible(true);
        if (d.isOkExitCode()) {
          textPane.setFont(new Font("TimesRoman",Font.BOLD,d.getFontSize()));
        }
      }
    });
    textPane.setComponentPopupMenu(menu);
    textPane.setCaretPosition(0);    
  }  

  public NotesPanel(NotesPanel otherPanel) {
    this.setLayout(new BorderLayout());
    textPane = new JTextPane();
    textPane.setCaretPosition(0);
    textPane.setMargin(new Insets(5, 5, 5, 5));
    //doc = (AbstractDocument) textPane.getStyledDocument();
    scrollPane = new JScrollPane(textPane);
    textPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
    
    textPane.setFont(new Font("TimesRoman", Font.BOLD, 18));
    add(scrollPane, BorderLayout.CENTER);
    JPopupMenu menu = new JPopupMenu();
    
    menu.add(new StyledEditorKit.ForegroundAction("Red", Color.red));
    menu.add(new StyledEditorKit.ForegroundAction("Green", Color.green));
    menu.add(new StyledEditorKit.ForegroundAction("Black", Color.black));
    menu.add(new StyledEditorKit.FontSizeAction("12", 12));    
    menu.add(new StyledEditorKit.FontSizeAction("14", 14));  
    menu.add(new AbstractAction("Paste") {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        textPane.paste();
      }
    });
    menu.add(new AbstractAction("Copy") {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        textPane.copy();
      }
    });
    menu.add(new AbstractAction("Font") {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        textPane.setFont(new Font("TimesRoman",Font.BOLD,12));
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
  
  public String getString() throws BadLocationException {  
    return textPane.getDocument().getText(0, textPane.getDocument().getLength());
  }

  private String getRTFString() throws IOException, BadLocationException {  
    RTFEditorKit htmlKit = new RTFEditorKit();
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    htmlKit.write(output, textPane.getDocument(), 0,
                  textPane.getDocument().getLength()); 
    return output.toString();
  }
  
  private void SetFromRTFString(String theString) throws IOException, BadLocationException  {
    RTFEditorKit htmlKit = new RTFEditorKit();
    ByteArrayInputStream input = new ByteArrayInputStream(theString.getBytes());
    htmlKit.read(input, textPane.getDocument(), 0); 
  }
  
  public void SetFromString(String theString) throws BadLocationException  {
    textPane.getStyledDocument().remove(0, textPane.getStyledDocument().getLength());
    textPane.getStyledDocument().insertString(0,
              theString, null);
  }
  
  public void append(String text) {
    try {
      textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
              text, null);
    } catch (BadLocationException ex) {
    }
  }
}
