package tests;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import network_data.Binding;
import network_data.Pattern;
import network_data.header.Dst;
import network_data.header.Port;
import network_data.header.Src;
import network_data.value.Any;
import network_data.value.StringAtom;

/* THE CONTENTS OF THIS FILE MUST NOT BE CHANGED */

public class TestPattern extends TestCase {

	static private List<Binding> cl1, cl2, cl3, clp;
	static private Pattern p1, p2, p3, pp1, pp2, pp3, pp4, pp;
	
	@Override	
	protected void setUp() {
		
		cl1 = new ArrayList<Binding>() {{ 
			add(new Binding(Src.getInstance(), new StringAtom("a")));
			add(new Binding(Dst.getInstance(), Any.getInstance()));
			add(new Binding(Port.getInstance(), Any.getInstance()));
			}};
		
		cl2 = new ArrayList<Binding>() {{ 
			add(new Binding(Src.getInstance(), new StringAtom("b")));
			add(new Binding(Dst.getInstance(), Any.getInstance()));
			add(new Binding(Port.getInstance(), Any.getInstance()));
			}};
			
		cl3 = new ArrayList<Binding>() {{ 
			add(new Binding(Src.getInstance(), Any.getInstance()));
			add(new Binding(Dst.getInstance(), new StringAtom("c")));
			add(new Binding(Port.getInstance(), Any.getInstance()));
			}};
			
		clp = new ArrayList<Binding>() {{ 
			add(new Binding(Src.getInstance(), Any.getInstance()));
			add(new Binding(Dst.getInstance(), Any.getInstance()));
			add(new Binding(Port.getInstance(), new StringAtom("x")));
			}};
								

		pp = new Pattern(clp);
		p1 = new Pattern(cl1);
		p2 = new Pattern(cl2);
		p3 = new Pattern(cl3);
		
		List<Binding> temp = new ArrayList<>();
		temp.addAll(cl1);
		temp.addAll(cl2);
		pp2 = new Pattern(temp);
		temp.clear();
		temp.addAll(cl3);
		temp.addAll(cl1);
		pp3 = new Pattern(temp);
		temp.clear();
		temp.addAll(cl1);
		temp.addAll(cl2);
		temp.addAll(cl3);
		pp4 = new Pattern(temp);	
		
	}
	
	/* --- Test basic equalities between patterns. --- */

	public void testDeepClone() {
		Pattern nou = p1.deepClone();
		assertEquals(p1, nou);
		nou = nou.rewrite(Src.getInstance(), new StringAtom("b"));
		assertEquals(p2, nou);
		assertNotSame(p1, nou);
	}
	
	public void testEquals0() {
		assertEquals(Pattern.mostGeneralPattern(), Pattern.mostGeneralPattern());
		assertEquals(p1, p1);
	}
	public void testEquals1() {
		assertNotSame(p1, pp2);
	}
	public void testEquals2() {
		assertNotSame(p1, pp3);
	}
	public void testEquals3() {
		assertNotSame(p1, pp4);
	}
	public void testEquals4() {
		assertNotSame(p1, Pattern.mostGeneralPattern());
	}
	public void testEquals5() {
		assertNotSame(pp3, pp2);
	}
	public void testEquals6() {
		assertNotSame(pp2, pp4);
		assertNotSame(pp3, pp4);
	}
	
	/* --- Test rewrite patterns --- */
	
	public void testRewrite0() {
		assertEquals(pp2.rewrite(Src.getInstance(), new StringAtom("a")), p1);
	}
	public void testRewrite1() {
		assertEquals(Pattern.mostGeneralPattern(), pp2.rewrite(Src.getInstance(), Any.getInstance()));
	}
	public void testRewrite2() {
		assertEquals(pp4.rewrite(Src.getInstance(), new StringAtom("a")), pp3.rewrite(Src.getInstance(), new StringAtom("a")));
	}
	
	public void testSetRemoveDuplicates() {
		Pattern p = new Pattern(new ArrayList<Binding>()
				{{ addAll(cl2); addAll(cl1); addAll(cl1); addAll(cl2); }});
		assertEquals(p, pp2);
	}

	/* --- Test subset patterns --- */ 

	public void testSubsetP0() {
		assertTrue(p1.subset(pp2));
	}
	public void testSubsetP1() {
		assertTrue(p1.subset(pp3));
	}
	public void testSubsetP2() {
		assertTrue(p1.subset(Pattern.mostGeneralPattern()));
	}
	public void testSubsetP3() {
		assertTrue(p1.subset(p1));
	}
	public void testSubsetP4() {
		assertFalse(p1.subset(p2));
	}
	public void testSubsetP5() {
		assertTrue(pp2.subset(pp4));
	}
	public void testSubsetP6() {
		assertTrue(pp3.subset(pp4));
	}
	public void testSubsetP7() {
		assertTrue(p3.subset(pp3));
	}
	public void testSubsetP8() {
		assertTrue(pp3.subset(Pattern.mostGeneralPattern()));
	}
	public void testSubsetP9() {
		assertFalse(pp2.subset(p1));
	}
	public void testSubsetP10() {
		assertFalse(pp3.subset(p1));
	}
	public void testSubsetP11() {
		assertFalse(Pattern.mostGeneralPattern().subset(pp3));
	}

