package adefault.postfixcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.List;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.math.BigDecimal;


public class MainActivity extends AppCompatActivity {

    EditText edittxtislem;
    Button btnhesap;
    TextView lbsonuc1;
    TextView lbsonucpostfix;
    TextView lbsonucinfix;
   // TextView lbfarklıyol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edittxtislem = (EditText) findViewById(R.id.txtislem);
        btnhesap = (Button) findViewById(R.id.btnHesapla);
        lbsonuc1 = (TextView) findViewById(R.id.lbsonucshow);
        lbsonucinfix = (TextView) findViewById(R.id.lbinfixshow);
        lbsonucpostfix = (TextView) findViewById(R.id.lbpostfixshow);
     //   lbfarklıyol=(TextView) findViewById(R.id.lbfarklı);

        edittxtislem.setText("3+8*((2-9/3+5)-((12+2)/2)*3)+5");

        btnhesap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String expression = edittxtislem.getText().toString();
                PostFixConverter postfixconvert = new PostFixConverter(expression);
                postfixconvert.printExpression();
                PostFixCalculator postfixresult = new PostFixCalculator(postfixconvert.getPostfixAsList());

                lbsonuc1.setText(" Sonuç :" + postfixresult.result());
                lbsonucinfix.setText("İnfix :" + expression);

            }
        });
    }
    public class PostFixConverter {
        private String infixexpression;
        private Deque<Character> stack = new ArrayDeque<Character>();
        public List<String> postfixexpression = new ArrayList<String>();
        public List<String> postfixshow = new ArrayList<String>();


        public PostFixConverter(String expression) {
            infixexpression = expression;
            convertExpression();
        }

        private void convertExpression()
        {
            StringBuilder temp = new StringBuilder();

            for (int i = 0; i != infixexpression.length(); ++i) {
                if (Character.isDigit(infixexpression.charAt(i))) {
                    temp.append(infixexpression.charAt(i));

                    while ((i + 1) != infixexpression.length() && (Character.isDigit(infixexpression.charAt(i + 1))
                            || infixexpression.charAt(i + 1) == '.')) {
                        temp.append(infixexpression.charAt(++i));
                    }
                    postfixexpression.add(temp.toString());
                    temp.delete(0, temp.length());
                }
                else
                    inputToStack(infixexpression.charAt(i));
            }
            clearStack();
        }

        private void inputToStack(char input) {
            if (stack.isEmpty() || input == '(')
                stack.addLast(input);
            else {
                if (input == ')') {
                    while (!stack.getLast().equals('(')) {
                        postfixexpression.add(stack.removeLast().toString());
                    }
                    stack.removeLast();
                } else {
                    if (stack.getLast().equals('('))
                        stack.addLast(input);
                    else {
                        while (!stack.isEmpty() && !stack.getLast().equals('(') &&
                                getPrecedence(input) <= getPrecedence(stack.getLast())) {
                            postfixexpression.add(stack.removeLast().toString());
                        }
                        stack.addLast(input);
                    }
                }
            }
        }
        private int getPrecedence(char op) {
            if (op == '+' || op == '-')
                return 1;
            else if (op == '*' || op == '/')
                return 2;
            else if (op == '^')
                return 3;
            else return 0;
        }

        private void clearStack() {
            while (!stack.isEmpty()) {
                postfixexpression.add(stack.removeLast().toString());
            }
        }

        public void printExpression() {
            for (String str : postfixexpression) {

               // lbsonucpostfix.setText("Postfix:"+ postfixexpression);
            }
            postfixshow=postfixexpression;

            StringBuilder sb = new StringBuilder();
            for (String s : postfixshow)
            {
                sb.append(s);
                sb.append("\t");
            }


            lbsonucpostfix.setText("Postfix:"+sb);
        }

        public List<String> getPostfixAsList() {

            return postfixexpression;
        }
    }

    public class PostFixCalculator {
        private List<String> expression = new ArrayList<String>();
        private Deque<Double> stack = new ArrayDeque<Double>();


        public PostFixCalculator(List<String> postfix) {
            expression = postfix;

        }


        public BigDecimal result() {
            for (int i = 0; i != expression.size(); ++i) {


                if (Character.isDigit(expression.get(i).charAt(0))) {
                    stack.addLast(Double.parseDouble(expression.get(i)));
                } else {
                    double tempResult = 0;
                    double temp;

                    switch (expression.get(i)) {
                        case "+":
                            temp = stack.removeLast();
                            tempResult = stack.removeLast() + temp;
                            break;

                        case "-":
                            temp = stack.removeLast();
                            tempResult = stack.removeLast() - temp;
                            break;

                        case "*":
                            temp = stack.removeLast();
                            tempResult = stack.removeLast() * temp;
                            break;

                        case "/":

                            temp = stack.removeLast();
                            if (temp==0 )
                            {
                            lbsonuc1.setText("Sonuc:Sıfır'a bölme işlemi yaptınız");
                            }
                          else
                                tempResult = stack.removeLast() / temp;
                            break;
                    }
                    stack.addLast(tempResult);

                }
            }
            return new BigDecimal(stack.removeLast()).setScale(3, BigDecimal.ROUND_HALF_UP);
        }
    }

}






