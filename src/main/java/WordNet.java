import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class WordNet {
    private Digraph digraph;
    private SAP sap;
    private Map<Integer, WordNetNode> wordNetNodes;
    private Map<String, List<WordNetNode>> nouns;

    /**
     * The constructor takes in the name of the input files
     *
     * @param synsets
     * @param hypernyms
     */
    public WordNet(String synsets, String hypernyms) {
        /*
         * The first thing to do would be to read in the synsets file,
         * and create the digraph nodes
         */
        //BufferedReader reader = new BufferedReader(new FileReader(synsets));
        if (synsets == null || hypernyms == null) throw new
                IllegalArgumentException();
        In reader = new In(synsets);
        String line;
        wordNetNodes = new HashMap<>();
        nouns = new HashMap<>();
        while ((line = reader.readLine()) != null) {
            String[] splitLine = line.split(",");
            WordNetNode node = new WordNetNode(Integer.parseInt(splitLine[0]),
                    splitLine[1], splitLine[2]);
            wordNetNodes.put(Integer.parseInt(splitLine[0]), node);
            Arrays.stream(splitLine[1].split(" ")).forEach(x -> {
                if (nouns.containsKey(x)) {
                    nouns.get(x).add(node);
                } else {
                    nouns.put(x, new ArrayList<>(Arrays.asList(node)));
                }
            });
        }

        digraph = new Digraph(wordNetNodes.size());
        Topological topological = new Topological(digraph);
        reader.close();
        reader = new In(hypernyms);

        while ((line = reader.readLine()) != null) {
            String[] splitLine = line.split(",");
            for (int i = 1; i < splitLine.length; i++) {
                digraph.addEdge(Integer.parseInt(splitLine[0]), Integer
                        .parseInt(splitLine[i]));
            }
        }
        sap = new SAP(digraph);
    }

    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nouns.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
        //we have to find out what is the distance between,
        //the two nodes, as in how many levels till they have
        //a common ancestor
        //From the noun// we can find the synsetId.
        //from the synsetId we can find the connected nodes.
        //One noun can have multiple synsetIds. All of them
        //can be retrieved from polling the set that contains
        //noun to wordnetnode mapping

        List<WordNetNode> wordNetNodesA = nouns.get(nounA);
        List<WordNetNode> wordNetNodesB = nouns.get(nounB);
        if (wordNetNodesA == null || wordNetNodesB == null) throw new
                IllegalArgumentException();
//        List<Integer> lengthsA = wordNetNodesA.stream().collect(Collectors
// .mapping(x -> x.synsetId, Collectors.toList()));
        List<Integer> lengthsA = new ArrayList<>();
        for (WordNetNode node : wordNetNodesA) {
            lengthsA.add(node.synsetId);
        }
//        List<Integer> lengthsB = wordNetNodesB.stream().collect(Collectors
// .mapping(x -> x.synsetId, Collectors.toList()));
        List<Integer> lengthsB = new ArrayList<>();
        for (WordNetNode node : wordNetNodesB) {
            lengthsB.add(node.synsetId);
        }
        //This looks like if I implement the SAP data structure
        //I could reuse some of the API's that it has to return values
        return sap.length(lengthsA, lengthsB);
    }

    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
        List<WordNetNode> wordNetNodesA = nouns.get(nounA);
        List<WordNetNode> wordNetNodesB = nouns.get(nounB);
        if (wordNetNodesA == null || wordNetNodesB == null) throw new
                IllegalArgumentException();
        //List<Integer> ancestorA = wordNetNodesA.stream().collect(Collectors
        // .mapping(x -> x.synsetId, Collectors.toList()));
        List<Integer> ancestorA = new ArrayList<>();
        for (WordNetNode node : wordNetNodesA) {
            ancestorA.add(node.synsetId);
        }

        //List<Integer> ancestorB = wordNetNodesB.stream().collect(Collectors
        // .mapping(x -> x.synsetId, Collectors.toList()));
        List<Integer> ancestorB = new ArrayList<>();
        for (WordNetNode node : wordNetNodesB) {
            ancestorB.add(node.synsetId);
        }

        int ancestor = sap.ancestor(ancestorA, ancestorB);
//        return wordNetNodesA.get(ancestor).synset.stream().collect
// (Collectors.joining());
        //ancestor is the synsetid.. which one of the wordnetNode for the corresponding
        //synsetid?
        WordNetNode wordNetNode = wordNetNodes.get(ancestor);
        String result = "";
        for (String val : wordNetNode.synset) {
            result += val + " ";
        }
        return result.trim();
    }

    private static final class WordNetNode {
        private Integer synsetId;
        private Set<String> synset;
        private String gloss;

        public WordNetNode(int synsetId, String synset, String gloss) {
            this.synsetId = synsetId;
            String[] splitSynset = synset.split(" ");
            this.synset = new HashSet<>(Arrays.asList(splitSynset));
            this.gloss = gloss;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WordNetNode that = (WordNetNode) o;
            return synsetId == that.synsetId &&
                    Objects.equals(synset, that.synset) &&
                    Objects.equals(gloss, that.gloss);
        }

        @Override
        public int hashCode() {
            return Objects.hash(synsetId, synset, gloss);
        }
    }

    private static class WordNetIterator implements Iterator<String> {
        private Iterator<String> iterator;

        public WordNetIterator(WordNet wordNet) {
            iterator = wordNet.nouns.keySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public String next() {
            return iterator.next();
        }
    }
}
