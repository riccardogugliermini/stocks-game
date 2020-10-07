package it.itisplanck.kazoo;

import java.net.SocketException;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;

import it.itisplanck.kazoo.control.ControlSchermataGioco;
import it.itisplanck.kazoo.control.ControlSchermataIniziale;
import it.itisplanck.kazoo.model.giocatori.Giocatori;
import it.itisplanck.kazoo.model.mercato.Andamento;
import it.itisplanck.kazoo.model.mercato.Azione;
import it.itisplanck.kazoo.model.mercato.Curva;
import it.itisplanck.kazoo.model.mercato.Mercato;
import it.itisplanck.kazoo.model.mercato.Societa;
import it.itisplanck.kazoo.model.sockets.SocketChannel;
import it.itisplanck.kazoo.view.config.MainStage;
import it.itisplanck.kazoo.view.game.GameStage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Classe Main
 * @author Luca Polese
 * @version 1.0
 */
public class Main extends Application {
	
	private static Vector<Societa> nuoveSocieta;
	private static MainStage mainstage;
	private static GameStage gamestage;
	private static ControlSchermataIniziale gestoreBottoni;
	private static SocketChannel channel;
	
	/**
	 * Metodo per l'avvio dello Stage
	 * @param stage Stage da inserire nella finestra principale
	 */
	public void start(Stage stage) {
		createInitialStage(stage);
	}
	
	/**
	 * Metodo per la creazione dello Stage Iniziale
	 * @param stage Stage Principale
	 */
	public static void createInitialStage(Stage stage) {
		mainstage = new MainStage(stage);
		stage.setScene(mainstage.getMainScene());
		stage.show();
		
		gestoreBottoni = new ControlSchermataIniziale(mainstage);
		gestoreBottoni.addControl();
		mainstage.getDisplayPane().getAdd().setOnAction((event) -> { gestoreBottoni.add(); });
		mainstage.getDisplayPane().getRemove().setOnAction((event) -> { gestoreBottoni.remove(); });
		
		nuoveSocieta = new Vector<Societa>();
	}
	
	/**
	 * Metodo per la creazione dello Stage di Gioco
	 */
	public static void createGameStage() {
		mainstage.close();
		gamestage = new GameStage();
		gamestage.setHost(channel.getAddress(), channel.getPort());
		gamestage.setPlayers(Giocatori.getGiocatori().size(), Giocatori.getGiocatoriMassimi());
		gamestage.setDefaultPrice(Giocatori.getSaldoIniziale());
		gamestage.show();
		
		new ControlSchermataGioco(gamestage);
		gamestage.appenToLog(Level.INFO, "Server avviato in: " + channel.getAddress().getHostAddress() + ":" + channel.getPort());
	}
	
	/**
	 * Metodo per l'esclusione di un giocatore andato in bancarotta
	 */
	public static void kick() {
		new Alert(AlertType.INFORMATION, "Sei andato in banca rotta!").showAndWait();
		gamestage.close();
	}
	
	/*@Override
	public void stop() {
		if(channel != null) channel.close();
	}*/
	
	/**
	 * Metodo per la creazione di un canale
	 * @param port Porta del Server
	 * @throws SocketException Eccezione sul Socket
	 */
	public static void createChannel(int port) throws SocketException {
		channel = new SocketChannel(port);
		channel.enableListener();
		channel.enableAutoBroadcast(3);
	}
	
	/**
	 * Metodo Getter del {@link SocketChannel}
	 * @return channel Canale
	 */
	public static SocketChannel getChannel() {
		return channel;
	}
	
	/**
	 * Metodo Getter del {@link GameStage}
	 * @return gamestage GameStage
	 */
	public static GameStage getGameStage() {
		return gamestage;
	}
	
	/**
	 * Metodo principale
	 * @param args Parametri passati all'avvio del gioco
	 */
	public static void main(String args[]) {
		launch(args);
	}

	/**
	 * Assegna il vettore di società di default
	 */
	public static void creaMercatoDefault() {
		final int varMAX = 100, varMIN = 0;
		Mercato.setSocieta(new Vector<Societa>(Arrays.asList(new Societa[] {
				new Societa(new Azione("AAPL", 50, 50), new Andamento(new Curva(varMIN, varMAX), Math.random())),
				new Societa(new Azione("SBUX", 50, 50), new Andamento(new Curva(varMIN, varMAX), Math.random())),
				new Societa(new Azione("BTC-USD", 50, 50), new Andamento(new Curva(varMIN, varMAX), Math.random())),
		})));
		Platform.runLater(()->{
			for(int i=0;i<Mercato.getSocieta().size();i++) {
				gamestage.appenToLog(Level.INFO, "Azione: "+Mercato.getSocieta().get(i).getAzione().getNome()+" min: "+Mercato.getSocieta().get(i).getAndamento().getAndamentoCurva().getMIN_VARIAZIONE()+" max:"+Mercato.getSocieta().get(i).getAndamento().getAndamentoCurva().getMAX_VARIAZIONE()+" rand:"+Mercato.getSocieta().get(i).getAndamento().getRandomizzatoreVariazione() );
			}
		});
	}
	
	/**
	 * Metodo Getter delle Società
	 * @return nuoveSocieta Vettore di Società
	 */
	public static Vector<Societa> getSocieta() {
		return nuoveSocieta;
	}
	
	/**
	 * Assegna il vettore di società personalizzate al mercato
	 */
	public static void creaMercatoPersonalizzato() {
		Mercato.setSocieta(nuoveSocieta);
	}
	
	/**
	 * Metodo per aggiungere al vettore di società , una nuova società
	 * @param nuova Nuova società
	 */
	public static void setSocieta(Societa nuova) {
		nuoveSocieta.add(nuova);
	}
	
}
