package decorator;

import network_data.Pattern;

/*
 * This decorator evaluates to the parameter pattern itself
 */
public class Id implements Decorator {

	@Override
	public Pattern execute(Pattern f) {
		/* TODO: implement evaluation */
		return f;
	}
}