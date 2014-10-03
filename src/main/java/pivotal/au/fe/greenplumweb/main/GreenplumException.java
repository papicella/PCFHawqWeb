package pivotal.au.fe.greenplumweb.main;

@SuppressWarnings("serial")
public class GreenplumException extends Exception
{
	public GreenplumException()
	{
	}

	public GreenplumException(final Throwable cause)
	{
		super(cause);   
	}
        
	public GreenplumException
            (final String msg,
             final Throwable cause)
	{
		super(msg, cause);      
	}
        
    public GreenplumException(final String msg)
    {
    	super(msg);     
    }
}
