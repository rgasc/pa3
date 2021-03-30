package pa3;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    public List<Integer> findPrimeFactors(int n) {
        List<Integer> factors = new ArrayList<>();

        for (int i = 2; i < n; i++) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }

        if (n > 2) {
            factors.add(n);
        }

        return factors;
    }

    public void firstStep(int n) {
        long start = System.nanoTime();
        List<Integer> factors = findPrimeFactors(n);
        long end = System.nanoTime();

        if (factors.size() <= 2) {
            System.out.printf("p is %d\nq is %d\n", factors.get(0), factors.get(1));
            System.out.printf("Amount of time busy finding p and q: %fms", (double) (end - start) / 1000000);
        } else {
            System.out.println("This number does not have 2 prime factors");
        }
    }
}
