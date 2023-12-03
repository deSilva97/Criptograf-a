package main;

import java.util.ArrayList;
import java.util.Scanner;

import controller.CryptoController;
import controller.LoginController;
import model.User;

/**
 * Clase principal que ejecuta la aplicación de cifrado.
 */

public class Application {
	// Scanner para la entrada del usuario
	private static Scanner sc;
	// Lista de usuarios registrados en la aplicación
	private static ArrayList<User> userList;
	// Variable para activar el inicio de sesión automático (autologin)
	private static boolean autologin = false;
	// Controlador para manejar el inicio de sesión
	private static LoginController loginController;
	// Controlador para realizar operaciones de cifrado y descifrado
	private static CryptoController cryptoController;
	
	
	/**
     * Inicializa los componentes necesarios para la aplicación.
     */
	
	private static void init() {
		sc = new Scanner(System.in);
		userList = new ArrayList<User>();
		
		loginController = new LoginController();
		cryptoController = null;
		
		// Agrega algunos usuarios a la lista (puedes personalizar esto según tus necesidades)
		userList.add(loginController.buildUser("DIEGO", "123456"));
		userList.add(loginController.buildUser("PABLO", "a1b2c3_"));
		userList.add(loginController.buildUser("JORGE", "123456"));
		userList.add(loginController.buildUser("FELIX", "contraseña"));
	
	}
	
	/**
     * Método principal que inicia la ejecución de la aplicación.
     *
     * @param args Argumentos de la línea de comandos.
     */
	public static void main(String[] args) {
		init();	// Inicializa la aplicación			
		
		User user = null;
		if(autologin) {
			user = loginController.buildUser("ADMIN", "");
		} else {
			int maxTries = 3;
			int currentTry = 0;		
			
			do {			
				currentTry++;
				if(currentTry > maxTries) {
					System.err.println("Máximo de intentos permitidos");
					System.out.flush();
					break;
				}
				
				System.out.println("\nLogin >> (" + currentTry + "/" + maxTries + ")");
				System.out.print("Nombre: ");
				String name = sc.nextLine();
				System.out.print("Contraseña: ");
				String pssw = sc.nextLine();
				
				user = loginController.getLoginUser(name, pssw, userList);
				
			} while(user == null);
		}
		
				
		if(user != null) {
			System.out.println("\nBienvenida/o " + user.getName() + "!");
			
			cryptoController = new CryptoController(sc);
			
			int opcion = -1;
			do {
				System.out.println("\nMENU " + user.getName());
				System.out.println("0. Salir del programa");
				System.out.println("1. Encriptar frase");
				System.out.println("2. Desencritptar frase");
				System.out.print("Opcion: ");

				opcion = sc.nextInt();

				switch (opcion) {
				case 0:
					System.out.println("\nHasta pronto!!");
					break;
				case 1:
					cryptoController.encrypt();
					break;
				case 2:
					cryptoController.descrypt();
					break;
				default:
					System.err.println("Opcion no válida");
					break;
				}

			} while (opcion != 0);	
		}
				
		System.out.println("\n\nFin del programa");
		sc.close();
	}
	
	
}
