package review.problem_11;

/**
 * Created by jellio on 3/8/2017.
 */
public class Problem11 {

    /* PROBLEM 11 */

    /*
    * See the directory exam/deadlock. This is a creative solution to homework 2, problem 1 (including the optional step 4).
    * There are a lot of details that can be ignored, but here is the gist of it:
    *
    *   When the client UI thread (executing the run() method on line 25 of ClientUI) gets a request from the console,
    *   it invokes the method Client.doLookup(). If the lookup for that key is not already in progress, it puts a lookup
    *   request into a shared BlockingQueue (Client.unhandledLookups).
    *
    *   Meanwhile, a dedicated thread reads requests from the queue, accesses the database, and updates the client.
    *   This is the loop on lines 34-39 of ClientDBConnection.
    *
    * The question is:
    *   Why does it deadlock if several requests come in quickly?
    * */

}
