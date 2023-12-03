package controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

import model.User;

public class LoginController {

	public User getLoginUser(String name, String password, ArrayList<User> list) {
		User user = buildUser(name, password);

		if (searchUser(user.getName(), list) == null) {
			System.err.println("No existe usuario con este nombre: " + name);
			return null;
		} else if (validateUserPassword(user, list)) {
			return user;
		} else {
			return null;
		}
	}
	
	public User buildUser(String name, String password) {
		User user = null;
		try {
			user = new User(name.toUpperCase(), generateHash(password));
			 System.out.println("Usuario construido >> ");
			 System.out.println("| Usuario: " + user.getName());
			 System.out.println("| Contraseña: " + user.getHashPassword());
		} catch (NoSuchAlgorithmException e) {
			System.err.println("No se ha podido guardar la contraseña");
		}

		return user;
	}
	
	//==============================

	private String generateHash(String pssw) throws NoSuchAlgorithmException {
		byte[] pssw_bytes = pssw.getBytes();

		// Hash de los bytes de pssw
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(pssw_bytes);

		byte[] resume_hash = md.digest();

		return Base64.getEncoder().encodeToString(resume_hash);
	}
	

	private User searchUser(String name, ArrayList<User> userList) {
		for (User i : userList) {
			if (i.getName().equals(name)) {
				return i;
			}
		}
		return null;
	}

	private boolean validateUserPassword(User user, ArrayList<User> list) {
		if (user == null)
			return false;

		User aux = searchUser(user.getName().toUpperCase(), list);

		if (user.getHashPassword().equals(aux.getHashPassword())) {
			System.out.println("Contraseña correcta");
			System.out.flush();
			return true;
		} else {
			System.err.println("Contraseña incorrecta");
			System.out.flush();
			return false;
		}
	}
}
