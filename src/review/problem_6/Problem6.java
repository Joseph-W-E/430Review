package review.problem_6;

/**
 * Created by jellio on 3/8/2017.
 */
public class Problem6 {

    /* PROBLEM 6 */

    /*
    * In the 'examples/week4' directory, there is a file BoundedBufferStillWrong.java.
    * How does it violate Rule 4?
    * Give a concrete scenario in which this code fails.
    * */
    

    /**
     * INCORRECT Implementation of a blocking queue with fixed size using
     * Object.wait() and Object.notify().
     *
     * Note the violations of Rule 4 and Rule 4b; that is, it is still subject
     * to "slipped conditions" (due to using 'if' instead of 'while') and potential
     * deadlock (due to using notify() instead of notifyAll()).
     */
    public class BoundedBufferStillWrong
    {
        /**
         * Elements in the buffer
         */
        private Object[] queue;

        /**
         * The number of elements in the queue
         */
        private int dataCount;

        /**
         * Index into the buffer for inserting
         */
        private int putIndex;

        /**
         * Index into the buffer for removing
         */
        private int getIndex;

        /**
         * Initialize the object
         */
        public BoundedBufferStillWrong( int size )
        {
            queue = new Object[size];
        }

        /**
         * Write an object to the data queue.  If the queue is full, this method
         * blocks until space is available.
         *
         * @param data
         *    The data to be entered into the queue.
         */
        public void put( Object data ) throws InterruptedException
        {
            synchronized(this)
            {
                // Wait if the buffer is full.
                if (dataCount == queue.length)
                {
                    wait();
                }

                // Write the data.
                queue[putIndex] = data;
                putIndex = (putIndex + 1) % queue.length;
                ++dataCount;

                // Notify a read thread that the buffer is no longer empty,
                notify();
            }
        }


        /**
         * Reads an object from the data queue.  If the queue is empty,
         * this method blocks  until an object is
         * available.
         *
         * @return
         *    The object removed from the queue.
         */
        public Object take( ) throws InterruptedException
        {
            Object data;
            synchronized(this)
            {
                // Wait if the buffer is empty
                if( dataCount == 0 )
                {
                    wait();
                }

                // Read the next data, wrap the index around if necessary
                data = queue[getIndex];
                getIndex = (getIndex + 1) % queue.length;
                --dataCount;

                // Notify a writer that the buffer is no longer full
                notify();
            }

            return data;
        }

    }

}