	/* --- Test intersect patterns --- */
	public void testIntersectP0() {
		Pattern expected = p1;
		Pattern actual = p1.intersect(pp2);
		assertEquals(expected, actual);
	}

	public void testIntersectP1() {
		assertTrue(p1.subset(pp3.intersect(pp2)));
	}
	public void testIntersectP2() {
		assertTrue(p2.subset(pp4.intersect(pp2)));
	}
	public void testIntersectP3() {
		assertTrue(p3.subset(pp4.intersect(pp3)));
	}
	public void testIntersectP4() {
		assertFalse(p2.subset(pp3.intersect(pp2)));
	}
	public void testIntersectP5() {
		assertFalse(p3.subset(pp3.intersect(pp2)));
	}
	public void testIntersectP6() {
		assertFalse(pp.subset(pp2));
	}
	public void testIntersectP7() {
		Pattern expected = new Pattern(new ArrayList<Binding>());
		Pattern actual = pp.intersect(pp2);
		assertNotSame(expected, actual);
	}
	public void testIntersectP8() {
		Pattern expected = p2;
		Pattern actual = pp3.intersect(pp2);
		assertNotSame(expected, actual);
	}
	public void testIntersectP9() {
		Pattern expected = p3;
		Pattern actual = pp3.intersect(pp2);
		assertNotSame(expected, actual);
	}
	public void testIntersectP10() {		
		Pattern expected = pp3;
		Pattern actual = pp3.intersect(Pattern.mostGeneralPattern());
		assertNotSame(expected, actual);
	}
	public void testIntersectP11() {
		assertTrue(pp3.subset(pp3.intersect(Pattern.mostGeneralPattern())));
	}
	public void testIntersectP12() {
		assertFalse(pp3.subset(pp2.intersect(Pattern.mostGeneralPattern())));
	}
	public void testIntersectP13() {
		Pattern mgp = Pattern.mostGeneralPattern();
		assertEquals(mgp, mgp.intersect(mgp));
	}
	
	/* --- Test reunion patterns --- */
	
	public void testReunionP0() {
		Pattern expected = new Pattern(new ArrayList<Binding>()
				{{ addAll(cl1); addAll(cl2); }});
		Pattern actual = p1.reunion(p2);
		assertEquals(expected, actual);
	}
	
	public void testReunionP1() {
		Pattern expected = new Pattern(new ArrayList<Binding>()
				{{ addAll(cl1); addAll(cl3); }});
		Pattern actual = p1.reunion(p3);
		assertEquals(expected, actual);
	}
	
	public void testReunionP2() {
		Pattern expected = new Pattern(new ArrayList<Binding>()
				{{ addAll(clp); addAll(cl3); }});
		Pattern actual = pp.reunion(p3);
		assertEquals(expected, actual);
	}
	
	public void testReunionP3() {
		Pattern expected = new Pattern(new ArrayList<Binding>()
				{{ addAll(cl1); addAll(clp); }});
		Pattern actual = p1.reunion(pp);
		assertEquals(expected, actual);
	}
	
	public void testReunionP4() {
		assertEquals(p1.reunion(p3), p3.reunion(p1));
	}
	
	public void testReunionP5() {
		assertEquals(p3.reunion(pp), pp.reunion(p3));
	}
	
	public void testReunionP6() {
		assertEquals(pp2.reunion(pp3), pp3.reunion(pp2));
	}
	
	public void testReunionP7() {
		Pattern expected = pp4;
		Pattern actual = pp2.reunion(pp3);
		assertEquals(expected, actual);
	}
	
	public void testReunionP8() {
		Pattern vp = new Pattern(new ArrayList<Binding>());
		assertEquals(pp4, pp4.reunion(vp));
	}
	
	public void testReunionP9() {
		Pattern mgp = Pattern.mostGeneralPattern();
		assertEquals(mgp, mgp.reunion(mgp));
	}
	
	public void testReunionP10() {
		Pattern mgp = Pattern.mostGeneralPattern();
		assertEquals(pp4.reunion(mgp), mgp);
	}
	
	public void testReunionP11() {
		Pattern mgp = Pattern.mostGeneralPattern();
		assertEquals(mgp.reunion(pp), mgp);
	}
}