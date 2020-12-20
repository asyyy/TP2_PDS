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
	public String pp(int nbIndent) {
		return name + " := " + e.pp() +"\n";
	}

	@Override
	public RetInstruction toIR(SymbolTable ts) throws TypeException {
		String nameLlvm = "";
		VariableSymbol var = new VariableSymbol(new Int(),"");
		
		//Regarde si pr�sent dans la table de symbole
		Symbol vara = ts.lookup(this.name);
		
		//Regarde si c'est une variable et non une fonction
		if(vara == null) {
			throw new TypeException("Variable non d�clarer");
		}else {
			if(vara instanceof FunctionSymbol) {
				throw new TypeException(name +" est le nom d'une fonction et non d'une variable");
			}	
			var = (VariableSymbol)vara;
			
			Expression.RetExpression retExp = e.toIR(ts);
			if(!var.getType().equals(retExp.type)) {
				throw new TypeException("Type non similaire :"+var.getType() + " et " + var.getType());
			}
			nameLlvm = "%" + var.getName();
			//Cr�er llvm de instruction vide
			RetInstruction ret = new RetInstruction(new Llvm.IR(Llvm.empty(), Llvm.empty()),name);
			
			//Il faut d'abord commencer par les expressions pour qu'elle soit calculer
			ret.ir.append(retExp.ir);
			
			//Faire le code llvm store du r�sultat 
			Llvm.Instruction store = new Llvm.Store(retExp.type.toLlvmType(), retExp.result,var.getType().toLlvmType(), nameLlvm);
			
			ret.ir.appendCode(store);
			return ret;
		}
		
	}


}
