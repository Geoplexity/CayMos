/*
 This file is part of CayMos. 

 CayMos is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 CayMos is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ccs.graph;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

public class Realization implements Serializable {

	// TODO: resize? screen to mouse?
	// static int width = 800, height = 600;

	// point coordinates for each vertex
	private HashMap<Vertex, Point2D> points;

	private LinkageGraph graph;

	public LinkageGraph getGraph() {
		return graph;
	}

	/**
	 * Realization of empty graph
	 */
	public Realization() {
		this.points = new HashMap<Vertex, Point2D>();
		this.graph = new LinkageGraph();
	}

	/**
	 * Realization for graph g s.t. all coordinates are (0,0)
	 */
	public Realization(LinkageGraph g) {
		this.graph = g;
		this.points = new HashMap<Vertex, Point2D>();
		for (Vertex v : graph.getVertices()) {
			points.put(v, new Point2D(0, 0));
		}
	}

	public int getSize() {
		return graph.size();
	}

	// create a new vertex
	public Vertex addVertex(Point2D p) {
		Vertex v = graph.addVertex();
		points.put(v, p);
		return v;
	}

	public Vertex addVertex(double x, double y) {
		Point2D p = new Point2D(x, y);
		return this.addVertex(p);
	}

	public Vertex removeVertex(Vertex v) {
		graph.removeVertex(v);
		points.remove(v);
		return v;
	}

	public Vertex getVertex(Point2D p, double precision) {
		for (Vertex v : graph.getVertices()) {
			if (getPoint(v).distance(p) < precision) {
				return v;
			}
		}
		return null;
	}

	public Point2D getPoint(int i) {
		return getPoint(graph.getVertex(i));
	}

	public Point2D getPoint(Vertex v) {
		assert (points.containsKey(v));
		return points.get(v);
	}

	public EdgePos getPoints(Edge e) {
		return new EdgePos(getPoint(e.v1()), getPoint(e.v2()));
	}

	public EdgePos getPoints(Vertex v1, Vertex v2) {
		return getPoints(new Edge(v1, v2));
	}

	public void setPoint(Vertex v, Point2D newloc) {
		assert (points.containsKey(v));
		points.put(v, newloc);
	}

	public void setPoint(Vertex v, double x, double y) {
		setPoint(v, new Point2D(x, y));
	}

	public String pointsToString() {
		String s = "";
		for (Vertex v : graph.getVertices()) {
			s += v.toString() + getPoint(v) + "\t";
		}
		return s;
	}

	// returns realization on a subgraph induced by vertices in vset
	// share vertices, copied points
	// public Realization inducedSubgraph(ArrayList<Vertex> vset) {
	// Graph g = graph.inducedSubgaph(vset);
	// Realization r = new Realization();
	// r.graph = g;
	//
	// for (Vertex v : vset)
	// r.setPoint(v, this.getPoint(v));
	//
	// return r;
	// }

	/**
	 * copy a realization that shares the same underlying graph
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Realization clone() {
		Realization r = new Realization();
		r.graph = graph;
		r.points = new HashMap<Vertex, Point2D>(points);
		return r;
		// return this.inducedSubgaph(vertices);
		// Graph g = new Graph();
		// for (Vertex v : vertices)
		// g.insertVertex(v, getPoint(v));
		// for (int i = 0; i < getSize(); ++i)
		// for (int j = 0; j < i; ++j) {
		// Vertex v1 = getVertex(i), v2 = getVertex(j);
		// if (isAdjacent(v1, v2))
		// g.addEdge(v1, v2);
		// }
		// return g;
	}

	// TODO do we really need to make a clone???
	public HashMap<Vertex, Point2D> getPoints() {
		return new HashMap<Vertex, Point2D>(points);
	}

	public void setPoints(Realization r) {
		assert (r.graph == this.graph);
		for (Vertex v : r.graph.getVertices()) {
			Point2D newp = r.getPoint(v);
			this.setPoint(v, newp);
		}
	}

	public void setPoints(HashMap<Vertex, Point2D> map) {
		assert (map.size() == points.size());
		for (Vertex v : map.keySet()) {
			assert (points.containsKey(v));
			points.put(v, map.get(v));
		}
	}

	public double distance(Vertex v1, Vertex v2) {
		Point2D p1 = getPoint(v1), p2 = getPoint(v2);
		return p1.distance(p2);
	}

	public double length(Edge e) {
		return distance(e.v1(), e.v2());
	}

	public int orientationOf(Vertex v1, Vertex v2, Vertex v) {
		return Point2D.orientationOf(getPoint(v1), getPoint(v2), getPoint(v));
	}

	public void transformVertex(Vertex v, AffineTransform transform) {
		Point2D newp = getPoint(v).transform(transform);
		setPoint(v, newp);
	}

	public void transformVertices(AffineTransform transform) {
		for (Vertex v : graph.getVertices()) {
			Point2D newp = getPoint(v).transform(transform);
			setPoint(v, newp);
		}
	}

	public void writeToStream(ObjectOutputStream o) throws IOException {
		int n = getSize();
		o.writeInt(n); // write size

		// write positions
		for (int i = 0; i < n; ++i) {
			o.writeObject(getPoint(graph.getVertex(i)));
		}

		// write adjacency matrix
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < i; ++j) {
				if (graph.isAdjacent(graph.getVertex(i), graph.getVertex(j)))
					o.writeInt(1);
				else
					o.writeInt(0);
			}
		}
	}

	public static Realization readFromStream(ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		Realization r = new Realization();
		int n = in.readInt(); // read size

		// read vertices
		for (int i = 0; i < n; ++i) {
			Point2D p = (Point2D) in.readObject();
			r.addVertex(p);
		}

		// read adjacency matrix
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < i; ++j) {
				int adjacent = in.readInt();
				if (adjacent == 1)
					r.graph.addEdge(i, j);
			}
		}

		return r;
	}
}
