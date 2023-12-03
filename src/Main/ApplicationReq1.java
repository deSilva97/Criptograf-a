package Main;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

public class ApplicationReq1 {
	
	private static SealedObject so;
	
	
	public static void main(String[] args) {		
		int opcion = 0;
		
		Scanner sc = new Scanner(System.in);
		
		SecretKey key = null;
		Cipher cip = null;
		
		//Configuración del encryptado
		try {
			key = KeyGenerator.getInstance("AES").generateKey();
			cip = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			//Esscepcion del key.getInstance
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
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
			case 1: encrypt(sc, key, cip);				
				break;
			case 2: descrypt(key, cip);
				break;
			default:
				System.err.println("Opcion no válida");
			}
			
			
		}while(opcion != 0);
		
		System.out.println("Fin del programa");
		
		sc.close();				
		
	}
	
	private static void encrypt(Scanner sc, SecretKey key, Cipher cip) {
		sc.nextLine();
		System.out.print("> Frase: ");
		String str = sc.nextLine();
		
		System.out.println("Cifrando... " + str);
		
		try {		
			//Configuramos el Cipher para encryptar
			cip.init(Cipher.ENCRYPT_MODE, key);
			
			so = new SealedObject(str, cip);;
			
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
		System.out.println("Descifrando...");
		
		try {
			cip.init(Cipher.DECRYPT_MODE, key);
			
			String str = (String) so.getObject(cip);
			
			System.out.println(str);
			
		} catch (NullPointerException npe) {
			// TODO: handle exception
			System.err.println("No hay un cifrado existente pruebe a crear uno");
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
	
}
