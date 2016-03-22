package tests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import network_data.Binding;
import network_data.Pattern;
import network_data.header.Dst;
import network_data.header.Port;
import network_data.header.Src;
import network_data.value.Any;
import network_data.value.StringAtom;
import reachability.Reachability;

/* THE CONTENTS OF THIS FILE MUST NOT BE CHANGED */

public class TestReachability extends TestCase{
	
	public void testReachabilityLink() {
		String net = "_/_/a!_/_/b;_/_/b!_/_/c>1;_";

		/* Compute the reachable patterns. */
		Reachability r = new Reachability(net, "a", "c");
		assertEquals(1, r.getReachable().size());

		Set<Pattern> actual = r.getReachable();

		/* Result: [Pattern [(Port,c)AND(Src,Any)AND(Dst,Any)]]. */
		final Pattern p = Pattern.mostGeneralPattern()
			.rewrite(Port.getInstance(), new StringAtom("c"));
		Set<Pattern> expected = new HashSet<Pattern>() {{ add(p); }};

		assertEquals(expected, actual);
	}

	public void testReachabilityDiamond() {
		String net = "_/_/a!_/_/b,d;_/_/b!_/_/c;_/_/d!_/_/c>1,2;_";

		/* Compute the reachable patterns */
		Reachability r = new Reachability(net, "a", "c");
		assertEquals(1, r.getReachable().size());

		Set<Pattern> actual = r.getReachable();

		/* Result: [Pattern [(Port,c)AND(Src,Any)AND(Dst,Any)]]. */
		final Pattern p = Pattern.mostGeneralPattern()
			.rewrite(Port.getInstance(), new StringAtom("c"));
		Set<Pattern> expected = new HashSet<Pattern>() {{ add(p); }};

		assertEquals(expected, actual);
	}

	public void testReachabilityLoop() {
		String net = "_/_/a!_/_/b;_/_/b!_/_/a>1;0";

		/* Compute the reachable patterns */
		Reachability r = new Reachability(net, "a", "a");
		assertEquals(1, r.getReachable().size());

		Set<Pattern> actual = r.getReachable();

		/* Result: [Pattern [(Port,a)AND(Src,Any)AND(Dst,Any)]]. */
		final Pattern p = Pattern.mostGeneralPattern()
			.rewrite(Port.getInstance(), new StringAtom("a"));
		Set<Pattern> expected = new HashSet<Pattern>() {{ add(p); }};

		assertEquals(expected, actual);
	}


	public void testFilter() {
		String net = "p/_/a!_/_/b>_";

		Reachability r = new Reachability(net, "a", "b");
		Set<Pattern> actual = r.getReachable();

		/* Result: [ Pattern [(Dst,Any)AND(Port,b)AND(Src,p)] ]. */
		final Pattern p = Pattern.mostGeneralPattern()
			.rewrite(Port.getInstance(), new StringAtom("b"))
			.rewrite(Src.getInstance(), new StringAtom("p"));
		Set<Pattern> expected = new HashSet<Pattern>() {{ add(p); }};

		assertEquals(expected, actual);
	}

	public void testOneRuleFilter() {
		String net = "p,q/_/a!_/_/b>_";
		Reachability r = new Reachability(net, "a", "b");
		Set<Pattern> actual = r.getReachable();

		/* Result: [Pattern [(Src,q)AND(Dst,Any)AND(Port,b) OR (Src,p)AND(Dst,Any)AND(Port,b)] ].
		 */
		final List<Binding> b1 = new ArrayList<Binding>() {{
			add(new Binding(Src.getInstance(), new StringAtom("q")));
			add(new Binding(Dst.getInstance(), Any.getInstance()));
			add(new Binding(Port.getInstance(), new StringAtom("b")));
			}};

		final List<Binding> b2 = new ArrayList<Binding>() {{
			add(new Binding(Src.getInstance(), new StringAtom("p")));
			add(new Binding(Dst.getInstance(), Any.getInstance()));
			add(new Binding(Port.getInstance(), new StringAtom("b")));
			}};

		final List<Binding> bgs = new ArrayList<Binding>()
				{{ addAll(b1); addAll(b2); }};
		Set<Pattern> expected = new HashSet<Pattern>() {{ add(new Pattern(bgs)); }};

		assertEquals(expected, actual);
	}


