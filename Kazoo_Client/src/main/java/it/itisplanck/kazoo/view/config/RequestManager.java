package it.itisplanck.kazoo.view.config;

import java.util.Timer;
import java.util.TimerTask;

import it.itisplanck.kazoo.Main;
import it.itisplanck.kazoo.model.sockets.SocketChannel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Rappresenta la schermata di collegamento al server con una barra di caricamento.
 * @author Luca Polese, Onnivello Emanuele
 */
public class RequestManager {
	
	private Stage d;
	private Timer timer;
	private TimerTask task;
	
	/**
	 * Costruttore che istanzia il nuovo stage e il timer.
	 * @param owner La finestra principale
	 */
	public RequestManager(Stage owner) {
		timer = new Timer();
		d = new Stage();
		d.setResizable(false);
		d.setOnCloseRequest(e -> { e.consume(); }); // Evita la chiusura della finestra dalla 'X'
		d.setTitle("Collegamento al server");
		Text title = new Text("Collegamento al server in corso...");
		title.setFont(new Font("Arial", 20));
		title.setStyle("-fx-font-weight: bold; text-alignment: center;");
		d.initModality(Modality.APPLICATION_MODAL);
		d.initOwner(owner);
		
		ProgressBar bar = new ProgressBar();
		bar.setVisible(true);
		bar.setPrefWidth(title.getLayoutBounds().getWidth());
		
		Button backButton = new Button("Annulla");
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				closeTimer();
				Main.getChannel().close(false);
				d.hide();
			}
		});
		
		HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER);
		VBox vb = new VBox();
		vb.setAlignment(Pos.CENTER);
		vb.setSpacing(8);
		vb.getChildren().add(title);
		vb.getChildren().add(bar);
		vb.getChildren().add(backButton);
		hb.getChildren().add(vb);
		
		HBox.setMargin(hb, new Insets(8, 8, 8, 8));
		Scene scena = new Scene(hb, 400, 90);
		
		d.setScene(scena);
	}
	
	/**
	 * Cancella il {@link TimerTask} attivo nel timer.
	 */
	public void closeTimer() {
		if(task != null) {
			task.cancel();
			task = null;
		}
		if(timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	
	/**
	 * Rende visibile la finestra e avvia il timer per la connessione al server,<br>
	 * se il timer arriva alla fine, la finestra verrà chiusa, il {@link SocketChannel} verrà chiuso e<br>
	 * e verrà mostrato un {@link Alert} con la richiesta fallita.
	 */
	public void start() {
		d.show();
		if(timer == null) timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					d.hide();
					new Alert(AlertType.WARNING, "Impossibile collegarsi al server.").showAndWait();
				});
				Main.getChannel().close(false);
			}
		};
		timer.schedule(task, 15000);
	}
	
	/**
	 * Chiude anche la schermata, oltre che al timer
	 */
	public void stop() {
		Platform.runLater(d::hide);
		closeTimer();
	}
	
}
