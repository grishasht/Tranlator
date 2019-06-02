package SyntaxAnalizer;

import java.util.PriorityQueue;
import java.util.Queue;

public class Node {
    private String rule;
    private String data;
    private Queue<Node> children = new PriorityQueue<>();

    private Node() {
    }

    public static Builder newBuilder() {
        return new Node().new Builder();
    }

    public class Builder {
        private Builder() {
        }

        public Builder setRule(String rule) {
            Node.this.rule = rule;
            return this;
        }

        public Builder addChildren(Node node) {
            if (node != null)
                Node.this.children.add(node);
            return this;
        }

        public Builder setData(String data) {
            Node.this.data = data;
            return this;
        }

        public String getRule() {
            return Node.this.rule;
        }

        public String getData() {
            return Node.this.data;
        }

        public Node build() {
            return Node.this;
        }
    }

    public void print(Node node, int tab) {
        int n = 0;
        if (node.data == null) {
            System.out.print("rule= " + rule);
        } else {
            System.out.println("data= " + data);
        }
        if (children != null) {
            for (int i = 0; i < tab; i++)
                System.out.print("\t");

            System.out.println(toString());
            print(children.poll(), ++n);
        }
    }


    @Override
    public String toString() {
        if (data != null)
            return "\n" +
                    "rule: " + rule +
                    "  data: " + data +
                    "  children: " + children.toString()
                    .replace("[", "")
                    .replace("]", "");
        else return "\n" +
                    "rule: " + rule +
                    "  children: " + children.toString()
                    .replace("[", "")
                    .replace("]", "");
    }
}
