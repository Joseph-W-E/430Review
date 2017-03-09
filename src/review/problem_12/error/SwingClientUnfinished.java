package review.problem_12.error;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Same as 'Client' from homework 1, but has a GUI instead of a console UI.
 * Run the SimpleServer from homework 1.
 */
public class SwingClientUnfinished extends JPanel
{
    public static final String HOST = "localhost";
    public static final int PORT = 2222;

    /**
     * Local cache of key/value pairs we've already looked up.
     */
    private ArrayList<Record> cache;

    private JButton getButton;
    private JButton displayButton;
    private JTextField getField;
    private JTextArea textArea;

    public SwingClientUnfinished()
    {
        cache = new ArrayList<Record>();

        // Field for number entry
        getField = new JTextField(6);
        this.add(getField);

        // Add button
        getButton = new JButton("Find");
        getButton.addActionListener(new AddButtonListener());
        this.add(getButton);

        // Display button
        displayButton = new JButton("Display");
        displayButton.addActionListener(new DisplayButtonListener());
        this.add(displayButton);

        // Output region
        textArea = new JTextArea(25, 25);
        this.add(textArea);
    }

    /**
     * Entry point. This method should normally do
     * nothing except (possibly) parse command-line
     * arguments and invoke a helper method for creating
     * and starting up the UI.
     */
    public static void main(String[] args)
    {
        Runnable r = new Runnable()
        {
            public void run()
            {
                createAndShow();
            }
        };
        SwingUtilities.invokeLater(r);
    }

    /**
     * Static helper method creates the frame and
     * makes it visible.
     */
    private static void createAndShow()
    {
        // create the frame
        JFrame frame = new JFrame();
        frame.setContentPane(new SwingClientUnfinished());

        // give it a nonzero size
        frame.setSize(400, 150);
        //frame.pack();

        // we want to shut down the application if the
        // "close" button is pressed on the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // make the frame visible and start the UI machinery
        frame.setVisible(true);
    }


    private class AddButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            String text = getField.getText();
            int index;
            try
            {
                index = Integer.parseInt(text);
            }
            catch (NumberFormatException e)
            {
                // do nothing
                getField.setText("Error");
                return;
            }
            doLookup(index);
        }

    }

    private class DisplayButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            display();
        }
    }

    /**
     * Performs a lookup for the given key and displays result, retrieving it from the
     * slow database if not present in the local list.
     * @param key
     * @return
     */
    private void doLookup(final int key)
    {
        String value = getLocalValue(key);
        if (value == null)
        {
            getValueFromDB(key);
        }
        else
        {
            textArea.setText(value);
        }
    }

    /**
     * Look up given key in the slow database and add it to the local list.
     * @param key
     */
    private void getValueFromDB(final int key)
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
            textArea.setText(result);
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
                if (s != null) s.close();
            }
            catch (IOException ignore){}
        }

    }

    /**
     * Displays all key/value pairs in local list.
     */
    private void display()
    {
        textArea.setText("");
        for (int i =  0; i < cache.size(); ++i)
        {
            Record r = cache.get(i);
            textArea.append(r.key() + " " + r.value() + "\n");
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