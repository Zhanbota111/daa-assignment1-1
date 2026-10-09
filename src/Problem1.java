public class Problem1 {

    public int countFreqBrute(int key, int[] A) {
        if (A == null || A.length == 0) {
            return 0;
        }

        int count = 0;
        for (int num : A) {
            if (num == key) {
                count++;
            }
        }
        return count;
    }

    public int countFreqSmart(int key, int[] A) {
        if (A == null || A.length == 0) {
            return 0;
        }

        int first = findFirst(A, key);
        if (first == -1) {
            return 0; // Элемент не найден
        }

        int last = findLast(A, key);
        return last - first + 1;
    }

    // Вспомогательный метод для поиска первого вхождения key
    private int findFirst(int[] A, int key) {
        int low = 0;
        int high = A.length - 1;
        int result = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (A[mid] == key) {
                result = mid;      // Запоминаем возможный ответ
                high = mid - 1;    // Продолжаем поиск слева
            } else if (A[mid] < key) {
                low = mid + 1; //right side
            } else {
                high = mid - 1; //left side
            }
        }
        return result;
    }

    // Вспомогательный метод для поиска последнего вхождения key
    private int findLast(int[] A, int key) {
        int low = 0;
        int high = A.length - 1;
        int result = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (A[mid] == key) {
                result = mid;      // Запоминаем возможный ответ
                low = mid + 1;     // Продолжаем поиск справа
            } else if (A[mid] < key) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Problem1 solver = new Problem1();

        // Тест 1
        int[] A1 = {1, 1, 1, 2, 2, 2, 2, 2, 2, 4, 4, 4, 5, 5, 5, 5};
        int key1 = 4;
        System.out.println("Test 1 Brute: " + solver.countFreqBrute(key1, A1)); // Ожидается: 3
        System.out.println("Test 1 Smart: " + solver.countFreqSmart(key1, A1)); // Ожидается: 3

        // Тест 2
        int key2 = 3;
        System.out.println("Test 2 Brute: " + solver.countFreqBrute(key2, A1)); // Ожидается: 0
        System.out.println("Test 2 Smart: " + solver.countFreqSmart(key2, A1)); // Ожидается: 0

        // Замер времени для отчета (Бенчмарк)
        int size = 100_000;
        int[] largeArr = new int[size];
        for (int i = 0; i < size; i++) {
            largeArr[i] = i / 2; // Массив {0, 0, 1, 1, 2, 2, ...}
        }
        int searchKey = 25000;

        long startTime = System.nanoTime();
        int bruteAns = solver.countFreqBrute(searchKey, largeArr);
        long bruteTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        int smartAns = solver.countFreqSmart(searchKey, largeArr);
        long smartTime = System.nanoTime() - startTime;

        System.out.println("\n--- Benchmark Results ---");
        System.out.println("Brute-Force Time: " + bruteTime + " ns");
        System.out.println("Divide & Conquer Time: " + smartTime + " ns");
    }
}
