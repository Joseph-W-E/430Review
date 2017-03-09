package review.problem_12.error;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Singleton class for managing the abstract syntax trees obtained from parsing
 * source files.
 *
 * In this version, a sync lock is held on the SyntaxTreeManager for the
 * duration of every call to the parser.
 */
public class SyntaxTreeManager2 {

    /**
     * The actual cache of syntax trees
     */
    private final Map<String, SyntaxTreeHolder> m_cache = new HashMap<String, SyntaxTreeHolder>();

    // Include filenames we'll be searching for
    private static ArrayList<String> treesBeingSearched = new ArrayList<String>();

    /**
     * Return the syntax tree for the given source file, parsing the file if
     * necessary.
     *
     * @param filename
     * @return
     * @throws InterruptedException
     */

    public SyntaxTree getSyntaxTree(String filename)
            throws InterruptedException {
        SyntaxTreeHolder holder = m_cache.get(filename);
        if (holder == null) {
            synchronized (treesBeingSearched) {
                while (treesBeingSearched.contains(filename)) {
                    try {
                        treesBeingSearched.wait();
                        if ((holder = m_cache.get(filename)) == null) {
                            throw new NullPointerException();
                        } else
                            return holder.get();
                    } catch (InterruptedException e) {
                        throw e;
                    }
                }
                treesBeingSearched.add(filename);
            }
            Parser parser = new Parser(filename);
            SyntaxTree ast = parser.parse();
            holder = new SyntaxTreeHolder();
            holder.set(ast);
            m_cache.put(filename, holder);

            synchronized (treesBeingSearched) {
                treesBeingSearched.remove(filename);
                treesBeingSearched.notifyAll();
            }
        }
        return holder.get();
    }

    /**
     * Helper class for storing completed syntax trees. Doesn't do much at this
     * point, but could be useful...
     */
    private static class SyntaxTreeHolder {
        private SyntaxTree m_ast;

        public SyntaxTree get() {
            return m_ast;
        }

        public void set(SyntaxTree ast) {
            m_ast = ast;
        }
    }


}