import java.util.Scanner;
import java.util.Arrays;

public class minimum_range_query {

    static class SqrtDecomposition {
        private int[] arr;
        private int[] block;
        private int blockSize;

        public SqrtDecomposition(int[] input) {
            this.arr = input;
            int n = input.length;
            blockSize = (int) Math.sqrt(n);
            int numBlocks = (int) Math.ceil((double) n / blockSize);
            block = new int[numBlocks];
            Arrays.fill(block, Integer.MAX_VALUE);

            for (int i = 0; i < n; i++) {
                block[i / blockSize] = Math.min(block[i / blockSize], arr[i]);
            }
        }

        public int query(int l, int r) {
            int min = Integer.MAX_VALUE;
            while (l <= r && l % blockSize != 0) {
                min = Math.min(min, arr[l]);
                l++;
            }
            while (l + blockSize - 1 <= r) {
                min = Math.min(min, block[l / blockSize]);
                l += blockSize;
            }
            while (l <= r) {
                min = Math.min(min, arr[l]);
                l++;
            }
            return min;
        }
    }

    static class SparseTable {
        private int[][] sparseTable;
        private int[] log;

        public SparseTable(int[] input) {
            int n = input.length;
            log = new int[n + 1];
            sparseTable = new int[n][log2(n) + 1];

            for (int i = 2; i <= n; i++) {
                log[i] = log[i / 2] + 1;
            }

            for (int i = 0; i < n; i++) {
                sparseTable[i][0] = input[i];
            }

            for (int j = 1; (1 << j) <= n; j++) {
                for (int i = 0; i + (1 << j) - 1 < n; i++) {
                    sparseTable[i][j] = Math.min(sparseTable[i][j - 1], sparseTable[i + (1 << (j - 1))][j - 1]);
                }
            }
        }

        public int query(int l, int r) {
            int length = r - l + 1;
            int j = log[length];
            return Math.min(sparseTable[l][j], sparseTable[r - (1 << j) + 1][j]);
        }

        private int log2(int n) {
            return (int) (Math.log(n) / Math.log(2));
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the size of the array:");
        int n = sc.nextInt();

        int[] input = new int[n];
        System.out.println("Enter the elements of the array:");
        for (int i = 0; i < n; i++) {
            input[i] = sc.nextInt();
        }

        SqrtDecomposition sqrtDecomp = new SqrtDecomposition(input);
        SparseTable sparseTable = new SparseTable(input);

        System.out.println("Enter the number of queries:");
        int q = sc.nextInt();

        for (int i = 0; i < q; i++) {
            System.out.println("Enter the range (l r):");
            int l = sc.nextInt();
            int r = sc.nextInt();

            System.out.println("Minimum using Sqrt Decomposition: " + sqrtDecomp.query(l, r));
            System.out.println("Minimum using Sparse Table: " + sparseTable.query(l, r));
        }

        sc.close();
    }
}
