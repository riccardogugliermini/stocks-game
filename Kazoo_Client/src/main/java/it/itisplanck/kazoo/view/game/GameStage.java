package it.itisplanck.kazoo.view.game;

import it.itisplanck.kazoo.model.giocatori.Giocatore;
//import javafx.application.Appli
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Classe contenete la finestra di gioco.
 * @author Riccardo Gugliermini
 *
 */
public class GameStage extends Stage {
	private Scene scene;
	private BorderPane rootLayout;
	private TopBar topBar;
	private BottomBar bottomBar;
	
	private Giocatore giocatore;
	
	/**
	 * Metodo costruttore della finestra.
	 * @param m informazioni sul mercato.
	 * @param g informazioni sul giocatore.
	 */
	public GameStage(Giocatore g) {
		this.giocatore = g;
		
		VBox root = new VBox ();
		
		rootLayout = new BorderPane ();
		topBar = new TopBar (this);
		bottomBar = new BottomBar (this);
		
		root.getChildren().addAll(topBar,new Separator (),bottomBar);
		//VBox.setVgrow(topBar, Priority.ALWAYS);
		bottomBar.setMaxHeight(Double.MAX_VALUE);
		topBar.setMaxHeight(Double.MAX_VALUE);
		
		VBox.setVgrow(bottomBar, Priority.ALWAYS);
		VBox.setVgrow(topBar, Priority.ALWAYS);
		
		scene = new Scene (root);
		
		root.setPadding(new Insets (10,10,10,10));
		scene.getStylesheets().add("it/itisplanck/kazoo/view/game/style.css");
		this.setScene(scene);
		this.sizeToScene();
		
		this.setHeight(760);
		this.setWidth(1024);
		this.setMinHeight(500);
		this.setMinWidth(850);
		this.setTitle("Borsa");
		this.show();
	}

	public BorderPane getRootLayout() {
		return rootLayout;
	}

	public TopBar getTopBar() {
		return topBar;
	}
	
	public BottomBar getBottomBar () {
		return bottomBar;
	}
	
	public Giocatore getGiocatore() {
		return giocatore;
	}
	
	/**
	 * Metodo richiamato dal sever per eseguire update della finestra.
	 * @param m informazioni sul mercato.
	 */
	public void update() {
		Platform.runLater(() -> {
			topBar.update();
			bottomBar.update();
		});
	}
	
}
