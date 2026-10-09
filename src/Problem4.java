import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Problem4 {

    public double minDistBrute(double[][] p) {
        if (p == null || p.length < 2) return 0.0;
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < p.length; i++) {
            for (int j = i + 1; j < p.length; j++) {
                double dist = dist(p[i], p[j]);
                if (dist < min) {
                    min = dist;
                }
            }
        }
        return min;
    }

    public double minDistSmart(double[][] p) {
        if (p == null || p.length < 2) return 0.0;

        double[][] px = p.clone();
        Arrays.sort(px, Comparator.comparingDouble(a -> a[0]));

        double[][] py = p.clone();
        Arrays.sort(py, Comparator.comparingDouble(a -> a[1]));

        return closestPairHelper(px, py);
    }

    private double closestPairHelper(double[][] px, double[][] py) {
        int n = px.length;
        if (n <= 3) {
            return minDistBrute(px);
        }

        int mid = n / 2;
        double[] midPoint = px[mid];

        double[][] lx = Arrays.copyOfRange(px, 0, mid);
        double[][] rx = Arrays.copyOfRange(px, mid, n);

        List<double[]> lyList = new ArrayList<>();
        List<double[]> ryList = new ArrayList<>();
        for (double[] point : py) {
            if (point[0] < midPoint[0] || (point[0] == midPoint[0] && lyList.size() < mid)) {
                lyList.add(point);
            } else {
                ryList.add(point);
            }
        }

        double[][] ly = lyList.toArray(new double[0][]);
        double[][] ry = ryList.toArray(new double[0][]);

        double d1 = closestPairHelper(lx, ly);
        double d2 = closestPairHelper(rx, ry);
        double d = Math.min(d1, d2);

        List<double[]> strip = new ArrayList<>();
        for (double[] point : py) {
            if (Math.abs(point[0] - midPoint[0]) < d) {
                strip.add(point);
            }
        }

        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && (strip.get(j)[1] - strip.get(i)[1]) < d; j++) {
                double dist = dist(strip.get(i), strip.get(j));
                if (dist < d) {
                    d = dist;
                }
            }
        }

        return d;
    }

    private double dist(double[] p1, double[] p2) {
        double dx = p1[0] - p2[0];
        double dy = p1[1] - p2[1];
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static void main(String[] args) {
        var solver = new Problem4();

        double[][] P = {{0, 0}, {3, 4}, {-5, -3}};
        System.out.println("Brute: " + solver.minDistBrute(P));
        System.out.println("Smart: " + solver.minDistSmart(P));

        // Benchmarking
        int size = 1000;
        double[][] largeP = new double[size][2];
        for (int i = 0; i < size; i++) {
            largeP[i][0] = (i * 37) % 500;
            largeP[i][1] = (i * 73) % 500;
        }

        long t1 = System.nanoTime();
        solver.minDistBrute(largeP);
        long timeBrute = System.nanoTime() - t1;

        long t2 = System.nanoTime();
        solver.minDistSmart(largeP);
        long timeSmart = System.nanoTime() - t2;

        System.out.println("Brute time: " + timeBrute + " ns");
        System.out.println("Smart time: " + timeSmart + " ns");
    }
}
