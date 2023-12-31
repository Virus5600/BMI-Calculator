package com.satch_navida.myapplication.validation.rules;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * Tests whether the {@code value} provided is a number or can be converted to a number.
 *
 * @author Virus5600
 * @version 1.0.0
 */
public class Numeric extends Rule implements RuleInterface {

	// OVERRIDE VARIABLES
	protected String message = "The :key should be a number";
	// CONSTRUCTORS
	/**
	 * Creates an instance of {@link Rule}, containing all the necessary parameters: the
	 * {@code key} and {@code value}, and optional parameters: {@code message} and {@code validatorValues}.<br>
	 *
	 * @param key A unique identifier (ID) of value being tested.
	 * @param value The value that will be tested.
	 * @param message A message that will be displayed when the test fails.
	 * @param validatorValues An array of {@link Object}s that will be used to test against the {@code value}.
	 */
	public Numeric(@NotNull String key, @NotNull Object value, @Nullable String message, @Nullable Object[] validatorValues) {
		super(key, value, message, validatorValues);
	}

	// PUBLIC METHOD

	@Override
	/**
	 * {@inheritDoc}
	 */
	public HashMap<String, Object> validate() {
		boolean isValid = true;
		try {
			Double.parseDouble(this.value.toString());
		} catch (NumberFormatException e) {
			isValid = false;
		}

		this.setValid(isValid);

		return new HashMap<String, Object>() {
			{
				this.put(Rule.VALIDATED_KEYS[0], isValid());
				this.put(Rule.VALIDATED_KEYS[1], isValid() ? "" : getFinalMessage());
				this.put(Rule.VALIDATED_KEYS[2], getRunOtherValidations());
			}
		};
	}
}
