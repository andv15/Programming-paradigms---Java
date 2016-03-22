package network_data;

import java.util.ArrayList;
import java.util.List;

import network_data.header.Dst;
import network_data.header.Header;
import network_data.header.Src;
import network_data.header.Port;
import network_data.value.Any;
import network_data.value.Null;
import network_data.value.Value;
/**
 * 
 * @author Sonea Andreea 323CB Tema1
 *
 */
public class Pattern {
	private List<PatternSet> setsPattern = new ArrayList<PatternSet>();

	/* Build the pattern using the bindings in the list allBindings.
	 * The bindings in the list are ordered in the following form:
	 * src_bind1, dst_bind1, port_bind1, src_bind2, dst_bind2, port_bind2, ...
	 */
	public Pattern(List<Binding> allBindings) {

		for (int i = 0; i < allBindings.size(); i+=3) {
			PatternSet p = new PatternSet(allBindings.get(i), 
					allBindings.get(i+1), allBindings.get(i+2));
			if (!setsPattern.contains(p))
				setsPattern.add(p);
		}
	}	
	
	public boolean is_empty() {
		return setsPattern.isEmpty();
	}
	
	/* pattern which contains bindings with each value of header
	 * set to 'Any' : [Src:Any, Dst:Any, Port:Any]
	 */
	public static Pattern mostGeneralPattern() {	

		List<Binding> mostGeneralPattern = new ArrayList<Binding>();
		
		mostGeneralPattern.add(new Binding(Src.getInstance(), Any.getInstance()));
		mostGeneralPattern.add(new Binding(Dst.getInstance(), Any.getInstance()));
		mostGeneralPattern.add(new Binding(Port.getInstance(), Any.getInstance()));
		
		return new Pattern(mostGeneralPattern);
	}
	
	/* pattern which contains bindings with each value of header
	 * set to 'Any' : [Src:Null, Dst:Null, Port:Null]
	 */
	public static Pattern nullPattern() {
		List<Binding> nullPattern = new ArrayList<Binding>();
		
		nullPattern.add (new Binding(Src.getInstance(), Null.getInstance()));
		nullPattern.add (new Binding(Dst.getInstance(), Null.getInstance()));
		nullPattern.add (new Binding(Port.getInstance(), Null.getInstance()));
		
		return new Pattern(nullPattern);
		
	}
	/* intersection of two patterns
	 * Using deepClone() ! 
	 */
	public Pattern intersect(Pattern q) {
		
		if (q.is_empty() || this.is_empty()){
			return Pattern.nullPattern();
		}
		
		List<Binding> intersection = new ArrayList<>();
		Pattern thisPattern = this.deepClone();		//clonam obiectul
		
		for (PatternSet setP : thisPattern.getSetsPattern()) {
			for (PatternSet setQ : q.getSetsPattern()) {
				//se afla intersectia fiecarul set
				//daca intersectia exista atunci este adaugat la lista noua
				PatternSet intersectSet = setP.intersect(setQ);
				if (intersectSet != null) {
					intersection.add(intersectSet.getSrc());
					intersection.add(intersectSet.getDst());
					intersection.add(intersectSet.getPort());
				}
			}
		}
		return new Pattern(intersection);
	}
	
	/**
	 * @param q	- the second pattern of reunion
	 * @return reunion of 'this' and q pattern
	 */
	public Pattern reunion(Pattern q) {

		if (q.is_empty() && this.is_empty()){
			return Pattern.nullPattern();
		}

		List<PatternSet> list = new ArrayList<PatternSet>();
		for (PatternSet setP : this.getSetsPattern()) {
				list.add(setP);		//sunt adaugate seturile din patternul 'this'
		}
		for (PatternSet setQ : q.getSetsPattern()) {
		//sunt adaugate seturile din patternul q daca aceastea nu au fost deja adaugate
			if (!list.contains(setQ)) {
				list.add(setQ);
			}
		}
		Pattern newPattern = new Pattern(new ArrayList<Binding>());
		newPattern.setsPattern = list;
		return newPattern;
	}

	public boolean subset(Pattern q) {
		/*
		 * check if 'this' is subset of pattern q
		 */
		if ( q == null || this == null || q.is_empty() || this.is_empty()) {
			return false;
		}

		for (PatternSet setThis : this.getSetsPattern()) {
			boolean contains = false;
			//este verificat daca pentru fiecare set din 'this' exista un set 
			//'mai permisiv' in q, adica unul egal sau mai general
			for (PatternSet setQ : q.getSetsPattern()) {
				if (setQ.isMorePermisive(setThis)) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				return false;
			}
		}
		return true;	
	}
	
	public Pattern rewrite(Header h, Value v){
		
		/* rewrite value of header h in 'this' pattern
		 * use deepClone() ! 
		 */
		if (this.is_empty()) {
			return Pattern.nullPattern();
		}
		
		Pattern thisPattern = this.deepClone();
		//se verifica ce al cui header trebuie suprascrisa valoarea
		for (PatternSet thisSet : thisPattern.getSetsPattern()) {
			if (h instanceof Src) {
				thisSet.getSrc().setValue(v);
			}
			if (h instanceof Dst) {
				thisSet.getDst().setValue(v);
			}
			if (h instanceof Port) {
				thisSet.getPort().setValue(v);
			}
		}
		return thisPattern;
	}
	
	@Override
	public int hashCode() {
		final int code = 51;
		return code + ((setsPattern.isEmpty())? 0 : code * setsPattern.size());
	}

	@Override
	public boolean equals(Object obj) {
		/* two patterns are equal if each of them is a subset of the other */
		return (this.subset((Pattern)obj) && ((Pattern)obj).subset(this));
	}
	
	/* The deepClone is needed because tests and code in general
	 * considers that when doing o1.intersect(o2), a new object
	 * is created, and that o1 is not affected.
	 */
	public Pattern deepClone() {
		List<Binding> bindings = new ArrayList<>();
		for (PatternSet set : setsPattern) {
			bindings.add(new Binding(set.getSrc().getHeader(), set.getSrc().getValue()));
			bindings.add(new Binding(set.getDst().getHeader(), set.getDst().getValue()));
			bindings.add(new Binding(set.getPort().getHeader(), set.getPort().getValue()));
		}
		return new Pattern(bindings);	
	}
		
	public List<PatternSet> getSetsPattern() {
		return setsPattern;
	}
	
}
	