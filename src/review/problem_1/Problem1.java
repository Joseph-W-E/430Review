package review.problem_1;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by jellio on 3/8/2017.
 */
public class Problem1 {

    public static void main(String[] args) throws Exception {

        String[] data = { "one", "two", "three" };

        Problem1 problem1 = new Problem1();

        Future<String> result1 = problem1.doTheThingAsyncThread(data);

        if (result1 == null || !result1.get().equals("1 2 3")) {
            throw new Exception("result1 failed");
        }

        Future<String> result2 = problem1.doTheThingAsyncExecutorServiceCallable(data);

        if (result2 == null || !result2.get().equals("1 2 3")) {
            throw new Exception("result2 failed");
        }

    }

    /* PROBLEM 1 */

    /*
    * For testing, do this:
    *   Input: { "one", "two", "three" }
    *   Output: "1 2 3"
    * In other words, given an array of Strings where the elements are english version of numbers,
    * output a String of those numbers in numerical form, separated by spaces.
    * Assume that the numbers will only be between 0 and 10.
    * */

    // Use a plain vanilla Thread to perform the operation asynchronously
    private Future<String> doTheThingAsyncThread(String[] args) {
        throw new UnsupportedOperationException();
    }

    // Use an ExecutorService and a Callable<String>
    private Future<String> doTheThingAsyncExecutorServiceCallable(String[] args) {
        throw new UnsupportedOperationException();
    }

}
