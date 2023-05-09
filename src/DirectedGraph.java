import java.util.*;
import java.util.function.Consumer;

public class DirectedGraph {

    private class Node{
        private String item;

        public Node(String item) {
            this.item = item;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return item.equals(node.item);
        }

        @Override
        public int hashCode() {
            return Objects.hash(item);
        }

        @Override
        public String toString() {
                return item;
        }
    }

    private Map<String, Node> nodeMap = new HashMap<>();
    private Map<Node, List<Node>> adjacencyMap = new HashMap<>();

    public void addNode(String item){
        if (item == null)
            return;

        Node node = new Node(item);

        nodeMap.putIfAbsent(item, node);

        adjacencyMap.putIfAbsent(node, new LinkedList<>());
    }

    public void addEdge(String fromItem, String toItem){
        if (fromItem == null || toItem == null || fromItem.equals(toItem))
            throw new IllegalArgumentException();

        Node fromNode = nodeMap.get(fromItem);
        Node toNode = nodeMap.get(toItem);

        if (fromNode == null || toNode == null)
            throw new IllegalArgumentException();

        List<Node> bucket = adjacencyMap.get(fromNode);

        if (!bucket.contains(toNode))
            bucket.add(toNode);
    }

    public void removeNode(String item){
        if (item == null)
            return;

        if (!nodeMap.containsKey(item))
            return;

        Node node = new Node(item);

        nodeMap.remove(item);

        adjacencyMap.remove(node);

        for (var entry : adjacencyMap.entrySet()){
            entry.getValue().remove(node);
        }
    }

    public void removeEdge(String fromItem, String toItem){
        if (fromItem == null || toItem == null || fromItem.equals(toItem))
            return;

        Node fromNode = nodeMap.get(fromItem);
        Node toNode = nodeMap.get(toItem);

        if (fromNode == null || toNode == null)
            throw new IllegalArgumentException();


        adjacencyMap.get(fromNode).remove(toNode);
    }

    public void depthFirstTraversalRecursive(String item, Consumer<String> consumer){
        if (!nodeMap.containsKey(item))
            throw new IllegalArgumentException();

        depthFirstTraversalRecursive(new Node(item), consumer, new HashSet<>());
    }

    public void depthFirstTraversalIterative(String item, Consumer<String> consumer) {
        if (!nodeMap.containsKey(item))
            throw new IllegalArgumentException();

        depthFirstTraversalIterative(new Node(item), consumer);
    }

    public void breadthFirstTraversal(String item, Consumer<String> consumer) {
        if (!nodeMap.containsKey(item))
            throw new IllegalArgumentException();

        breadthFirstTraversal(new Node(item), consumer);
    }

    // Return the list with elements in the right order
    // Work only in the graph without the cycle
    public List<String> topologicalSorting(){
        ArrayDeque<String> itemStack = new ArrayDeque<>();
        List<String> itemList = new ArrayList<>();
        Set<Node> nodeSet = new HashSet<>();

        for (var node : adjacencyMap.keySet())
            topologicalSorting(node, itemStack, nodeSet);

        while(!itemStack.isEmpty())
            itemList.add(itemStack.removeLast());

        return itemList;
    }

    public boolean cycleDetection(){
        Set<Node> remainingSet = new HashSet<>();
        Set<Node> visitingSet = new HashSet<>();
        Set<Node> visitedSet = new HashSet<>();

        remainingSet.addAll(nodeMap.values());

        while (!remainingSet.isEmpty()){
            if (cycleDetection(remainingSet.iterator().next(), remainingSet, visitingSet, visitedSet))
                return true;
        }

        return false;
    }

    private void depthFirstTraversalRecursive(Node currentNode, Consumer<String> consumer, Set<Node> nodeSet){
        if (!nodeSet.add(currentNode))
            return;

        consumer.accept(currentNode.item);

        for (var node : adjacencyMap.get(currentNode))
            depthFirstTraversalRecursive(node, consumer, nodeSet);
    }

    private void depthFirstTraversalIterative(Node rootNode, Consumer<String> consumer){
        ArrayDeque<Node> nodeStack = new ArrayDeque<>();
        Set<Node> nodeSet = new HashSet<>();
        Node currentNode;
        List<Node> list;

        nodeStack.addLast(rootNode);

        while (!nodeStack.isEmpty()){
            currentNode = nodeStack.removeLast();

            if (!nodeSet.add(currentNode))
                continue;

            consumer.accept(currentNode.item);

            list = adjacencyMap.get(currentNode);

            for (int i = list.size() - 1; i >= 0; i--)
                nodeStack.addLast(list.get(i));
        }
    }

    private void breadthFirstTraversal(Node rootNode, Consumer<String> consumer){
        ArrayDeque<Node> nodeQueue = new ArrayDeque<>();
        Set<Node> nodeSet = new HashSet<>();
        Node currentNode;
        List<Node> list;

        nodeQueue.addLast(rootNode);

        while (!nodeQueue.isEmpty()){
            currentNode = nodeQueue.removeFirst();

            if (!nodeSet.add(currentNode))
                continue;

            consumer.accept(currentNode.item);

            list = adjacencyMap.get(currentNode);

            for (int i = 0; i < list.size(); i++)
                nodeQueue.addLast(list.get(i));
        }
    }

    private void topologicalSorting(Node currentNode, ArrayDeque<String> itemStack, Set<Node> nodeSet){
        if (!nodeSet.add(currentNode))
            return;

        for (var node : adjacencyMap.get(currentNode))
            topologicalSorting(node, itemStack, nodeSet);

        itemStack.addLast(currentNode.item);
    }

    private boolean cycleDetection(Node currentNode, Set<Node> remainingSet, Set<Node> visitingSet,
                                   Set<Node> visitedSet){
        remainingSet.remove(currentNode);
        visitingSet.add(currentNode);

        for (var node : adjacencyMap.get(currentNode)) {
            if (visitedSet.contains(node))
                continue;

            if (visitingSet.contains(node))
                return true;

            if (cycleDetection(node, remainingSet, visitingSet, visitedSet))
                return true;
        }

        visitingSet.remove(currentNode);
        visitedSet.add(currentNode);

        return false;
    }

}
