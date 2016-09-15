package com.emiliano.fmframework.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The class Feature represents a selectable element of a model. A feature is
 * compound by an identifier name, an optional set of attributes (each attribute
 * with a name and Object value), and an optional FeatureAction. element.
 *
 * @param <SoftElement>
 *            the generic type
 */
public class Feature {

	/** The name. */
	private String name;

	/** The attributes. */
	private Map<String, Object> attributes;

	/**
	 * Instantiates a new feature with an action.
	 *
	 * @param name
	 *            the feature name
	 * @param action
	 *            the feature action
	 */
	public Feature(String name) {
		this.name = name;
		this.attributes = new HashMap<String, Object>();
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Contains attribute.
	 *
	 * @param name
	 *            the name
	 * @return true, if successful
	 */
	public boolean containsAttribute(String name) {
		return this.attributes.containsKey(name);
	}

	/**
	 * Put attribute.
	 *
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 * @return the object
	 */
	public Object addAttribute(String name, Object value) {
		return this.attributes.put(name, value);
	}

	/**
	 * Gets the attribute.
	 *
	 * @param name
	 *            the name
	 * @return the attribute
	 */
	public Object getAttribute(String name) {
		return this.attributes.get(name);
	}

	/**
	 * Removes the attribute.
	 *
	 * @param name
	 *            the name
	 * @return the object
	 */
	public Object removeAttribute(String name) {
		return this.attributes.remove(name);
	}

	/**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
	public Set<Entry<String, Object>> getAttributes() {
		return this.attributes.entrySet();
	}

}
