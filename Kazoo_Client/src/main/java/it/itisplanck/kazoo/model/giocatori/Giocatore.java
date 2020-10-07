package it.itisplanck.kazoo.model.giocatori;

import java.io.Serializable;
import java.util.Vector;

import it.itisplanck.kazoo.model.mercato.Societa;
import it.itisplanck.kazoo.model.sockets.SocketChannel;

/**
 * Classe {@link Giocatore} implementa Serializable<br>
 * Permette di creare il {@link Giocatore}
 * @author Luca Polese
 * @version 1.0
 */
public class Giocatore  implements Serializable {
	
	private static final long serialVersionUID = -2407386438868057161L;
	
	private String nome;
	private double saldo = -1;
	private Vector<Societa> societaAcquistate = null;
	private boolean inGioco;
	
	/**
	 * Metodo costruttore del {@link Giocatore}
	 * @param nome Nome del Giocatore da inserire nel costruttore
	 */
	public Giocatore(String nome) {
		this.nome = nome;
		inGioco = false;
	}
	
	/**
	 * Metodo per controllare se il {@link Giocatore} è ancora in Gioco<br>
	 * per mezzo del ritorno di un boolean.
	 * @return inGioco Boolean per il controllo del giocatore, se in gioco
	 */
	public boolean isInGioco() {
		return inGioco;
	}
	
	/**
	 * Metodo Setter, per modificare lo stato del giocatore
	 * @param inGioco Varbiabile che indica lo stato del giocatore
	 */
	public void setInGioco(boolean inGioco) {
		this.inGioco = inGioco;
	}
	
	/**
	 * Metodo Setter, per il salvataggio del nuovo {@link Vector} di {@link Societa} <br>
	 * creato passato come parametro al metodo
	 * @param S Vettore di {@link Societa}
	 */
	public void setSocieta(Vector<Societa> S) {
		societaAcquistate = S;
	}
	
	/**
	 * Metodo Getter per il ritorno del {@link Vector} di {@link Societa}<br>
	 * presente in {@link Giocatore}
	 * @return societaAcquistate Società Acquistate dal {@link Giocatore}
	 */
	public Vector<Societa> getSocietaAcquistate() {
		return societaAcquistate;
	}
	
	/**
	 * Metodo Getter del Nome del {@link Giocatore}
	 * @return nome Nome del {@link Giocatore}
	 */
	public String getNome() {
		return nome;
	}
	
	/**
	 * Metodo Getter del Saldo del {@link Giocatore}
	 * @return saldo Saldo del {@link Giocatore}
	 */
	public double getSaldo() {
		return saldo;
	}
	
	/**
	 * Metodo Setter del Saldo del {@link Giocatore}
	 * @param saldo Saldo
	 */
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	/**
	 * Metodo di ricerca di una società in base al nome, passato come parametro al metodo<br>
	 * Ritorna la posione nella società nel vettore oppure -1 in caso di assenza
	 * @param nome Nome della {@link Societa} da ricercare
	 * @return i Posizione della {@link Societa}
	 */
	public int searchForSocietaAcquistate(String nome) {
		for(int i=0;i<societaAcquistate.size();i++) if(societaAcquistate.get(i).getAzione().getNome().equals(nome)) return i;
		return -1;
	}
	
	/**
	 * Metodo Setter per comprare un'azione<br>
	 * L'azione comprata e la quantità vengono inserite come parametri<br>
	 * Vengono utilizzati infine come parametro, per il metodo buy() della classe {@link SocketChannel}
	 * @param S azione da acquistare
	 * @param quantita quantità di azione da acquistare
	 */
	public void buy(Societa S, int quantita) {
		SocketChannel.buy(nome, S.getAzione().getNome(), quantita);
	}
	
	/**
	 * Metodo Setter per vendere un'azione<br>
	 * L'azione venduta e la quantità vengono inserite come parametri<br>
	 * Vengono utilizzati infine come parametro, per il metodo sell() della classe {@link SocketChannel}
	 * @param S azione da vendere
	 * @param quantita quantità di azione da vendere
	 */
	public void sell(Societa S, int quantita) {
		SocketChannel.sell(nome, S.getAzione().getNome(), quantita);
	}
	
	/**
	 * Metodo per l'update delle Società acquistate
	 */
	public void updateSocietaAcquistate() {
		for(int i=0;i<societaAcquistate.size();i++) {
			societaAcquistate.get(i).updateCapitalizzazione();
		}
	}
	
	/**
	 * Metodo per il confronto di più giocatori
	 */
	@Override
	public boolean equals(Object object) {
		if(object == null || !(object instanceof Giocatore)) return false;
		Giocatore gioc = (Giocatore)object;
		return nome.equals(gioc.getNome());
	}
	
}
