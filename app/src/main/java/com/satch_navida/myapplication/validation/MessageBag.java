package com.satch_navida.myapplication.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A message bag is an instance of object containing various messages for different keys. It is
 * similar to a JSON object wherein you can provide an array of messages for a single key, with
 * multiple keys in a single instance of the object.<br>
 * <br>
 * The code of this {@code MessageBag} is designed to allow chain-calling and only stops when using
 * {@code getters} or calling {@code variables} (if there are any).<br>
 *
 * @author Virus5600
 * @version 1.0.0
 */
public class MessageBag {
	// PRIVATE VARIABLES
	/**
	 * A map that contains all the messages, serving as a replacement for JSONs as JSON aren't easily
	 * traversable unlike maps in the Java context.
	 */
	private HashMap<String, ArrayList<String>> messages;

	// CONSTRUCTORS

	/**
	 * Creates an instance of {@link MessageBag}, containing messages given through {@code messages}
	 * parameter.<br>
	 *
	 * @param messages A map containing key-value pair whereas the {@code key} is the identifier while
	 *                 {@code value} is an {@link ArrayList} containing a list of {@code String}s.
	 */
	public MessageBag(HashMap<String, ArrayList<String>> messages) {
		this.messages = messages;
	}

	/**
	 * Creates an empty instance of {@link MessageBag}.
	 */
	public MessageBag() {
		this(new HashMap<>());
	}

	// PUBLIC METHODS

	/**
	 * Fetches all the available keys in the {@link MessageBag}.
	 *
	 * @return String[] An array of keys.
	 */
	@SuppressWarnings("ConstantConditions")
	public String[] keys() {
		return (String[]) this.messages.keySet().toArray();
	}

	/**
	 * Adds a new message to the array of messages from a key. If the key isn't present yet,
	 * a new instance of array will be created wherein the new message will be placed.
	 *
	 * @param key Key identifier of the message.
	 * @param message The new message to put for the key.
	 *
	 * @return MessageBag This instance of {@code MessageBag}.
	 */
	public MessageBag add(String key, String message) {
		ArrayList<String> currentMessages = this.messages.get(key);

		if (currentMessages == null)
			currentMessages = new ArrayList<>();

		currentMessages.add(message);
		this.messages.put(key, currentMessages);

		return this;
	}

	/**
	 * Merge a new array of messages into this message bag.
	 *
	 * @param messages An instance of {@link HashMap} containing a key-value pair whereas the {@code key} is the identifier while
	 *                  {@code value} is an {@link ArrayList} containing a list of {@code String}s.
	 *
	 * @return MessageBag This instance of {@code MessageBag}.
	 */
	public MessageBag merge(HashMap<String, ArrayList<String>> messages) {
		return this.merge(new MessageBag(messages));
	}

	/**
	 * Merge a new array of messages into this message bag.
	 *
	 * @param messages An instance of {@link MessageBag} containing all the messages that will be
	 *                 merged with this {@code MessageBag}.
	 *
	 * @return MessageBag This instance of {@code MessageBag}.
	 */
	public MessageBag merge(MessageBag... messages) {
		ConcurrentHashMap<String, ArrayList<String>> msgHolder = new ConcurrentHashMap<>();

		for (MessageBag m : messages) {
			m.get().entrySet().parallelStream().forEach(e ->
				msgHolder.merge(e.getKey(), e.getValue(), (v1, v2) -> {
					Set<String> set = new TreeSet<>(v1);
					set.addAll(v2);
					return new ArrayList<>(set);
				})
			);
		}

		this.messages = new HashMap<>();
		this.messages.putAll(msgHolder);

		return this;
	}

	/**
	 * Fetches the first message within the given {@code key}. If no such {@code key} is present, a
	 * empty string will be returned. Likewise, if a {@code key} is present but has no existing values,
	 * an empty string will also be returned.
	 *
	 * @param key Key identifier of the message.
	 *
	 * @return String the very first message in the list of the given key.
	 */
	public String first(String key) {
		if (!this.messages.containsKey(key))
			return "";

		ArrayList<String> keyMessages = this.messages.get(key);

		if (keyMessages == null)
			return "";

		return keyMessages.size() > 0 ? keyMessages.get(0) : "";
	}

	public String get(String key, int index) {
		ArrayList<String> keyMessage = this.messages.get(key);

		if (keyMessage == null)
			throw new NullPointerException("Such key does not exists.");
		if (index >= keyMessage.size())
			throw new IndexOutOfBoundsException("Provided index is greater than the number of messages present.");

		return keyMessage.get(index);
	}

	/**
	 * Fetches a list of messages based on the provided key.
	 *
	 * @param key The key used to fetch the messages.
	 *
	 * @return ArrayList<String> The list of messages.
	 */
	public ArrayList<String> get(String key) {
		return this.messages.get(key);
	}

	// PROTECTED METHODS

	/**
	 * Fetches and clone the message's raw form (a {@link HashMap}).
	 *
	 * @return HashMap<String, ArrayList<String>> A clone of the original map used by the
	 * {@link #messages} variable.
	 */
	@SuppressWarnings("unchecked")
	protected HashMap<String, ArrayList<String>> get() {
		return (HashMap<String, ArrayList<String>>) this.messages.clone();
	}
}
