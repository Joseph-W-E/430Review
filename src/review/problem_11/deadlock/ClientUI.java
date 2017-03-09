package review.problem_11.deadlock;


import java.util.Scanner;

public class ClientUI implements Runnable
{
    /**
     * Scanner for console input.
     */
    private final Scanner scanner;

    public final Client client;

    public ClientUI(Client client)
    {
        scanner = new Scanner(System.in);
        this.client = client;
    }

    /**
     * Main Client UI loop.
     */
    @Override
    public void run()
    {
        while (!client.isDone())
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
        System.out.print("Your choice: ");
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
            int key = Integer.parseInt(s);
            client.doLookup(key);
        }
        else
        {
            char ch = s.charAt(0);
            if (ch == 'd')
            {
                client.display();
            }
            else if (ch == 'q')
            {
                client.setDone();
            }
            else
            {
                System.out.println("Please enter 'd', 'q', or an id number");
            }
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
}