package isadbtool.gui;

import java.util.ArrayList;
import javax.swing.JEditorPane;

public class UtilsHTML {  
  
  public static void getTableStyle(StringBuilder content) {
    content.append("<head><style>"
            + " table.grid {"
            + "	margin: 0px;"
            + "	width: fixed;"
            + "	border-collapse: collapse;"
            + "	border: 0px solid black;"
            + " border-spacing: 0px;");
    content.append(" white-space: nowrap;");    
    content.append("}"
            + "table.grid th {"
            + "border: 0px solid black;"
            + "font-weight:bold;"
            + "}"
            + "table.grid td {"
            + "border: 0px solid red;"
            + "text-align: left;"
            + "padding: 0px 0px 0px 8px;"
            + "}"
            + "table.grid td#first {"
            + "border: 0px solid black;"
            + "padding: 0px 0px 0px 2px;"
            + "}"
            + "</head>");
  }

  public static int getContentHeight(StringBuilder content) {
    JEditorPane dummyEditorPane = new JEditorPane();
    dummyEditorPane.setContentType("text/html");
    dummyEditorPane.setSize(1000, Short.MAX_VALUE);
    dummyEditorPane.setText(content.toString());
    return dummyEditorPane.getPreferredSize().height;
  }

  public static int getContentWidth(StringBuilder content) {
    JEditorPane dummyEditorPane = new JEditorPane();
    dummyEditorPane.setContentType("text/html");
    dummyEditorPane.setSize(Short.MAX_VALUE, 1000);
    dummyEditorPane.setText(content.toString());
    return dummyEditorPane.getPreferredSize().width;
  }

}
