package review.problem_2;

/**
 * Created by jellio on 3/8/2017.
 */
public class Problem2 {

    public static void main(String[] args) {

        String[] data = { "one", "two", "three" };

        Problem2 problem2 = new Problem2();

        CustomCallback callback1 = new CustomCallback();
        problem2.doTheThingAsyncThread(data, callback1);

        CustomCallback callback2 = new CustomCallback();
        problem2.doTheThingAsyncExecutorServiceCallable(data, callback2);

        while (!callback1.hasData() && !callback2.hasData()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("It worked!");

    }

    /* PROBLEM 2 */

    /*
    * For testing, do this:
    *   Input: { "one", "two", "three" }
    *   Output: "1 2 3"
    * In other words, given an array of Strings where the elements are english version of numbers,
    * output a String of those numbers in numerical form, separated by spaces.
    * Assume that the numbers will only be between 0 and 10.
    * */

    // Use a plain vanilla Thread to perform the operation asynchronously
    private void doTheThingAsyncThread(String[] args, IAsyncCallback callback) {
        throw new UnsupportedOperationException();
    }

    // Use an ExecutorService and a Callable<String>
    private void doTheThingAsyncExecutorServiceCallable(String[] args, IAsyncCallback callback) {
        throw new UnsupportedOperationException();
    }

}
