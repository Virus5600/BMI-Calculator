package com.satch_navida.myapplication.validation.rules;

import java.util.HashMap;

/**
 * Tests whether the {@code value} provided is present and a valid value.
 *
 * @author Virus5600
 * @version 1.0.0
 */
public class Required extends Rule implements RuleInterface {

	// OVERRIDE VARIABLES
	protected String message = "The :key field is required";

	// CONSTRUCTORS

	/**
	 * Creates an instance of {@link Rule}, containing all the necessary parameters: the
	 * {@code key} and {@code value}, and some optional parameter: {@code message}.<br>
	 *
	 * @param key A unique identifier (ID) of value being tested.
	 * @param value The value that will be tested.
	 * @param message A message that will be displayed when the test fails.
	 */
	public Required(String key, Object value, String message) {
		super(key, value, message);
	}

	/**
	 * Creates an instance of {@link Rule}, containing all the necessary parameters: the
	 * {@code key} and {@code value}.<br>
	 *
	 * @param key A unique identifier (ID) of value being tested.
	 * @param value The value that will be tested.
	 */
	public Required(String key, Object value) {
		super(key, value);
	}

	// PUBLIC METHOD

	@Override
	/**
	 * {@inheritDoc}
	 */
	public HashMap<String, Object> validate() {
		if (this.value == null || this.value.toString().isEmpty())
			this.setValid(false);
		else
			this.setValid(true);

		return new HashMap<String, Object>() {
			{
				this.put(Rule.VALIDATED_KEYS[0], isValid());
				this.put(Rule.VALIDATED_KEYS[1], isValid() ? "" : getFinalMessage());
				this.put(Rule.VALIDATED_KEYS[2], getRunOtherValidations());
			}
		};
	}
}
