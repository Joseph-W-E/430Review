package review.problem_11.deadlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import review.problem_11.deadlock.Lookup.Kind;


public class Client
{
    private static final int UNHANDLED_REQUESTS_QUEUE_SIZE = 1;

    /**
     * Local cache of key/value pairs we've already looked up.
     */
    private final ArrayList<Record> cache;

    /**
     * Used to keep track of the status of all previously seen {@link Lookup} requests.
     */
    private final LookupsMap previousLookups;

    /**
     * Indicates whether the client should be shut down.
     */
    private final AtomicBoolean done = new AtomicBoolean(false);

    /**
     * A queue of unhandled lookup requests waiting to be handled by a `ClientDBConnection`.
     */
    public final BlockingQueue<Lookup> unhandledLookups;

    /**
     * Entry point.
     * @param args
     */
    public static void main(String[] args)
    {
        // The `ClientUI` and and `ClientDBConnection` are both foci for threads. They both reach out
        // and interact with the `Client` in a thread-safe way. The `Client` itself is not a focus for
        // a thread.
        Client client = new Client();
        (new Thread(new ClientDBConnection(client))).start();
        new ClientUI(client).run();
    }

    public Client()
    {
        cache = new ArrayList<>();
        unhandledLookups = new ArrayBlockingQueue<>(UNHANDLED_REQUESTS_QUEUE_SIZE);
        previousLookups = new LookupsMap();
    }

    public boolean isDone()
    {
        return done.get();
    }

    /**
     * Changes the client's state to "done".
     */
    public void setDone()
    {
        done.set(true);
        unhandledLookups.add(Lookup.make(Kind.TERMINATE, 0));
    }

    /**
     * Returns the value for the given key, retrieving it from the
     * slow database if not present in the local list.
     * @param key
     * @return
     */
    public synchronized void doLookup(int key)
    {
        Lookup lookup = null;
        synchronized(this)
        {
            switch (previousLookups.get(key).getKind())
            {
                case UNREQUESTED:
                    // This is the first time that this `key` has been requested. Mark this as `UNHANDLED`.
                    lookup = Lookup.make(Kind.UNHANDLED, key);
                    previousLookups.set(lookup);
                    try
                    {
                        System.out.println("putting");
                        unhandledLookups.put(lookup);
                    }
                    catch (InterruptedException e) {throw new RuntimeException(e);}
                    return;
                case UNHANDLED:
                    // This key has been requested before, but its handling has not yet been completed. There is
                    // nothing to be done here, because the print statement will happen once it is handled.
                    return;

                case CACHED:
                    // This key's result has been cached locally. Print it out right away.
                    printCachedKeyValuePair(key);
                    return;

                case TERMINATE:
                default:
                    throw new RuntimeException("Unexpected/unknown lookup kind.");
            }

        }

    }

    /**
     * Displays all key/value pairs in local list.
     */
    public synchronized void display()
    {
        for (int i =  0; i < cache.size(); ++i)
        {
            Record r = cache.get(i);
            System.out.println(r.key() + " " + r.value());
        }
    }

    public BlockingQueue<Lookup> getUnhandledLookupsQueue()
    {
        return unhandledLookups;
    }

    public synchronized void finishLookup(int key, String valueFromDB)
    {
        System.out.println("finish for " + key);
        // TODO: Assert that the previous lookup for this key is of kind `UNHANDLED`.
        addToCache(key, valueFromDB);
        previousLookups.set(key, Kind.CACHED);
        printCachedKeyValuePair(key);
    }

    private synchronized void addToCache(int key, String val)
    {
        cache.add(new Record(key, val));
        Collections.sort(cache);
    }

    private synchronized void printCachedKeyValuePair(int key)
    {
        System.out.println("\nValue for id " + key + ": " + getLocalValue(key));
    }

    /**
     * Returns the value for given key, or null if not present in the list.
     * @param key
     * @return
     */
    private synchronized String getLocalValue(int key)
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