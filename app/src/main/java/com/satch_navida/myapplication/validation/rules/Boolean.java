package com.satch_navida.myapplication.validation.rules;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Predicate;

/**
 * Tests whether the value is a boolean or can be converted to a boolean.
 *
 * @author Virus5600
 * @version 1.0.0
 */
public class Boolean extends Rule implements RuleInterface {

	// OVERRIDE VARIABLES
	protected String message = "The :key must either be true or false";

	// CONSTRUCTORS
	/**
	 * Creates an instance of {@link Rule}, containing all the necessary parameters: the
	 * {@code key} and {@code value}, and some optional parameter: {@code message}.<br>
	 *
	 * @param key A unique identifier (ID) of value being tested.
	 * @param value The value that will be tested.
	 * @param message A message that will be displayed when the test fails.
	 */
	public Boolean(String key, Object value, String message) {
		super(key, value, message);
	}

	/**
	 * Creates an instance of {@link Rule}, containing all the necessary parameters: the
	 * {@code key} and {@code value}.<br>
	 *
	 * @param key A unique identifier (ID) of value being tested.
	 * @param value The value that will be tested.
	 */
	public Boolean(String key, Object value) {
		super(key, value);
	}

	// PUBLIC METHOD

	@Override
	/**
	 * {@inheritDoc}
	 */
	public HashMap<String, Object> validate() {
		String[] matches = new String[]{"0", "1", "true", "false", "on", "off"};

		if (Arrays.stream(matches).anyMatch(Predicate.isEqual(this.value.toString().toLowerCase())))
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
