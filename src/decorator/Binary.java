package decorator;

/* THE CONTENTS OF THIS FILE SHOULD NOT BE CHANGED */

/*
 * Abstract class which models the binary operations between decorators
 */
public abstract class Binary implements Decorator {
	Decorator d1, d2;
	public Binary(Decorator d1, Decorator d2) {
		this.d1 = d1; this.d2 = d2;
	}
}