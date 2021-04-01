package pa3;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    private Text EOutputValue;

    @FXML
    private TextField messageField;

    @FXML
    private TextArea encryptedMessage;

    @FXML
    private TextField inputNDecrypt;

    @FXML
    private TextField inputEDecrypt;

    @FXML
    private TextField dOutput;

    private int n = 323;

    private BigInteger p = BigInteger.valueOf(17);
    private BigInteger q = BigInteger.valueOf(19);
    private BigInteger e;

    public void initialize() {
        encryptedMessage.setWrapText(true); //WHY THE FUCK DO I HAVE TO SET IT LIKE THIS, AND NOT JUST ON THE ELEMENT IN THE FUCKING FXML FILE

//        calculatePQ();
//        calculateE();
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

    @FXML
    private void encryptMessage(){
        //check if e is set
        if (e == null){
            encryptedMessage.setText("e not calculated, please calculate e first. ");
        }
        else{
            String text = messageField.getText();

            //check if message is set
            if (text.length() == 0){
                encryptedMessage.setText("No message given ");
            }
            else{
                ArrayList<BigInteger> resultList = new ArrayList<>();

                //encrypt all chars
                for (char character: text.toCharArray()) {
                    BigInteger charNumber = BigInteger.valueOf((int)character);

                    BigInteger result = pow(charNumber, e).mod(BigInteger.valueOf(n));

                    resultList.add(result);
                }

                //use string builder for output generation of final crypt string.
                StringBuilder stringBuilder = new StringBuilder();
                for (BigInteger result: resultList) {
                    stringBuilder.append(result.toString());
                    stringBuilder.append(", ");
                }
                stringBuilder.setLength(stringBuilder.length() - 2);
                String resultString = stringBuilder.toString();

                System.out.println("encryptedMessage: " + resultString);

                encryptedMessage.setText(resultString);
            }
        }
    }

    //method for calculating power on BigIntegers
    BigInteger pow(BigInteger base, BigInteger exponent) {
        BigInteger result = BigInteger.ONE;
        while (exponent.signum() > 0) {
            if (exponent.testBit(0)) result = result.multiply(base);
            base = base.multiply(base);
            exponent = exponent.shiftRight(1);
        }
        return result;
    }

    @FXML
    private void calculateD(){
        String nString = inputNDecrypt.getText();
        String eString = inputEDecrypt.getText();

        if (nString.isEmpty() || eString.isEmpty()){
            System.out.println("n or e empty");

            dOutput.setText("n or e empty");
            return;
        }

        BigInteger n = BigInteger.valueOf(Long.parseLong(nString));
        BigInteger e = BigInteger.valueOf(Long.parseLong(eString));

//        BigInteger[] pq = calculatePQForDecrypt(n);

//        BigInteger p = pq[0];
//        BigInteger q = pq[1];

        BigInteger p = BigInteger.valueOf(17);
        BigInteger q = BigInteger.valueOf(19);

        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        BigInteger d = e.modInverse(phi);

        System.out.println("d = " + d.toString());

        dOutput.setText("d is " + d.toString());
    }

//    private BigInteger[] calculatePQForDecrypt(BigInteger n) {
//        List<Integer> factors = findPrimeFactors(n);
//
//        if (factors.size() <= 2) {
//            BigInteger p = new BigInteger(String.valueOf(factors.get(0)));
//            BigInteger q = new BigInteger(String.valueOf(factors.get(1)));
//            System.out.printf("p is %d\nq is %d\n", factors.get(0), factors.get(1));
//
//        } else {
//            BigInteger p = new BigInteger(String.valueOf(1));
//            BigInteger q = new BigInteger(String.valueOf(1));
//
//            System.out.println("This number does not have 2 prime factors");
//        }
//
//        BigInteger[] test = new BigInteger[2];
//        test[0] = p;
//        test[1] = q;
//
//        return test;
//    }

}
