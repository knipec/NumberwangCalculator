package com.knipec.numberwangcalc.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.util.Random;


public class CalculatorActivity extends Activity implements View.OnClickListener{

    Random rnd = new Random();
    /**
     * Determines number of button clicks left until wangernumb mode becomes numberwang mode
     * If 0, you are in numberwang mode.
     */
    private int wangernumb;

    /**
     * Determines whether or not a function is pending a second "argument"
     * We don't care which function
     */
    private boolean isPendingFunction;

    /**
     * If isPendingFunction is true, indicates whether the pending function has had an argument provided
     * (and hence should be resolved), or hasn't (and can be overwritten)
     */
    private boolean argumentProvided;

    private int timeToNextNumberwang;

    private int defaultOutputTextSize = 27;
    private int defaultOutputTextColor = Color.rgb(0,0,0);

    private Toast lastToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        //Number buttons
        findViewById(R.id.button0).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button8).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);

        //Function buttons
        findViewById(R.id.clearbutton).setOnClickListener(this);
        findViewById(R.id.divbutton).setOnClickListener(this);
        findViewById(R.id.dotbutton).setOnClickListener(this);
        findViewById(R.id.eqbutton).setOnClickListener(this);
        findViewById(R.id.minbutton).setOnClickListener(this);
        findViewById(R.id.multbutton).setOnClickListener(this);
        findViewById(R.id.plusbutton).setOnClickListener(this);
        findViewById(R.id.toggleButton).setOnClickListener(this);

        //state setup
        wangernumb = 0;
        isPendingFunction = false;
        argumentProvided = false;
        //It goes up to 11
        timeToNextNumberwang = (int)(Math.random()*12);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        //Internal state
        savedInstanceState.putInt("wangernumb", wangernumb);
        savedInstanceState.putBoolean("isPendingFunction", isPendingFunction);
        savedInstanceState.putBoolean("argumentProvided", argumentProvided);
        savedInstanceState.putInt("timeToNextNumberwang", timeToNextNumberwang);
        //Output value and operator
        savedInstanceState.putCharSequence("currentOutput", ((TextView) findViewById(R.id.outputDisplay)).getText());
        savedInstanceState.putCharSequence("currentOperator", ((TextView)findViewById(R.id.operatorfield)).getText());
        //Output Field format
        savedInstanceState.putFloat("outputSize", ((TextView) findViewById(R.id.outputDisplay)).getTextSize());
        savedInstanceState.putInt("outputColor", ((TextView) findViewById(R.id.outputDisplay)).getCurrentTextColor());
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        //Internal state
        wangernumb = savedInstanceState.getInt("wangernumb");
        isPendingFunction = savedInstanceState.getBoolean("isPendingFunction");
        argumentProvided = savedInstanceState.getBoolean("argumentProvided");
        timeToNextNumberwang = savedInstanceState.getInt("timeToNextNumberwang");
        //Output value and operator
        ((TextView)findViewById(R.id.outputDisplay)).setText(savedInstanceState.getCharSequence("currentOutput"));
        ((TextView)findViewById(R.id.operatorfield)).setText(savedInstanceState.getCharSequence("currentOperator"));
        //Output field format
        ((TextView)findViewById(R.id.outputDisplay)).setTextSize(TypedValue.COMPLEX_UNIT_PX, savedInstanceState.getFloat("outputSize"));
        ((TextView)findViewById(R.id.outputDisplay)).setTextColor(savedInstanceState.getInt("outputColor"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calculator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() != null)
        {
            if (view.getTag().toString().equals("number"))
            {
                if (wangernumb == 0)
                {
                    handlePossiblyDisplayNumberwang();
                }
                setRandomResult();

                if (isPendingFunction)
                {
                    argumentProvided = true;
                }
            }
            else if (view.getTag().toString().equals("function"))
            {
                if (isPendingFunction && argumentProvided)
                {
                    if (wangernumb == 0)
                    {
                        handlePossiblyDisplayNumberwang();
                    }
                    setRandomResult();
                    argumentProvided = false;
                }
                isPendingFunction= true;

                switch (view.getId())
                {
                    case R.id.plusbutton:
                        setFunctionArea("+");
                        break;
                    case R.id.minbutton:
                        setFunctionArea("-");
                        break;
                    case R.id.multbutton:
                        setFunctionArea("*");
                        break;
                    case R.id.divbutton:
                        setFunctionArea("/");
                        break;
                }
            }
        }
        else if (view.getId() == R.id.eqbutton)
        {
            if (isPendingFunction && argumentProvided)
            {
                if (wangernumb == 0)
                {
                    handlePossiblyDisplayNumberwang();
                }
                setRandomResult();
            }
            isPendingFunction = false;
            argumentProvided = false;
            setFunctionArea("");
        }

        if (view.getId() == R.id.clearbutton)
        {
            clearBehavior();
        }
        else
        {
            resetToDefaultOutputText();
        }

        boolean wasWangernumb;
        if (wangernumb > 0)
        {
            wasWangernumb = true;
            if (view.getId() == R.id.toggleButton)
            {
                wangernumb = 0;
            }
            else
            {
                wangernumb -= 1;
                double r = 1;
                if (wangernumb != 0)
                {
                    r = Math.random();
                }
                if (r < 0.2)
                {
                    displayShortToast(getString(R.string.hmm));
                }
                else if (r < 0.4)
                {
                    displayShortToast(getString(R.string.err));
                }
                else if (r < 0.6)
                {
                    displayShortToast(getString(R.string.ehh));
                }
                else if (r < 0.8)
                {
                    displayShortToast(getString(R.string.ohh));
                }
            }
        }
        else
        {
            wasWangernumb = false;
            if (view.getId() == R.id.toggleButton)
            {
                displayCenteredToast(getString(R.string.lets_play_wangernum), Color.rgb(rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200)));
                // After 20 button clicks, state becomes numberwang mode again
                wangernumb = 10 + rnd.nextInt(10);
            }
        }
        if (wangernumb == 0 && wasWangernumb == true)
        {
            displayCenteredToast(getString(R.string.thats_wangernum), Color.rgb(rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200)));
            ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
            toggle.setChecked(false);
        }
    }

    /**
     * Updated numberwang counter, checks whether to display numberwang, and if it should displays it
     */
    private void handlePossiblyDisplayNumberwang()
    {
        timeToNextNumberwang -= 1;
        if (timeToNextNumberwang <= 0)
        {
            displayCenteredToast(getString(R.string.numberwang_message), Color.rgb(rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200)));
            //It goes up to 11
            timeToNextNumberwang = (int)(Math.random()*12);
        }
    }

    private void displayCenteredToast(String message, int color)
    {
        LayoutInflater inflater = getLayoutInflater();
        View toastLayout = inflater.inflate(R.layout.numberwang_toast, (ViewGroup) findViewById(R.id.numberwang_toast_root));

        TextView text = (TextView) toastLayout.findViewById(R.id.text);
        text.setText(message);
        text.setTextColor(color);

        final Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 500);
    }

    private void displayShortToast(String message)
    {
        final Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();

        if (lastToast != null)
        {
            lastToast.cancel();
        }
        lastToast = toast;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 500);
    }


    private void resetToDefaultOutputText() {
        TextView outputField = (TextView)findViewById(R.id.outputDisplay);
        outputField.setTextColor(defaultOutputTextColor);
        outputField.setTextSize(defaultOutputTextSize);
    }

    private void clearBehavior() {
        TextView outputField = (TextView)findViewById(R.id.outputDisplay);
        outputField.setTextColor(Color.rgb(rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200)));
        outputField.setTextSize(outputField.getTextSize() + 10);
    }

    private void setFunctionArea(String s) {
        TextView functionArea = (TextView)findViewById(R.id.operatorfield);
        functionArea.setText(s);
    }

    /**
     * Generates a random output and puts it into the output field
     */
    private void setRandomResult() {
        TextView outputField = (TextView)findViewById(R.id.outputDisplay);

        String output = "";
        output = output+getRandomInt();

        //5% change of a decimal
        if (Math.random() < .05)
        {
            String decimalPortion = Integer.toString(getRandomPositiveInt());

            output = output.substring(0, Math.min(6, output.length()))+"."+decimalPortion.substring(0, Math.min(6, decimalPortion.length()));
        }
        //2% chance of adding a fraction, mutually exclusive
        else if (Math.random() < .02)
        {
            double fracType = Math.random();
            if (fracType < .33)
            {
                output = output+"\u00BD";
            }
            else if (fracType < .67)
            {
                output = output+"\u2153";
            }
            else
            {
                output = output+"\u00BC";
            }
        }

        if (Math.random() < .02)
        {
            if (!(outputField.getText().charAt(0) == (char)8730))
            {
                output = "\u221A"+outputField.getText().toString();
            }
        }
        outputField.setText(output);
    }

    /**
     * @return A positive integer subject to the same constraints as getRandomInt
     */
    private int getRandomPositiveInt()
    {
        return getRandomInt(false);
    }

    /**
     * @return An integer subject to the same constraints as getrandomint
     */
    private int getRandomInt()
    {
        return getRandomInt(true);
    }

    /**
     * Returns a random integer in a given range. Biased towards small numbers with 90% probability.
     * Returns a negative number with 5% probability
     */
    private int getRandomInt(boolean canBeNegative) {
        int result = 0;
        if (Math.random() < .9)
        {
            result = (int)(Math.random()*500);
        }
        else
        {
            result = (int)(Math.random()*10000000);
        }
        if (Math.random() < .05)
        {
            return result*-1;
        }
        return result;

    }
}
