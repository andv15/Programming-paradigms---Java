package decorator;

import network_data.Pattern;

/*
 * This decorator evaluates to a constant pattern, received at construction
 */
public class Const implements Decorator {
	Pattern f;
	public Const (Pattern f){
		/* TODO: implement constructor */
		this.f = f;
	}
	@Override
	public Pattern execute(Pattern f) {
		/* TODO: implement evaluation */
		
		return this.f;
	}
}