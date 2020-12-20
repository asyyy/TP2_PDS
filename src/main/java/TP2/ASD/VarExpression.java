package TP2.ASD;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;
import TP2.SymbolTable.FunctionSymbol;
import TP2.SymbolTable.Symbol;
import TP2.SymbolTable.VariableSymbol;
import TP2.Utils;


public class VarExpression extends Expression {

	private String name;


	public VarExpression(String name) {
		this.name = name;
	}

	public String pp() {
		return name;
	}

	public RetExpression toIR(SymbolTable ts) throws TypeException {
		//Vérifier existance dans tableSymbole
		Symbol vara = ts.lookup(name);
		if(vara== null) {
			throw new TypeException("VarExpression -> variable non reconnue dans ts");
		}
		if(vara instanceof FunctionSymbol) {
			throw new TypeException("VarExpression -> le nom de la variable représente le nom d'une fonction");
		}
		VariableSymbol var = (VariableSymbol) vara;
		String tnmpName = Utils.newtmp();
		RetExpression ret = new RetExpression(new Llvm.IR(Llvm.empty(), Llvm.empty()), new Int(), name);
		Llvm.Instruction load = new Llvm.load(tnmpName,var.getType().toLlvmType(),name,var.getType().toLlvmType());
		ret.ir.appendCode(load);
		return ret;
	}
}