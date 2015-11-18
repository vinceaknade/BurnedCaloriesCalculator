package com.beech.burnedcaloriescalculator;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.TextView.OnEditorActionListener;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class BurnedCaloriesCalculatorActivity extends Activity
implements OnEditorActionListener{

    // define the SharedPreferences object
    private SharedPreferences savedValues;

    private EditText txtWeight;
    private EditText txtName;

    private TextView lblMilesRan;
    private TextView lblCaloriesBurned;
    private TextView lblBMI;

    private SeekBar sbarMilesRan;

    private Spinner spinHeightFeet;
    private Spinner spinHeightInches;

    public float weight;
    public int milesRan;
    public int feet;
    public int inches;
    public String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burned_calories_calculator);

        weight = 0f;
        milesRan = 0;
        feet = 3;
        inches = 0;
        name = "nobody";

        txtWeight = (EditText) findViewById(R.id.txtWeight);
        txtName = (EditText) findViewById(R.id.txtName);

        lblMilesRan = (TextView) findViewById(R.id.lblDisplayMilesRan);
        lblCaloriesBurned = (TextView) findViewById(R.id.lblDisplayCaloriesBurned);
        lblBMI = (TextView) findViewById(R.id.lblDisplayBMI);

        sbarMilesRan = (SeekBar) findViewById(R.id.sbarMilesRan);

        spinHeightFeet = (Spinner) findViewById(R.id.spinHeightFeet);
        spinHeightInches = (Spinner) findViewById(R.id.spinHeightInches);

        // set array adapter for spinners
        ArrayAdapter<CharSequence> adapterFeet = ArrayAdapter.createFromResource(
                this, R.array.feet, android.R.layout.simple_spinner_item);
        adapterFeet.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinHeightFeet.setAdapter(adapterFeet);

        ArrayAdapter<CharSequence> adapterInches = ArrayAdapter.createFromResource(
                this, R.array.inches, android.R.layout.simple_spinner_item);
        adapterInches.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinHeightInches.setAdapter(adapterInches);

        txtWeight.setOnEditorActionListener(this);
        txtName.setOnEditorActionListener(this);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

        sbarMilesRan.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lblMilesRan.setText(seekBar.getProgress() + "mi");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekbar)
            {

            }
        });
    }

    @Override
    public void onPause() {
        // save the instance variables
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putFloat("weight", weight);
        editor.putInt("milesRan", milesRan);
        editor.putInt("feet", feet);
        editor.putInt("inches", inches);
        editor.putString("name", name);
        editor.commit();

        super.onPause();
    }

    public void onResume() {
        super.onResume();

        // get the instance variables
        weight = savedValues.getFloat("weight", 0f);
        milesRan = savedValues.getInt("milesRan", 0);
        feet = savedValues.getInt("feet", 3);
        inches = savedValues.getInt("inches", 0);
        name = savedValues.getString("name", "nobody");

        txtWeight.setText(weight + "");
        sbarMilesRan.setProgress(milesRan);
        spinHeightFeet.setSelection(feet - 3);
        spinHeightInches.setSelection(inches);
        txtName.setText(name);

        calculateAndDisplay();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            calculateAndDisplay();
        }
        return false;
    }

    private void calculateAndDisplay()
    {
        float caloriesBurned;
        String strWeight = txtWeight.getText().toString();
        milesRan = sbarMilesRan.getProgress();

        float bmi = 0f;
        String strFeet = spinHeightFeet.getSelectedItem().toString();
        String strInches = spinHeightFeet.getSelectedItem().toString();

        // catch all bad solution but in a hurry
        try
        {
            weight = Float.parseFloat(strWeight);
        }
        catch(Exception e)
        {
            weight = 0f;
        }

        try
        {
            feet = Integer.parseInt(strFeet);
        }
        catch(Exception e)
        {
            feet = 3;
        }

        try
        {
            inches = Integer.parseInt(strInches);
        }
        catch(Exception e)
        {
            inches = 0;
        }

        caloriesBurned = 0.75f * weight * milesRan;

        bmi = (weight * 703) / ((12 * feet + inches) * (12 * feet + inches));

        lblCaloriesBurned.setText(caloriesBurned + "");
        lblBMI.setText(bmi + "");
    }
}
