package sintactico;

import java.util.ArrayList;

public class Produccion {
	private ArrayList<Element> structure = new ArrayList<Element>();

	public Produccion() {}
	public Produccion(ArrayList<Element> structure) {
		this.structure=structure;
	}
	
	public void add(Element nt) {
		structure.add(nt);
	}
	public ArrayList<Element> getStructure() {
		return structure;
	}
	public void setStructure(ArrayList<Element> structure) {
		this.structure = structure;
	}
	
	public String toString() {	
		String s="";
		for(Element e : structure) {s+=e.getName()+" ";}
		return s;
	}
}
