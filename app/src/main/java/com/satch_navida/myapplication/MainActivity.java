package com.satch_navida.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
	//PUBLIC OBJECTS
	Spinner weightSpinner, heightSpinner;
	EditText weightInput, heightInput;
	Button submitBtn;
	TextView weightErrMsg, heightErrMsg;

	// PRIVATE OBJECTS

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// CODE START
		this.initializeObjects();
		this.initializeValues();
	}

	// PRIVATE METHODS

	/**
	 * Initializes class variables as needed.
	 */
	private void initializeObjects() {
		this.weightSpinner = findViewById(R.id.weight_spinner);
		this.weightInput = findViewById(R.id.weight_value);

		this.heightSpinner = findViewById(R.id.height_spinner);
		this.heightInput = findViewById(R.id.height_value);

		this.submitBtn = findViewById(R.id.submit_btn);

		this.weightErrMsg = findViewById(R.id.weight_error_msg);
		this.heightErrMsg = findViewById(R.id.height_error_msg);
	}

	/**
	 * Initializes the values of some objects within the context.
	 */
	private void initializeValues() {
		this.weightSpinner.setSelection(0);
		this.heightSpinner.setSelection(0);

		this.submitBtn.setOnClickListener((e) -> {
			onSubmit();
		});

		// Sets the validation error message texts' content to none and its color similar to Bootstrap's danger color.
		this.weightErrMsg.setText("");
		this.weightErrMsg.setTextColor(0xffdc3545);
		this.heightErrMsg.setText("");
		this.heightErrMsg.setTextColor(0xffdc3545);
	}

	/**
	 * Actions to be done when the submit button is clicked.
	 */
	private void onSubmit() {
		double weight = Double.parseDouble(this.weightInput.getText().toString().isEmpty() ? "0" : this.weightInput.getText().toString());
		double height = Double.parseDouble(this.heightInput.getText().toString().isEmpty() ? "0" : this.heightInput.getText().toString());

		double bmi = computeBMI(weight, height);
	}

	// PUBLIC METHODS

	/**
	 * Calculates the BMI using the given {@code weight} and {@code height}. The measurement unit
	 * that is used for the computation is at <b><i>kg</i></b> and <b><i>m</i></b> (Metric System)
	 * and thus, all Imperial measurements needs to be converted before being passed to the method.
	 *
	 * @param weight The weight of the body being computed (in Metric Measurement)
	 * @param height The height of the body being computed (in Metric Measurement)
	 *
	 * @return float The expected BMI of the body, based on what the given {@code weight} and
	 * {@code height} is.
	 */
	public double computeBMI(double weight, double height) {
		return weight / Math.pow(height, 2);
	}
}