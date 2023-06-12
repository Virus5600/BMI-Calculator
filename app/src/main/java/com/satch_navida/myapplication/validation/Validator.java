package com.satch_navida.myapplication.validation;

import com.satch_navida.myapplication.validation.rules.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Validator {
	// PRIVATE VARIABLES
	/**
	 * A private global variable container for all the values passed to this.
	 */
	private Map<String, Object> valueList;
	/**
	 * A private global variable container for all the rules passed to this.
	 */
	private Map<String, String> ruleList;
	/**
	 * A private global variable container for all the messages passed to this.
	 */
	private Map<String, String> msgList;
	/**
	 * A private global variable container for the {@link MessageBag} instance.
	 */
	private MessageBag errorList = new MessageBag();
	/**
	 * A private global variable container that determines if the validation has been run already.
	 */
	private boolean validationDone = false;
	/**
	 * A private global variable container, identifying if this validator's validation has failed.
	 */
	private boolean failed = false;
	private Map<String, Object> validated = new HashMap<String, Object>();

	// CONSTRUCTORS

	/**
	 * Creates an instance of {@code Validator}.<br>
	 * <br>
	 * <ul>
	 *     <li>
	 *         <b>Example A.1: </b> {@link com.satch_navida.myapplication.validation.rules.Required Required}
	 *     </li>
	 *
	 *     <li>
	 *         <b>Example A.2:</b> {@link com.satch_navida.myapplication.validation.rules.Numeric Numeric}
	 *     </li>
	 *
	 *     <li>
	 *         <b>Example B.1:</b> {@link com.satch_navida.myapplication.validation.rules.Min Min}
	 *     </li>
	 * </ul>
	 *
	 * @param values A {@link Map} object containing a key-value pair for the keys and its value.
	 * @param rules A {@link Map} object containing a key-value pair for the key's rules. A rule
	 *                 must be one of the classes that extends {@link Rule} class, or a custom class
	 *                 that also extends {@code Rule} which takes in 1 parameter (value) and returns
	 *                 a {@link Map} object with 3 keys derived from {@link Rule#VALIDATED_KEYS}.
	 *                 An example of a function is <b>Example A.1</b>, <b>Example A.2</b>, and <b>Example B.1</b>.
	 * @param messages A {@link Map} object containing a key-value pair for the keys' messages
	 */
	public Validator(Map<String, Object> values, Map<String, String> rules, Map<String, String> messages) {
		this.valueList = values;
		this.ruleList = rules;
		this.msgList = messages;
	}

	/**
	 * Creates an instance of {@code Validator}.<br>
	 * <br>
	 * <ul>
	 *     <li>
	 *         <b>Example A.1: </b> {@link com.satch_navida.myapplication.validation.rules.Required Required}
	 *     </li>
	 *
	 *     <li>
	 *         <b>Example A.2:</b> {@link com.satch_navida.myapplication.validation.rules.Numeric Numeric}
	 *     </li>
	 *
	 *     <li>
	 *         <b>Example B.1:</b> {@link com.satch_navida.myapplication.validation.rules.Min Min}
	 *     </li>
	 * </ul>
	 *
	 * @param values A {@link Map} object containing a key-value pair for the fields and its value.
	 * @param rules A {@link Map} object containing a key-value pair for the key's rules. A rule
	 *                 must be one of the classes that extends {@link Rule} class, or a custom class
	 *                 that also extends {@code Rule} which takes in 1 parameter (value) and returns
	 *                 a {@link Map} object with 3 keys derived from {@link Rule#VALIDATED_KEYS}.
	 *                 An example of a function is <b>Example A.1</b>, <b>Example A.2</b>, and <b>Example B.1</b>.
	 */
	public Validator (Map<String, Object> values, Map<String, String> rules) {
		this(values, rules, null);
	}

	// PUBLIC METHODS
	/**
	 * Identifies whether the validation failed or not. A single rule that fail will result in a
	 * total fail of the validation.
	 *
	 * @return boolean Returns {@code true} if the validation fails; {@code false} otherwise.
	 */
	public boolean fails() {
		if (!this.validationDone)
			this.runValidation();

		return this.failed;
	}

	/**
	 * Fetches all the error message.
	 *
	 * @return MessageBag An instance of {@link MessageBag} containing all error messages.
	 *
	 * @see MessageBag
	 */
	public MessageBag errors() {
		if (!this.validationDone)
			this.runValidation();

		return this.errorList;
	}

	/**
	 * Validates the fields provided and returns a JSON object, containing all the values from the
	 * fields that passed.
	 *
	 * @return Map<String, Object> A {@link Map} object, containing a key-value pair format of keys
	 * and their values that passed the validation.
	 */
	public Map<String, Object> validate() {
		if (!this.validationDone)
			this.runValidation();

		return this.validated;
	}

	/**
	 * Determine if messages exist for the given field.
	 *
	 * @param key field	The name of the key that will be tested.
	 *
	 * @return boolean A boolean value; either a {@code true} if the field exists, or {@code false} if it does not.
	 */
	public boolean has(String key) {
		return this.valueList.containsKey(key);
	}

	/**
	 * Get the first message from the message bag for a given field.
	 *
	 * @param key The name of the key that the message will be fetched upon.
	 *
	 * @return String The first message contained within the said field. Returns a {@code null} when the field does not exists.
	 */
	public String first(String key) {
		return this.errorList.first(key);
	}

	/**
	 * Get all of the messages from the message bag for a given field. If no value or a {@code null} is
	 * provided to the field, it returns all the messages contained within the current instance of
	 * {@link MessageBag}.
	 *
	 * @param key The name of the key that the message will be fetched upon.
	 *
	 * @return ArrayList<String> An {@link ArrayList} object that contains the key-value pair for all
	 * the message of the field.
	 */
	public ArrayList<String> get(String key) {
		return this.errorList.get(key);
	}

	/**
	 * Retrieves an array of field names that are invalid.
	 *
	 * @return String[] An array of field names that failed the validation.
	 */
	public String[] invalidFields() {
		return this.errorList.keys();
	}

	/**
	 * Retrieves an array of field names that are valid.
	 *
	 * @return String[] An array of field names that passed the validation.
	 */
	public String[] validFields() {
		Map<String, Object> vl = new HashMap<String, Object>();
		vl.putAll(this.valueList);

		vl.keySet().removeAll(new HashSet<String>(Arrays.asList(this.invalidFields())));

		return (String[]) vl.keySet().toArray();
	}

	/**
	 * Retrieves all the field names that are used in this validator.
	 *
	 * @return String[] An array of all the field names used in this {@code Validator}.
	 */
	public String[] fields() {
		return (String[]) this.valueList.keySet().toArray();
	}

	// PRIVATE METHODS
	/**
	 * Runs the entire validation algorithm.
	 */
	private void runValidation() {

	}
}
