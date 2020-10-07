package it.itisplanck.kazoo.model.giocatori;

import java.io.Serializable;

/**
 * Classe {@link GiocatoreAvversario} implementa Serializable<br>
 * Permette di creare il {@link GiocatoreAvversario}
 * Ogni {@link Giocatore} contiene un vettore di {@link GiocatoreAvversario} 
 * @author Luca Polese
 * @version 1.0
 */
public class GiocatoreAvversario  implements Serializable {
	
	private static final long serialVersionUID = -855110152429582503L;
	
	private String nome;
	private double saldo;
	
	/*
	 * Metodo costruttore della classe {@link GiocatoreAvversario}<br>
	 * Vengono passati come parametri il nome ed il saldo dei giocatori avversari
	 */
	public GiocatoreAvversario(String nome,double saldo) {
		this.nome=nome;
		this.saldo=saldo;
	}
	
	/**
	 * Metodo Getter del Nome del {@link GiocatoreAvversario}<br>
	 * @return nome Nome del {@link GiocatoreAvversario}
	 */
	public String getNome() {
		return nome;
	}
	
	/**
	 * Metodo Setter del Nome del {@link GiocatoreAvversario}<br>
	 * @param nome Nome nuovo
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/**
	 * Metodo Getter del Saldo {@link GiocatoreAvversario}<br>
	 * @return saldo Saldo del {@link GiocatoreAvversario}
	 */
	public double getSaldo() {
		return saldo;
	}
	
	/**
	 * Metodo Setter del Saldo {@link GiocatoreAvversario}
	 * @param saldo Saldo nuovo
	 */
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
}
