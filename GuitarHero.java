/**
 * GuitarHero.java
 *
 * Compilation:  javac GuitarHero.java
 * Execution:    java GuitarHero
 * Dependencies: StdDraw.java StdAudio.java
 *
 * @author chindesaurus
 * @version 1.00
 */

public class GuitarHero {

    /**
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        final int SAMPLING_RATE = 44100;
        final double CONCERT_A = 440.0;
        final String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

        // an array of GuitarString objects, each representing a note
        GuitarString[] notes = new GuitarString[keyboard.length()];

        // set up each note with the correct frequency
        for (int i = 0; i < keyboard.length(); i++) {
            // 1. equal temperament
            //double freq = CONCERT_A * Math.pow(2, (i - 24.0) / 12.0); 
            
            // 2. stretched tuning
            double freq = CONCERT_A * Math.pow(1.05956, i - 24.0);
            notes[i] = new GuitarString(freq);
        }

        // a pointer to the current, plucked GuitarString
        GuitarString string = null;

        // the main input loop
        while (true) {

            // check if the user has typed a key, and, if so, process it
            if (StdDraw.hasNextKeyTyped()) {
 
                // the user typed this character
                char key = StdDraw.nextKeyTyped();

                int index = keyboard.indexOf(key);
                if (index >= 0 && index < notes.length) {
                    string = notes[index];
                    string.pluck();
                }
            }

            double sample = 0.0;
            if (string != null) {
            
                // compute the superposition of the samples
                for (int i = 0; i < notes.length; i++)
                    sample += notes[i].sample();

                // send the result to standard audio
                StdAudio.play(sample);
                    
                // advance the simulation of each guitar string by one step
                for (int i = 0; i < notes.length; i++)
                    notes[i].tic();
            }
        }
    }
}
