package sintactico;

import java.util.ArrayList;

public class Precedence {
	public boolean left=false;
	public boolean right=false;
	private int level=0;
	private ArrayList<Token> tokens = new ArrayList<Token>();
	public Precedence(int level, String direction, Token token) {
		this.level=level;
		if(direction.toLowerCase().equals("left")) left=true;
		else if(direction.toLowerCase().equals("right")) right=true;
		else {System.out.println("No existe dicha precedencia");}
		addTokens(token);
	}
	
	public Precedence(int level, String direction, ArrayList<Token> tokens) {
		this.level=level;
		if(direction.toLowerCase().equals("left")) left=true;
		else if(direction.toLowerCase().equals("right")) right=true;
		else {System.out.println("No existe dicha precedencia");}
		this.tokens=tokens;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public ArrayList<Token> getTokens() {
		return tokens;
	}
	public void setTokens(ArrayList<Token> tokens) {
		this.tokens = tokens;
	}
	public void addTokens(Token token) {
		tokens.add(token);
	}
	
}
