package review.problem_12.error;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;


public class FileLogger2
{
    private String filename;
    private ConcurrentLinkedQueue<String> queue;
    Thread thread;

    public FileLogger2(String filename)
    {
        this.filename = filename;
        queue = new ConcurrentLinkedQueue<String>();
        thread = new Thread() {
            public void run() {
                while(true) {
                    PrintWriter pw = null;
                    try {pw = new PrintWriter(filename, "UTF-8");}
                    catch(Exception e) {System.err.println("Unable to open log file: " + filename); return;}
                    for (String item : queue) {
                        pw.println(item);
                    }
                    pw.close();
                    try {
                        synchronized (thread) {
                            thread.wait();
                        }
                    } catch (InterruptedException e) {
                        continue;
                    }
                }
            }
        };
        thread.start();
    }

    public void log(String msg)
    {
        Date d = new Date();
        queue.add(d + " " + msg);
        synchronized (thread) {
            thread.notify();
        }
    }

}