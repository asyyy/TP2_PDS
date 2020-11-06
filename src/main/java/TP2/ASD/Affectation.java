package TP2.ASD;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.SymbolTable.FunctionSymbol;
import TP2.SymbolTable.Symbol;
import TP2.SymbolTable.VariableSymbol;
import TP2.TypeException;


public class Affectation extends Instruction {
	private String name;
	private Expression e;
	
	public Affectation(String name, Expression e) {
		this.name = name;
		this.e = e;
	}

	@Override
	public String pp() {
		return name + " := " + e.pp() +"\n";
	}

	@Override
	public RetInstruction toIR(SymbolTable ts) throws TypeException {
		String nameLlvm = "";
		VariableSymbol var = new VariableSymbol(new Int(),"");
		
		//Regarde si présent dans la table de symbole
		Symbol vara = ts.lookup(this.name);
		
		//Regarde si c'est une variable et non une fonction
		if(vara instanceof VariableSymbol) {
			var = (VariableSymbol)vara;
		}else {
			System.err.println(name +" est le nom d'une fonction et non d'une variable");
		}
		
		if(vara == null) {
			System.err.println("Variable non déclarer");
		}
		
		Expression.RetExpression retExp = e.toIR(ts);
		if(!var.getType().equals(retExp.type)) {
			throw new TypeException("Type non similaire :"+var.getType() + " et " + var.getType());
		}
		nameLlvm = "%" + nameLlvm;
		//Créer llvm de instruction vide
		RetInstruction ret = new RetInstruction(new Llvm.IR(Llvm.empty(), Llvm.empty()),name);
		
		//Il faut d'abord commencer par les expressions pour qu'elle soit calculer
		ret.ir.append(retExp.ir);
		
		//Faire le code llvm store du résultat 
		Llvm.Instruction store = new Llvm.Store(var.getType().toLlvmType(), retExp.result, nameLlvm);
		
		ret.ir.appendCode(store);
		return ret;
	}


}
