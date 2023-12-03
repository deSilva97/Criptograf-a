package controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

public class CryptoController {

	//Declaración de variables que vamos a usar
	private Scanner scanner;
	private SealedObject so;
	private SecretKey key;
	private Cipher cipher;

	public CryptoController(Scanner scanner) {
		this.scanner = scanner;

		// Configuración del encryptado
		try {
			//Generamos una key
			key = KeyGenerator.getInstance("AES").generateKey();
			//Instanciamos el cifrador
			cipher = Cipher.getInstance("AES");
		//Control de excepciones que pudiera arrojar
		} catch (NoSuchAlgorithmException e) {			
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {			
			e.printStackTrace();
		}
	}
	
	//Este método encripta el mensaje con la key generada
	public void encrypt() {
		scanner.nextLine();
		System.out.print("Frase: ");
		String str = scanner.nextLine();

		try {
			// Configuramos el Cipher para encryptar
			cipher.init(Cipher.ENCRYPT_MODE, key);
			//Sellamos el objeto
			so = new SealedObject(str, cipher);
		
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
	
	//Este método desencripta el mensaje con la key generada
	public void descrypt() {
		System.out.println("Descifrar: " + so);

		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
			String str = (String) so.getObject(cipher);
			System.out.println("Frase: " + str);

		//Control de excepciones que puediera arrojar
		} catch (NullPointerException npe) {
			System.err.println("No hay un cifrado existente pruebe a crear uno");
			System.out.flush();			
		} catch(GeneralSecurityException gse) {
			gse.printStackTrace();
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}

	}
}
