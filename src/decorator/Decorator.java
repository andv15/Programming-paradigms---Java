package decorator;
import network_data.Pattern;

/* THE CONTENTS OF THIS FILE SHOULD NOT BE CHANGED */

/*
 * Interface which models the operations a device 
 * can make on the patterns that go through it
 */
public interface Decorator {
	public Pattern execute (Pattern f);
}
