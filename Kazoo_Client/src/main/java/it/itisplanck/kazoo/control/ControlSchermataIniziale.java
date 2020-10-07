package it.itisplanck.kazoo.control;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import it.itisplanck.kazoo.Main;
import it.itisplanck.kazoo.model.sockets.SocketChannel;
import it.itisplanck.kazoo.view.config.MainStageClient;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

/**
 * Classe ControlSchermataIniziale<br>
 * Da utilizzare per effettare i controlli sulla schermata {@link MainStageClient}
 * @author Luca Polese, Emanuele Onnivello
 * @version 1.0
 */

public class ControlSchermataIniziale implements EventHandler<ActionEvent>{
	private MainStageClient mainstage;
	private Button sent;
	
	/**
	 * Metodo costruttore della Classe<br>
	 * Richiede come parametro lo Stage che verrà prelevato dalla classe {@link MainStageClient}
	 * @param stage Stage principale
	 */
	public ControlSchermataIniziale(MainStageClient stage) {
		this.mainstage = stage;
		this.sent = stage.getButton();
	}
	
	/**
	 * Override del metodo handle presente in {@link EventHandler}
	 * Permette di effettuare i controlli in base all'evento ricevuto<br>
	 * In caso di errore apre una finestra di allerta, altrimenti<br>
	 * crea l'IP del Server e la relativa porta, in base ai dati ricavati<br>
	 * tentando infine il collegamento.
	 */
	@Override
	public void handle(ActionEvent event) {
		
		if(mainstage.getIp().equals("")) {
			new Alert(AlertType.WARNING, "Indirizzo inserito non valido").showAndWait();
			return;
		} else if(!portaValida(mainstage.getPorta())) {
			new Alert(AlertType.WARNING, "Porta inserita non valida").showAndWait();
			return;
		} else if(!portaValida(mainstage.getPortaLocale())) {
			new Alert(AlertType.WARNING, "Porta locale inserita non valida").showAndWait();
			return;
		} else if(mainstage.getName().equals("")) {
			new Alert(AlertType.WARNING, "Nome inserito non valido").showAndWait();
			return;
		}
		if (event.getSource() == sent) {
			try {
				
				InetAddress ip = InetAddress.getByName(mainstage.getIp());
				
				// Crea il canale di comunicazione tra client e server per poter inviare la richiesta
				Main.createChannel(mainstage.getPortaLocale(), ip, mainstage.getPorta());
				// Abilita l'ascoltatore per la risposta dal server
				Main.getChannel().enableListener(mainstage);
				// Fa partire il timer di 30 secondi per verificare che il server abbia ricevuto la richiesta
				mainstage.getRequestManager().start();
				// Fa la richiesta vai socket
				SocketChannel.sendRequest(mainstage.getName());
				
			} catch (UnknownHostException e) {
				new Alert(AlertType.WARNING, "Indirizzo inserito non valido").showAndWait();
			} catch(SocketException e0) {
				new Alert(AlertType.WARNING, "Porta locale già utilizzata").showAndWait();
			}
			
		} else new Alert(AlertType.WARNING, "Dati inseriti non validi").showAndWait();
				
	}
	
	/**
	 * Metodo per la registrazione del Gestore dei Bottoni neella classe {@link Main}
	 */
	public void addControl() {
		sent.setOnAction(this);
	}

	/**
	 * Controllo delle porte inserite nella classe {@link InputElements}
	 * @param porta
	 * @return
	 */
	private boolean portaValida(int porta) {
		return !(porta < 1024 || porta > 65535);
	}

}
