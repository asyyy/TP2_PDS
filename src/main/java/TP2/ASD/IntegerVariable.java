package TP2.ASD;

import TP2.SymbolTable;

public class IntegerVariable extends Variable {
	String name;
	public IntegerVariable(String s) {
		this.name = s;
	}
	@Override
	public String pp() {
		return name;
	}
	@Override
	public RetVariable toIR(SymbolTable ts) {
		//On regarde si la Variable est déjà présente dans TS et ses parents
		
		return null;
	}
	
}
