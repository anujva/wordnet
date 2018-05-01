import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SAP {
    private Digraph digraph;
    private Map<String, SAPResult> mapSAPResult;

    public SAP(Digraph digraph) {
        this.digraph = digraph;
        mapSAPResult = new HashMap<>();
    }

    public static void main(String[] args) {
        In inputStream = new In("C:\\Users\\anujv\\Downloads\\wordnet\\digraph-ambiguous-ancestor.txt");
        Digraph digraph = new Digraph(inputStream);
        SAP sap = new SAP(digraph);
        System.out.println(sap.length(0, 10));
        System.out.println(sap.ancestor(0, 10));
    }

    private SAPResult calcLengthAndAncestor(int v, int w) {
        //from two synset Ids in the digraph..
        //find the common ancestor.. that will give the
        //length the idea is to find the shortest distance.
        //from source vertex v
        Map<Integer, Integer> vNeighbours = new HashMap<>();
        Map<Integer, Integer> wNeighbours = new HashMap<>();

        List<Integer> queueForV = new LinkedList<>();
        List<Integer> queueForW = new LinkedList<>();

        int length = Integer.MAX_VALUE;//the farthest possible away
        int ancestor = -1;
        queueForV.add(v);
        queueForW.add(w);
        vNeighbours.put(v, 0);
        wNeighbours.put(w, 0);
        while (!queueForV.isEmpty() || !queueForW.isEmpty()) {
            Integer vInt = !queueForV.isEmpty() ? ((LinkedList<Integer>) queueForV).poll() : null;
            //find the connections for the particular integer..
            //if there is a connection there..
            Integer wInt = !queueForW.isEmpty() ? ((LinkedList<Integer>) queueForW).poll() : null;
            if (vInt != null && wNeighbours.containsKey(vInt)) {
                //This would mean that we have seen this node on the bfs that was started from the
                //node w.
                if (length > wNeighbours.get(vInt) + vNeighbours.get(vInt)) {
                    length = wNeighbours.get(vInt) + vNeighbours.get(vInt);
                    ancestor = vInt;
                }
            }

            if (wInt != null && vNeighbours.containsKey(wInt)) {
                //Same thing with w
                if (length > vNeighbours.get(wInt) + wNeighbours.get(wInt)) {
                    length = vNeighbours.get(wInt) + wNeighbours.get(wInt);
                    ancestor = wInt;
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

        if (length == Integer.MAX_VALUE && ancestor == -1) {
            length = -1;
        }

        return new SAPResult(length, ancestor);
    }

    public int length(int v, int w) {
        if (v == w) return 0;
        String key;
        if (v < w) {
            key = v + "" + w;
        } else {
            key = w + "" + v;
        }
        if (mapSAPResult.containsKey(key)) {
            return mapSAPResult.get(key).length;
        } else {
            SAPResult result = calcLengthAndAncestor(v, w);
            mapSAPResult.put(key, result);
            return result.length;
        }
    }

    public int ancestor(int v, int w) {
        if (v == w) return v;
        String key;
        if (v < w) {
            key = v + "" + w;
        } else {
            key = w + "" + v;
        }

        if (mapSAPResult.containsKey(key)) {
            return mapSAPResult.get(key).ancestor;
        } else {
            SAPResult result = calcLengthAndAncestor(v, w);
            mapSAPResult.put(key, result);
            return result.ancestor;
        }
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int length = -1;
        int count = 0;
        for (Integer first : v) {
            for (Integer second : w) {
                if (count++ == 0) {
                    length = length(first, second);
                } else {
                    if (length > length(first, second)) {
                        length = length(first, second);
                    }
                }
            }
        }
        return length;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int ancestor = -1;
        int count = 0;
        for (Integer first : v) {
            for (Integer second : w) {
                if (count++ == 0) {
                    ancestor = ancestor(first, second);
                } else {
                    if (ancestor > ancestor(first, second)) {
                        ancestor = ancestor(first, second);
                    }
                }
            }
        }
        return ancestor;
    }

    private static final class SAPResult {
        Integer length;
        Integer ancestor;

        public SAPResult() {
            this.length = -1;
            ancestor = null;
        }

        public SAPResult(int length, int ancestor) {
            this.length = length;
            this.ancestor = ancestor;
        }
    }
}
