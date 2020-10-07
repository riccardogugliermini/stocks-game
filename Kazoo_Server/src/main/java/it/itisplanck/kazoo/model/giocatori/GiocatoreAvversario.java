package it.itisplanck.kazoo.model.giocatori;

import java.io.Serializable;

/**
 * 
 * @author Nicola Bovolato
 * Implementa un giocatore avversario di @class Giocatore
 */
public class GiocatoreAvversario implements Serializable {
	
	private static final long serialVersionUID = -855110152429582503L;
	
	private String nome;
	private double saldo;
	
	public GiocatoreAvversario(String nome,double saldo) {
		this.nome=nome;
		this.saldo=saldo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
}
