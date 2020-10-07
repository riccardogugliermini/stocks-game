package it.itisplanck.kazoo.view.game;

import java.text.DecimalFormat;

import it.itisplanck.kazoo.Main;
import it.itisplanck.kazoo.model.giocatori.GiocatoreAvversario;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Classe che contiene le informazioni dei giocatori avversari.
 * @author Riccardo Gugliermini
 *
 */
public class PlayersInfo extends VBox {
	private int currentPlayers=0;
	private DecimalFormat formatter;
	
	/**
	 * Metodo costruttore
	 * @param stage finestra principale del gioco
	 */
	public PlayersInfo(GameStage stage) {
		formatter = new DecimalFormat("#.##");
		update();
	}
	
	public void update() {
		for(int i = currentPlayers; i < Main.getAvversari().size(); i++) {
			GiocatoreAvversario avv = Main.getAvversari().get(i);
			Label name = new Label(avv.getNome());
			Label saldo = new Label();
			saldo.setText("Saldo: " + formatter.format(avv.getSaldo()).replaceAll(",", ".") + " €");
			name.setFont(new Font(20));
			Separator hSep = new Separator();
			hSep.setHalignment(HPos.CENTER);
			hSep.setMaxWidth(290);
			hSep.setMinWidth(290);
			getChildren().add(name);
			getChildren().add(saldo);
			getChildren().add(hSep);
			setSpacing(5);
			setFillWidth(true);
			VBox.setMargin(name, new Insets(0, 5, 0, 5));
			VBox.setMargin(saldo, new Insets(0, 5, 0, 5));
			currentPlayers++;
		}
	}
}
