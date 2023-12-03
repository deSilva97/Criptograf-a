package Main;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

import model.User;

public class ApplicationReq2 {

	static Scanner sc;
	static ArrayList<User> userList;
	
	static void init() {
		userList = new ArrayList<User>();
		sc = new Scanner(System.in);
		
		userList.add(buildUser("DIEGO", "123456"));
		userList.add(buildUser("PABLO", "a1b2c3_"));
		userList.add(buildUser("JORGE", "123456"));
		userList.add(buildUser("FELIX", "contraseña"));
	}
	
	public static void main(String[] args) {
		init();				
		
		User user = getLoginUser();
		if(user != null) {
			System.out.println("\nBienvenida/o " + user.getName() + "!");
		}
	
		System.out.println("\n\nFin del programa");
	}

	private static User getLoginUser() {
		String name = "";
		String pssw = "";		

		User user = null;		
		
		int maxTries = 3;
		int currentTry = 0;
		
		do {
			
			user = null;
			
			currentTry++;
			if(currentTry > maxTries) {
				System.err.println("Máximo de intentos permitidos");
				System.out.flush();
				break;
			}
			
			System.out.println("\nLogin >> (" + currentTry + "/" + maxTries + ")");
			System.out.print("Nombre: ");
			name = sc.nextLine();
			System.out.print("Contraseña: ");
			pssw = sc.nextLine();

			System.out.flush();
			
			user = buildUser(name, pssw);			
			
			if(searchUser(user.getName()) == null) {
				System.err.println("No existe usuario con este nombre: " + name);
				user = null;
			} 
		} while (!validateUserPassword(user));

		return user;
	}

	static String generateHash(String pssw) throws NoSuchAlgorithmException {
		byte[] pssw_bytes = pssw.getBytes();

		// Hash de los bytes de pssw
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(pssw_bytes);

		byte[] resume_hash = md.digest();

		return Base64.getEncoder().encodeToString(resume_hash);
	}



	static User buildUser(String name, String password)  {
		User user = null;
		try {
			user = new User(name.toUpperCase(), generateHash(password));
			//System.out.println("Usuario construido >> ");
			//System.out.println("| Usuario:    " + user.getName());
			//System.out.println("| Contraseña: " + user.getHashPassword());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			System.err.println("No se ha podido guardar la contraseña");			
		}
		
		return user;
	}

	private static User searchUser(String name) {		
		for (User i : userList) {
			if (i.getName().equals(name)) {
				return i;
			}
		}
		return null;
	}

	private static boolean validateUserPassword(User user) {
		if (user == null)
			return false;

		User aux = searchUser(user.getName().toUpperCase());		
		
		if(user.getHashPassword().equals(aux.getHashPassword())) {
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
