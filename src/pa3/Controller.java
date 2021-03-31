package pa3;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    private Text EOutputValue;

    private int n = 59503;

    private BigInteger p;
    private BigInteger q;

    public void initialize() {
        calculatePQ();
        calculateE();
    }

    private void calculatePQ() {
        long start = System.nanoTime();
        List<Integer> factors = findPrimeFactors(n);
        long end = System.nanoTime();

        if (factors.size() <= 2) {
            p = new BigInteger(String.valueOf(factors.get(0)));
            q = new BigInteger(String.valueOf(factors.get(1)));
            System.out.printf("p is %d\nq is %d\n", factors.get(0), factors.get(1));
            System.out.printf("Amount of time busy finding p and q: %fms\n", (double) (end - start) / 1000000);
        } else {
            System.out.println("This number does not have 2 prime factors");
        }
    }

    private List<Integer> findPrimeFactors(int n) {
        List<Integer> factors = new ArrayList<>();

        for (int i = 2; i < n; i++) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }

        if (n > 2) factors.add(n);

        return factors;
    }

    @FXML
    private void calculateE() {
        BigInteger temp = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = BigInteger.ONE;

//        This works, but almost always gives 5, so we made a different solution
//        for (BigInteger x = BigInteger.valueOf(3); temp.compareTo(x) > 0; x = x.add(BigInteger.ONE)) {
//            if (temp.gcd(x).equals(BigInteger.ONE)) {
//                e = x;
//                break;
//            }
//        }

        ArrayList<BigInteger> results = new ArrayList<>();

        for (BigInteger x = BigInteger.valueOf(3); temp.compareTo(x) > 0; x = x.add(BigInteger.ONE)) {
            if (temp.gcd(x).equals(BigInteger.ONE)) {
                results.add(x);
            }
        }

        int randomIndex = (int) (Math.random() * results.size());

        e = results.get(randomIndex);

        EOutputValue.setText("e is " + e.toString());

        System.out.println("e = " + e.toString() + " (click to regenerate)");
    }
}
