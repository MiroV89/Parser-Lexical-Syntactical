package sintactico;

public class Subname extends Element{
	private String subname="";

	public Subname(String subname) {
		super();
		this.name = subname;
	}

	public String getSubname() {
		return subname;
	}

	public void setSubname(String subname) {
		this.subname = subname;
	}
}
