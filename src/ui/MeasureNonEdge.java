package ui;

import java.util.ArrayList;

import ccs.graph.Vertex;
import ccs.graph.Edge;

public class MeasureNonEdge {
	
	private ArrayList<Edge> nonEdges;
	Vertex v1;
	
	private MeasureNonEdge(){
		nonEdges = new ArrayList<Edge>();
		v1 = null;
	}
	
	private static MeasureNonEdge me = new MeasureNonEdge();
	
	public static MeasureNonEdge getInstance() {
		return me;
	}
	
	public ArrayList<Edge> getNonEdges(){
		return nonEdges;
	}
	
	public Vertex getV1(){
		return v1;
	}
	
	public void setV1(Vertex v){
		v1 = v;
	}
	
	public void resetV1(){
		v1 = null;
	}
	
	public void addVertex(Vertex v){
		if(v1 == null){
			v1 = v;
		}
		else{
			Edge edge = new Edge(v1, v);
			nonEdges.add(edge);
			v1 = null;
		}
	}
}
