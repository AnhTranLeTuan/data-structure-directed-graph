public class Main {
    public static void main(String[] args) {
        DirectedGraph directedGraph = new DirectedGraph();

        directedGraph.addNode("A");
        directedGraph.addNode("B");
        directedGraph.addNode("X");
        directedGraph.addNode("Y");
        directedGraph.addEdge("X", "A");
        directedGraph.addEdge("X", "B");
        directedGraph.addEdge("A", "Y");
        directedGraph.addEdge("B", "Y");
        directedGraph.addEdge("Y", "X");
        System.out.println(directedGraph.cycleDetection());
    }
}