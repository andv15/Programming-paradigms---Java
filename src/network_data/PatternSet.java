package network_data;

import network_data.header.Dst;
import network_data.header.Port;
import network_data.header.Src;
import network_data.value.Any;
/**
 * 
 * @author Sonea Andreea
 * 					323CB Tema1 PP
 *
 * Clasa este folosita pentru a retine un set dintr-un pattern, continand
 * 3 bindinguri pentru Src, Dst si Port.
 */
public class PatternSet {
	private Binding src;
	private Binding dst;
	private Binding port;
		
	public PatternSet(Binding src, Binding dst, Binding port) {
		super();
		this.src = src;
		this.dst = dst;
		this.port = port;
	}
	
	@Override
	public boolean equals(Object x){
		
		if (!src.equals(((PatternSet)x).getSrc()) || !dst.equals(((PatternSet)x).getDst()) 
				|| !port.equals(((PatternSet)x).getPort()))
			return false;
		return true;
	}
	
	public boolean isMorePermisive(PatternSet x) {
		/*
		 * Verifica daca setul 'this' e mai permisiv decat x.
		 * Intoarce true daca valorile celor 3 headere au aceeasi valoare
		 * cu cele din x sau contin valoarea 'Any'.
		 */
		if (this.equals(x)) {
			return true;
		}
		if (!src.equals(x.getSrc()) && !(src.getValue() instanceof Any)) {
			return false;
		}
		if (!dst.equals(x.getDst()) && !(dst.getValue() instanceof Any)) {
			return false;
		}
		if (!port.equals(x.getPort()) && !(port.getValue() instanceof Any)) {
			return false;
		}
		return true;
	}
	
	
	public PatternSet intersect(PatternSet q) {
		/*
		 *Realizeaza intersectia fiecarei valori din ambele
		 *header-uri. Daca intersectia este egala cu unul dintre seturi, intoarce acel set,
		 *altfel intoarce null.
		 */
		Binding srcQ = q.getSrc();
		Binding dstQ = q.getDst();
		Binding portQ = q.getPort();
		
		Binding srcIntersect = new Binding(Src.getInstance(),this.src.getValue().intersect(srcQ.getValue()));
		Binding dstIntersect = new Binding(Dst.getInstance(), this.dst.getValue().intersect(dstQ.getValue()));
		Binding portIntersect = new Binding(Port.getInstance(), this.port.getValue().intersect(portQ.getValue()));
		
		PatternSet intersect = new PatternSet(srcIntersect, dstIntersect, portIntersect);
		
		if (intersect.equals(this) || intersect.equals(q)) {
			return intersect;
		}
		return null;
	}
	
	public Binding getSrc() {
		return src;
	}
	public void setSrc(Binding src) {
		this.src = src;
	}
	public Binding getDst() {
		return dst;
	}
	public void setDst(Binding dst) {
		this.dst = dst;
	}
	public Binding getPort() {
		return port;
	}
	public void setPort(Binding port) {
		this.port = port;
	}

}
