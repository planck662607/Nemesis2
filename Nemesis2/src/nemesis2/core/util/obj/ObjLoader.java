package nemesis2.core.util.obj;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ObjLoader {

    public static final String VERTEX_DATA = "v ";
    public static final String NORMAL_DATA = "vn ";
    public static final String TEXTURE_DATA = "vt ";
    public static final String FACE_DATA = "f ";
    public static final String SMOOTHING_GROUP = "s ";
    public static final String GROUP = "g ";
    public static final String OBJECT = "o ";
    public static final String COMMENT = "#";
    
    private boolean useNormal = true;
    private boolean useIndices = true;
    
	//private ObjectDef currentObject;
    private ObjectDef[] objects;
    
    
    
    
	public boolean isUseNormal() {
		return useNormal;
	}

	public void setUseNormal(boolean useNormal) {
		this.useNormal = useNormal;
	}
	
	public void load(String resource) {
		BufferedReader reader = null;
		try {
			reader=  new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(resource)));
			load(reader);
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			safeClose(reader);
		}
	}
	
	private void load(BufferedReader reader) throws IOException {
		objects = null;
		String line;
		while((line=reader.readLine())!=null) {
			parseLine(line);
		}
		if(objects.length>0) objects[objects.length-1].complete();
	}
	
	private void safeClose(Closeable c) {
		if(c!=null)
			try {
				c.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public void load(File file) {
		//BufferedInputStream bif =null;
		BufferedReader reader = null;
		try {
			//		bif= new BufferedInputStream(new FileInputStream(file));
			reader=  new BufferedReader(new FileReader(file));
			load(reader);
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			safeClose(reader);
		}
	}

	private void parseLine(String line) {
		if(line.startsWith(COMMENT)) {
			System.out.println("Commentaire : "+line);
		} else if(line.startsWith(OBJECT)) {
			System.out.println("Objet : "+line);
		} else if(line.startsWith(GROUP)) {
			System.out.println("Groupe : "+line);
			String s[] = line.split("\\s+");
	        addObject(new ObjectDef(s[1]));
		} else if(line.startsWith(VERTEX_DATA)) {
			String s[] = line.split("\\s+");
			float x = Float.parseFloat(s[1]);
			float y = Float.parseFloat(s[2]);
			float z = Float.parseFloat(s[3]);
			objects[objects.length-1].addVertex(x, y, z);
		} else if(line.startsWith(NORMAL_DATA)) {
			String s[] = line.split("\\s+");
			float x = Float.parseFloat(s[1]);
			float y = Float.parseFloat(s[2]);
			float z = Float.parseFloat(s[3]);
			objects[objects.length-1].addNormal(x, y, z);
		} else if(line.startsWith(TEXTURE_DATA)) {
			String s[] = line.split("\\s+");
			float x = Float.parseFloat(s[1]);
			float y = Float.parseFloat(s[2]);
			objects[objects.length-1].addTextCoords(x, y);
		} else if(line.startsWith(FACE_DATA)) {
			String s[] = line.split("\\s+");
	        if (line.contains("//")) { // Pattern is present if obj has no texture
	            for (int loop=1; loop < s.length; loop++) {
	                s[loop] = s[loop].replaceAll("//","/0/"); //insert -1 for missing vt data
	            }
	        }
	        // XXX 
	        System.out.println("taiter la face");
	        int vdata[] = new int[s.length-1];
	        int vtdata[] = new int[s.length-1];
	        int vndata[] = new int[s.length-1];
	        for(int i=1; i<s.length; i++) {
	        	String[] temp = s[i].split("/");
	        	vdata[i-1] = Integer.parseInt(temp[0])-1;
	        	vtdata[i-1] = Integer.parseInt(temp[1])-1;
	        	vndata[i-1] = Integer.parseInt(temp[2])-1;
	        }
//	        // inversion
//	        swap(vdata, 1,2);
//	        swap(vtdata, 1,2);
//	        swap(vndata, 1,2);
	        objects[objects.length-1].addFace(vdata, vtdata, vndata);
		}
	}

	private void swap(int[] t, int i, int j) {
		int k = t[i];
		t[i] = t[j];
		t[j]=k;
	}
	private void addObject(ObjectDef objectDef) {
		objectDef.setUseNormals(useNormal);
		objectDef.setUseIndices(useIndices);
		if(objects==null) objects=new ObjectDef[] {objectDef};
		else {
			if(objects.length>0) objects[objects.length-1].complete();
			ObjectDef[] tmp = new ObjectDef[objects.length+1];
			System.arraycopy(objects, 0, tmp, 0, objects.length);
			tmp[objects.length] = objectDef;
		}
	}

	public static void main(String[] args) {
		ObjLoader loader = new ObjLoader();
		//loader.setUseNormal(false);
		loader.load(new File("/Users/francois/Pictures/graphic/metalion.obj"));
		loader.scale(0.5f, 0.5f, 0.5f);
		loader.center();
		loader.getBounds();
		loader.center();
		loader.getBounds();
	}
	
	public void scale(float sx, float sy, float sz) {
		System.out.format("scale sx %3.2f sy %3.2f sz %3.2f\n", sx, sy, sz);
		getBounds();
		for(ObjectDef o : objects) {
			float[] tv = o.getVertices();
			for(int i=0;i<tv.length/3; i++) {
				tv[3*i] = tv[3*i]*sx;
				tv[3*i+1] = tv[3*i+1]*sy;
				tv[3*i+2] = tv[3*i+2]*sz;
			}
		}
		getBounds();
	}
	
	public float[] getBounds() {
		float[] bounds={Float.MAX_VALUE,Float.MAX_VALUE,Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE};
		for(ObjectDef o : objects) {
			float[] tv = o.getVertices();
			for(int i=0;i<tv.length/3; i++) {
				bounds[0] = Math.min(bounds[0],tv[3*i]);
				bounds[1] = Math.min(bounds[1],tv[3*i+1]);
				bounds[2] = Math.min(bounds[2],tv[3*i+2]);
				bounds[3] = Math.max(bounds[3],tv[3*i]);
				bounds[4] = Math.max(bounds[4],tv[3*i+1]);
				bounds[5] = Math.max(bounds[5],tv[3*i+2]);
			}
		}
		System.out.format("Bounds min v %3.2f, %3.2f, %3.2f max v %3.2f, %3.2f, %3.2f\n", bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
		return bounds;
	}
	public void center() {
		centerRelativeTo(0, 0, 0);
	}
	
	public void centerRelativeTo(float x, float y, float z) {
		float[] bounds = getBounds();
		float vx = x+(-bounds[0]-bounds[3])/2f;
		float vy = y+(-bounds[1]-bounds[4])/2f;
		float vz = z+(-bounds[2]-bounds[5])/2f;
		for(ObjectDef o : objects) {
			float[] tv = o.getVertices();
			for(int i=0;i<tv.length/3; i++) {
				tv[3*i] += vx;
				tv[3*i+1] += vy;
				tv[3*i+2] += vz;
			}
		}
	}
	
	public ObjectDef[] getObjectsDef() {
		return objects;
	}

	public void translate(float x, float y, float z) {
		for(ObjectDef o : objects) {
			float[] tv = o.getVertices();
			for(int i=0;i<tv.length/3; i++) {
				tv[3*i] = tv[3*i] + x;
				tv[3*i+1] = tv[3*i+1] + y;
				tv[3*i+2] = tv[3*i+2] + z;
			}
		}

	}
}
