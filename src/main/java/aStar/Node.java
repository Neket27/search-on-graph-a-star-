package aStar;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
    // Id for readability of result purposes
    private static int idCounter = 0;
    public int id;

    // Parent in the path
    private Node parent = null;

    private List<Edge> neighbors;

    // Evaluation functions
    protected double f = Double.MAX_VALUE;
    protected double g = Double.MAX_VALUE;
    // Hardcoded heuristic
    protected double h;

    Node(int id,double h){
        this.h = h;
        this.id = id;
        this.neighbors = new ArrayList<>();
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Node.idCounter = idCounter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Edge> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Edge> neighbors) {
        this.neighbors = neighbors;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    @Override
    public int compareTo(Node n) {
        return Double.compare(this.f, n.f);
    }

    public static class Edge {
        private int weight;
        private Node node;
        Edge(int weight, Node node){
            this.weight = weight;
            this.node = node;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public Node getNode() {
            return node;
        }

        public void setNode(Node node) {
            this.node = node;
        }
    }

    public void addBranch(int weight, Node node){
        Edge newEdge = new Edge(weight, node);
        neighbors =new ArrayList<>(neighbors);
        neighbors.add(newEdge);
    }

    public void removeBranch(int numberNode){
            neighbors =neighbors.stream().filter(eg -> eg.getNode().id!=numberNode).toList();
    }



    public double calculateHeuristic(Node target){
        return this.h ;
    }
}



