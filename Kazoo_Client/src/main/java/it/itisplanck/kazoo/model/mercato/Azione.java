package it.itisplanck.kazoo.model.mercato;

import java.io.Serializable;

/**
 * Classe Azione 
 * @author Luca Polese
 * @version 1.0
 */
public class Azione implements Serializable {
	
	private static final long serialVersionUID = 3273537385709606233L;
	
	private String nome;
	private double quotazione;
	private int quantita;
	
	/**
	 * Metodo Costruttore dell'{@link Azione} che contiene:
	 * @param nome Nome dell'{@link Azione}
	 * @param quotazione Quotazione dell'{@link Azione}
	 * @param quantita Quantità di {@link Azione}
	 */
	public Azione(String nome,double quotazione,int quantita) {
		this.nome = nome;
		this.quotazione=quotazione;
		this.quantita=quantita;
	}

	/**
	 * Metodo Getter della Quotazione
	 * @return quotazione Quotazione dell'{@link Azione}
	 */
	public double getQuotazione() {
		return this.quotazione;
	}
	
	/**
	 * Metodo Setter della Quotazione
	 * @param quotazione Quotazione dell'{@link Azione}
	 */
	public void setQuotazione(double quotazione) {
		this.quotazione = quotazione;
	}

	/**
	 * Metodo Getter della Quantità
	 * @return quotazione Quantità dell'{@link Azione}
	 */
	public int getQuantita() {
		return this.quantita;
	}

	/**
	 * Metodo Getter della Quantità
	 * @param quantita Quantità dell'{@link Azione}
	 */
	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}
	
	/**
	 * Metodo Getter del Nome dell'{@link Azione}
	 * @return nome Nome dell'{@link Azione}
	 */
	public String getNome() {
		return nome;
	}
	
}
