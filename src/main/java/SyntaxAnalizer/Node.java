package SyntaxAnalizer;

import java.util.PriorityQueue;
import java.util.Queue;

public class Node {
    public String data;
    public Queue<Node> children = new PriorityQueue<>();
}
