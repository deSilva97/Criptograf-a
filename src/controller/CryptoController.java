package controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
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

/**
 * Clase que controla las operaciones de cifrado y descifrado.
 */

public class CryptoController {

	private Scanner scanner;
	private SealedObject so;
	private SecretKey key;
	private Cipher cipher;
	
	/**
     * Constructor de la clase CryptoController.
     *
     * @param scanner Objeto Scanner para la entrada del usuario.
     */
	
	public CryptoController(Scanner scanner) {
		this.scanner = scanner;

		// Configuración del encryptado
		try {
			// Generar clave simétrica (AES)
			key = KeyGenerator.getInstance("AES").generateKey();
			cipher = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {			
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {			
			e.printStackTrace();
		}
	}
	
	/**
     * Método para encriptar una frase.
     */
	
	public void encrypt() {
		scanner.nextLine();
		System.out.print("Frase: ");
		String str = scanner.nextLine();

		try {
			// Configuramos el Cipher para encryptar
			cipher.init(Cipher.ENCRYPT_MODE, key);

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
	
	/**
     * Método para descifrar una frase previamente cifrada.
     */
	
	public void descrypt() {
		System.out.println("Descifrar: " + so);

		try {
			cipher.init(Cipher.DECRYPT_MODE, key);

			String str = (String) so.getObject(cipher);

			System.out.println("Frase: " + str);

		} catch (NullPointerException npe) {
			// TODO: handle exception
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
