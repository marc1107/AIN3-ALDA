package Aufgabenblatt2.directedGraph;

public class Anziehreihenfolge {
    public static void main(String[] args) {
        DirectedGraph<String> g = new AdjacencyListDirectedGraph<>();
        g.addEdge("Socken", "Schuhe");
        g.addEdge("Unterhose", "Hose");
        g.addEdge("Unterhemd", "Hemd");
        g.addEdge("Hose", "Gürtel");
        g.addEdge("Hemd", "Pulli");
        g.addEdge("Gürtel", "Mantel");
        g.addEdge("Pulli", "Mantel");
        g.addEdge("Mantel", "Schal");
        g.addEdge("Schuhe", "Handschuhe");
        g.addEdge("Schal", "Handschuhe");
        g.addEdge("Mütze", "Handschuhe");
        g.addEdge("Schal", "Hose");
        System.out.println(g);

        TopologicalSort<String> ts = new TopologicalSort<>(g);

        if (ts.topologicalSortedList() != null) {
            System.out.println(ts.topologicalSortedList());
        }
    }
}
