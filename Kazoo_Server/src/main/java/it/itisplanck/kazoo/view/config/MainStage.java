package it.itisplanck.kazoo.view.config;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Classe che contiene il Frame principale per la creazione della GUI del Server
 * @author Luca Polese
 * @version 1.0
 */
public class MainStage {

	private Scene scena;
	private InputElements elementi;
	private VBox mainPane;
	private CompaniesChoice societa;
	private Stage stage;
	
	public MainStage (Stage mainStage) {
		mainStage.setTitle("Configurazione del Server");
		mainStage.setResizable(false);
		stage = mainStage;
		elementi = new InputElements();
		mainPane =new VBox();
		mainPane.setAlignment(Pos.CENTER);
		mainPane.getChildren().addAll(elementi.getPane0(), elementi.getPane1());
		mainPane.setSpacing(30);
		
		societa = new CompaniesChoice(mainStage);
		mainPane.getChildren().add(societa.getPane());
		
		scena = new Scene(mainPane, 450, 250);
	}
	
	/**
	 * Metodo per la chiusura dello Stage
	 */
	public void close() {
		stage.hide();
	}

	/**
	 * Metodo Getter del MainStage
	 * @return scena Stage principale
	 */
	public Scene getMainScene() {
		return scena;
	}
	
	/**
	 * Metodo Getter della Porta
	 * @return elementi Porta
	 */
	public int getPorta() {
		return elementi.getPorta();
	}
	
	/**
	 * Metodo Getter del Bottone
	 * @return elementi Bottone
	 */
	public Button getButton() {
		return societa.getButton();
	}
	
	/**
	 * Metodo Getter per il ritorno del numero di giocatori massimo
	 * @return elementi Giocatori Massimi
	 */
	public int getMaxPlayers() {
		return elementi.getMaxPlayers();
	}
	
	/**
	 * Metodo Getter del prezzo di Default
	 * @return elementi Prezzo di default
	 */
	public int getDefaultPrice() {
		return elementi.getDefaultPrice();
	}
	
	/**
	 * Metodo Getter del {@link DisplayPane}
	 * @return società Pannello
	 */
	public CompaniesChoice getDisplayPane() {
		return societa;
	}
	
}
