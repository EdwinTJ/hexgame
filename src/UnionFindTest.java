public class UnionFindTest {
    public static void main(String[] args) {
        UnionFind uf = new UnionFind(10);  // smaller size for easy testing

        // Initial state
        System.out.println("Initial state: ");
        printArray(uf);

        // Perform some unions
        uf.union(0, 1);
        System.out.println("\nAfter union(0, 1): ");
        printArray(uf);

        uf.union(2, 3);
        System.out.println("\nAfter union(2, 3): ");
        printArray(uf);

        uf.union(1, 2);
        System.out.println("\nAfter union(1, 2): ");
        printArray(uf);

        // Test find and path compression
        System.out.println("\nFind(3): " + uf.find(3));
        System.out.println("After find(3): ");
        printArray(uf);

        // Union different components
        uf.union(4, 5);
        System.out.println("\nAfter union(4, 5): ");
        printArray(uf);

        uf.union(3, 4);
        System.out.println("\nAfter union(3, 4): ");
        printArray(uf);

        // Final state
        System.out.println("\nFinal state: ");
        printArray(uf);

        // Test 1: Initialize Union-Find with 10 elements
        UnionFind uf2 = new UnionFind(10);
        System.out.println("Initial state: " + uf2);

        // Test 2: Perform some union operations
        uf2.union(2, 1);
        uf2.union(3, 2);
        uf2.union(4, 3);
        System.out.println("After some unions: " + uf2);

        // Test 3: Check if path compression works
        System.out.println("Find 4 (should compress path): " + uf2.find(4));
        System.out.println("After path compression (find operation): " + uf2);

        // Test 4: Union disjoint sets
        uf2.union(5, 6);
        uf2.union(7, 8);
        uf2.union(6, 7);
        System.out.println("After more unions (disjoint sets): " + uf2);

        // Test 5: Union sets that are already connected
        uf2.union(1, 4);
        uf2.union(5, 8);
        System.out.println("After unions of connected sets: " + uf2);

    }

    private static void printArray(UnionFind uf) {
        for (int i = 0; i < 10; i++) {
            System.out.print(uf.find(i) + " ");
        }
        System.out.println();
    }
}
