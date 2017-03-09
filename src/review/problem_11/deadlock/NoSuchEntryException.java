package review.problem_11.deadlock;

public class NoSuchEntryException extends Exception
{
    public NoSuchEntryException()
    {
        super();
    }

    public NoSuchEntryException(String msg)
    {
        super(msg);
    }
}