package tests;

import java.util.ArrayList;

import sintactico.*;

public class NTTree {
	private Element root=null;
	int i=-1;
	int size=0;
	private boolean token=false;
	private ArrayList<NTTree> children=new ArrayList<NTTree>();
	public NTTree(Element e) {root=e;}
	public NTTree(Element e,boolean token) {root=e;this.token=token;}
	public boolean isToken() {return token;}
	public ArrayList<NTTree>getChildren(){
		return children;
	}
	public void addChild(NTTree ntt) {
		children.add(ntt);
		size++;
	}	
	public void addChild(Produccion p) {
		for(Element e:p.getStructure()) {
			if(e instanceof Token || e instanceof NonTerminal) {
				children.add(new NTTree(e));
				size++;
			}
		}
	}
	public boolean haveChild() {
		return children.size()!=0;
	}
	public int numChild() {
		return size;
	}
	public NTTree getChild() {
		if(i<size-1) {
			return children.get(i++);
		}
		return null;
	}
	public void setFather(NTTree father) {
		father.addChild(this);
	}
	public Element getRoot() {
		return root;
	}
	public String toString() {
		return root.getName()+" {"+children.toString()+"}";
	}
}
