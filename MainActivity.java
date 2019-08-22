package com.example.constructioncalculater;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing all objects used to retrieve information from the user
        final Spinner spinner = findViewById(R.id.spinner);
        final Button calculateButton = findViewById(R.id.buttonCalculate);
        final Button outputFormatButton = findViewById(R.id.outputFormatButton);

        // Setting up the spinner to read out the operations
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.calculations, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Output Button switches text to show the output format
        outputFormatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (outputFormatButton.getText().toString().contains("Output Tenths")) {
                    outputFormatButton.setText(R.string.outputStandard);
                }
                else{
                    outputFormatButton.setText(R.string.outputTenths);
                }
            }
        });
        //


        //
        //Calculate button performs the operation
        calculateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //When clicked this retrieves all the user input
                EditText input1 = findViewById(R.id.editTextInput1);
                TextView answerTextView = findViewById(R.id.answerTextView);
                String spinnerInput = spinner.getSelectedItem().toString();
                String outputFormat = outputFormatButton.getText().toString();

                // Initializes the two input values locally
                double double1 = 0.0, double2 = 0.0;

                //checking if input1 is empty, if so make toast.
                if (input1.getText().toString().trim().equals("")) {
                   Toast.makeText(MainActivity.this, "You did'nt input anything!",Toast.LENGTH_LONG).show();
                }

                //If input1 contains a value, format it to tenths for internal operations
                else{
                    double1 += formatInput(input1);
                }

                //Make sure the operation doesn't only require input1.
                //Im sure there is a better way of doing this
                if (spinnerInput.contains("Tenths to Standard") || spinnerInput.contains("Standard to Tenths")){

                }

                //Runs when the user operation requires the second EditText
                else {
                    EditText input2 = findViewById(R.id.editTextInput2);

                    // Make sure that EditText isn't empty
                    if (input2.getText().toString().trim().equals("")) {
                        Toast.makeText(MainActivity.this, "You did'nt input a second number!", Toast.LENGTH_LONG).show();
                    }
                    // Formats input2 into tenths
                    else {
                        double2 += formatInput(input2);
                    }
                }

                // Takes in user input, completes operation and returns String containing answer
                // Sets the TextView text to the output string
                answerTextView.setText(calculate(double1, double2, spinnerInput, outputFormat));

            }
        });
    }

    //Spinner operations
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        adapterView.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(MainActivity.this,"You Did'nt Select an Operation", Toast.LENGTH_LONG).show();

    }

    //This method formats the string input from EditText to tenths(double).
    public double formatInput(EditText text){

        //Set the local string to the EditText string input
        String tempString = text.getText().toString();

        //Initializing the output double
        double outputDouble = 0.0;

        //Checking if the input is in Standard format
        //Then separate the feet and the inches into two different variables
        if (tempString.contains("\'")&& tempString.contains("\"")) {
            double feet = Double.parseDouble(tempString.substring(0,tempString.indexOf("\'")));
            double inches = Double.parseDouble("." + tempString.substring(tempString.indexOf("\'") + 1,tempString.indexOf("\"")));

            //changes inches to tenths of feet then add them back together
            inches = inches * (10.0/12.0);
            feet = feet + inches;
            outputDouble += feet;
        }

        // checks if it only contains feet in Standard format
        else if (tempString.contains("\'")){
            double feet = Double.parseDouble(tempString.replace("\'", " ").trim());
            outputDouble += feet;
        }

        //checks if its only contains inches in Standard input
        else if (tempString.contains("\"")){
            double inches = Double.parseDouble("." + tempString.replace("\"", " ").trim());
            if (inches >= 0.12){
                inches /= 0.12;
                String temp = Double.toString(inches);
                double feet = Double.parseDouble(temp.substring(0, temp.indexOf("\\.")));
                inches = Double.parseDouble("." + temp.substring(temp.indexOf("\\."), temp.length()- 1));
                inches /= (10/12);
                outputDouble = feet + inches;

            }
            else {
                outputDouble += inches * (10.0 / 12.0);
            }
        }

        // Checks if its already in tenths format
        else if (tempString.contains("\\.")) {
            outputDouble += Double.parseDouble(tempString);
        }

        //If its a whole number it treats it like tenths format
        else {
            outputDouble += Double.parseDouble(tempString);
        }

        return outputDouble;
    }

    //calculate reads the operation input and reads the desired output and formats it.
    public String calculate(double input1, double input2, String operation, String outputFormat){
        String answer = "";

        //only takes one input
        if (operation.contains("Tenths to Standard")){
            if (input1 <= 1.0){
                input1 = input1 /(10.0/12.0);
                String str = Double.toString(input1);
                String[] strArray = str.split("\\.");
                answer = strArray[1] + "\"";
            }
            else{
                answer = outputToStandard(input1);
            }
        }

        else if(operation.contains("Standard to Tenths")){
            answer = Double.toString(input1);
        }
        else if(operation.contains("Add")){
            input1 += input2;
            if (outputFormat.contains("Output Tenths")){
                answer = Double.toString(input1);
            }
            else{
                answer = outputToStandard(input1);
            }
        }
        else if(operation.contains("Subtract")){
            input1 -= input2;
            if (outputFormat.contains("Output Tenths")){
                answer = Double.toString(input1);
            }
            else{
                answer = outputToStandard(input1);
            }
        }
        else if(operation.contains("Multiply")) {
            input1 *= input2;
            if (outputFormat.contains("Output Tenths")){
                answer = Double.toString(input1);
            }
            else{
                answer = outputToStandard(input1);
            }
        }
        else if(operation.contains("Divide")) {
            input1 /= input2;
            if (outputFormat.contains("Output Tenths")){
                answer = Double.toString(input1);
            }
            else{
                answer = outputToStandard(input1);
            }
        }

        return answer;
    }

    //takes in a double and outputs a fraction to compute fractions of inches
    public static String fraction(double x) {
        String a = "" + x;
        String splits[] = a.split("\\."); // split using decimal
        int b = splits[1].length(); // find the decimal length
        int denominator = (int) Math.pow(10, b); // calculate the denominator
        int numerator = (int) (x * denominator); // calculate the numerator
        int gcd = getGCD(numerator, denominator); // Find the greatest common divisor
        String fraction = "" + numerator / gcd + "/" + denominator / gcd;
        return fraction;
    }

    //recursive function to calculate greatest common denominator
    public static int getGCD(int n1, int n2) {
        if (n2 == 0) {
            return n1;
        }
        return getGCD(n2, n1 % n2);
    }

    // formats the double input to Standard format
    public static String outputToStandard(Double input1){
        String answer;
        //check if input1 is less than a foot
        if (input1 < 1.0){
            //resets the input t inch format
            input1 /= (10.0/12.0);
            String str = Double.toString(input1);
            // checks if input is a whole inch and formats otherwise
            if (str.length() > 3){
                String[] strArray = str.split("\\.");
                char[] remainderArray = strArray[1].toCharArray();
                String remainder = fraction(Double.parseDouble("." + remainderArray[1]));
                answer = remainderArray[0] + " " + remainder + "\"";
            }
            else {
                String[] strArray = str.split("\\.");
                answer = strArray[1] + "\"";
            }
        }
        //when the output is more then a foot this runs and formats it to standard
        else{
            String str = Double.toString(input1);
            String[] strArray = str.split("\\.");
            String feet = strArray[0];
            double inches = Double.parseDouble("." + strArray[1]);
            if (inches == 0.0){
                answer = feet + "\'";
            }
            else {

                inches /= (10.0 / 12.0);
                String inchesRemainder = Double.toString(inches);
                if (inchesRemainder.length() > 3){
                    String[] inchArray = inchesRemainder.split("\\.");
                    char[] remainderArray = inchArray[1].toCharArray();
                    String remainder = fraction(Double.parseDouble("." + remainderArray[1]));
                    answer = feet + "\'" + " " + remainderArray[0] + " " + remainder + "\"";
                }
                else{
                    String[] inchArray = inchesRemainder.split("\\.");
                    answer = feet + "\'" + " " + inchArray[1] + "\"";
                }
            }
        }
        return answer;
    }
}

