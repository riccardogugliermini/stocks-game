package it.itisplanck.kazoo.model.sockets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Vector;
import java.util.logging.Level;

import it.itisplanck.kazoo.Main;
import it.itisplanck.kazoo.Utils;
import it.itisplanck.kazoo.model.giocatori.Giocatore;
import it.itisplanck.kazoo.model.giocatori.Giocatori;
import javafx.application.Platform;

/**
 * Protocollo che elabora li connessioni UDP tramite<br>
 * l'uso di {@link DatagramSocket} e {@link DatagramPacket}.
 * @author Onnivello Emanuele
 */
public class Protocol {
	
	/**
	 * Classe interna del Protocollo che rappresenta il pacchetto UDP<br>
	 * che dovrà essere spedito nel {@link DatagramSocket}.<br>
	 * E' stato creato per semplificare l'uso dei pacchetti e delle eccezioni.
	 * @author Onnivello Emanuele
	 */
	public static class OutgoingPacket {
		
		private final DatagramSocket socket;
		private final InetSocketAddress address;
		
		private ByteArrayOutputStream byteStream;
		private ObjectOutputStream output;
		
		/**
		 * L'unico costruttore, fondamentale per la creazione del pacchetto
		 * @param socket Indica il {@link DatagramSocket} di output del server, su cui verrà inviato il pacchetto.
		 * @param address E' l'indirizzo contenente IP e porta del destinatario
		 */
		public OutgoingPacket(DatagramSocket socket, InetSocketAddress address) {
			this.socket = socket;
			this.address = address;
			
			byteStream = new ByteArrayOutputStream();
			try {
				output = new ObjectOutputStream(byteStream);
			} catch (IOException e) {
				Utils.error("Errore nella creazione del pacchetto", e);
			}
		}
		
		/**
		 * Scrive nel buffer dello stream un vettore di oggetti, indicando prima<br>
		 * il numero di oggetti che scriverà e poi gli stessi oggetti del vettore.
		 * @param vector Il vettore da scrivere
		 * @return L'istanza dell'oggetto dove si è richiamato il metodo.
		 */
		public OutgoingPacket writeVector(Vector<? extends Object> vector) {
			try {
				output.writeInt(vector.size());
				for(Object obj : vector) {
					output.writeObject(obj);
				}
			} catch(IOException e) {
				Utils.error("Errore nella scrittura (Array) del pacchetto", e);
			}
			return this;
		}
		
		/**
		 * Scrive nel buffer dello stream un oggetto.
		 * @param object L'oggetto da scrivere
		 * @return L'istanza dell'oggetto dove si è richiamato il metodo.
		 */
		public OutgoingPacket writeObject(Object object) {
			try {
				output.writeObject(object);
			} catch (IOException e) {
				Utils.error("Errore nella scrittura (Object) del pacchetto", e);
				e.printStackTrace();
			}
			return this;
		}
		
		/**
		 * Scrive nel buffer dello stream un byte.
		 * @param bite Il byte da scrivere
		 * @return L'istanza dell'oggetto dove si è richiamato il metodo.
		 */
		public OutgoingPacket writeByte(int bite) {
			try {
				output.writeByte(bite);
			} catch (IOException e) {
				Utils.error("Errore nella scrittura (Byte) del pacchetto", e);
				e.printStackTrace();
			}
			return this;
		}
		
		/**
		 * Spedisce il pacchetto nel socket e nell'indirizzo specificati nel costruttore.
		 */
		public void send() {
			try {
				output.flush();
			} catch(IOException e) {
				Utils.error("Errore nel confezionamento del pacchetto", e);
			}
			
			byte[] bytes = byteStream.toByteArray();
			
			DatagramPacket packet = new DatagramPacket(bytes, (bytes.length < 5120 ? bytes.length : 5120), address);
			try {
				socket.send(packet);
				byteStream.close();
			} catch (IOException e) {
				Utils.error("Errore nell'invio del pacchetto", e);
			}
		}
		
	}
	
