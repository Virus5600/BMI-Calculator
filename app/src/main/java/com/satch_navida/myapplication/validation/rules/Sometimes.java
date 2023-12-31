package com.satch_navida.myapplication.validation.rules;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * Works similar as the {@link Required} rule but only works if the value is present.
 *
 * @author Virus5600
 * @version 1.0.0
 */
public class Sometimes extends Rule implements RuleInterface {

	// OVERRIDE VARIABLES
	protected String message = "The :key field is required to have a value, or leave it as blank";

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
	public Sometimes(@NotNull String key, @NotNull Object value, @Nullable String message, @Nullable Object[] validatorValues) {
		super(key, value, message, validatorValues);
	}

	// PUBLIC METHOD

	@Override
	/**
	 * {@inheritDoc}
	 */
	public HashMap<String, Object> validate() {
		this.setRunOtherValidations(false);
		this.setValid(true);

		if (!this.value.toString().isEmpty() || this.value != null)
			this.setRunOtherValidations(true);


		return new HashMap<String, Object>() {
			{
				this.put(Rule.VALIDATED_KEYS[0], isValid());
				this.put(Rule.VALIDATED_KEYS[1], isValid() ? "" : getFinalMessage());
				this.put(Rule.VALIDATED_KEYS[2], getRunOtherValidations());
			}
		};
	}
}
