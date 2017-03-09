package review.problem_12.error;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client1
{
    public static final String HOST = "localhost";
    public static final int PORT = 2222;

    /**
     * Local cache of key/value pairs we've already looked up.
     */
    private volatile ArrayList<Record> cache;

    /**
     * Indication of whether we're currently making a request for an id number
     */
    Map<Integer, Boolean> currentRequests = new HashMap<>();

    /**
     * Scanner for console input.
     */
    private Scanner scanner;

    /**
     * Indicates whether the client should be shut down.
     */
    private boolean done = false;

    /**
     * Entry point.
     * @param args
     */
    public static void main(String[] args)
    {
        new Client1().go();
    }

    public Client1()
    {
        cache = new ArrayList<Record>();
        scanner = new Scanner(System.in);
    }

    /**
     * Main client loop.
     */
    public void go()
    {
        while (!done)
        {
            String response = getResponse();
            parseResponse(response);
        }

    }

    /**
     * Prints a menu and returns the text entered by user.
     * @return
     */
    private String getResponse()
    {
        System.out.println();
        System.out.println("Enter id number to look up, 'd' to display list, 'q' to quit");
        System.out.print("Your choice: \n");
        return scanner.nextLine();
    }

    /**
     * Parses the string entered by user and takes appropriate action.
     * @param s
     */
    private void parseResponse(String s)
    {
        s = s.trim();
        if (isNumeric(s))
        {
            final int key = Integer.parseInt(s);
            Thread thread = new Thread()
            {
                public void run()
                {
                    //first check if we are already making a request for this id
                    Boolean makingRequest = currentRequests.get(key);
                    if (makingRequest != null && makingRequest.booleanValue())
                    {
                        return;
                    }
                    String result = doLookup(key);
                    System.out.println("Value for id " + key + ": " + result);
                }
            };
            thread.start();
        }
        else
        {
            char ch = s.charAt(0);
            if (ch == 'd')
            {
                display();
            }
            else if (ch == 'q')
            {
                done = true;
            }
            else
            {
                System.out.println("Please enter 'd', 'q', or an id number");
            }
        }
    }

    /**
     * Returns the value for the given key, retrieving it from the
     * slow database if not present in the local list.
     * @param key
     * @return
     */
    private String doLookup(int key)
    {
        String value = getLocalValue(key);
        if (value == null)
        {
            //flag that we're making a request for this id number
            currentRequests.put(key, true);
            getValueFromDB(key);
            //unflag now that we're finished
            currentRequests.put(key, false);
            value = getLocalValue(key);
        }
        return value;
    }

    /**
     * Look up given key in the slow database and add it to the local list.
     * @param key
     */
    private void getValueFromDB(int key)
    {
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
            String result = scanner.nextLine();
            if (getLocalValue(key) == null)
            {
                cache.add(new Record(key, result));
                Collections.sort(cache);
            }
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
    }

    /**
     * Returns true if the given string represents a positive integer.
     * @param s
     * @return
     */
    private boolean isNumeric(String s)
    {
        for (int i = 0; i < s.length(); ++i)
        {
            if (!Character.isDigit(s.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Displays all key/value pairs in local list.
     */
    private void display()
    {
        for (int i =  0; i < cache.size(); ++i)
        {
            Record r = cache.get(i);
            System.out.println(r.key() + " " + r.value());
        }
    }

    /**
     * Returns the value for given key, or null if not present in the list.
     * @param key
     * @return
     */
    private String getLocalValue(int key)
    {
        for (Record r : cache)
        {
            if (r.key() == key)
            {
                return r.value();
            }
        }
        return null;
    }

    /**
     * Key/value pair.
     */
    private static class Record implements Comparable<Record>
    {
        private final int key;
        private final String value;

        public Record(int key, String value)
        {
            this.key = key;
            this.value = value;
        }

        public int key()
        {
            return key;
        }

        public String value()
        {
            return value;
        }

        @Override
        public int compareTo(Record rhs)
        {
            return this.key - rhs.key;
        }
    }
}