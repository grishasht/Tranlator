package SyntaxAnalizer;

import java.util.LinkedList;
import java.util.List;

public class Node {
    private String rule;
    private String data;
    private int i = 0;
    private List<Node> children = new LinkedList<>();

    public Node() {
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

        public Node build() {
            return Node.this;
        }
    }

    public void addChildren(Node node) {
        if (node != null)
            children.add(node);
    }

    public String getData() {
        return data;
    }

    public String getRule() {
        return rule;
    }

    public List<Node> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        if (data != null)
            return "\n" + Service.setTabs(++i)
                    + rule
                    + "  data: " + data
                    + children.toString()
                    .replace("[", "")
                    .replace("]", "")
                    .replace(",", "");
        else return "\n" + Service.setTabs(++i)
                    + rule
                    + children.toString()
                    .replace("[", "")
                    .replace("]", "")
                    .replace(",", "");

    }
}
