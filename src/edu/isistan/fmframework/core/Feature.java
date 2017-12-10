package edu.isistan.fmframework.core;

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

	public Feature() {
		this(null);
	}
	
	public Feature(String name) {
		this.name = name;
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
	
	@Override
	public String toString() {
		return this.name;
	}

}
