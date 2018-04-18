package com.coursera;

import edu.princeton.cs.algs4.Digraph;

import java.util.Objects;
import java.util.Set;

public class WordNet {
    private Digraph digraph;

    /**
     * The constructor takes in the name of the input files
     *
     * @param synsets
     * @param hypernyms
     */
    public WordNet(String synsets, String hypernyms) {

    }

    public static void main(String[] args) {

    }

    public Iterable<String> nouns() {

    }

    public boolean isNoun(String word) {

    }

    public int distance(String nounA, String nounB) {

    }

    public String sap(String nounA, String nounB) {

    }

    private static final class WordNetNode {
        private int synsetId;
        private Set<String> synset;
        private String gloss;

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
}
