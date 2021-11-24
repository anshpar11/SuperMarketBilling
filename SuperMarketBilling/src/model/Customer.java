/**
 * 
 */
package model;

/**
 * @author Neelanshu Parnami
 *
 */
public class Customer {
	
	/**
	 * @param id
	 * @param name
	 */
	public Customer(long id, String name) {
		this.id = id;
		this.name = name;
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	private final long id;
	private final String name;

}
