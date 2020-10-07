package it.itisplanck.kazoo.model.mercato;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Classe Mercato: base di dati delle varie società create
 * @author Luca Polese
 * @version 1.0
 */
public class Mercato {
	
	private static Vector<Societa> societa = new Vector<>();
	private static Map<String, Vector<Integer>> andamenti = new HashMap<>();
	
	/**
	 * Metodo Getter della {@link Societa}
	 * @return società Società
	 */
	public static Vector<Societa> getSocieta() {
		return societa;
	}
	
	/**
	 * Metodo Setter della {@link Societa}
	 * @param societa Società come parametro
	 */
	public static void setSocieta(Vector<Societa> societa) {
		Mercato.societa = societa;
		for(Societa nuova : societa) {
			Vector<Integer> get = andamenti.get(nuova.getAzione().getNome());
			if(get == null) get = new Vector<>();
			addAndamento(get, (int)nuova.getAzione().getQuotazione());
			andamenti.put(nuova.getAzione().getNome(), get);
		}
	}
	
	/**
	 * Metodo per l'aggiunta dell'Andamento 
	 * @param vec Vettore passato come parametro
	 * @param nuovo Valore intero da inserire
	 */
	public static void addAndamento(Vector<Integer> vec, int nuovo) {
		if(vec == null) return;
		if(vec.size() >= 60) vec.remove(0);
		vec.add(nuovo);
	}
	
	/**
	 * Ricerca per società
	 * @param nome Nome da cercare
	 * @return posizione da ritornare
	 */
	public static int searchForSocieta(String nome) {
		for(int i=0;i<societa.size();i++) {
			if(societa.get(i).getAzione().getNome().equals(nome)) return i;
		}
		return -1;
	}
	
	/**
	 * Metodo Getter dell'andamento
	 * @param soc Nome della società da ricercacre
	 * @return andamenti Andamento da ritornare
	 */
	public static Vector<Integer> getAndamento(String soc) {
		return andamenti.get(soc);
	}
	
}
