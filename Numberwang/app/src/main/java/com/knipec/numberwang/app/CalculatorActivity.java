package com.knipec.numberwang.app;

import com.knipec.numberwang.app.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class CalculatorActivity extends Activity implements View.OnClickListener{

    /**
     * Determines whether or not we are currently in wangernumb mode
     */
    private boolean wangernumb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        //Set click listeners
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
        wangernumb = false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() != null)
        {
            if (view.getTag().toString().equals("number"))
            {
                setRandomResult();
            }
            else if (view.getTag().toString().equals("function"))
            {

            }
        }
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
            output = output.substring(0, 6)+"."+Integer.toString(getRandomPositiveInt()).substring(0, 6);
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
