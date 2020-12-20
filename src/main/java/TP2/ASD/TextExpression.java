package TP2.ASD;

import TP2.SymbolTable;
import TP2.TypeException;

public class TextExpression extends Expression{
	private String text;
	
	public TextExpression(String text) {
		this.text = text;
	}
	public String getString() {
		return text;
	}
	@Override
	public String pp() {
		return "\"" + text + "\"";
	}

	@Override
	public RetExpression toIR(SymbolTable ts) throws TypeException {
		// TODO Auto-generated method stub
		
		return null;
	}
	
}
