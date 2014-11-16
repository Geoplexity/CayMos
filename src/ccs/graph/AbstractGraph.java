package ccs.graph;

import java.util.Collection;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @param <T>
 *            type of vertex
 */
public abstract class AbstractGraph<T> {
	public abstract int size();

	public abstract List<T> getNeighbors(T v);

	/**
	 * Insert a new vertex. Throws an exception if not implemented in the
	 * subclass
	 * 
	 * @return the vertex added.
	 */
	public T addVertex() {
		throw new NotImplementedException();
	}

	public abstract T removeVertex(T v);

	public abstract boolean contains(T v);

	public void addEdge(T v1, T v2) {
		if (v1 == v2 || isAdjacent(v1, v2))
			return;
		assert (contains(v1));
		assert (contains(v2));
		getNeighbors(v1).add(v2);
		getNeighbors(v2).add(v1);
	}

	public void removeEdge(T v1, T v2) {
		assert (this.isAdjacent(v1, v2));
		getNeighbors(v1).remove(v2);
		getNeighbors(v2).remove(v1);
	}

	public boolean isAdjacent(T v1, T v2) {
		if (v1 == v2)
			return false;
		assert (contains(v1));
		assert (contains(v2));
		boolean r = (getNeighbors(v1).contains(v2));
		assert (r == getNeighbors(v2).contains(v1));
		return r;
	}

	public abstract Collection<? extends T> getVertices();

	public int degree(T v) {
		return getNeighbors(v).size();
	}

}
