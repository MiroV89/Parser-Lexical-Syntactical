package tests;

import sintactico.NonTerminal;

public class Nodo {
	private NTTree nttree;
	private String name;
	public Nodo(NTTree nttree, String name) {
		super();
		this.nttree = nttree;
		this.name = name;
	}
	
	public boolean reduced() {
		return ((NonTerminal)nttree.getRoot()).reduced();
	}
	
	public NTTree getNttree() {
		return nttree;
	}
	public void setNttree(NTTree nttree) {
		this.nttree = nttree;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
}
