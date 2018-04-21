package com.coursera;

import edu.princeton.cs.algs4.Digraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class WordNet implements Iterable<String> {
    private Digraph digraph;
    private List<WordNetNode> wordNetNodes;
    private Map<String, WordNetNode> nouns;

    /**
     * The constructor takes in the name of the input files
     *
     * @param synsets
     * @param hypernyms
     */
    public WordNet(String synsets, String hypernyms) throws IOException {
        /*
         * The first thing to do would be to read in the synsets file,
         * and create the digraph nodes
         */

        BufferedReader reader = new BufferedReader(new FileReader(synsets));
        String line;
        wordNetNodes = new ArrayList<>();
        nouns = new HashMap<>();
        while ((line = reader.readLine()) != null) {
            String[] splitLine = line.split(",");
            WordNetNode node = new WordNetNode(Integer.parseInt(splitLine[0]),
                    splitLine[1], splitLine[2]);
            wordNetNodes.add(node);
            Arrays.stream(splitLine[1].split(" ")).forEach(x -> nouns.put(x,
                    node));
        }

        digraph = new Digraph(wordNetNodes.size());
        reader.close();
        reader = new BufferedReader(new FileReader(hypernyms));

        while ((line = reader.readLine()) != null) {
            String[] splitLine = line.split(",");
            for (int i = 1; i < splitLine.length; i++) {
                digraph.addEdge(Integer.parseInt(splitLine[0]), Integer
                        .parseInt(splitLine[i]));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        WordNet wordNet = new WordNet("/home/anuj/Downloads/wordnet/synsets" +
                ".txt",
                "/home/anuj/Downloads/wordnet/hypernyms.txt");

        for (String noun : wordNet) {
            System.out.println(noun);
        }
    }

    public Iterable<String> nouns() {
        return this;
    }

    public boolean isNoun(String word) {
        return nouns.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        throw new UnsupportedOperationException();
    }

    public String sap(String nounA, String nounB) {
        return null;
    }

    @Override
    public Iterator<String> iterator() {
        return new WordNetIterator(this);
    }

    @Override
    public void forEach(Consumer<? super String> action) {
        for (String noun : nouns.keySet()) {
            action.accept(noun);
        }
    }

    private static final class WordNetNode {
        private int synsetId;
        private Set<String> synset;
        private String gloss;

        public WordNetNode(int synsetId, String synset, String gloss) {
            this.synsetId = synsetId;
            String[] splitSynset = synset.split(" ");
            this.synset = new HashSet<>(Arrays.asList(splitSynset));
            this.gloss = gloss;
        }

        public WordNetNode(int synsetId, Set<String> synset, String gloss) {
            this.synsetId = synsetId;
            this.synset = synset;
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

    public static class WordNetIterator implements Iterator<String> {
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
