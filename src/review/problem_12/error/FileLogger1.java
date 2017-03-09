package review.problem_12.error;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.concurrent.*;

public class FileLogger1
{
    private String filename;

    private BlockingQueue<String> queue;

    public FileLogger1(String filename)
    {
        this.filename = filename;
        this.queue = new ArrayBlockingQueue<String>(20, true);
    }

    public void log(String msg) throws InterruptedException
    {
        queue.put(msg);

        Runnable r = new Runnable()
        {
            public void run()
            {
                try
                {
                    Date d = new Date();
                    OutputStream os = new FileOutputStream(filename, true);
                    PrintWriter pw = new PrintWriter(os);
                    pw.println(d + " " + queue.poll());
                    pw.close();
                }
                catch (FileNotFoundException e)
                {
                    System.err.println("Unable to open log file: " + filename);
                }
            }
        };
        new Thread(r).start();
    }
}