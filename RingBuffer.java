/**
 * RingBuffer.java
 *
 * Defines a ring buffer data type for the
 * Karplus-Strong algorithm.
 *
 * @author chindesaurus
 * @version 1.00
 */

public class RingBuffer {
    private double[] rb;          // items in the buffer
    private int first;            // index for the next dequeue or peek
    private int last;             // index for the next enqueue
    private int size;             // number of items in the buffer
    private int capacity;         // capacity of the buffer

    /**
     * Constructor: creates an empty ring buffer 
     * with given max capacity.
     *
     * @param capacity the maximum capacity of the buffer (must be >=0)
     */
    public RingBuffer(int capacity) {
        this.capacity = capacity;
        rb = new double[capacity];
        first = 0;
        last = 0;
        size = 0;
    }

    /**
     * Returns the buffer size.
     *
     * @return the number of items currently in the buffer
     */
    public int size() {
        return size;
    }

    /**
     * Is the buffer empty?
     * 
     * @return true iff buffer is empty, else false
     */
    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * Is the buffer full?
     *
     * @return true iff size equals the buffer's capacity, else false
     */
    public boolean isFull() {
        return (size == capacity); 
    }

    /**
     * Adds item x to the end of the ring buffer.
     *
     * @param x  the double to add to the end of the buffer
     * @throws RuntimeException Ring buffer overflow if buffer is full
     */
    public void enqueue(double x) {
        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        
        // put x at index last and increment last 
        rb[last++] = x;
    
        // make the buffer wrap around
        last %= capacity;

        // keep track of the number of items
        size++;
    }

    /**
     * Deletes and returns the item at the front of the buffer.
     *
     * @return the item deleted from the front of the buffer
     * @throws RuntimeException Ring buffer underflow if buffer is empty
     */
    public double dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }

        // store the item at the front, and increment first
        double front = rb[first++];

        // make the buffer wrap around
        first %= capacity;
  
        // keep track of the number of items
        size--;

        return front;
    }

    /**
     * Returns (but does not delete) the item at the front of the buffer.
     *
     * @return the item at the front of the buffer
     * @throws RuntimeException Ring buffer underflow if buffer is empty
     */
    public double peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
    
        return rb[first];
    }


    /**
     * A simple test of the constructor and methods in RingBuffer.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        int N = Integer.parseInt(args[0]);
        RingBuffer buffer = new RingBuffer(N);

        for (int i = 1; i <= N; i++) {
            buffer.enqueue(i);
        }

        double t = buffer.dequeue();
        buffer.enqueue(t);
        System.out.println("Size after wrap-around is " + buffer.size());

        while (buffer.size() >= 2) {
            double x = buffer.dequeue();
            double y = buffer.dequeue();
            buffer.enqueue(x + y);
        }
        System.out.println(buffer.peek());
    }

}
