package nemesis2.core.util.obj;
// XXX textures
public class ObjectDef {

	private String name;
	private float[] vertices;
	private float[] normals;
	private float[] textCoords;
	private short[] indices;
	
	private float[] tmpVertices;
	private float[] tmpNormals;
	private float[] tmpTextCoords;
	
	private double compareTolerance = 0.001;
	private boolean useNormals = true;
	private boolean useIndices;
	
	public ObjectDef(String name) {
		vertices = new float[0];
		normals=new float[0];
		textCoords = new float[0];
		indices=new short[0];
		setName(name);
	}

	public double getCompareTolerance() {
		return compareTolerance;
	}

	public void setCompareTolerance(double compareTolerance) {
		this.compareTolerance = compareTolerance;
	}

	public void setUseNormals(boolean b) {
		useNormals = b;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float[] getVertices() {
		return vertices;
	}

//	public void setVertices(float[] vertices) {
//		this.vertices = vertices;
//	}

	public float[] getNormals() {
		return normals;
	}

//	public void setNormals(float[] normals) {
//		this.normals = normals;
//	}

	public short[] getIndices() {
		return indices;
	}

//	public void setIndices(short[] indices) {
//		this.indices = indices;
//	}
	
	public void addVertex(float x, float y, float z) {
		vertices = addArray(vertices, x, y, z);
	}
	
	public void addNormal(float x, float y, float z) {
		if(useNormals)
			normals = addArray(normals, x, y, z);
	}
	
	public void addTextCoords(float x, float y) {
		textCoords = addArray(textCoords, x, y);
	}

	private float[] addArray(float[] tab, float ...values) {
		float[] tmp = new float[tab.length+values.length];
		if(tab.length>0) {
			System.arraycopy(tab, 0, tmp, 0, tab.length);
		}
		System.arraycopy(values, 0, tmp, tab.length, values.length);
//		if(tab==vertices) vertices=tmp;
//		else if(tab==normals) normals=tmp;
//		else if(tab==textCoords) textCoords = tmp;
		return tmp;
	}
	
//	private void addIndice(int first, int second, int third) {
//		addArray(indices, (short) first, (short) second, (short) third);
//	}
	
	private short[] addArray(short[] tab, short ...values) {
		short[] tmp = new short[tab.length+values.length];
		if(tab.length>0) {
			System.arraycopy(tab, 0, tmp, 0, tab.length);
		}
		System.arraycopy(values, 0, tmp, tab.length, values.length);
		//if(tab==indices) indices=tmp;
		return tmp;
	}
	
	public void addFace(int[] verti, int[] texti, int[] normi) {
		if(tmpVertices==null) {
			// on commence à définir des faces
			// on mets tout en tmp
			// et on va construire les tableaux
			
			tmpVertices = vertices;
			tmpNormals = normals;
			tmpTextCoords = textCoords;
			vertices = new float[0];
			normals=new float[0];
			textCoords = new float[0];

		}
		
		// recherche de coordonnées déjà existantes
		for(int i=0; i<3; i++) {
			short indice = findMatch(verti[i], texti[i], normi[i]);
			if(indice<0) {
				// création de l'indice
				indice = shortcreateIndice(verti[i], texti[i], normi[i]);
			} 
			// ajout de l'indices
			indices = addArray(indices,indice);
		}
	}

	private short shortcreateIndice(int v, int t, int n) {
		boolean useTex = (t!=-1);//tex[0]!=-1 && tex[1]!=-1;
		boolean useNormal = tmpNormals.length>0;
		float[] vert= {tmpVertices[v*3], tmpVertices[v*3+1], tmpVertices[v*3+2]};
		float[] tex= (useTex ? new float[] {tmpTextCoords[t*2], tmpTextCoords[t*2+1]} : new float[] {});
		float[] norm= useNormal ? new float[] {tmpNormals[n*3], tmpNormals[n*3+1], tmpNormals[n*3+2]} : new float[] {};
        
		vertices = addArray(vertices, vert);
		if(useTex) textCoords = addArray(textCoords, tex);
		if(useNormal) normals = addArray(normals, norm);
		return (short) ((vertices.length-vert.length)/3);
	}

	private short findMatch(int v, int t, int n) {
		//System.out.format("find match v %3d t %3d n %3d\n", v,t,n);
		// recherche des valeurs dans les tmp
		boolean useTex = (t!=-1);//(t!=-1);//tex[0]!=-1 && tex[1]!=-1;
		boolean useNormal = tmpNormals.length>0;
		float[] vert= {tmpVertices[v*3], tmpVertices[v*3+1], tmpVertices[v*3+2]};
		float[] tex= (useTex ? new float[] {tmpTextCoords[t*2], tmpTextCoords[t*2+1]} : new float[] {});
		float[] norm= useNormal ? new float[] {tmpNormals[n*3], tmpNormals[n*3+1], tmpNormals[n*3+2]} : new float[] {};
		// recherche d'une correspondance dans les points definitifs
		for(int i=0; i<(vertices.length/3); i++) {
//			if(vertices[3*i]==vert[0] && vertices[3*i+1]==vert[1] && vertices[3*i+2]==vert[2]) {
			if(etol(vertices[3*i], vert[0]) && etol(vertices[3*i+1], vert[1]) && etol(vertices[3*i+2], vert[2])) {
//				System.out.format("potential match v %3.2f/%3.2f %3.2f/%3.2f %3.2f/%3.2f %d\n", vertices[3*i], vert[0], vertices[3*i+1], vert[1], vertices[3*i+2], vert[2],i);
				if(!useTex || (textCoords[2*i]==tex[0] && textCoords[2*i+1]==tex[1])) {
//					System.out.println("potential match t "+i);
					if(!useNormal || (etol(normals[3*i],norm[0]) && etol(normals[3*i+1], norm[1]) && etol(normals[3*i+2], norm[2]))) {
//						System.out.println("confirmed match "+i);
						return (short) i;
					}
				}
			}
		}
//		System.out.format("no match v %3.2f %3.2f %3.2f \n", vert[0], vert[1], vert[2]);
		return -1;
	}
	
	private boolean etol(float a, float b) {
		return Math.abs(b-a)<=compareTolerance;
	}

	public void complete() {
		tmpVertices = null;
		tmpNormals = null;
		tmpTextCoords = null;
		
		/*
		System.out.format("Complete v %d   n %d   t %d i %d [%d faces]\n", vertices.length, normals.length, textCoords.length, indices.length, (indices.length/3));
		for(int i=0; i<vertices.length/3; i++) 
			if(vertices.length==normals.length) System.out.format("v %3.2f, %3.2f, %3.2f n %3.2f, %3.2f, %3.2f \n", vertices[3*i], vertices[3*i+1], vertices[3*i+2], normals[3*i], normals[3*i+1], normals[3*i+2]);
			else System.out.format("v %3.4f, %3.4f, %3.4f\n", vertices[3*i], vertices[3*i+1], vertices[3*i+2]);
		
		for(int i=0; i<normals.length/3; i++) 
			System.out.format("n %3.2f, %3.2f, %3.2f\n", normals[3*i], normals[3*i+1], normals[3*i+2]);
		for(int i=0; i<textCoords.length/2; i++) 
			System.out.format("v %3.2f, %3.2f\n", textCoords[2*i], textCoords[2*i+1]);
		for(int i=0; i<indices.length/3; i++) 
			System.out.format("F %3d %3d %3d\n", indices[3*i], indices[3*i+1], indices[3*i+2]);
		*/
	}

	public void setUseIndices(boolean useIndices) {
		this.useIndices = useIndices;
	}
}
