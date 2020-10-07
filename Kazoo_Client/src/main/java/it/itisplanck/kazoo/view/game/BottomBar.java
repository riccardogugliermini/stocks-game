package it.itisplanck.kazoo.view.game;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import it.itisplanck.kazoo.Main;
import it.itisplanck.kazoo.view.NumberSpinner;

/**
 * Parte inferiore della finestra. Visualizza tutte le informazioni e le modalità per la gestione del portafogli.
 * @author Riccardo Gugliermini
 *
 */
public class BottomBar extends BorderPane {
	private GameStage stage;
	
	private HBox bar;
	
	private PlayersInfo playersInfo;
	private ActionsTable actions;
	private Label playerName;
	private Label mieAzioni;
	private Label mioSaldo;
	private Button sell = new Button ("Sell");
	private NumberSpinner qta=new NumberSpinner();
	
	/**
	 * Metodo costruttore
	 * @param stage finestra principale del gioco
	 */
	public BottomBar(GameStage stage) {
		this.stage = stage;
		this.setPadding(new Insets(10));
		playerName = new Label(stage.getGiocatore().getNome());
		playerName.setFont(new Font (18));
		playerName.setAlignment(Pos.CENTER);
		setMargin(playerName, new Insets (0,0,10,0));
		mioSaldo = new Label (Double.toString(stage.getGiocatore().getSaldo()));
		this.setTop(new VBox(playerName, new HBox (new Label("Saldo: "), mioSaldo, new Label (" €"))));
		this.setHeight(USE_COMPUTED_SIZE);
		this.setPrefHeight(BASELINE_OFFSET_SAME_AS_HEIGHT);
		this.setMinHeight(200);
		createBar(true);
	}
	
	/**
	 * Metodo che crea la barra per la gestione
	 * @param createPlayersInfo flag
	 */
	public void createBar(boolean createPlayersInfo) {
		bar = new HBox();
		playersInfo = new PlayersInfo(stage);
		actions = new ActionsTable (stage.getGiocatore().getSocietaAcquistate());
		
		bar.getChildren().add(actions);
		bar.getChildren().add(sell);
		bar.getChildren().add(qta);
		bar.getChildren().add(new Separator(Orientation.VERTICAL));
		ScrollPane scroll = new ScrollPane(playersInfo);
		//scroll.fitToWidthProperty();
		scroll.setMinWidth(300);
		scroll.setMaxWidth(300);
		//scroll.getStyleClass().add("scroll");
		//scroll.setStyle("-fx-background-color: transparent;");
		bar.getChildren().add(scroll);
		HBox.setHgrow(actions, Priority.ALWAYS);
		HBox.setHgrow(sell, Priority.ALWAYS);
		HBox.setHgrow(scroll, Priority.ALWAYS);
		bar.setSpacing(30);
		setCenter(bar);
	}
	
	public Button getSell() {
		return sell;
	}
	
	public ActionsTable getActions () {
		return actions;
	}

	public Label getMieAzioni() {
		return mieAzioni;
	}
	
	public PlayersInfo getPlayersInfo() {
		return playersInfo;
	}
	
	/**
	 * Metodo che, chiamato dal server, permette l'update dell'interfaccia.
	 */
	public void update() {
		playersInfo.update();
		mioSaldo.setText(Double.toString(Main.getGiocatore().getSaldo()));
		actions.update(Main.getGiocatore().getSocietaAcquistate());
	}

	public NumberSpinner getQta() {
		return qta;
	}
	
}
