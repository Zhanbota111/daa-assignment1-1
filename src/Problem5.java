import java.math.BigInteger;

public class Problem5 {

    public String multBrute(String A, String B) {
        if (A.equals("0") || B.equals("0")) return "0";

        boolean isNegative = (A.charAt(0) == '-') ^ (B.charAt(0) == '-');
        if (A.charAt(0) == '-') A = A.substring(1);
        if (B.charAt(0) == '-') B = B.substring(1);

        int n = A.length();
        int m = B.length();
        int[] result = new int[n + m];

        for (int i = n - 1; i >= 0; i--) {
            for (int j = m - 1; j >= 0; j--) {
                int mul = (A.charAt(i) - '0') * (B.charAt(j) - '0');
                int sum = mul + result[i + j + 1];

                result[i + j + 1] = sum % 10;
                result[i + j] += sum / 10;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int num : result) {
            if (!(sb.length() == 0 && num == 0)) {
                sb.append(num);
            }
        }

        if (sb.length() == 0) return "0";
        if (isNegative) sb.insert(0, '-');

        return sb.toString();
    }

    public String multSmart(String A, String B) {
        if (A.equals("0") || B.equals("0")) return "0";

        boolean isNegative = (A.charAt(0) == '-') ^ (B.charAt(0) == '-');
        if (A.charAt(0) == '-') A = A.substring(1);
        if (B.charAt(0) == '-') B = B.substring(1);

        String result = karatsuba(A, B);

        if (result.equals("0")) return "0";
        return isNegative ? "-" + result : result;
    }

    private String karatsuba(String A, String B) {
        // Базовый случай: малые числа умножаем простым способом
        if (A.length() < 10 || B.length() < 10) {
            return multBrute(A, B);
        }

        int n = Math.max(A.length(), B.length());
        int m = n / 2;

        A = padLeft(A, n);
        B = padLeft(B, n);

        String high1 = A.substring(0, n - m);
        String low1 = A.substring(n - m);
        String high2 = B.substring(0, n - m);
        String low2 = B.substring(n - m);

        // 3 рекурсивных вызова вместо 4
        String z2 = karatsuba(high1, high2);
        String z0 = karatsuba(low1, low2);
        String z1 = karatsuba(addStrings(high1, low1), addStrings(high2, low2));

        // z1 = z1 - z2 - z0
        String middle = subtractStrings(subtractStrings(z1, z2), z0);

        // Result = z2 * 10^(2m) + middle * 10^m + z0
        String term1 = shiftLeft(z2, 2 * m);
        String term2 = shiftLeft(middle, m);

        return addStrings(addStrings(term1, term2), z0);
    }

    private String addStrings(String num1, String num2) {
        StringBuilder sb = new StringBuilder();
        int i = num1.length() - 1, j = num2.length() - 1, carry = 0;
        while (i >= 0 || j >= 0 || carry != 0) {
            int sum = carry;
            if (i >= 0) sum += num1.charAt(i--) - '0';
            if (j >= 0) sum += num2.charAt(j--) - '0';
            sb.append(sum % 10);
            carry = sum / 10;
        }
        return sb.reverse().toString();
    }

    private String subtractStrings(String num1, String num2) {
        StringBuilder sb = new StringBuilder();
        int i = num1.length() - 1, j = num2.length() - 1, borrow = 0;
        while (i >= 0) {
            int sub = (num1.charAt(i--) - '0') - borrow - (j >= 0 ? num2.charAt(j--) - '0' : 0);
            if (sub < 0) {
                sub += 10;
                borrow = 1;
            } else {
                borrow = 0;
            }
            sb.append(sub);
        }
        while (sb.length() > 1 && sb.charAt(sb.length() - 1) == '0') {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.reverse().toString();
    }

    private String shiftLeft(String num, int zeros) {
        if (num.equals("0")) return "0";
        StringBuilder sb = new StringBuilder(num);
        for (int i = 0; i < zeros; i++) {
            sb.append('0');
        }
        return sb.toString();
    }

    private String padLeft(String str, int length) {
        StringBuilder sb = new StringBuilder();
        while (sb.length() + str.length() < length) {
            sb.append('0');
        }
        sb.append(str);
        return sb.toString();
    }

    public static void main(String[] args) {
        Problem5 solver = new Problem5();

        String A = "12345678987654321";
        String B = "98765432123456789";

        System.out.println("Brute: " + solver.multBrute(A, B));
        System.out.println("Smart: " + solver.multSmart(A, B));

        // Проверка через BigInteger
        BigInteger bigA = new BigInteger(A);
        BigInteger bigB = new BigInteger(B);
        System.out.println("Valid: " + bigA.multiply(bigB));

        // Benchmarking
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            sb1.append((i * 7) % 10);
            sb2.append((i * 3) % 10);
        }
        String largeA = sb1.toString();
        String largeB = sb2.toString();

        long t1 = System.nanoTime();
        solver.multBrute(largeA, largeB);
        long timeBrute = System.nanoTime() - t1;

        long t2 = System.nanoTime();
        solver.multSmart(largeA, largeB);
        long timeSmart = System.nanoTime() - t2;

        System.out.println("Brute time: " + timeBrute + " ns");
        System.out.println("Smart time: " + timeSmart + " ns");
    }
}
