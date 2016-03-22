package network_data;

import network_data.header.Header;
import network_data.value.Any;
import network_data.value.Null;
import network_data.value.StringAtom;
import network_data.value.Value;

/* Class used to bind a header to a value */
public class Binding {
	private Header header;
	private Value value;
	
	public Binding(Header header, Value value) {
		this.header = header;
		this.value = value;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}
	
	/* TODO if needed, implement hashCode() and equals() methods */
	@Override
	public boolean equals(Object obj) {
		
		if (value instanceof Any && !((((Binding)obj).getValue()) instanceof Any)) {
			return false;
		}
		if (!(value instanceof Any) && (((Binding)obj).getValue() instanceof Any)) {
			return false;
		}
		if (value instanceof StringAtom && (((Binding)obj).getValue()) instanceof StringAtom) {
			return ((StringAtom)value).equals(((Binding)obj).getValue());
		}
		if ((value instanceof Null) && ((((Binding)obj).getValue()) instanceof Null)) {
			return true;
		}
		if ((value instanceof Null) || ((((Binding)obj).getValue()) instanceof Null)) {
			return false;
		}
			
		return true;
	}
	
	@Override
	public String toString() {
		return "[" + this.header + ", " + this.value + "]";
	}

}