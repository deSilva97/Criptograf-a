package controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

import model.User;

/**
 * Controlador para manejar las operaciones relacionadas con el inicio de sesión y la gestión de usuarios.
 */

public class LoginController {
	
	/**
     * Obtiene el usuario para el inicio de sesión.
     *
     * @param name El nombre del usuario.
     * @param password La contraseña del usuario.
     * @param list La lista de usuarios registrados.
     * @return El objeto User si el inicio de sesión es exitoso, o null si no es válido.
     */
	
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
	
	/**
     * Construye un objeto User a partir del nombre y la contraseña proporcionados.
     *
     * @param name El nombre del usuario.
     * @param password La contraseña del usuario.
     * @return El objeto User construido.
     */
	
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
	
	/**
     * Genera un hash para la contraseña utilizando el algoritmo SHA-512.
     *
     * @param pssw La contraseña a hashear.
     * @return El hash de la contraseña.
     * @throws NoSuchAlgorithmException Si no se encuentra el algoritmo de hash.
     */
	
	private String generateHash(String pssw) throws NoSuchAlgorithmException {
		byte[] pssw_bytes = pssw.getBytes();

		// Hash de los bytes de pssw
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(pssw_bytes);

		byte[] resume_hash = md.digest();

		return Base64.getEncoder().encodeToString(resume_hash);
	}
	
	
	/**
     * Busca un usuario en la lista por su nombre.
     *
     * @param name El nombre del usuario a buscar.
     * @param userList La lista de usuarios.
     * @return El objeto User si se encuentra, o null si no se encuentra.
     */
	
	private User searchUser(String name, ArrayList<User> userList) {
		for (User i : userList) {
			if (i.getName().equals(name)) {
				return i;
			}
		}
		return null;
	}
	
	 /**
     * Valida la contraseña del usuario comparándola con la almacenada en la lista de usuarios.
     *
     * @param user El usuario cuya contraseña se va a validar.
     * @param list La lista de usuarios registrados.
     * @return true si la contraseña es válida, false de lo contrario.
     */
	
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
