/**
 * 
 */
package org.mcplissken.repository.models.account;

import org.mcplissken.repository.ModelRepository;
import org.mcplissken.repository.models.RestModel;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 15, 2015
 */
@Table("profile")
public class Profile implements RestModel{

	@PrimaryKey
	private String email;
	@Column("first_name")
	private String firstName;
	@Column("last_name")
	private String lastName;

	@Transient
	private transient ModelRepository repository;
	@Transient
	private transient Account account;


	public Profile(String email, String firstName, String lastName) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Profile(String email, String firstName, String lastName, ModelRepository repository) {
		
		this(email, firstName, lastName);
		
		this.repository = repository;
	}

	public Profile(String email) {
		this.email = email;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(ModelRepository repository) {
		this.repository = repository;
	}

	public void update() {

		repository.write(this);
	}

	public Account getAccount() {

		if(account == null){
			
			account = new Account(email, repository);
		}

		return account;

	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.models.RestModel#getUniqueId()
	 */
	@Override
	public Object getUniqueId() {
		return email;
	}
}
