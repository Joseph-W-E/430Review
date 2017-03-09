package review.problem_7;

/**
 * Created by jellio on 3/8/2017.
 */
public class Problem7 {

    /* PROBLEM 7 */

    /*
    * In the 'examples/week4' directory, there is a file BoundedBuffer.java.
    * This is a correct implementation of a bounded blocking queue.
    * Suppose we add the method:
    *
    * public T peek() {
    *   if (dataCount == 0)
    *       throw new NoSuchElementException();
    *   return elements[getIndex];
    * }
    *
    * a) Describe two distinct ways in which this code fails.
    * b) What if the variable dataCount was declared volatile?
    * */

    /**
     * Implementation of a blocking queue with fixed size.
     */
    private class BoundedBuffer
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
        public BoundedBuffer( int size )
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
                while (dataCount == queue.length)
                {
                    wait();
                }

                // Write the data.
                queue[putIndex] = data;
                putIndex = (putIndex + 1) % queue.length;
                ++dataCount;

                // Notify a read thread that the buffer is no longer empty,
                notifyAll();
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
                while( dataCount == 0 )
                {
                    wait();
                }

                // Read the next data, wrap the index around if necessary
                data = queue[getIndex];
                getIndex = (getIndex + 1) % queue.length;
                --dataCount;

                // Notify a writer that the buffer is no longer full
                notifyAll();
            }

            return data;
        }


    }

}
