package it.itisplanck.kazoo.model.mercato;

import java.io.Serializable;

/**
 * Viene creata una {@link Curva} di variazione di prezzo dell'azione
 * @author Luca Polese
 * @version 1.0
 */
public class Curva implements Serializable {
	
	private static final long serialVersionUID = 5052022663188576502L;
	
	private final int MAX_VARIAZIONE, MIN_VARIAZIONE;
	
	private double variazione;

	/**
	 * Metodo costruttore con due parametri
	 * @param MIN_VARIAZIONE La variazione minima (es. 0)
	 * @param MAX_VARIAZIONE La variazione massima (es. 100)
	 */
	public Curva(int MIN_VARIAZIONE,int MAX_VARIAZIONE) {
		
		this.MIN_VARIAZIONE=MIN_VARIAZIONE;
		this.MAX_VARIAZIONE=MAX_VARIAZIONE;
		
		randomize();
	}
	
	/**
	 * Crea un randomizzatore di base per l'Azione
	 */
	private void randomize() {
		this.variazione = MIN_VARIAZIONE + (Math.random() * (MAX_VARIAZIONE - MIN_VARIAZIONE));
	}

	/**
	 * Metodo Getter della Variazione della {@link Curva}
	 * @return variazione Variazione della {@link Curva}
	 */
	public double getVariazione() {
		return this.variazione;
	}
	
	/**
	 * Metodo Getter della Variazione Massima
	 * @return MAX_VARIAZIONE variabile final con massima variazione
	 */
	public int getMAX_VARIAZIONE() {
		return this.MAX_VARIAZIONE;
	}
	
	/**
	 * Metodo Getter della Variazione Minima
	 * @return MIN_VARIAZIONE variabile final con massima variazione
	 */
	public int getMIN_VARIAZIONE() {
		return this.MIN_VARIAZIONE;
	}

	/**
	 * Metodo Setter della Variazione
	 * @param variazione Varizione passata come parametro
	 */
	public void setVariazione(double variazione) {
		this.variazione = variazione;
	}
	
	
	
}
