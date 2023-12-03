package Main;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

import model.User;

public class ApplicationReqAll {
	private static Scanner sc;
	private static ArrayList<User> userList;
	private static SealedObject so;
	
	
	private static void init() {
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
			Menu();			
		}
				
		System.out.println("\n\nFin del programa");
	}
	
	// ==== Menuses: Métodos que utilizan la entrada de datos del usuario ====
	// ==== ============================================================= ====
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
	
	private static void Menu() {
		int opcion = 0;		
		
		SecretKey key = null;
		Cipher cip = null;
		
		//Configuración del encryptado
		try {
			key = KeyGenerator.getInstance("AES").generateKey();
			cip = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {			
			//Esscepcion del key.getInstance
			e.printStackTrace();
		} catch (NoSuchPaddingException e) { 
			//Excepcion del cip.getInstance
			e.printStackTrace();
		}
	
		
		do {
			
			System.out.println("\nMENU");
			System.out.println("0. Salir del programa");
			System.out.println("1. Encriptar frase");
			System.out.println("2. Desencritptar frase");
			System.out.print("Opcion: ");
			
			try {
				opcion = sc.nextInt();	
				System.out.flush();
			} catch (InputMismatchException ime) {
				// TODO: handle exception
				sc.next();
				opcion = -1;
			} 
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			//Debugear el Buffer
			
			switch (opcion) {
			case 0: System.out.println("Hasta pronto!!");
				break;
			case 1: encrypt( key, cip);				
				break;
			case 2: descrypt(key, cip);
				break;
			default:
				System.err.println("Opcion no válida");
			}
			
			
		}while(opcion != 0);
	}
	
	// ==== ============================================================= ====
	
	// ==== Login Controller: Métodos relacionados con el Login ====
	// ==== =================================================== ====
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
	// ==== =================================================== ====
	
	// ==== Encrypt Controller: Métodos relacionados con la encryptación ====
	// ==== ============================================================ ====
	private static void encrypt(SecretKey key, Cipher cip) {
		sc.nextLine();
		System.out.print("Frase: ");
		String str = sc.nextLine();
		
		try {		
			//Configuramos el Cipher para encryptar
			cip.init(Cipher.ENCRYPT_MODE, key);
			
			so = new SealedObject(str, cip);;
			System.out.println("Cifrado: " + so);
		} catch (InvalidKeyException e) {
			// Excepcion del Cipher init
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// Escepción del constructor so
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void descrypt(SecretKey key, Cipher cip) {		
		System.out.println("Descifrar: " + so);
		
		try {
			cip.init(Cipher.DECRYPT_MODE, key);
			
			String str = (String) so.getObject(cip);
			
			System.out.println("Frase: " + str);
			
		} catch (NullPointerException npe) {			
			System.err.println("No hay un cifrado existente pruebe a crear uno");
			System.out.flush();;
		} 
		
		catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	// ==== ============================================================ ====

}
