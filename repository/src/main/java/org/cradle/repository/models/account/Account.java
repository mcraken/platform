/**
 * 
 */
package org.cradle.repository.models.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cradle.repository.ModelRepository;
import org.cradle.repository.exception.NoResultException;
import org.cradle.repository.models.RestModel;
import org.cradle.repository.query.SimpleSelectionAdapter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 9, 2014
 */
@Table("account")
public class Account implements RestModel, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6353630196029156544L;
	
	@PrimaryKey
	private String email;
	private String password;
	private List<String> roles;
	
	@Transient
	private transient ModelRepository modelRepository;
	
	public Account(String email, ModelRepository modelRepository){
		
		this.email = email;
		
		this.modelRepository = modelRepository;
	}
	
	public Account(String email, String password) {
		
		this.email = email;
		
		this.password = password;
	}

	public Account(String email, String password, ModelRepository modelRepository) {
		
		this(email, password);
		
		this.modelRepository = modelRepository;
	}

	public Account(String email, String password, List<String> roles) {
		
		this(email, password);
		
		this.roles = roles;
	}
	
	public boolean equals(Object otherAccountObj){
		
		Account otherAccount = (Account) otherAccountObj;
		
		if(this.email.equals(otherAccount.email) && this.password.equals(otherAccount.password))
			return true;
		
		return false;
	}
	
	public void find() throws NoResultException {
		
		SimpleSelectionAdapter<Account> selectionAdapter = modelRepository.createSimpleSelectionAdapter("account");

		Account foundAccount = selectionAdapter
				.eq("email", email)
				.eq("password", password)
				.result().get(0);
		
		this.roles = foundAccount.roles;
	}
	
	public void findByEmail() throws NoResultException {
		
		SimpleSelectionAdapter<Account> selectionAdapter = modelRepository.createSimpleSelectionAdapter("account");

		Account foundAccount = selectionAdapter
				.eq("email", email)
				.result().get(0);
		
		this.password = foundAccount.password;
		
		this.roles = foundAccount.roles;
	}
	
	public List<Role> filterRoles(List<Role> allRoles){
		
		ArrayList<Role> filteredRoles = new ArrayList<>();
		
		for(Role role : allRoles){
			
			if(role.equals(role))
				filteredRoles.add(role);
		}
		
		return filteredRoles;
	}
	
	public void create(String password, String[] roles){
		
		this.password = password;
		
		this.roles = Arrays.asList(roles);
		
		modelRepository.write(this);
		
	}
	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * @return the roles
	 */
	public List<String> getRoles() {
		return roles;
	}

	/* (non-Javadoc)
	 * @see org.cradle.repository.models.RestModel#getUniqueId()
	 */
	@Override
	public Object getUniqueId() {
		return email;
	}
}
