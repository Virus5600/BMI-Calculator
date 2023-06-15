package com.satch_navida.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.satch_navida.myapplication.validation.Validator;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
	//PUBLIC OBJECTS
	public Spinner weightSpinner, heightSpinner;
	public EditText weightInput, heightInput;
	public Button submitBtn;
	public Map<String, TextView> validationErrMsg;

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

		validationErrMsg = new HashMap<String, TextView>() {
			{
				this.put(getElementId(findViewById(R.id.weight_value_error_msg)), findViewById(R.id.weight_value_error_msg));
				this.put(getElementId(findViewById(R.id.height_value_error_msg)), findViewById(R.id.height_value_error_msg));
				this.put(getElementId(findViewById(R.id.weight_spinner_error_msg)), findViewById(R.id.weight_spinner_error_msg));
				this.put(getElementId(findViewById(R.id.height_spinner_error_msg)), findViewById(R.id.height_spinner_error_msg));
			}
		};
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
		this.validationErrMsg.forEach((String key, TextView val) -> {
			val.setText("");
			val.setTextColor(0xffdc6545);
		});
	}

	/**
	 * Actions to be done when the submit button is clicked.
	 */
	private void onSubmit() {
		String[] ids = new String[] {
				getElementId(weightInput),
				getElementId(heightInput),
				getElementId(weightSpinner),
				getElementId(heightSpinner)
		};
		/**
		 * The values from the form. They'll be identified using their respective IDs.
		 */
		Map<String, Object> values = new HashMap<String, Object>() {
			{
				this.put(ids[0], weightInput.getText());
				this.put(ids[1], heightInput.getText());
				this.put(ids[2], weightSpinner.getSelectedItem().toString());
				this.put(ids[3], heightSpinner.getSelectedItem().toString());
			}
		};

		/**
		 * Validation rules for the weight and height input fields.
		 */
		Map<String, String[]> rules = new HashMap<String, String[]>() {
			{
				this.put(ids[0], new String[] {"Required", "Numeric", "Min:1"});
				this.put(ids[1], new String[] {"Required", "Numeric", "Min:1"});
				this.put(ids[2], new String[] {"Required"});
				this.put(ids[3], new String[] {"Required"});
			}
		};

		/**
		 * Custom validation messages for each rule. This will only be shown if the rules failed.
		 */
		Map<String, String> messages = new HashMap<String, String>() {
			{
				// Weight Validation Messages
				this.put(String.format("%1$s.%2$s", ids[0], "Required"), "The weight is required.");
				this.put(String.format("%1$s.%2$s", ids[0], "Numeric"), "Weight should be a number.");
				this.put(String.format("%1$s.%2$s", ids[0], "Min"), "The value should be no less than :min.");

				// Height Validation Messages
				this.put(String.format("%1$s.%2$s", ids[1], "Required"), "The height is required.");
				this.put(String.format("%1$s.%2$s", ids[1], "Numeric"), "Height should be a number.");
				this.put(String.format("%1$s.%2$s", ids[1], "Min"), "The value should be no less than :min.");

				// Weight Spinner Validation Message
				this.put(String.format("%1$s.%2$s", ids[2], "Required"), "The weight type is required.");

				// Height Spinner Validation Message
				this.put(String.format("%1$s.%2$s", ids[3], "Required"), "The height type is required.");
			}
		};
		Validator validator = new Validator(values, rules, messages);

		// If the validation failed...
		Map<String, Object> validatedFields = validator.validate();

		if (validator.fails()) {
			// Iterate through the invalid fields...
			for (String field : validator.invalidFields())
				this.validationErrMsg.get(field + "_error_msg").setText(validator.errors().first(field));

			// Iterate through the valid fields and remove their error message
			for (String field : validator.validFields())
				this.validationErrMsg.get(field + "_error_msg").setText("");

			// Then stop all process from this function.
			return;
		}
		// Iterate through the valid fields and remove their error message
		else {
			for (String field : validator.validFields())
				this.validationErrMsg.get(field + "_error_msg").setText("");
		}

		// If the validation succeeded, convert the values to kilogram and meter first...
		double weight = Double.parseDouble(validatedFields.get(ids[0]).toString()),
				height = Double.parseDouble(validatedFields.get(ids[1]).toString());

		if (validatedFields.get(ids[2]).toString().equalsIgnoreCase("lbs"))
			weight /= 2.205;

		if (validatedFields.get(ids[3]).toString().equalsIgnoreCase("ft/in"))
			height *= 30.48;
		height /= 100;

		// Then pass them here
		double bmi = this.computeBMI(weight, height);

		String alertMsg = "Your BMI is " + String.format("%.2f", (float) bmi) + " which means you're %1$s.";
		if (bmi < 18.5)
			alertMsg = String.format(alertMsg, "Underweight");
		else if (bmi >= 18.5 && bmi <= 24.9)
			alertMsg = String.format(alertMsg, "Healthy Weight");
		else if (bmi >= 25.0 && bmi <= 29.9)
			alertMsg = String.format(alertMsg, "Overweight");
		else if (bmi > 30)
			alertMsg = String.format(alertMsg, "Obese");

		// Build the alert dialog
		Builder alert = new AlertDialog.Builder(MainActivity.this);
		alert.setMessage(alertMsg)
				.setTitle(R.string.alert_title);

		// Shows the alert
		alert.create()
			.show();
	}

	/**
	 * Fetches the provided string ID of an element.
	 *
	 * @param element The element in question.
	 *
	 * @return String The string ID of the provided {@link View} element.
	 */
	private String getElementId(View element) {
		return getResources().getResourceEntryName(element.getId());
	}

	// PUBLIC METHODS

	/**
	 * Calculates the BMI using the given {@code weight} and {@code height}. The measurement unit
	 * that is used for the computation is at <b><i>kg</i></b> and <b><i>m</i></b> (Metric System)
	 * and thus, all Imperial measurements needs to be converted before being passed to the method.<br>
	 * <br>
	 * The formula used for this method is <b><u>{@code BMI = kg/m²}</u></b>.
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