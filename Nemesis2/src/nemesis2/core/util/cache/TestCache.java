package nemesis2.core.util.cache;

public class TestCache implements CacheIndexManager<String> {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Mode normal / range en index manager");
		TestCache t = new TestCache();
		t.testSetRange(0, 1);
		t.testSetRange(0, 2);
		t.testSetRange(0, 3);
		t.testSetRange(0, 4);
		t.testSetRange(0, 5);
		t.testSetRange(1, 5);
		t.testSetRange(0, 5);
		t.testSetRange(2, 5);
		t.testSetRange(3, 5);
		t.testSetRange(1, 5);

		System.out.println();
		System.out.println();
		System.out.println("Mode direct /range en index cache");
		t = new TestCache();
		t.testSetRangeDirect(0, 1);
		t.testSetRangeDirect(0, 2);
		t.testSetRangeDirect(0, 3);
		t.testSetRangeDirect(0, 4);
		t.testSetRangeDirect(0, 5);
		t.testSetRangeDirect(1, 5);
		t.testSetRangeDirect(2, 5);
		t.testSetRangeDirect(3, 5);
		t.testSetRangeDirect(1, 5);
		
		System.out.println();
		System.out.println();
		System.out.println("Mode normal / list en index manager");
		t = new TestCache();
		t.testSetList(0);
		t.testSetList(0, 1);
		t.testSetList(0, 1, 2);
		t.testSetList(0, 1, 2, 3);
		t.testSetList(0, 1, 2, 3, 4);
		t.testSetList(1, 2, 3, 4, 5);
		t.testSetList(2, 3, 4, 5, 6);
		t.testSetList(3, 4, 5, 6, 7);
		t.testSetList(1, 2, 3, 4, 5);
		
		System.out.println();
		System.out.println();
		System.out.println("Conversion d'index");
		t = new TestCache();
		t.testIndexConversion();
	}
	
	
	CacheIndexU<String> cache;
	TestCache() {
		cache = new CacheIndexU<String>(this, new String[6]);
	}
	
	// apelé par le cache pour demande d'initialisation de bande manquante
	public void setUpData(int index, String[] data, int dataSetupIndex) {
		data[dataSetupIndex] = String.format("S%2d",index);
	}
	
	public void testSetRange(int start, int count) {
		System.out.println("*****************STANDARD*********************");
		cache.setRange(start, count);
		for(int i=0;i<count;i++) System.out.print(cache.getData(start+i)+" ");
		System.out.println();
	}

	public void testSetRangeDirect(int start, int count) {
		System.out.println("****************DIRECT*******************");
		cache.setRange(start, count);
		for(int i=0;i<count;i++) System.out.print(cache.getDataLocalIndex(i)+" ");
		System.out.println();
	}
	
	public void testSetList(int... indices) {
		System.out.println("****************List*******************");
		cache.setIndices(indices);
		for(int i=0;i<indices.length;i++) System.out.print(cache.getData(indices[i])+" ");
		System.out.println();
	}
	
	public void testIndexConversion() {
		System.out.println("***************INDEX CONV*******************");
		int[] indices = {12,6,4,3,1};
		cache.setIndices(indices);
		for(int i=0; i<indices.length; i++) System.out.format("%2d, ", cache.getIndexFromLocalIndex(i));
		System.out.println();
		for(int i=0; i<indices.length; i++) System.out.format("%2d, ", cache.getLocalIndexFromIndex(indices[i]));
		System.out.println();
		for(int i=0; i<indices.length; i++) System.out.format("%2d, ", cache.getLocalIndexFromIndex(indices[indices.length-1-i]));
		System.out.println();
		for(int i=0; i<indices.length; i++) {
			System.out.println(indices[i]+"="+cache.getIndexFromLocalIndex(cache.getLocalIndexFromIndex(indices[i]))+" " +
					"/ "+i+"="+cache.getLocalIndexFromIndex(cache.getIndexFromLocalIndex(i)));
		}
	}
}
