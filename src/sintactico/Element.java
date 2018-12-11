package sintactico;

public class Element {
	public String name="";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean equals(Object o) {
		if(name.equals(((Element)o).getName())) return true;
		return false;
	}
	public boolean equals(String s) {
		if(name.equals(s)) return true;
		return false;
	}
}
