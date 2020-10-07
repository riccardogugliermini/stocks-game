package it.itisplanck.kazoo.view.config;

import it.itisplanck.kazoo.view.NumberSpinner;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Classe che permette di inserire tutti gli elementi per l'acquisizione <br>
 * dell'Input della schermata principale
 * @author Luca Polese
 * @version 1.0
 */
public class InputElements {
	private Text configPlayer, serverIp, doublePoint, playerName, localPort;
	
	private NumberSpinner tfServerPort, tfLocalPort;
	private TextField tfServerIp, tfName;

	private Button play;
	
    private HBox hb;
    private VBox vb;
	
    /**
     * Metodo costruttore per l'istanziazione del pannello grafico<br>
     * per l'acquisizione dei dati per l'avvio del gioco da parte del Giocatore
     */
	public InputElements() {
		
		hb = new HBox();
		vb = new VBox();
		
		configPlayer = new Text("Configurazione del Giocatore"); // Centrata
		
		serverIp = new Text("Inserisci l'indirizzo del server e la porta");
		doublePoint = new Text(":");
		localPort = new Text("Inserisci la porta locale");
		playerName = new Text("Inserisci il tuo nickname");
		
		tfServerIp = new TextField();
		tfServerPort = new NumberSpinner(9090);
		tfLocalPort = new NumberSpinner(9191);
		tfName = new TextField();
		
		play = new Button("Gioca");
		
		doublePoint.setFont(new Font("Calibri", 30));
		doublePoint.setStyle("-fx-font-weight:bold;");
		configPlayer.setFont(new Font("Arial", 20));
		configPlayer.setStyle("-fx-font-weight:bold;");
		serverIp.setFont(new Font("Arial", 15));
		//serverIp.setStyle("-fx-font-weight:bold;");
		localPort.setFont(new Font("Arial", 15));
		//localPort.setStyle("-fx-font-weight:bold;");
		playerName.setFont(new Font("Arial", 15));
		//playerName.setStyle("-fx-font-weight:bold;");
		
	    hb.getChildren().add(configPlayer);
	    hb.setAlignment(Pos.CENTER);
	    
	    vb.getChildren().add(serverIp);
	    HBox hb0 = new HBox();
	    hb0.getChildren().addAll(tfServerIp, doublePoint, tfServerPort);
	    hb0.setSpacing(10);
	    vb.getChildren().add(hb0);
	    
	    vb.getChildren().add(localPort);
	    vb.getChildren().add(tfLocalPort);
	    vb.getChildren().add(playerName);
	    
	    BorderPane borderP = new BorderPane();
	    borderP.setLeft(tfName);
	    borderP.setRight(play);
	    vb.getChildren().add(borderP);
	    vb.setSpacing(10);
	}
	
	/**
	 * Metodo Getter del layout del titolo del della GUI
	 * @return hb HBox creata
	 */
	public HBox getHBox() { return hb; }
	/**
	 * Metodo Getter del layout principale della GUI
	 * @return vb VBox creata
	 */
	public VBox getVBox() { return vb; }

	/**
	 * Metodo Getter, ritorna il pulsante di avvio del gioco
	 * @return play {@link Button} per avviare il gioco e tentare la connessione
	 */
	public Button getPlay() {
		return play;
	}
	
	/**
	 * Metodo Getter, ritorna la stringa del nome inserito del Giocatore
	 * @return tfName {@link String} Nome del Giocatore
	 */
	public String getTfName() {
		return tfName.getText();
	}

	/**
	 * Metodo Getter, ritorna la stringa dell'IP del Server
	 * @return tfName {@link String} Indirizzo IP
	 */
	public String getTfIp() {
		return tfServerIp.getText();
	}
	
	/**
	 * Metodo Getter, ritorna un int contenente la Porta del Server
	 * @return tfServerPort Porta 
	 */
	public int getPorta(){
		return tfServerPort.getNumber();
	}
	
	/**
	 * Metodo Getter, ritorna un int contenente la Porta Locale
	 * @return tfLocalPort Porta
	 */
	public int getPortaLocale() {
		return tfLocalPort.getNumber();
	}
	
}
