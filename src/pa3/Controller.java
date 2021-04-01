package pa3;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    private TextArea resultPQ;

    @FXML
    private TextField inputN;

    @FXML
    private Text EOutputValue;

    @FXML
    private TextField messageField;

    @FXML
    private TextArea encryptedMessage;

    private BigInteger n;
    private BigInteger p;
    private BigInteger q;
    private BigInteger e;

    public void initialize() {
        encryptedMessage.setWrapText(true);
        resultPQ.setWrapText(true);
    }

    @FXML
    private void calculatePQ() {
        String input = inputN.getText();

        if (input.isEmpty()) {
            resultPQ.setText("Please enter a value for n");
            return;
        }

        if (!input.matches("\\d*")) {
            inputN.setText(input.replaceAll("[^\\d]", ""));
            input = inputN.getText();
        }

        n = BigInteger.valueOf(Integer.parseInt(input));

        long start = System.nanoTime();
        List<BigInteger> factors = findPrimeFactors(n);
        long end = System.nanoTime();

        if (factors.size() > 2) {
            resultPQ.setText("This number does not have 2 prime factors");
            p = null;
            q = null;
            return;
        }

        p = new BigInteger(String.valueOf(factors.get(0)));
        q = new BigInteger(String.valueOf(factors.get(1)));

        String result = "p is " + factors.get(0) + "\nq is " + factors.get(1);
        result += "\nAmount of time busy finding p and q: " + (double) (end - start) / 1000000 + "ms\n";

        resultPQ.setText(result);
        System.out.println(result);
    }

    private List<BigInteger> findPrimeFactors(BigInteger n) {
        List<BigInteger> factors = new ArrayList<>();

        for (BigInteger i = BigInteger.TWO; i.compareTo(n) < 0; i = i.add(BigInteger.ONE)) {
            while (n.mod(i).equals(BigInteger.ZERO)) {
                factors.add(i);
                n = n.divide(i);
            }
        }

        if (n.compareTo(BigInteger.TWO) > 0) factors.add(n);

        return factors;
    }

    @FXML
    private void calculateE() {
        if (p == null && q == null) {
            EOutputValue.setText("Calculate p and q first");
            return;
        }

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

                    BigInteger result = pow(charNumber, e).mod(n);

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
}
