package review.problem_1;

import java.util.concurrent.Future;

/**
 * Created by jellio on 3/8/2017.
 */
public class Problem1 {

    public static void main(String[] args) throws Exception {

        String[] data = { "one", "two", "three" };

        /* PROBLEM 1 a */

        BetterThing betterThing = new BetterThing();

        Future<String> result = betterThing.doThingAsync(data);

        if (result != null) {
            System.out.println(String.format(
                    "Got result %s",
                    result.get()
            ));
        }

        /* PROBLEM 1 b */

        CustomCallback customCallback = new CustomCallback();

        AlsoAGoodThing alsoAGoodThing = new AlsoAGoodThing();

        alsoAGoodThing.doThingAsync(data, customCallback);

        while (!customCallback.ready());

        System.out.println(String.format(
                "Gor result %s",
                customCallback.getResult()
        ));
    }

    private static class Thing {
        public String doThing(String[] args) throws InterruptedException {
            Thread.sleep(1000);
            return "potato";
        }
    }

    /**
     * PROBLEM 1 a
     *
     * Implement BetterThing.doThingAsync() to return a Future holding the result from Thing.doThing()
     */
    private static class BetterThing extends Thing {
        public Future<String> doThingAsync(String[] args) {
            return null;
        }
    }

    /**
     * PROBLEM 1 b
     *
     * Implement AlsoAGoodThing.doThingAsync() to send the output of Thing.doThing() to the callback class.
     */

    private static class AlsoAGoodThing {
        public String doThingAsync(String[] args, IAsyncCallback callback) {
            return null;
        }
    }
}
