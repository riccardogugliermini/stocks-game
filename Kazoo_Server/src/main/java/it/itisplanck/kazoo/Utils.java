package it.itisplanck.kazoo;

public class Utils {
	
	/**
	 * Invia al terminale il messaggio e l'eccezione
	 * @param message Il messaggio da inviare
	 * @param e L'eccezione da catturare
	 */
	public static void error(String message, Exception e) {
		System.out.println(message + ": " + e.getMessage());
	}
	
	/**
	 * Per inviare semplici messaggi
	 */
	public static void write(Object message) {
		System.out.println(message);
	}
	
}
