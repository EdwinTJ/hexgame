import java.util.Arrays;

public class UnionFind {
    private int[] parent;  // parent[i] points to the parent of i, if i is a root node then -size of its set
    private int count;     // number of elements in the Union Find

    public UnionFind(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size <= 0 is not allowed");
        }
        parent = new int[size];
        count = size;
        for (int i = 0; i < size; i++) {
            parent[i] = -1;  // each element is initially in its own set
        }
    }

    public int find(int element) {
        validate(element);
        int root = element;
        // Find the root of the component
        while (parent[root] >= 0) {
            root = parent[root];
        }
        // Path compression
        while (element != root) {
            int next = parent[element];
            parent[element] = root;
            element = next;
        }
        return root;
    }

    public void union(int element1, int element2) {
        if (element1==element2) return;
        int root1 = find(element1);
        int root2 = find(element2);

        if (root1 != root2) {
            // Union by size
            if (parent[root1] <= parent[root2]) { // root1 has more members
                parent[root1] += parent[root2]; // update size
                parent[root2] = root1;
            } else {
                parent[root2] += parent[root1]; // update size
                parent[root1] = root2;
            }
        }
    }

    public int count() {
        return count;
    }

    private void validate(int element) {
        if (element < 0 || element >= parent.length) {
            throw new IllegalArgumentException("Element out of bound");
        }
    }

    @Override
    public String toString() {
        return "UnionFind{" +
                "parent=" + Arrays.toString(parent) +
                ", count=" + count +
                '}';
    }

}