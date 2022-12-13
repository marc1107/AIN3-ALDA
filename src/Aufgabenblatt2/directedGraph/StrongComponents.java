// O. Bittel;
// 26.09.22

package Aufgabenblatt2.directedGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Klasse für Bestimmung aller strengen Komponenten.
 * Kosaraju-Sharir Algorithmus.
 * @author Oliver Bittel
 * @since 02.03.2020
 * @param <V> Knotentyp.
 */
public class StrongComponents<V> {
	// comp speichert fuer jede Komponente die zughörigen Knoten.
    // Die Komponenten sind numeriert: 0, 1, 2, ...
    // Fuer Beispielgraph in Aufgabenblatt 2, Abb3:
    // Component 0: 5, 6, 7,
    // Component 1: 8,
    // Component 2: 1, 2, 3,
    // Component 3: 4,

	private final Map<Integer,Set<V>> comp = new TreeMap<>();
	private int index = 0;
	
	/**
	 * Ermittelt alle strengen Komponenten mit
	 * dem Kosaraju-Sharir Algorithmus.
	 * @param g gerichteter Graph.
	 */
	public StrongComponents(DirectedGraph<V> g) {
		kosaraju(g);
	}

	private void kosaraju(DirectedGraph<V> g) {
		DepthFirstOrder<V> dfo = new DepthFirstOrder<>(g);
		// a.
		List<V> postOrder = dfo.postOrder();
		List<V> postOrderInverted = new LinkedList<>();

		// postOrder invertieren
		for (int i = postOrder.size() - 1; i >= 0; i--) {
			postOrderInverted.add(postOrder.get(i));
		}

		// b.
		DirectedGraph<V> gi = g.invert();

		// c.
		Set<V> besucht = new TreeSet<>();
		for (V v : postOrderInverted)
		{
			if (!besucht.contains(v))
			{
				this.comp.put(index, new TreeSet<V>());
				visitDF(v, gi, besucht);
				index++;
			}
		}
	}

	private void visitDF(V v, DirectedGraph<V> g, Set<V> besucht)
	{
		besucht.add(v);
		this.comp.get(index).add(v);

		for (var w : g.getSuccessorVertexSet(v))
		{
			if (!besucht.contains(w))
				visitDF(w, g, besucht);
		}
	}
	
	/**
	 * 
	 * @return Anzahl der strengen Komponeneten.
	 */
	public int numberOfComp() {
		return comp.size();
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();

		for(var v : comp.entrySet()) {
			s.append("Component ").append(v.getKey()).append(":");
			for(var w : v.getValue()) {
				s.append(" ").append(w).append(",");
			}
			s.append("\n");
		}

		return s.toString();
	}
	
	/**
	 * Liest einen gerichteten Graphen von einer Datei ein. 
	 * @param fn Dateiname.
	 * @return gerichteter Graph.
	 * @throws FileNotFoundException
	 */
	public static DirectedGraph<Integer> readDirectedGraph(File fn) throws FileNotFoundException {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		Scanner sc = new Scanner(fn);
		sc.nextLine();
        sc.nextLine();
		while (sc.hasNextInt()) {
			int v = sc.nextInt();
			int w = sc.nextInt();
			g.addEdge(v, w);
		}
		return g;
	}
	
	private static void test1() {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1,2);
		g.addEdge(1,3);
		g.addEdge(2,1);
		g.addEdge(2,3);
		g.addEdge(3,1);
		
		g.addEdge(1,4);
		g.addEdge(5,4);
		
		g.addEdge(5,7);
		g.addEdge(6,5);
		g.addEdge(7,6);
		
		g.addEdge(7,8);
		g.addEdge(8,2);
		
		StrongComponents<Integer> sc = new StrongComponents<>(g);
		
		System.out.println(sc.numberOfComp());  // 4
		
		System.out.println(sc);
			// Component 0: 5, 6, 7, 
        	// Component 1: 8, 
            // Component 2: 1, 2, 3, 
            // Component 3: 4, 
	}
	
	private static void test2() throws FileNotFoundException {
		DirectedGraph<Integer> g = readDirectedGraph(new File("mediumDG.txt"));
		System.out.println(g.getNumberOfVertexes());
		System.out.println(g.getNumberOfEdges());
		System.out.println(g);
		
		System.out.println("");
		
		StrongComponents<Integer> sc = new StrongComponents<>(g);
		System.out.println(sc.numberOfComp());  // 10
		System.out.println(sc);
		
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		test1();
		test2();
	}
}
