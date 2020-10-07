package it.itisplanck.kazoo.model.sockets;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

import it.itisplanck.kazoo.Main;
import it.itisplanck.kazoo.model.sockets.Protocol.OutgoingPacket;
import it.itisplanck.kazoo.view.config.MainStageClient;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Questa classe si occupa di gestire gli invii della richiesta di entrare in partita e<br>
 * di acquisto e vendita di azioni.<br>
 * @author Onnivello Emanuele
 */
public class SocketChannel {
	
	private static DatagramSocket inputSocket, outputSocket;
	private static int localPort;
	private final Protocol protocol;
	private static InetSocketAddress serverAddress;
	private boolean listenerEnabled = false;
	
	private static Timer t = new Timer();
	
	/**
	 * Il costruttore crea le istanze dei socket di input e output e il protocollo che li gestirà.
	 * @param port La porta che ascolterà le informazioni in arrivo dal server di gioco.
	 * @throws SocketException L'eccezione della porta già utilizzata che andrà ad essere catturata dal View<br>
	 *  	   					per garantire una risposta dell'interfaccia immediata con un {@link Alert}.
	 */
	public SocketChannel(int localPort, InetSocketAddress serverAddress) throws SocketException {
		SocketChannel.localPort = localPort;
		SocketChannel.serverAddress = serverAddress;
		inputSocket = new DatagramSocket(localPort);
		outputSocket = new DatagramSocket();
		this.protocol = new Protocol();
	}
	
	/**
	 * Fa ripartire da 0 il timer di 30 secondi che serve a controllare<br>
	 * se la connessione con il server è ancora presente.<br>
	 * Se il timer arriva al termine, la schermata di gioco verrà chiusa<br>
	 * e l'utente verrà riportato alla schermata iniziale.
	 */
	public void updateTimer() {
		t.cancel();
		t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				close(true);
				Platform.runLater(() -> {
					Main.getGameStage().close();
					new Alert(AlertType.WARNING, "Connessione al server persa.").showAndWait();
					Main.createInitialStage();
				});
			}
		}, 30000);
	}
	
	/**
	 * Manda la richiesta per entrare nella partita del server specificato nel costruttore.
	 * @param Il nickname del giocatore che fa la richiesta.
	 */
	public static void sendRequest(String playerName) {
		new OutgoingPacket(outputSocket, serverAddress)
			.writeByte(0)																// Scrive il byte per il join
			.writeString(playerName)													// Scrive il nome del giocatore
			.writeInt(localPort) 														// Scrive la porta dove desidera ricevere i dati
		.send();
	}
	
	/**
	 * Manda il pacchetto per richiedere l'acquisto di una o più azioni di una società.
	 * @param playerName Il giocatore che intende comprare
	 * @param society Il nome della società che si desidera comprare
	 * @param amount La quantità di azioni
	 */
	public static void buy(String playerName, String society, int amount) {
		new OutgoingPacket(outputSocket, serverAddress)
			.writeByte(1)																// Scrive il byte per comprare
			.writeString(playerName)													// Scrive il nome del giocatore
			.writeString(society)														// Scrive il nome della società
			.writeInt(amount)															// Scrive la quantità
		.send();
	}
	
	/**
	 * Manda il pacchetto per richiedere la vendita di una o più azioni di una società.
	 * @param playerName Il giocatore che intende vendere
	 * @param society Il nome della società che si desidera vendere
	 * @param amount La quantità di azioni
	 */
	public static void sell(String playerName, String society, int amount) {
		new OutgoingPacket(outputSocket, serverAddress)
			.writeByte(2)																// Scrive il byte per vendere
			.writeString(playerName)													// Scrive il nome del giocatore
			.writeString(society)														// Scrive il nome della società
			.writeInt(amount)															// Scrive la quantità
		.send();
	}
	
	/**
	 * Invia l'informazione che sta uscendo dalla partita
	 */
	public void sendQuit() {
		new OutgoingPacket(outputSocket, serverAddress)
			.writeByte(3)																	// Scrive il byte che indica l'uscita
			.writeString(Main.getGiocatore().getNome())										// Scrive il nome del giocatore che vuole uscire
		.send();
	}
	
	/**
	 * Mette in ascolto la porta specificata nella configurazione alle risposte del server e all'aggiornamento dei dati di gioco.
	 * @param mainStage Lo stage della schermata inziale che serve a disabilitare la finestra di attesa.
	 */
	public void enableListener(MainStageClient mainStage) {
		listenerEnabled = true;
		new Thread(() -> { // Thread per l'ascoltatore della porta
			
			while(listenerEnabled) {
				protocol.processInput(inputSocket, mainStage); // Lascia la gestione dell'input al protocollo
			}
			
		}, "ClientPort_" + localPort + "_Listener").start(); // Es. 'Port_9090_Listener'
	}
	
	/**
	 * Ferma l'ascoltatore della porta e chiude il socket di input.
	 */
	public void stopListener() {
		listenerEnabled = false;
		inputSocket.close();
	}
	
	/**
	 * Chiude le eventuali connessioni ancora aperte e il timer, se ancora attivo
	 * @param sendQuit Se è un uscita volontaria oppure no
	 */
	public void close(boolean sendQuit) {
		t.cancel();
		stopListener();
		
		if(!outputSocket.isClosed()) {
			if(sendQuit && Main.getGameStage() != null) sendQuit();
			outputSocket.close();
		}
	}
	
}
