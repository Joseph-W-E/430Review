package review.problem_11.deadlock;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import review.problem_11.deadlock.Lookup.Kind;

/**
 * Termination Conditions: A `ClientDBConnection` object will stop running once it decides that
 * there will be no more lookup requests coming from the client. This class decides that by looking
 * for both `TERMINATE` lookup messages and also looking to see whether the client says that it is
 * done. Termination will happen after it sees either one of these conditions. Termination will not
 * happen during the processing of a lookup request (i.e. while it is communicating with the
 * server).
 */
public class ClientDBConnection implements Runnable
{
    public static final String HOST = "localhost";
    public static final int PORT = 2222;

    public final Client client;

    public ClientDBConnection(Client client)
    {
        this.client = client;
    }

    @Override
    public void run()
    {
        Lookup lookup;
        while ((lookup = getNextLookup()) != null)
        {
            int key = lookup.getKey();
            String value = getValueFromDB(key);
            client.finishLookup(key, value);
        }
    }

    /**
     * @return The next lookup obtained from the client's unhandled lookups queue, or `null` if there
     *         will be no more lookups.
     */
    private Lookup getNextLookup()
    {
        if (client.isDone()) {
            return null;
        }

        // There are two conditions which indicate that there will be no more `Lookup` requests coming
        // from the queue: the client may indicate that it is done or the kind of message read from the
        // queue is `TERMINATE`. Checking both of these conditions is desirable in order to ensure that
        // the processing of lookup requests ends quickly. Consider two cases:
        //
        // 1. Suppose that the queue is rather full and the client decides that it is done. `isDone()`
        //    is checked to make the system stop processing messages quickly, even before all unhandled
        //    lookup requests are handled.
        // 2. Suppose that the queue is empty when the client decides that it is done. In this case, a
        //    `ClientDBConnection` may be blocked waiting on the queue. A `TERMINATE` message is pushed
        //    to the queue to wake up the connection from blocking on the queue. Without the `TERMINATE`
        //    message to consume, it may block indefinitely waiting for a message to consume.
        try
        {
            Lookup lookup = client.getUnhandledLookupsQueue().take();
            return (lookup.getKind() == Kind.TERMINATE) ? null : lookup;
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Look up given key in the slow database and add it to the local list.
     * @param key
     */
    private String getValueFromDB(int key)
    {
        String result = null;
        Socket s = null;
        try
        {
            // open a connection to the server
            s = new Socket(HOST, PORT);

            // for line-oriented output we use a PrintWriter
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.println("" + key);
            pw.flush();  // don't forget to flush...

            // read response, which we expect to be line-oriented text
            Scanner scanner = new Scanner(s.getInputStream());
            result = scanner.nextLine();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        finally
        {
            // be sure streams are closed
            try
            {
                s.close();
            }
            catch (IOException ignore){}
        }
        return result;
    }
}