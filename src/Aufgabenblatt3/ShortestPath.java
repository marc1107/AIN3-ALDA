// O. Bittel;
// 26.09.2022

package Aufgabenblatt3;

import Aufgabenblatt2.directedGraph.*;
import Aufgabenblatt3.sim.SYSimulation;

import java.awt.*;
import java.util.*;
import java.util.List;

// ...

/**
 * Kürzeste Wege in Graphen mit A*- und Dijkstra-Verfahren.
 * @author Oliver Bittel
 * @since 27.01.2015
 * @param <V> Knotentyp.
 */
public class ShortestPath<V> {
	
	SYSimulation sim = null;
	
	Map<V,Double> dist; 		// Distanz für jeden Knoten
	Map<V,V> pred; 				// Vorgänger für jeden Knoten
	IndexMinPQ<V,Double> cand; 	// Kandidaten als PriorityQueue PQ

	Heuristic<V> h;
	DirectedGraph<V> dg;

	V s;
	V g;

	/**
	 * Konstruiert ein Objekt, das im Graph g k&uuml;rzeste Wege 
	 * nach dem A*-Verfahren berechnen kann.
	 * Die Heuristik h schätzt die Kosten zwischen zwei Knoten ab.
	 * Wird h = null gewählt, dann ist das Verfahren identisch 
	 * mit dem Dijkstra-Verfahren.
	 * @param g Gerichteter Graph
	 * @param h Heuristik. Falls h == null, werden kürzeste Wege nach
	 * dem Dijkstra-Verfahren gesucht.
	 */
	public ShortestPath(DirectedGraph<V> g, Heuristic<V> h) {
		dist = new HashMap<>();
		pred = new HashMap<>();
		cand = new IndexMinPQ<>();
		this.dg = g;
		this.h = h;
	}

	/**
	 * Diese Methode sollte nur verwendet werden, 
	 * wenn kürzeste Wege in Scotland-Yard-Plan gesucht werden.
	 * Es ist dann ein Objekt für die Scotland-Yard-Simulation zu übergeben.
	 * <p>
	 * Ein typische Aufruf für ein SYSimulation-Objekt sim sieht wie folgt aus:
	 * <p><blockquote><pre>
	 *    if (sim != null)
	 *       sim.visitStation((Integer) v, Color.blue);
	 * </pre></blockquote>
	 * @param sim SYSimulation-Objekt.
	 */
	public void setSimulator(SYSimulation sim) {
		this.sim = sim;
	}

	/**
	 * Sucht den kürzesten Weg von Starknoten s zum Zielknoten g.
	 * <p>
	 * Falls die Simulation mit setSimulator(sim) aktiviert wurde, wird der Knoten,
	 * der als nächstes aus der Kandidatenliste besucht wird, animiert.
	 * @param s Startknoten
	 * @param g Zielknoten
	 */
	public void searchShortestPath(V s, V g) {
		this.s = s;
		this.g = g;

		for(V v: dg.getVertexSet()) {
			dist.put(v, Double.MAX_VALUE);
			pred.put(v, null);
		}
		dist.put(s, 0.0);
		if (h == null)
			cand.add(s, 0.0);
		else
			cand.add(s, 0.0 + h.estimatedCost(s, g));

		while(!cand.isEmpty()) {
			V v = cand.removeMin();
			/*V v = cand.getMinKey();
			cand.remove(v);*/

			printNode(v);

			// A*- Algorithmus Prüfung
			if (/*h != null && */v.equals(g)) {
				cand.clear(); // ansonsten ist beim nächsten Methodenaufruf cand noch gefüllt und alles kaputt
				return;
			}

			for(var w: dg.getSuccessorVertexSet(v)) {
				if(dist.get(w) == Double.MAX_VALUE) {
					pred.put(w, v);
					dist.put(w, dist.get(v) + dg.getWeight(v, w));
					if (h == null)
						cand.add(w, dist.get(w));
					else
						cand.add(w, dist.get(w) + h.estimatedCost(w, g));
				} else if (dist.get(v) + dg.getWeight(v, w) < dist.get(w)) {
					pred.put(w, v);
					dist.put(w, dist.get(v) + dg.getWeight(v, w));
					if (h == null)
						cand.change(w, dist.get(w));
					else
						cand.change(w, dist.get(w) + h.estimatedCost(w, g));
				}
			}
		}
	}

	private void printNode(V v) {
		StringBuilder sb = new StringBuilder();
		sb.append("Besuche Knoten ").append(v).append(" mit d = ").append(dist.get(v));
		System.out.println(sb);
		if (sim != null)
			sim.visitStation((Integer) v, Color.BLUE);
	}

	/**
	 * Liefert einen kürzesten Weg von Startknoten s nach Zielknoten g.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return kürzester Weg als Liste von Knoten.
	 */
	public List<V> getShortestPath() {
		LinkedList<V> list = new LinkedList<>();
		list.add(g);
		V v = pred.get(g);
		if (v == null)
			throw new IllegalArgumentException();
		while (v != null) {
			list.add(v);
			v = pred.get(v);
		}
		Collections.reverse(list);
		return list;
	}

	/**
	 * Liefert die Länge eines kürzesten Weges von Startknoten s nach Zielknoten g zurück.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return Länge eines kürzesten Weges.
	 */
	public double getDistance() {
		if (pred.get(g) == null)
			throw new IllegalArgumentException();
		return dist.get(g);
	}

}
