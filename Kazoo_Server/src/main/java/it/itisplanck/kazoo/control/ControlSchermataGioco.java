package it.itisplanck.kazoo.control;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import it.itisplanck.kazoo.Main;
import it.itisplanck.kazoo.view.game.GameStage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Controlla la schermata a server avviato
 * @author Onnivello Emanuele
 */
public class ControlSchermataGioco implements EventHandler<ActionEvent> {
	
	private GameStage stage;
	
	public ControlSchermataGioco(GameStage gameStage) {
		stage = gameStage;
		stage.getStop().setOnAction(this);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == stage.getStop()) {
			
			stage.appenToLog(Level.WARNING, "Server fermato.");
			stage.setGameStatus(false);
				
			Main.getChannel().sendCloseOperation();
			
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Main.getChannel().close();
					Platform.runLater(stage::hide);
					timer.cancel();
				}
			}, 1000);
			
		}
	}
	
}
