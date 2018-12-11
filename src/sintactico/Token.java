package sintactico;

public class Token extends Element{
	String value="";
	public Token(String name) {
		super();
		this.setName(name);
	}
	public Token(String value,String name) {
		super();
		this.setName(name);
		this.value=value;
	}
	public String getValue() {
		return value;
	}
	
}
