package nemesis2.core.util.bounds;

public interface Bounding {

	/** la distance de l'objet (à priori -z) */
	public float getZDistance();
	public boolean isVisible();
	public Bounds getBoundingBox();
}
