package it.itisplanck.kazoo.model.mercato;

import java.io.Serializable;
/**
 * 
 * @author Nicola Bovolato
 * Implementazione di un'azione
 */
public class Azione implements Serializable {
	
	private static final long serialVersionUID = 3273537385709606233L;
	
	private String nome;
	private double quotazione;
	private int quantita;
	
	public Azione(String nome,double quotazione,int quantita) {
		this.nome=nome;
		this.quotazione=quotazione;
		this.quantita=quantita;
	}

	public double getQuotazione() {
		return this.quotazione;
	}

	public void setQuotazione(double quotazione) {
		this.quotazione = quotazione;
	}

	public int getQuantita() {
		return this.quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	public String getNome() {
		return this.nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
