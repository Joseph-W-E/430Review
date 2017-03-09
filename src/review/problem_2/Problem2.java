package review.problem_2;

import java.util.concurrent.Future;

/**
 * Created by jellio on 3/8/2017.
 */
public class Problem2 {

    public static void main(String[] args) {


    }

    private class OriginalThing {
        public String doThing(String[] args) {
            return "potato";
        }
    }

    /**
     * PROBLEM 2
     *
     * Implement OmgThisThingIsSoGood.doThingAsyncThread() to use a Thread to process the Future.
     *
     * Implement OmgThisThingIsSoGood.doThingAsyncExecutor() to use an ExecutorService and Callable to process the Future
     */
    private class OmgThisThingIsSoGood extends OriginalThing {
        public Future<String> doThingAsyncThread(String[] args) {
            return null;
        }

        public Future<String> doThingAsyncExecutor(String[] args) {
            return null;
        }
    }

}
