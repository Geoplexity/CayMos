package ccs.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import ui.Debug;

/**
 * The combinatorics of a graph. Using adjacent-list.
 */
public class LinkageGraph extends AbstractGraph<Vertex> {

	// vertices
	private ArrayList<Vertex> vertices;

	// adjacent-list, indexed by v
	private HashMap<Vertex, LinkedList<Vertex>> vNeighbors;

	public LinkageGraph() {
		this.vertices = new ArrayList<Vertex>();
		this.vNeighbors = new HashMap<Vertex, LinkedList<Vertex>>();
	}

	@Override
	public int size() {
		return vertices.size();
	}

	@Override
	public LinkedList<Vertex> getNeighbors(Vertex v) {
		return vNeighbors.get(v);
	}

	public Vertex getRandomVertex() {
		return vertices.get((int) (Math.random() * vertices.size()));
	}

	@Override
	public Vertex addVertex() {
		int index = vertices.size();
		Vertex v = new Vertex(index);
		vertices.add(v);
		vNeighbors.put(v, new LinkedList<Vertex>());
		return v;
	}

	/**
	 * insert an existing vertex v into the graph
	 */
	public void insertVertex(Vertex v) {
		assert (!vertices.contains(v));
		vertices.add(v);
		vNeighbors.put(v, new LinkedList<Vertex>());
	}

	@Override
	public Vertex removeVertex(Vertex v) {
		LinkedList<Vertex> neighbors = this.getNeighbors(v);
		for (Vertex u : neighbors) {
			getNeighbors(u).remove(v);
		}
		neighbors.clear();
		vertices.remove(v);
		vNeighbors.remove(v);

		for (int i = 0; i < vertices.size(); ++i)
			vertices.get(i).index = i;

		return v;
	}
	
	public Vertex removeVertexWithoutChangingIndex(Vertex v) {
		LinkedList<Vertex> neighbors = this.getNeighbors(v);
		for (Vertex u : neighbors) {
			getNeighbors(u).remove(v);
		}
		neighbors.clear();
		vertices.remove(v);
		vNeighbors.remove(v);

		return v;
	}

	public void addEdge(Edge e) {
		addEdge(e.v1(), e.v2());
	}

	public void addEdge(int i1, int i2) {
		addEdge(getVertex(i1), getVertex(i2));
	}

	public void removeEdge(Edge e) {
		removeEdge(e.v1(), e.v2());
	}

	public void removeEdge(int i1, int i2) {
		removeEdge(getVertex(i1), getVertex(i2));
	}

	public boolean isAdjacent(Edge edge) {
		return isAdjacent(edge.v1(), edge.v2());
	}

	public boolean isAdjacent(int i1, int i2) {
		return isAdjacent(getVertex(i1), getVertex(i2));
	}

	public Vertex getVertex(int i) {
		assert (i < vertices.size());
		return vertices.get(i);
	}

	@Override
	public Collection<? extends Vertex> getVertices() {
		return this.vertices;
	}

	@Override
	public String toString() {
		String s = new String();
		// int n = vertices.size();
		for (Vertex v : vertices) {
			s += v + ": ";
			for (Vertex u : vNeighbors.get(v))
				s += u + " ";
			s += "\n";
		}
		return s;
	}

	// returns a subgraph induced by vertices in vset
	// share vertices
	public LinkageGraph inducedSubgraph(ArrayList<Vertex> vset) {
		int n = vset.size();
		LinkageGraph g = new LinkageGraph();
		for (Vertex v : vset) {
			g.vertices.add(v);
			g.vNeighbors.put(v, new LinkedList<Vertex>());
		}
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < i; ++j) {
				Vertex v1 = vset.get(i), v2 = vset.get(j);
				if (this.isAdjacent(v1, v2))
					g.addEdge(v1, v2);
			}
		}
		return g;
	}

	@Override
	public boolean contains(Vertex v) {
		/*
		 * assert (v.index >= 0); if (v.index > size()) return false; return
		 * vertices.get(v.index).equals(v);
		 */
		return vertices.contains(v);
	}
}
