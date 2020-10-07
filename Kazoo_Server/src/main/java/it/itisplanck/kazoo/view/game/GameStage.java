package it.itisplanck.kazoo.view.game;


import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.logging.Level;

import it.itisplanck.kazoo.model.giocatori.Giocatore;
import it.itisplanck.kazoo.model.giocatori.Giocatori;
import it.itisplanck.kazoo.model.sockets.SocketChannel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
/**
 * 
 * @author Nicola Bovolato
 * 
 */
public class GameStage extends Stage {
	
	private HBox mainBox;
	private VBox infoBox;
	private VBox logBox;
	
	private Label statusTitle;
	private Label playerListTitle;
	private Label logTitle;
	
	private Label host;
	private Label players;
	private Label defaultPrice;
	private Label running;
	
	private Button stop;
	
	private Separator HSeparator;
	private Separator VSeparator;
	
	private ListView<String> playerList;
	private TextArea log;
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	public GameStage() {
		super();
		
		setOnCloseRequest(e -> {
			setIconified(true);
			e.consume();
		});
		
		setResizable(false);
		
		playerList = new ListView<>();
		mainBox=new HBox();
		infoBox=new VBox();
		logBox=new VBox();
		
		statusTitle=new Label("Stato server");
		statusTitle.setFont(new Font(15));
		statusTitle.setStyle("-fx-font-weight:bold;");
		
		playerListTitle=new Label("Lista giocatori");
		logTitle=new Label("Log Server");
		host=new Label("Host: ");
		players=new Label("Giocatori online: ");
		defaultPrice=new Label("Saldo iniziale: ");
		running=new Label("Stato: Avviato");
		log = new TextArea();
		log.setEditable(false);
		
		stop=new Button("Ferma server");
		
		HSeparator=new Separator();
		VSeparator=new Separator();
		VSeparator.setOrientation(Orientation.VERTICAL);
		
		infoBox.getChildren().add(statusTitle);
		infoBox.getChildren().add(host);
		infoBox.getChildren().add(players);
		infoBox.getChildren().add(defaultPrice);
		infoBox.getChildren().add(running);
		infoBox.getChildren().add(stop);
		infoBox.getChildren().add(HSeparator);
		infoBox.getChildren().add(playerListTitle);
		infoBox.getChildren().add(playerList);
		infoBox.setSpacing(10);
		
		logBox.getChildren().add(logTitle);
		logBox.getChildren().add(log);
		logBox.setSpacing(10);
		VBox.setVgrow(log, Priority.ALWAYS);
		
		mainBox.getChildren().add(infoBox);
		mainBox.getChildren().add(VSeparator);
		mainBox.getChildren().add(logBox);
		
		logBox.setFillWidth(true);
		
		HBox.setMargin(infoBox, new Insets(10));
		HBox.setMargin(logBox, new Insets(10, 10, 10, 5));
		
		setScene(new Scene(mainBox, 750, 450));
	}
	
	public void setHost(InetAddress host, int port) {
		this.host.setText("Host: " + host.getHostAddress() + ":" + port);
	}
	
	public void setPlayers(int online, int max) {
		players.setText("Giocatori online: " + online + "/" + max);
	}
	
	public void setDefaultPrice(double price) {
		defaultPrice.setText("Saldo iniziale: " + price + " €");
	}
	
	/**
	 * @param avviato Avviato o in spegnimento
	 */
	public void setGameStatus(boolean avviato) {
		running.setText("Stato: " + (avviato ? "Avviato" : "In spegnimento"));
	}
	
	public Button getStop() {
		return stop;
	}
	
	public void addPlayer(String name, InetSocketAddress address) {
		playerList.getItems().add(name + " - " + address.getAddress().getHostAddress() + ":" + address.getPort() + " (Saldo: " + new DecimalFormat("#.##").format(Giocatori.getSaldoIniziale()) +" €)");
		playerList.refresh();
	}
	
	public void removePlayer(String name) {
		String str = null;
		for(String s : playerList.getItems())
			if(s.contains(name)) str = s;
		if(str != null) {
			playerList.getItems().remove(str);
			playerList.refresh();
		}
	}
	
	public void setPlayerList() {
		ObservableList<String> obsList = FXCollections.observableArrayList();
		DecimalFormat formatter = new DecimalFormat("#.##");
		
		for(Entry<InetSocketAddress, Giocatore> entry : SocketChannel.getConnectedPlayers().entrySet()) {
			obsList.add(
					entry.getValue().getNome() + " - " + 
					entry.getKey().getAddress().getHostAddress() + ":" + 
					entry.getKey().getPort() + 
					" (Saldo: " + formatter.format(entry.getValue().getSaldo()) +" €)");
		}
		
		playerList.setItems(obsList);
		playerList.refresh();
	}
	
	public void appenToLog(Level level, String message) {
		log.appendText(dateFormat.format(new Date()) + " [" + level.getName() + "] " + message + "\n");
		log.selectPositionCaret(log.getText().length());
	}
	
}