	/**
	 * Elabora il pacchetto in entrata che riceverà il socket di input del server,<br>
	 * rispondendo alle richieste di entrata in gioco su {@link Thread} differenti<br>
	 * per permettere un elaborazione simultanea di più clients.
	 * @param socket Il socket di output del server su cui verranno attesi i pacchetti.
	 */
	public void processInput(DatagramSocket socket) {
		DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			if(!e.getMessage().contains("ocket closed")) Utils.error("Errore nella ricezione del pacchetto", e);
			else return;
		}
		
		new Thread(() -> { // L'elaborazione è all'interno del thread per non rallentare le ricezioni
			
			try(ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(packet.getData()))) {
				// Input da parte di un client
				
				byte operation = input.readByte();
				
				if(operation == 0) { // Richiesta di entrare nella partita
					
					String playerName = input.readUTF();
					int result = Giocatori.addGiocatore(playerName); // Lettura del giocatore da aggiungere e controllo se � possibile aggiungerli
					int port = input.readInt();
					
					if(result == 1) { // Nome già esistente
						new OutgoingPacket(socket, new InetSocketAddress(packet.getAddress(), port)).writeByte(1).send(); // Informa l'utente che il nome esiste già
						Main.getGameStage().appenToLog(Level.WARNING, "Richiesta di entrata respinta (" + playerName + "): Nome già presente.");
					} else if(result == 2) { // Giocatori massimi raggiunti
						new OutgoingPacket(socket, new InetSocketAddress(packet.getAddress(), port)).writeByte(2).send(); // Informa l'utente che il server è pieno
						Main.getGameStage().appenToLog(Level.WARNING, "Richiesta di entrata respinta (" + playerName + "): Il server è pieno.");
					}
					
					// Controllo dell'IP e aggiunta alla lista di broadcast
					else if(result == 0) {
						if(!SocketChannel.aggiungiGiocatore(new InetSocketAddress(packet.getAddress(), port), playerName))
							Main.getGameStage().appenToLog(Level.WARNING, "Richiesta di entrata respinta (" + playerName + "): Indirizzo già presente.");
						else Main.getGameStage().appenToLog(Level.INFO, playerName + " è entrato in gioco! (" + Giocatori.getGiocatori().size() + "/" + Giocatori.getGiocatoriMassimi() + ")");
					}
					
				} else if(operation == 1) { // Operazione compra
					
					String playerName = input.readUTF();
					Giocatore player = Giocatori.getGiocatore(playerName); // Input: Stringa (Nome del giocatore)
					if(player != null) {
						String societa = input.readUTF();
						int amount = input.readInt();
						player.buy(societa, amount);				  // Input: Societa e quantità
						player.checkIfInGioco();
						Main.getGameStage().appenToLog(Level.INFO, playerName + " ha acquistato " + amount + " azion" + (amount == 1 ? "e" : "i") + " da " + societa + ".");
					} else
						Main.getGameStage().appenToLog(Level.WARNING, "Richiesta di acquisto negata a " + packet.getAddress().getHostAddress() + " (" + playerName + ").");
					
				} else if(operation == 2) { // Operazione vendita
					
					String playerName = input.readUTF();
					Giocatore player = Giocatori.getGiocatore(playerName); // Input: Stringa (Nome del giocatore)
					if(player != null) {
						String societa = input.readUTF();
						int amount = input.readInt();
						player.sell(societa, amount);				  // Input: Societa e quantità
						player.checkIfInGioco();
						Main.getGameStage().appenToLog(Level.INFO, playerName + " ha venduto " + amount + " azion" + (amount == 1 ? "e" : "i") + " di " + societa + ".");
					} else
						Main.getGameStage().appenToLog(Level.WARNING, "Richiesta di vendita negata a " + packet.getAddress().getHostAddress() + " (" + playerName + ").");
					
				} else if(operation == 3) { // Il giocatore è uscito
					
					String playerName = input.readUTF();
					if(SocketChannel.rimuoviGiocatore(playerName)) {
						Platform.runLater(() -> {
							Giocatori.removeGiocatore(playerName);
							Main.getGameStage().removePlayer(playerName);
							Main.getGameStage().appenToLog(Level.INFO, playerName + " è uscito dalla partita! (" + Giocatori.getGiocatori().size() + "/" + Giocatori.getGiocatoriMassimi() + ")");
						});
					}
					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				Utils.error("Errore nella decodifica del pacchetto", e);
			}
			
		}, "Packet_Processing").start();
		
	}
	
}