	public void testcascadeFilter1() {
		String net = "p,q/_/a!_/_/b;q/_/b!_/_/c>1;_";

		Reachability r = new Reachability(net, "a", "c");
		Set<Pattern> actual = r.getReachable();

		/* Result: [Pattern [(Dst,Any)AND(Port,c)AND(Src,q)] ].
		 */
		final Pattern p = Pattern.mostGeneralPattern()
			.rewrite(Port.getInstance(), new StringAtom("c"))
			.rewrite(Src.getInstance(), new StringAtom("q"));
		Set<Pattern> expected = new HashSet<Pattern>() {{ add(p); }};

		assertEquals(expected, actual);
	}

	public void testcascadeFilter2() {
		String net = "p,q/_/a!_/_/b;s/_/b!_/_/c>1;_";

		Reachability r = new Reachability(net, "a", "c");
		Set<Pattern> actual = r.getReachable();

		/* Result: [].
		 */
		assertEquals(0, actual.size());
	}

	public void testRewriteTest() {
		String net = "_/_/a!_/_/b;_/_/b!192.0.0.1/_/c>1;_";

		Reachability r = new Reachability(net, "a", "c");
		Set<Pattern> actual = r.getReachable();

		/* Result: [Pattern [(Dst,Any)AND(Port,c)AND(Src,192.0.0.1)] ].
		 */
		final Pattern p = Pattern.mostGeneralPattern()
			.rewrite(Port.getInstance(), new StringAtom("c"))
			.rewrite(Src.getInstance(), new StringAtom("192.0.0.1"));
		Set<Pattern> expected = new HashSet<Pattern>() {{ add(p); }};

		assertEquals(expected, actual);
	}

	public void testFinal() {
		String net = 	"1,3/_/F:0,F:2!_/_/F:1;"	+
						"_/_/P:0!_/2/P:1,P:2;"		+
						"_/_/R:0!3/3/R:1;"			+
						"_/_/F:1!_/_/P:0;"			+
						"_/_/P:2!_/_/R:0;"			+
						"_/_/R:1!_/_/F:2;"			+
						"_/_/R:0!_/_/P:2;"			+
						"_/_/F:2!_/_/R:1;"			+
						"_/_/P:0!_/_/F:1>"			+		
						"3,7;"	+	
						"4,8;"	+
						"5,6;"	+
						"1;2;0;1;2;0";
		Reachability r = new Reachability(net, "F:0", "P:1");
		Set<Pattern> actual = r.getReachable();

		/* Result:
		 * [ Pattern [(Dst,2)AND(Src,1)AND(Port,P:1) OR (Dst,2)AND(Src,3)AND(Port,P:1)],
		 *   Pattern [(Dst,2)AND(Src,3)AND(Port,P:1)]
		 * ]
		 */
		
		final Pattern p1 = Pattern.mostGeneralPattern()
			.rewrite(Port.getInstance(), new StringAtom("P:1"))
			.rewrite(Src.getInstance(), new StringAtom("3"))
			.rewrite(Dst.getInstance(), new StringAtom("2"));
		
		final List<Binding> b1 = new ArrayList<Binding>() {{ 
			add(new Binding(Src.getInstance(), new StringAtom("1")));
			add(new Binding(Dst.getInstance(), new StringAtom("2")));
			add(new Binding(Port.getInstance(), new StringAtom("P:1")));
			}};
			
		final List<Binding> b2 = new ArrayList<Binding>() {{ 
			add(new Binding(Src.getInstance(), new StringAtom("3")));
			add(new Binding(Dst.getInstance(), new StringAtom("2")));
			add(new Binding(Port.getInstance(), new StringAtom("P:1")));
			}};
			
		List<Binding> bgs = new ArrayList<Binding>() {{ addAll(b1); addAll(b2); }};
		final Pattern p2 = new Pattern(bgs);

		Set<Pattern> expected = new HashSet<Pattern>() {{ add(p1); add(p2); }};

		assertEquals(expected, actual);
	}
}
