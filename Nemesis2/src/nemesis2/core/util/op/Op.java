package nemesis2.core.util.op;

import com.jogamp.opengl.math.FloatUtil;

public class Op {
	public static void normalize(float v[]) {    
		float d = FloatUtil.sqrt(v[0]*v[0]+v[1]*v[1]+v[2]*v[2]); 
		if (d == 0.0) {
			//error("zero length vector");    
			return;
		}
		v[0] /= d; v[1] /= d; v[2] /= d; 
	}

	public static void normcrossprod(float v1[], float v2[], float out[])  { 
		//int i, j; 
		//float length;

		out[0] = v1[1]*v2[2] - v1[2]*v2[1]; 
		out[1] = v1[2]*v2[0] - v1[0]*v2[2]; 
		out[2] = v1[0]*v2[1] - v1[1]*v2[0]; 
		normalize(out); 
	}

}
