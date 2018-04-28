package com.coursera;

import edu.princeton.cs.algs4.Digraph;

import java.util.*;

public class SAP {
    private Digraph digraph;

    public SAP(Digraph digraph) {
        this.digraph = digraph;
    }

    public int length(int v, int w) {
        //from two synset Ids in the digraph..
        //find the common ancestor.. that will give the
        //length the idea is to find the shortest distance.
        //from source vertex v
        Map<Integer, Integer> vNeighbours = new HashMap<>();
        Map<Integer, Integer> wNeighbours = new HashMap<>();
        Map<Integer, Integer> ancestors = new HashMap<>();

        List<Integer> queueForV = new LinkedList<>();
        List<Integer> queueForW = new LinkedList<>();

        int length = Integer.MAX_VALUE;//the farthest possible away
        while (!queueForV.isEmpty() || !queueForW.isEmpty()) {
            Integer vInt = queueForV.isEmpty() ? ((LinkedList<Integer>) queueForV).getFirst() : null;
            //find the connections for the particular integer..
            //if there is a connection there..
            Integer wInt = queueForW.isEmpty() ? ((LinkedList<Integer>) queueForW).getFirst() : null;
            if (vInt != null && wNeighbours.containsKey(vInt)) {
                //This would mean that we have seen this node on the bfs that was started from the
                //node w.
                ancestors.put(vInt, wNeighbours.get(vInt) + vNeighbours.get(vInt));
                if (length < wNeighbours.get(vInt) + vNeighbours.get(vInt)) {
                    length = wNeighbours.get(vInt) + vNeighbours.get(vInt);
                }
            }

            if (wInt != null && vNeighbours.containsKey(wInt)) {
                //Same thing with w
                ancestors.put(wInt, vNeighbours.get(wInt) + wNeighbours.get(wInt));
                if (length < vNeighbours.get(wInt) + wNeighbours.get(wInt)) {
                    length = vNeighbours.get(wInt) + wNeighbours.get(wInt);
                }
            }

            if (vInt != null) {
                for (Integer neighbours : digraph.adj(vInt)) {
                    if (!vNeighbours.containsKey(neighbours)) {
                        queueForV.add(neighbours);
                        vNeighbours.put(neighbours, vNeighbours.get(vInt) + 1);
                    }
                }
            }

            if (wInt != null) {
                for (Integer neighbours : digraph.adj(wInt)) {
                    if (!wNeighbours.containsKey(neighbours)) {
                        queueForW.add(neighbours);
                        wNeighbours.put(neighbours, wNeighbours.get(wInt) + 1);
                    }
                }
            }
        }
        return length;
    }
}
