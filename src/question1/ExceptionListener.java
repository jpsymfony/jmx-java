package question1;

import java.io.Serializable;

public interface ExceptionListener extends Serializable
{
    public void onException(Throwable cause);
}
