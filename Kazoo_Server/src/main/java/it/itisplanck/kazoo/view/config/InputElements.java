package it.itisplanck.kazoo.view.config;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Classe per l'inserimento dei valori del server presi in Input
 * @author Luca Polese
 * @version 1.0
 */
public class InputElements {
	private Text server;
	private Text porta;
	private Text playerAmount;
	private Text playerPrice;
	
	private NumberSpinner tfPorta;
	private NumberSpinner tfPlayerAmount;
	private NumberSpinner tfPlayerPrice;
    private HBox hb0, hb1;
	
	public InputElements() {
		
		hb0 = new HBox();
		hb1 = new HBox();
		
		server = new Text("Configurazione del Server");
		porta = new Text("Porta (Default: 9090)");
		playerAmount = new Text("Numero giocatori");
		playerPrice = new Text("Importo iniziale");
		
		tfPorta = new NumberSpinner(9090);	
		tfPlayerAmount = new NumberSpinner(2);
		tfPlayerPrice = new NumberSpinner(100);
		
		//grid.add(child, columnIndex, rowIndex);
		server.setFont(new Font(20));
		porta.setStyle("-fx-font-weight:bold;");
		playerAmount.setStyle("-fx-font-weight:bold;");
		playerPrice.setStyle("-fx-font-weight:bold;");
		
		GridPane grid = new GridPane();
		grid.add(porta,0,1);
		grid.add(playerAmount,0,2);
		grid.add(playerPrice,0,3);
		
		grid.add(tfPorta,1,1);
		grid.add(tfPlayerAmount,1,2);
		grid.add(tfPlayerPrice,1,3);
		grid.setVgap(5.0);
	    grid.setHgap(5.0);
	    
	    hb0.getChildren().add(server);
	    hb0.setAlignment(Pos.CENTER);
	    hb1.getChildren().add(grid);
	    hb1.setAlignment(Pos.CENTER);
	}

	/**
	 * Metodo Getter del prezzo di Defautl
	 * @return tfPlayerPrice
	 */
	public int getDefaultPrice() {
		return tfPlayerPrice.getNumber();
	}

	/**
	 * Metodo Getter del numero massimo di giocatori
	 * @return tfPlayerAmount
	 */
	public int getMaxPlayers() {
		return tfPlayerAmount.getNumber();
	}
	
	/**
	 * Metodo Getter della porta
	 * @return tfPorta
	 */
	public int getPorta() {
		return tfPorta.getNumber();
	}

	/**
	 * Metodo Getter del Primo pannello
	 * @return hb0
	 */
	public HBox getPane0() {
		return hb0;
	}
	
	/**
	 * Metodo Getter del Secondo pannello
	 * @return hb0
	 */
	public HBox getPane1() {
		return hb1;
	}
	
}
