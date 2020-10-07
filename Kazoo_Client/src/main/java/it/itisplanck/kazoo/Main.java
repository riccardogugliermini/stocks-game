package it.itisplanck.kazoo;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Vector;

import it.itisplanck.kazoo.control.Control;
import it.itisplanck.kazoo.control.ControlSchermataIniziale;
import it.itisplanck.kazoo.model.giocatori.Giocatore;
import it.itisplanck.kazoo.model.giocatori.GiocatoreAvversario;
import it.itisplanck.kazoo.model.sockets.SocketChannel;
import it.itisplanck.kazoo.view.config.MainStageClient;
import it.itisplanck.kazoo.view.game.GameStage;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Classe Main
 * @author Luca Polese
 * @version 1.0
 */
public class Main extends Application {
	
	private static MainStageClient mainstage;
	private static ControlSchermataIniziale gestoreBottoni;
	
	private static Stage stage;
	private static GameStage gamestage = null;
	private static Giocatore giocatore;
	private static SocketChannel channel;
	
	private static Vector<GiocatoreAvversario> avversari;

	/**
	 * Metodo per l'avvio dello Stage
	 * @param stage Stage da inserire nella finestra principale
	 */
	public void start(Stage stage) {
		Main.stage = stage;
		createInitialStage();
	}
	
	/**
	 * Metodo per la chiususa della Schermata iniziale<br>
	 * Quando viene chiusa la finestra cliccando sul pulsante "X"
	 */
	@Override
	public void stop() {
		if(channel != null) channel.close(true);
		mainstage.getRequestManager().stop();
	}
	
	/**
	 * Metodo creato per la schermata iniziale,<br>
	 * per facilitare la riapertura anche dopo aver avviato la partita.
	 */
	public static void createInitialStage() {
		mainstage = new MainStageClient(stage);
		stage.setTitle("Configurazione del Client");
		stage.setScene(mainstage.getMainScene());
		stage.show();
		gestoreBottoni = new ControlSchermataIniziale(mainstage);
		gestoreBottoni.addControl();
	}
	
	/**
	 * Assegna gli avversari ricevuto dal server
	 */
	public static void setAvversari(Vector<GiocatoreAvversario> avversari) {
		Main.avversari = avversari;
	}
	
	/**
	 * Metodo che comporterà la rimozione della schermata di configurazione<br>
	 * e aprirà la schermata di gioco.
	 */
	public static void createGameStage() {
		stage.hide();
		gamestage = new GameStage(giocatore);
		Control controller = new Control(gamestage);
		controller.addControl();
	}

	/**
	 * Metodo Getter del Vettore di Avversari
	 * @return avversari Vettore di {@link GiocatoreAvversario}
	 */
	public static Vector<GiocatoreAvversario> getAvversari() {
		return avversari;
	}
	
	/**
	 * Metodo Getter del {@link GameStage}
	 * @return gamestage {@link GameStage}
	 */
	public static GameStage getGameStage() {
		return gamestage;
	}
	
	/**
	 * Metodo per la creazione del Canale
	 * @param localPort Porta locale
	 * @param host Indirizzo del Server
	 * @param port Porta Locale
	 * @throws SocketException
	 */
	public static void createChannel(int localPort, InetAddress host, int port) throws SocketException {
		channel = new SocketChannel(localPort, new InetSocketAddress(host, port));
	}
	
	/**
	 * Metodo Getter del {@link SocketChannel}
	 * @return channel {@link SocketChannel} creato
	 */
	public static SocketChannel getChannel() {
		return channel;
	}
	
	/**
	 * Aggiorna il giocatore in base ai dati del server.
	 * @param nuovaIstanza La nuova istanza, creata dal pacchetto
	 */
	public static void setGiocatore(Giocatore nuovaIstanza) {
		giocatore = nuovaIstanza;
	}
	
	/**
	 * Metodo Getter del {@link Giocatore}
	 * @return giocatore Giocatore
	 */
	public static Giocatore getGiocatore() {
		return giocatore;
	}
	
	/**
	 * Metodo per l'avvio
	 * @param args
	 */
	public static void main(String args[]) {
		launch(args);
	}

}