public class Problem2 {

    public double getMedianBrute(int[] A, int[] B) {
        int n = A.length, m = B.length;
        int total = n + m;
        int[] merged = new int[total];

        int i = 0, j = 0, k = 0;
        while (i < n && j < m) {
            merged[k++] = (A[i] <= B[j]) ? A[i++] : B[j++];
        }
        while (i < n) merged[k++] = A[i++];
        while (j < m) merged[k++] = B[j++];

        return (total % 2 == 1)
                ? merged[total / 2]
                : (merged[total / 2 - 1] + merged[total / 2]) / 2.0;
    }

    public double getMedianSmart(int[] A, int[] B) {
        if (A.length > B.length) {
            return getMedianSmart(B, A);
        }

        int n = A.length, m = B.length;
        int low = 0, high = n;

        while (low <= high) {
            int partA = low + (high - low) / 2;
            int partB = (n + m + 1) / 2 - partA;

            int maxLeftA = (partA == 0) ? Integer.MIN_VALUE : A[partA - 1];
            int minRightA = (partA == n) ? Integer.MAX_VALUE : A[partA];

            int maxLeftB = (partB == 0) ? Integer.MIN_VALUE : B[partB - 1];
            int minRightB = (partB == m) ? Integer.MAX_VALUE : B[partB];

            if (maxLeftA <= minRightB && maxLeftB <= minRightA) {
                if ((n + m) % 2 == 1) {
                    return Math.max(maxLeftA, maxLeftB);
                } else {
                    return (Math.max(maxLeftA, maxLeftB) + Math.min(minRightA, minRightB)) / 2.0;
                }
            } else if (maxLeftA > minRightB) {
                high = partA - 1;
            } else {
                low = partA + 1;
            }
        }
        return 0.0;
    }

    public static void main(String[] args) {
        Problem2 solver = new Problem2();

        int[] A = {2, 4}, B = {3, 5};
        System.out.println("Brute: " + solver.getMedianBrute(A, B));
        System.out.println("Smart: " + solver.getMedianSmart(A, B));

        // Benchmarking
        int[] largeA = new int[1000], largeB = new int[1000];
        for (int i = 0; i < 1000; i++) {
            largeA[i] = i * 2;
            largeB[i] = i * 2 + 1;
        }

        long t1 = System.nanoTime();
        solver.getMedianBrute(largeA, largeB);
        long timeBrute = System.nanoTime() - t1;

        long t2 = System.nanoTime();
        solver.getMedianSmart(largeA, largeB);
        long timeSmart = System.nanoTime() - t2;

        System.out.println("Brute time: " + timeBrute + " ns");
        System.out.println("Smart time: " + timeSmart + " ns");
    }
}
