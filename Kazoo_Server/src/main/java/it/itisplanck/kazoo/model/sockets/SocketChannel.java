package it.itisplanck.kazoo.model.sockets;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import it.itisplanck.kazoo.Main;
import it.itisplanck.kazoo.Utils;
import it.itisplanck.kazoo.model.giocatori.Giocatore;
import it.itisplanck.kazoo.model.giocatori.GiocatoreAvversario;
import it.itisplanck.kazoo.model.giocatori.Giocatori;
import it.itisplanck.kazoo.model.mercato.Mercato;
import it.itisplanck.kazoo.model.sockets.Protocol.OutgoingPacket;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Questa classe si occupa di gestire gli invii delle informazioni del gioco ai giocatori connessi<br>
 * e a gestire l'ascoltatore della porta del server.
 * @author Onnivello Emanuele
 */
public class SocketChannel {
	
	private DatagramSocket inputSocket, outputSocket;
	private InetAddress host;
	private int port;
	private final Protocol protocol;
	
	// Questa HashMap serve per salvare l'indirizzo e collegargli il riferimento al giocatore
	private static Map<InetSocketAddress, Giocatore> addresses;
	private boolean listenerEnabled = false, autoBroadcastEnabled = false;
	
	/**
	 * Il costruttore crea le istanze dei socket di input e output e il protocollo che li gestirà.
	 * @param port La porta che ascolterà le richieste dei giocatori.
	 * @throws SocketException L'eccezione della porta già utilizzata che andrà ad essere catturata dal View<br>
	 *  	   					per garantire una risposta dell'interfaccia immediata con un {@link Alert}.
	 */
	public SocketChannel(int port) throws SocketException {
		this.port = port;
		try {
			host = InetAddress.getLocalHost();
		} catch(UnknownHostException e) {
			host = InetAddress.getLoopbackAddress();
		}
		inputSocket = new DatagramSocket(port);
		outputSocket = new DatagramSocket();
		this.protocol = new Protocol();
		addresses = new HashMap<>();
	}
	
	/**
	 * Ritorna la porta dove il socket in ascolto
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Ritorna l'IP numerico della macchina
	 */
	public InetAddress getAddress() {
		return host;
	}
	
	/**
	 * Ricava tutti i giocatori connessi e il rispettivo IP
	 */
	public static Map<InetSocketAddress, Giocatore> getConnectedPlayers() {
		return addresses;
	}
	
	/**
	 * Abilita l'invio automatico delle informazioni sul mercato a tutti i giocatori connessi.
	 * @param refreshDelay Il tempo tra un refresh e l'altro in secondi
	 */
	public void enableAutoBroadcast(int refreshDelay) {
		autoBroadcastEnabled = true;
		new Thread(() -> { // Thread che gestisce l'auto-broadcasting
			
			while(autoBroadcastEnabled) {
				try {
					Thread.sleep(1000 * refreshDelay);
				} catch (InterruptedException e) {
					Utils.error("Errore nel refresh dello stato del gioco", e);
				}
				if(!outputSocket.isClosed()) {
					Mercato.update();
					for(int i=0;i<Giocatori.getGiocatori().size();i++) Giocatori.getGiocatori().get(i).updateSocietaAcquistate();
					broadcast();
				}
			}
			
		}, "Auto_Broadcast").start();
	}
	
	/**
	 * Invia le informazioni di gioco a tutti i giocatori su un thread parallelo per non rallentare quello principale.
	 */
	public void broadcast() {
		new Thread(() -> { // Thread per garantire la fluidità del programma
			
			for(Map.Entry<InetSocketAddress, Giocatore> entry : addresses.entrySet()) {
				
				Vector<GiocatoreAvversario> enemies = Giocatori.getAvversariOf(entry.getValue());
				new OutgoingPacket(outputSocket, entry.getKey())
					.writeByte(0) 											// Scrive il byte per indicare il refresh
					.writeVector(Mercato.getSocieta())						// Scrive il mercato
					.writeObject(entry.getValue())							// Scrive il giocatore
					.writeVector(enemies)									// Scrive i suoi avversari (Sotto forma di Vector<GiocatoreAvversario>)
				.send();
			}
			
		}, "Broadcaster").start();
	}
	
	/**
	 * Informa i giocatori connessi che il server è stato chiuso
	 */
	public void sendCloseOperation() {
		for(Map.Entry<InetSocketAddress, Giocatore> entry : addresses.entrySet()) {
			new OutgoingPacket(outputSocket, entry.getKey())
				.writeByte(3) 											// Scrive il byte per indicare la chiusura del server
			.send();
		}
	}
	
	/**
	 * Mette in ascolto la porta desiderata a gli input di richieste dei giocatori.
	 */
	public void enableListener() {
		listenerEnabled = true;
		new Thread(() -> { // Thread per l'ascoltatore della porta
			
			while(listenerEnabled) {
				protocol.processInput(inputSocket); // Lascia la gestione dell'input al protocollo
			}
			
		}, "ServerPort_" + port + "_Listener").start(); // Es. 'Port_9090_Listener'
	}
	
	/**
	 * Aggiunge il giocatore alla lista per il broadcast,<br>
	 * dando per scontato che sia già in gioco.
	 * @param address L'indirizzo del giocatore
	 * @param nome Il nome del giocatore
	 * @return Ritorna false nel caso in cui ci sia lo stesso ip
	 */
	public static boolean aggiungiGiocatore(InetSocketAddress address, String nome) {
		if(addresses.containsKey(address)) return false;
		addresses.put(address, Giocatori.getGiocatore(nome));
		Platform.runLater(() -> Main.getGameStage().addPlayer(nome, address));
		return true;
	}
	
	/**
	 * Rimuove il giocatore dalla lista per il broadcast
	 * @param playerName Il nome del giocatore
	 */
	public static boolean rimuoviGiocatore(String playerName) {
		InetSocketAddress address = null;
		for(Entry<InetSocketAddress, Giocatore> entry : addresses.entrySet())
			if(entry.getValue().getNome().equals(playerName)) address = entry.getKey();
		if(address != null) {
			addresses.remove(address);
			return true;
		}
		return false;
	}
	
	/**
	 * Ferma l'auto broadcasting
	 */
	public void stopAutoBroadcast() {
		autoBroadcastEnabled = false;
	}
	
	/**
	 * Ferma l'ascoltatore della porta e chiude il socket di input.
	 */
	public void stopListener() {
		listenerEnabled = false;
		inputSocket.close();
	}
	
	/**
	 * Chiude le eventuali connessioni ancora aperte
	 */
	public void close() {
		stopAutoBroadcast();
		stopListener();
		outputSocket.close();
	}
	
}
