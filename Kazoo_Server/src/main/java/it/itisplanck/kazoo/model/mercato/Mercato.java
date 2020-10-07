package it.itisplanck.kazoo.model.mercato;

import java.util.Vector;
import java.util.concurrent.Semaphore;
/**
 * 
 * @author Nicola Bovolato
 * Implementazione di un mercato azionario composto da piu societa
 */
public class Mercato {
	
	private static Vector<Societa> societa = new Vector<>();
	private static Semaphore mutex=new Semaphore(1);
	
	public static Vector<Societa> getSocieta(){
		return societa;
	}
	
	/**
	 * Ricava la societa tramite il nome dell'azione
	 * @param nome Il nome dell'azione
	 * @return La societa� corrispondente, altrimenti <code>null</code>
	 */
	public static Societa getSocieta(String nome){
		for(Societa s : societa)
			if(s.getAzione().getNome().equals(nome)) return s;
		return null;
	}
	/**
	 * Assegna vetore di societa al mercato
	 * @param societa vettore di societa da settare al mercato
	 */
	public static void setSocieta(Vector<Societa> societa) {
		Mercato.societa = societa;
	}
	
	/**
	 * Cerca societa in mercato per nome
	 * @param nome il nome della societa
	 * @return la posizione della societa nel Vector (-1 se non trovata)
	 */
	public static int searchForSocieta(String nome) {
		for(int i=0;i<societa.size();i++) {
			if(societa.get(i).getAzione().getNome().equals(nome)) return i;
		}
		return -1;
	}
	/**
	 * Aggiorna il mercato e i valori delle societa
	 */
	public static void update() {
		for(int i = 0; i < societa.size(); i++)
			societa.get(i).update();
	}
	
	public static Semaphore getMutex() {
		return mutex;
	}
}
