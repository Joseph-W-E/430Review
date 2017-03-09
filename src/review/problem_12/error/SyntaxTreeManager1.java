package review.problem_12.error;

//import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Singleton class for managing the abstract syntax trees obtained
 * from parsing source files.
 *
 * In this version, a sync lock is held
 * on the SyntaxTreeManager1 for the duration of every
 * call to the parser.
 */
public class SyntaxTreeManager1
{

    /**
     * The actual cache of syntax trees
     */
    //1
    private final ConcurrentMap<String, SyntaxTreeHolder> m_cache = new ConcurrentHashMap<String, SyntaxTreeHolder>();

    private final Set<String> p_cahce = Collections.synchronizedSet(new HashSet<String>());




    /**
     * Return the syntax tree for the given source file, parsing
     * the file if necessary.
     *
     * @param filename
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public synchronized SyntaxTree getSyntaxTree(String filename) throws InterruptedException, ExecutionException
    {
        SyntaxTreeHolder holder = m_cache.get(filename);

        if (holder == null)
        {
            synchronized (p_cahce) {
                Parser parser = new Parser(filename);
                p_cahce.add(filename);
                SyntaxTree ast = parser.parse();
                p_cahce.remove(filename);
                holder = new SyntaxTreeHolder();
                holder.set(ast);
                m_cache.put(filename, holder);
            }

        }
        return holder.get();
    }

    /**
     * Helper class for storing completed syntax trees.  Doesn't
     * do much at this point, but could be useful...
     */
    private static class SyntaxTreeHolder
    {
        private SyntaxTree m_ast;
        SimpleFutureAlt<SyntaxTree> future = new SimpleFutureAlt<SyntaxTree>();
        public SyntaxTree get() throws InterruptedException, ExecutionException
        {
            //3
            return future.get();
        }

        public void set(SyntaxTree ast)
        {
            m_ast = ast;
            future.set(m_ast);
        }
    }


    private static class SimpleFutureAlt<T> extends FutureTask<T>
    {
        public SimpleFutureAlt()
        {
            // aack - first argument can't be null, even though we don't plan
            // to "run" this task
            super(new Runnable(){ public void run(){} }, null);
        }

        @Override
        public void set(T result)
        {
            super.set(result);
        }

        @Override
        public T get() throws InterruptedException, ExecutionException
        {
            return super.get();
        }


        @Override
        public void setException(Throwable t)
        {
            super.setException(t);
        }
    }
}