package model;

import java.io.Serializable;

public class User implements Serializable{

	private static final long serialVersionUID = 1L;

	String name;
	String hash_password;	
	
	public String getName() {
		return name;
	}
	
	public String getHashPassword() {
		return hash_password;
	}
	
	public User(String name, String hash_password) {
		super();
		this.name = name;
		this.hash_password = hash_password;
	}
	
	@Override
	public String toString() {
		return "User [name=" + name + ", hash_password=" + hash_password + "]";
	}

}
