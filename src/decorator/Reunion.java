package decorator;

import network_data.Pattern;

/*
 * Reunion decorator
 */
public class Reunion extends Binary {
	
	public Reunion(Decorator d1, Decorator d2) {super(d1, d2);}

	@Override
	public Pattern execute(Pattern f) {
		/* TODO: implement evaluation */
		
		/* evaluate decorators first */
		/* implement reunion of f1 and f2 and return it */

		Pattern p1 = d1.execute(f);
		Pattern p2 = d2.execute(f);
		
		return p1.reunion(p2);
	};
	
}