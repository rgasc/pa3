package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.math.BigInteger;
import java.util.ArrayList;

public class Controller {

    @FXML
    private Text EOutputValue;

    private BigInteger p = new BigInteger(String.valueOf(157)); //todo insert calculated number
    private BigInteger q = new BigInteger(String.valueOf(379));; //todo insert calculated number

    public void initialize(){

    }

    @FXML
    private void calculateE(){
        BigInteger temp = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)) ;
        BigInteger e = BigInteger.ONE;

        /** This works, but almost always gives 5, so we made a different solution
        for (BigInteger x = BigInteger.valueOf(3); temp.compareTo(x) > 0; x = x.add(BigInteger.ONE)){
            if(temp.gcd(x).equals(BigInteger.ONE)){
                e = x;
                break;
            }
        }
         */

        ArrayList<BigInteger> results = new ArrayList<>();

        for (BigInteger x = BigInteger.valueOf(3); temp.compareTo(x) > 0; x = x.add(BigInteger.ONE)){
            if(temp.gcd(x).equals(BigInteger.ONE)){
                results.add(x);
            }
        }

        int randomIndex = (int) (Math.random() * results.size());

        e = results.get(randomIndex);

        EOutputValue.setText("e is " + e.toString());

        System.out.println("e = " + e.toString() + " (click to regenerate)");
    }
}
