package it.itisplanck.kazoo.view.config;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Classe MainStageClient<br>
 * Classe che implementa l'interfaccia grafica per la configurazione del Client<br>
 * Presenta una schermata per l'inserimento di:<br>
 * Indirizzo IP del Server<br>
 * Porta del Server<br>
 * Porta del Clien<br>
 * Nickname del Giocatore
 * @author Luca Polese, Emanuele Onnivello
 * @version 1.0
 */
public class MainStageClient {

	private Scene scena;
	private VBox mainPane;
	
	private InputElements elementi;
	private RequestManager sp;
	
	public MainStageClient(Stage stage) {
		stage.setResizable(false);
		elementi = new InputElements();
		mainPane = new VBox();
		mainPane.getChildren().add(elementi.getHBox());
		mainPane.getChildren().add(elementi.getVBox());
		mainPane.setSpacing(30);
		mainPane.setPadding(new Insets(20, 40, 20, 20));
		
		sp = new RequestManager(stage);
		scena = new Scene(mainPane);
	}
	
	/**
	 * Metodo Getter per il ritorno del {@link RequestManager}
	 * @return sp
	 */
	public RequestManager getRequestManager() {
		return sp;
	}

	/**
	 * Metodo Getter della Scena
	 * @return scena Scena creata
	 */
	public Scene getMainScene() {
		return scena;
	}
	
	/**
	 * Metoo Getter del Bottone per il Gioco
	 * @return elementi Bottone per il gioco
	 */
	public Button getButton() {
		return this.elementi.getPlay();
	}
	
	/**
	 * Metodo Getter del Nickname del Giocatore
	 * @return elementi Nome del Giocatore
	 */
	public String getName() {
		return elementi.getTfName();
	}

	/**
	 * Metodo Getter dell'IP del Giocatore
	 * @return elementi IP del Giocatore
	 */
	public String getIp() {
		return elementi.getTfIp();
	}
	
	/**
	 * Metodo Getter della Porta del server
	 * @return elementi Porta del Server
	 */
	public int getPorta() {
		return elementi.getPorta();
	}
	
	/**
	 * Metodo Getter della Porta Locale
	 * @return elementi Porta Locale
	 */
	public int getPortaLocale() {
		return elementi.getPortaLocale();
	}
}
