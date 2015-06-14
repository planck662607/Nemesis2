package nemesis2.core.util.cache;

/**
 * Mise en cache d'un tableau de données associées à des index (int)
 * Permet d'éviter une initialisation de données qui sont déjà disponibles dans le cache
 * 
 * Utilisation : Une classe doit gerer des données indexées et veut pouvoir réutiliser dans initialiser systématiquement
 *               des données de l'appel precedent (car ici on ne va pas conserver des données inutilisé lors d'un appel
 *               c'est une amélioration qui serait interressante à faire [clear des unused non systématique]) 
 *   
 *   Maclasse implémente CacheIndexManager
 *      - pour la demande (en provenance du cache) demandant l'initialisation d'une donnée
 *      
 *   ensuite ma Maclasse appelle  setRange ou setIndices pour initialiser les index (et données via CacheIndexManager.setUpData)
 *   puis appelle getData ou getDataLocalIndex pour recuperer les données du cache;
 * 
 * @author Francois
 *
 * @param <T>
 */
public class CacheIndex<T> {
	private T[] data;
	private int[] localIndices; // liste des index connu est asocié aux data[] : localIndices[n] = une valeur d'index associée à data[n]
	private int[] sequence;
	
	private CacheIndexManager<T> manager;

	public CacheIndex(CacheIndexManager<T> manager, T[] data) {
		this.manager = manager;
		this.data = data;
		localIndices = new int[data.length];
		sequence = new int[data.length];
		for(int i=0;i<data.length;i++) {
			localIndices[i]=-1;
			sequence[i]=-1;
		}
	}
	
	/**
	 * prepare les données associées aux valeurs comprises dans la fouchette [start, start+count[
	 * @param start les index des données que l'on veut avoir dans le cache
	 * @param count
	 */
	public void setRange(int start, int count) {
		// on a besoin des start à start+count-1
		// le reste on met -1
		int ind;
		
		int lim= start+count;
		// marquage des unused
		for(int i=0;i<data.length;i++) {
			ind =localIndices[i];
			if(ind!=-1 && (ind<start || ind>=lim)) {
				// on le flag unused (TODO si on a de la place essayer de conserver les valeurs inutilisées)
				//System.out.println("Cache suppression de l'index "+localIndices[i]+ "en position "+i);
				localIndices[i] = -1;
			} else {
				// on en a besoin on laisse
			}
		}
		
		// initialisation de la sequence
		int indRech;
		for(int i=0;i<count; i++) {
			indRech = start+i;
			ind = getIndicesIndex(indRech);
			// XXX if ind==-1 error
			if(localIndices[ind]==-1) {
				// initialisation à faire
				// ...
				manager.setUpData(indRech, data, ind);
				//System.out.println("setup de l'index "+indRech+"en position "+ind);
				localIndices[ind] = indRech;
			}
			sequence[i] = ind;
		}
	}
	/**
	 * alternative à setRange
	 * ici on precise directement les index à mettre en cache
	 * @param indices les index des données que l'on veut avoir dans le cache
	 */
	public void setIndices(int... indices) {
		// on a besoin des start à start+count-1
		// le reste on met -1
		int ind;
		
		// marquage des unused
		for(int i=0;i<data.length;i++) {
			ind =localIndices[i];
			if(ind!=-1 && getIndex(indices, ind)==-1) {
				// on le flag unused (TODO si on a de la place essayer de conserver les valeurs inutilisées)
				System.out.println("Cache suppression de l'index "+localIndices[i]+ "en position "+i);
				localIndices[i] = -1;
			} else {
				// on en a besoin on laisse
			}
		}
		
		// initialisation de la sequence
		int indRech;
		for(int i=0;i<indices.length; i++) {
			indRech = indices[i];
			ind = getIndicesIndex(indRech);
			// XXX if ind==-1 error
			if(localIndices[ind]==-1) {
				// initialisation à faire
				// ...
				manager.setUpData(indRech, data, ind);
				System.out.println("setup de l'index "+indRech+"en position "+ind);
				localIndices[ind] = indRech;
			}
			sequence[i] = ind;
		}
	}
	
	private int getIndex(int[] tab, int val) {
		for(int i=0; i<tab.length;i++) if(tab[i]==val) return i;
		return -1;
	}
	
	
	private int getIndicesIndex(int cacheIndex) {
		// on recherche la valeur cacheIndex dans indices[]
		// si on trouve on retourne l'index où il a été trouvé
		// sinon on retourne un index de indices[] où la valeur est -1;
		for(int i=0; i<localIndices.length;i++) if(localIndices[i]==cacheIndex) return i;
		return cacheIndex==-1 ? /*unused non trouvé, cas anormal on sort*/ -1 : getIndicesIndex(-1);
	}
	
	public T[] getAllData() {
		return data;
	}
	/**
	 * retourne la cache (la bande ...) en position index / manager
	 * ex getData(3812) puis getData(3813) ...
	 *  retournera les bandes correspondantes stockées en data[0], data[4], ...
	 *  
	 *  index : index en base manager
	 */
	public T getData(int index) {
		// FAUX !
		//return data[indices[sequence[index]]];
		for(int i=0; i<localIndices.length;i++) if(localIndices[i]==index) return data[i];
		return null;
	}
	
	/**
	 * Version plus rapide en index local
	 * ex : si on sait que l'on veut les n premiere bandes
	 *      on utilise un local index de 0 à n
	 * @param localIndex
	 * @return
	 */
	public T getDataLocalIndex(int localIndex) {
		return data[sequence[localIndex]];
	}
	
	public int getIndexFromLocalIndex(int localIndex) {
		return localIndices[sequence[localIndex]];
	}
	
	public int getLocalIndexFromIndex(int index) {
//		int dataIndex=-1;
//		for(int i=0; i<localIndices.length;i++) if(localIndices[i]==index) {dataIndex = i; break;}
//		if(dataIndex == -1) return dataIndex;
//		for(int i=0; i<sequence.length; i++) if(sequence[i]==dataIndex) return i;
//		return -1;
		
		// plus simple ?
		 for(int i=0; i<sequence.length; i++) if(localIndices[sequence[i]]==index) return i;
		 return -1;
	}
	
}
