/**
 * GuitarString.java
 * Dependencies: RingBuffer.java
 *
 * Simulates the plucking and vibration of a guitar string.
 *
 * @author chindesaurus
 * @version 1.00
 */

public class GuitarString {

    private final int SAMPLING_RATE = 44100;    // sampling rate in Hz
    private final double DECAY_FACTOR = 0.996;  // energy decay factor 
    private int tic_count = 0;                  // # of times tic() called
    private RingBuffer buffer;                  // ring buffer
    private final int N;                        // capacity of ring buffer

    /**
     * Constructor: creates a guitar string of the given frequency,
     * using SAMPLING_RATE.
     *
     * @param frequency the frequency of the GuitarString object
     */
    public GuitarString(double frequency) {
        N = (int) Math.ceil(SAMPLING_RATE / frequency);
        buffer = new RingBuffer(N);
        
        for (int i = 0; i < N; i++)
            buffer.enqueue(0.0);
    }

    /**
     * Constructor: creates a guitar string with the size and 
     * initial values of array init; for debugging.
     *
     * @param init the array from which to initialize GuitarString's buffer
     */
    public GuitarString(double[] init) {
        N = init.length;
        buffer = new RingBuffer(N);

        for (int i = 0; i < N; i++)
            buffer.enqueue(init[i]);
    }

    /**
     * Plucks the guitar string by replacing its buffer with white noise.
     */
    public void pluck() {
    
        // replace the N items in the ring buffer with
        // N random values in [-0.5, +0.5]
        java.util.Random generator = new java.util.Random();
        for (int i = 0; i < N; i++) {
            buffer.dequeue();
            buffer.enqueue(generator.nextDouble() - 0.5);
        }
    }

    /**
     * Advances the simulation one time step, 
     * applying the Karplus-Strong update.
     */
    public void tic() {
        
        // delete the sample at the front of the ring buffer
        double first = buffer.dequeue();

        // get the sample now at the front of the ring buffer
        double second = this.sample();
        
        // add to the end of the ring buffer the average of 
        // the first two samples, multiplied by the energy decay factor
        buffer.enqueue(DECAY_FACTOR * ((first + second) / 2));

        // keep track of how many times tic() is caled
        tic_count++;
    }

    /**
     * Returns the current sample.
     *
     * @return the value of the item at the front of GuitarString's buffer.
     */
    public double sample() {
        return buffer.peek();
    }

    /**
     * Returns the number of time steps made thus far.
     *
     * @return the total number of times tic() was called on this instance
     */
    public int time() {
        return tic_count;
    }

    /**
     * A test of the constructor and methods in GuitarString.
     *
     * @param args the command-line arguments
     */

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        double[] samples = { .2, .4, .5, .3, -.2, .4, .3, .0, -.1, -.3 };  
        GuitarString testString = new GuitarString(samples);
        for (int i = 0; i < N; i++) {
            int t = testString.time();
            double sample = testString.sample();
            System.out.printf("%6d %8.4f\n", t, sample);
            testString.tic();
        }
    }

}
