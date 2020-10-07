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

import it.itisplanck.kazoo.Main;
import it.itisplanck.kazoo.Utils;
import it.itisplanck.kazoo.model.giocatori.Giocatore;
import it.itisplanck.kazoo.model.giocatori.GiocatoreAvversario;
import it.itisplanck.kazoo.model.mercato.Mercato;
import it.itisplanck.kazoo.model.mercato.Societa;
import it.itisplanck.kazoo.view.config.MainStageClient;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Protocollo che elabora le connessioni UDP tramite<br>
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
		 * @param socket Indica il {@link DatagramSocket} di output del client, su cui verrà inviato il pacchetto.
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
		 * Scrive nel buffer dello stream un oggetto.
		 * @param object L'oggetto da scrivere
		 * @return L'istanza dell'oggetto dove si è richiamato il metodo.
		 */
		public OutgoingPacket writeObject(Object object) {
			try {
				synchronized(this) { output.writeObject(object); }
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
		 * Scrive nel buffer dello stream una stringa.
		 * @param string La stringa da scrivere
		 * @return L'istanza dell'oggetto dove si è richiamato il metodo.
		 */
		public OutgoingPacket writeString(String string) {
			try {
				output.writeUTF(string);
			} catch (IOException e) {
				Utils.error("Errore nella scrittura (String) del pacchetto", e);
				e.printStackTrace();
			}
			return this;
		}
		
		/**
		 * Scrive nel buffer dello stream un numero intero.
		 * @param integer Il numero da scrivere
		 * @return L'istanza dell'oggetto dove si è richiamato il metodo.
		 */
		public OutgoingPacket writeInt(int integer) {
			try {
				output.writeInt(integer);
			} catch (IOException e) {
				Utils.error("Errore nella scrittura (Int) del pacchetto", e);
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
			DatagramPacket packet = new DatagramPacket(bytes, (bytes.length < 1024 ? bytes.length : 1024), address);
			try {
				socket.send(packet);
				byteStream.close();
			} catch (IOException e) {
				Utils.error("Errore nell'invio del pacchetto", e);
			}
		}
		
	}
	
	/**
	 * Elabora il pacchetto in entrata che riceverà il socket di input del client,<br>
	 * ricevendo le risposte della richiesta di entrata in gioco oppure le informazioni<br>
	 * del gioco, che confermeranno la richiesta di entrata oppure aggiorneranno l'interfaccia.<br>
	 * Ogni elaborazione verrà gestita su {@link Thread} differenti per permettere al programma di esse più fluido possibile.
	 * @param socket Il socket di output del server su cui verranno attesi i pacchetti.
	 * @param mainstage Lo stage della schermata inziale che serve a disabilitare la finestra di attesa.
	 */
	public void processInput(DatagramSocket socket, MainStageClient mainstage) {
		DatagramPacket packet = new DatagramPacket(new byte[5120], 5120);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			if(!e.getMessage().contains("ocket closed")) Utils.error("Errore nella ricezione del pacchetto", e);
			else return;
		}
		
		new Thread(() -> { // L'elaborazione è all'interno del thread per non rallentare le ricezioni
			
			try(ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(packet.getData()))) {
				// Input da parte del server
				int result = input.readByte();
				
				if(result == 0) {
					
					int totSoc = input.readInt();
					Vector<Societa> mercato = new Vector<>(totSoc);
					for(int i = 0; i < totSoc; i++)
						mercato.add((Societa)input.readObject());
					
					Giocatore giocatore = (Giocatore)input.readObject();
					
					int totAvv = input.readInt();
					Vector<GiocatoreAvversario> avversari = new Vector<>(totAvv);
					for(int i = 0; i < totAvv; i++)
						avversari.add((GiocatoreAvversario)input.readObject());
					
					Mercato.setSocieta(mercato);
					Main.setAvversari(avversari);
					Main.setGiocatore(giocatore);
					
					if(Main.getGameStage() == null) {
						mainstage.getRequestManager().stop();
						Platform.runLater(Main::createGameStage);
					} else Platform.runLater(() -> Main.getGameStage().update());
					
					// Timer di 30 secondi, se non riceve l'aggiornamento, segnala la conessione persa
					Main.getChannel().updateTimer();
					
				} else if(result == 1) { // Nome già esistente
					
					mainstage.getRequestManager().stop();
					Platform.runLater(() -> new Alert(AlertType.WARNING, "Il nome è già presente in partita.").showAndWait());
					Main.getChannel().close(false);
					
				} else if(result == 2) { // Giocatori massimi raggiunti
					
					mainstage.getRequestManager().stop();
					Platform.runLater(() -> new Alert(AlertType.WARNING, "La partita è piena.").showAndWait());
					Main.getChannel().close(false);
					
				} else if(result == 3) { // Il server è stato chiuso
					
					Main.getChannel().close(false);
					Platform.runLater(() -> {
						new Alert(AlertType.INFORMATION, "Il server è stato chiuso.").showAndWait();
						Main.getGameStage().close();
					});
					
				}
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				Utils.error("Errore nella decodifica del pacchetto", e);
			}
			
		}, "Packet_Processing").start();
		
	}
	
}
