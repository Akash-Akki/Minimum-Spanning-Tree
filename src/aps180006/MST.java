// Authors  1. Akash P Akki     netid: apa190001
//          2. Anant Srivastava netid: aps180006;
package aps180006;

import aps180006.BinaryHeap.Index;
import aps180006.BinaryHeap.IndexedHeap;
import aps180006.Graph.Edge;
import aps180006.Graph.Factory;
import aps180006.Graph.GraphAlgorithm;
import aps180006.Graph.Timer;
import aps180006.Graph.Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Queue;
import java.util.Scanner;

public class MST extends GraphAlgorithm<MST.MSTVertex> {
	String algorithm;
	public long wmst;
    List<Edge> mst;

    MST(Graph g) {
        super(g, new MSTVertex((Vertex) null));
    }
    
    public static class MSTVertex implements Index, Comparable<MSTVertex>, Factory {
        boolean seen;
        Vertex parent;
        int distance;
        Vertex vertex;
        Edge incidentEdge;
        int index;
		int componentNumber;

        MSTVertex(Vertex u) {
            this.seen = false;
            this.parent = null;
            this.distance = Integer.MAX_VALUE;
            this.vertex = u;
            this.incidentEdge = null;
            this.index = 0;
        }

        MSTVertex(MSTVertex u) {
            
        }

        public MSTVertex make(Vertex u) {
            return new MSTVertex(u);
        }

        public void putIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }

        public int compareTo(MSTVertex other) {
            return other == null ? 1 : Integer.compare(this.distance, other.distance);
        }
    }

    public static MST mst(Graph g, Vertex s, int choice) {
        MST m = new MST(g);
        switch (choice) {
            case 1:
                m.boruvka(s);
                break;
            case 2:
                m.prim2(s);
                break;
            default:
            	break;
        }
        return m;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in;
        int choice =2;  // prim2
        if (args.length == 0 || args[0].equals("-")) {
            in = new Scanner(System.in);
        } else {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        }

        if (args.length > 1) {
            choice = Integer.parseInt(args[1]);
        }

        Graph g = Graph.readGraph(in);
        Vertex s = g.getVertex(1);

        Timer timer = new Timer();
        MST m = mst(g, s, choice);
        System.out.println(m.algorithm + "\n" + m.wmst);
        System.out.println(timer.end());
    }
    /**
     * Borvuka algorithm  finds the minimum spanning tree and returns the weight of minimum spanning tree
     * @return weight of minimum spannning tree
     */
    public long boruvka(Vertex s) {
        algorithm = "Boruvka";
        mst = new LinkedList<>();
        wmst = 0;
        int count=0;
        Graph forest = new Graph(g.size());
        MST mstForest  = new MST(forest);
        count = countAndLabel(mstForest);
        while(count >1){
            addAllSafeEdges(this.g.getEdgeArray(),mstForest,count);
            count = countAndLabel(mstForest);
        }
        wmst = weightMST(mstForest);
        return wmst;
    }
    /**
     * Takes the minimum spanning tree as input given 
     * by the borvuka and calculates the weight of minimum spanning tree
     * @param mstForest
     */
    private long weightMST(MST mstForest) {
    	for(Edge e: mstForest.g.getEdgeArray()){
          wmst+=e.weight;
        }
    	return wmst;
    }
    
    /**
     * Adds safeedges one by one to the minimum spanning tree
     * @param edgeArray - gives all the edges
     * @param mstForest - update mstForest with ccomponent number
     * @param count -  nmber of nodes
     */
    private void addAllSafeEdges(Edge[] edgeArray, MST mstForest, int count) {
    	Set<Integer> setFrom = new HashSet<Integer>();
        Set<Integer> setTo = new HashSet<Integer>();
        Edge[] safe = new Edge[count];
        for(int i=0;i<count;i++){
        	safe[i] =null;
        }
        for(Edge e: edgeArray){
        	Vertex u = e.from;
        	Vertex v = e.to;
        	try{
        		if(get(u).componentNumber != get(v).componentNumber){
        			if(safe[get(u).componentNumber] == null ||  e.weight <   safe[get(u).componentNumber].weight){
        				safe[get(u).componentNumber] = e;
        			}
        			if(safe[get(v).componentNumber] == null ||  e.weight <   safe[get(v).componentNumber].weight){
        				safe[get(v).componentNumber] = e;
        			}
        		}
        	}	
        	catch(Exception ex){
        		System.out.println(" u   comp"+get(u).componentNumber);
        		System.out.println(" v  comp"+get(v).componentNumber);
        	}
        }
        for(int i=0;i<count;i++){
        	if (!(setFrom.contains(safe[i].from.name) && setTo.contains(safe[i].to.name))) {
        		setFrom.add(safe[i].from.name);
        		setTo.add(safe[i].to.name);
        		mstForest.g.addEdge(safe[i].from,safe[i].to,safe[i].weight,safe[i].name);
        	}

        }
    }
    
    /**
     * to count the components of F and label each vertex v with an integer comp(v) indicating its
     * component
     * @param mstForest gives the empty nodes of components
     * @return returns count
     */
    private int countAndLabel(MST mstForest) {
    	int count =0;
        Vertex[] vertexArray = mstForest.g.getVertexArray();
        for(int i=0;i<vertexArray.length;i++){
              get(vertexArray[i]).seen=false;
        }
        for(int i=0;i<vertexArray.length;i++){
        	if(get(vertexArray[i]).seen == false){
        		count = count +1;
        		labelOne(vertexArray[i],count,mstForest);
        	}
        }
        return count;
      }
    
    /**
     * label each and every component using count
     * @param vertex  the given vertez
     * @param count number of nodes
     * @param mstForest given graph of type MST
     */
    private void labelOne(Vertex vertex,int count,MST mstForest) {
    	Queue<Vertex> queue = new LinkedList<>();
    	queue.add(vertex);
    	while(!queue.isEmpty()){
    		Vertex v = queue.poll();
    		if(get(v).seen ==false){
    			get(v).seen=true;
    			get(v).componentNumber=count-1;
    			for(Edge e: mstForest.g.incident(v)){
    				Vertex w = e.otherEnd(v);
    				queue.add(w);
    			}	
    		}
    	}
    }
    
    /***Takes the minimum spanning tree as input given 
    * by the Prim2 and calculates the weight of minimum spanning tree
    * @param wmst
    */
    public long prim2(Vertex s) {
        algorithm = "Indexed heaps";
        mst = new LinkedList<>();
        wmst = 0;
        IndexedHeap<MSTVertex> q = new IndexedHeap<>(g.size());

        for(Vertex v : g){
            get(v).seen = false;
            get(v).distance = Integer.MAX_VALUE;
            get(v).vertex = v;
            get(v).parent = null;
            get(v).putIndex(v.getIndex());
        }

        get(s).distance = 0;

        for(Vertex v : g){
            q.add(get(v));
        }

        int count = 0;
        while(!q.isEmpty()){
            if(g.size() - 1 == count){
                break;
            }
            MSTVertex u = q.remove();
            u.seen = true;
            wmst += u.distance;

            if(u.parent != null){
                mst.add(u.incidentEdge);
                count++;
            }
//
            for(Edge e : g.incident(u.vertex)){
                Vertex v = e.otherEnd(u.vertex);
                if(!get(v).seen && e.getWeight() < get(v).distance){
                    get(v).distance = e.getWeight();
                    get(v).parent = u.vertex;
                    get(v).incidentEdge = e;
                    q.decreaseKey(get(v));
                }
            }
        }
        return wmst;
    }
}
