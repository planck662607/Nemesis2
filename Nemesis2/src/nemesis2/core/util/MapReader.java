package nemesis2.core.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import nemesis2.core.util.bounds.BoundsModifier;

public class MapReader {
    public static final String COMMENT = "//";
    public static final String MAP = "[MAP]";
    public static final String BOUNDS = "[BOUNDS]";
    public final static String DEFAULT = "default";
	private static final BoundsModifier DEFAULT_BM = new BoundsModifier();
    

    private enum WHAT {
    	NOSE, MAP, BOUNDS;
    }
    
    private WHAT what;
    private short[][] theMap;
   // private List<BoundsModifier> boundsModifiers;
    private Object[][] boundsModifiers; // {index , boundsModifier}
    private BoundsModifier defaultBoundsModifier;
	private int currentLine;
    
	public void loadMap(String file) throws IOException {
		BufferedReader r = null;
		try {
			r = new BufferedReader(new FileReader(file));
			loadMap(r);
		} finally {
			if(r!=null) r.close();
		}
	}
	
	public void loadMap(BufferedReader reader) throws IOException {
		String line;
		what = WHAT.NOSE;
		while ((line = reader.readLine()) != null) {
			parseLine(line);
		}
	}

	private void parseLine(String line) {
		if(line.trim().startsWith(COMMENT)) {
		} else {
			line = removeComment(line).trim();
			if(line.isEmpty()) return;
			if(line.startsWith(MAP)) {
				what = WHAT.MAP;
			} else if(line.startsWith(BOUNDS)) {
				what = WHAT.BOUNDS;
			} else {
				switch(what) {
				case MAP : 
					readMap(line);
					break;
				case BOUNDS :
					readBounds(line);
					break;
				default : System.out.println("Erreur de line ["+what+"] "+line);
				}
			}
		}
	}
	
	private void readMap(String line) {
		//String coords = r.readLine();
		if(theMap==null) {
			int x = line.indexOf('x');
			int h = Integer.parseInt(line.substring(0, x));
			int w = Integer.parseInt(line.substring(x+1));

			theMap = new short[h][w];
			System.out.println(h+"x"+w);
			currentLine = 0;
		} else {
			int	x=0;
			for(String c : line.trim().split("\\s+")) {
				//System.out.println("X "+x+" Y "+y+" c='"+c+"'");
				theMap[currentLine][x]=Short.parseShort(c);					
				x++;
			}
			currentLine++;
		}
	}

	private void readBounds(String line) {
		String s[] = line.split("\\s+");
		BoundsModifier bm = new BoundsModifier();
		bm.top = getFloat(s[1]);
		bm.left = getFloat(s[2]);
		bm.bottom = getFloat(s[3]);
		bm.right = getFloat(s[4]);
		if(s[0].equals(DEFAULT)) {
			defaultBoundsModifier = bm;
		} else {
			Object[] ibm = {Integer.parseInt(s[0]), bm};
			if(boundsModifiers==null) {
				boundsModifiers = new Object[][] {ibm};
			} else {
				Object[][] tmp = new Object[boundsModifiers.length+1][];
				System.arraycopy(boundsModifiers, 0, tmp, 0, boundsModifiers.length);
				tmp[boundsModifiers.length] = ibm;
				boundsModifiers = tmp;
			}
		}
	}

	private String removeComment(String line) {
		int i = line.indexOf(COMMENT);
		if(i>=0) line = line.substring(0, i);
		return line.trim();
	}
	
	private float getFloat(String s) {
		if(s.endsWith(",")) s=s.substring(0, s.length()-1);
		return Float.parseFloat(s);
	}

	public short[][] getMap() {
		return theMap;
	}
	
	public BoundsModifier[] getBoundsModifiers() {
		if(defaultBoundsModifier==null && boundsModifiers==null) return new BoundsModifier[0];
		int nbIndices = 1+getMaxIndex(theMap);
		BoundsModifier[] ret = new BoundsModifier[nbIndices];
		for(int index = 0; index<nbIndices; index++) {
			ret[index] = getBoundsModifier(boundsModifiers, index);
		}
		return ret;
	}

	private BoundsModifier getBoundsModifier(Object[][] boundsModifiers, int index) {
		BoundsModifier bm =null;
		if(boundsModifiers!=null)
			for(Object[] o : boundsModifiers) {
				if(((Integer) o[0]).equals(index)) {
					bm = (BoundsModifier) o[1];
					break;
				}
			}
		return bm==null ? defaultBoundsModifier==null ? DEFAULT_BM: defaultBoundsModifier : bm;
	}

	private short getMaxIndex(short[][] theMap) {
		short current =0;
		for(int y=0; y<theMap.length; y++)
			for(int x=0; x<theMap[y].length; x++)
				current = (short) Math.max(current, theMap[y][x]);
		return current;
	}
}
