public class Problem3 {

    public int maxSumBrute(int[] A) {
        if (A == null || A.length == 0) return 0;
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < A.length; i++) {
            int currentSum = 0;
            for (int j = i; j < A.length; j++) {
                currentSum += A[j];
                if (currentSum > max) {
                    max = currentSum;
                }
            }
        }
        return max;
    }

    public int maxSumSmart(int[] A) {
        if (A == null || A.length == 0) return 0;
        return maxSubArrayHelper(A, 0, A.length - 1);
    }

    private int maxSubArrayHelper(int[] A, int low, int high) {
        if (low == high) {
            return A[low];
        }

        int mid = low + (high - low) / 2;

        int leftMax = maxSubArrayHelper(A, low, mid);
        int rightMax = maxSubArrayHelper(A, mid + 1, high);
        int crossMax = maxCrossingSum(A, low, mid, high);

        return Math.max(Math.max(leftMax, rightMax), crossMax);
    }

    private int maxCrossingSum(int[] A, int low, int mid, int high) {
        int leftSum = Integer.MIN_VALUE;
        int sum = 0;
        for (int i = mid; i >= low; i--) {
            sum += A[i];
            if (sum > leftSum) {
                leftSum = sum;
            }
        }

        int rightSum = Integer.MIN_VALUE;
        sum = 0;
        for (int i = mid + 1; i <= high; i++) {
            sum += A[i];
            if (sum > rightSum) {
                rightSum = sum;
            }
        }

        return leftSum + rightSum;
    }

    public static void main(String[] args) {
        Problem3 solver = new Problem3();

        int[] A = {-17, 5, 3, -10, 6, 1, 4, -3, 8, 1, -13, 4};
        System.out.println("Brute: " + solver.maxSumBrute(A));
        System.out.println("Smart: " + solver.maxSumSmart(A));

        // Benchmarking
        int size = 10000;
        int[] largeA = new int[size];
        for (int i = 0; i < size; i++) {
            largeA[i] = (i % 2 == 0) ? i % 100 : -(i % 100);
        }

        long t1 = System.nanoTime();
        solver.maxSumBrute(largeA);
        long timeBrute = System.nanoTime() - t1;

        long t2 = System.nanoTime();
        solver.maxSumSmart(largeA);
        long timeSmart = System.nanoTime() - t2;

        System.out.println("Brute time: " + timeBrute + " ns");
        System.out.println("Smart time: " + timeSmart + " ns");
    }
}
