import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNet;

    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    public static void main(String[] args) {
        if(args.length < 3)  {
            System.out.println("Too few arguments");
            return;
        }
        WordNet wordNet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordNet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

    public String outcast(String[] nouns) {
        String outcast = null;
        int distance = -1;
        for (int i = 0; i < nouns.length; i++) {
            int distanceInterim = 0;
            for (int j = 0; j < nouns.length; j++) {
                distanceInterim += wordNet.distance(nouns[i], nouns[j]);
            }
            if (distance == -1) {
                distance = distanceInterim;
                outcast = nouns[i];
            } else {
                if (distance < distanceInterim) {
                    distance = distanceInterim;
                    outcast = nouns[i];
                }
            }
        }
        return outcast;
    }
}
