package sintactico;

public class SemanticProduction extends Element{
	private static int nameint=0;
	private String content="";
	
	public SemanticProduction(String content) {
		super();
		nameint++; setName("SEM-"+content.trim().substring(0, 5).trim()+nameint);
		this.content = content;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
