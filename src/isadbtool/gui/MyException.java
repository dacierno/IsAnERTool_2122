package isadbtool.gui;

import java.util.Objects;

public class MyException extends Exception {

  public static Throwable findCauseUsingPlainJava(Throwable throwable) {
    Throwable requireNonNull = Objects.requireNonNull(throwable);
    Throwable rootCause = throwable;
    while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
      rootCause = rootCause.getCause();
    }
    return rootCause;
  }

  public MyException(String message) {
    super(message);
  }

  @Override
  public String getMessage() {
    Throwable rootCause = findCauseUsingPlainJava(this);
    return rootCause.getStackTrace()[0].getMethodName() + " in "
            + rootCause.getStackTrace()[0].getClassName() + " says:\n"
            + super.getMessage();
  }
  
  public String getSimpleMessage() {
    return super.getMessage();
  }

  public MyException(String message, Throwable cause) {
    super(message, cause);
  }

}
