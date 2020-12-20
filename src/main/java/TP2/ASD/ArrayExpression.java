package TP2.ASD;

import java.util.ArrayList;
import java.util.List;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;
import TP2.Utils;

public class ArrayExpression extends Expression {


	private String name;


	private Expression e;

	/**
	 * Constructeur
	 * @param name Nom de la variable dans l'expression
	 * @param index Position de l'élément dans le tableau
	 */
	public ArrayExpression(String name, Expression  e) {
		this.name = name;
		this.e = e;
	}

	@Override
	public String pp() {
		
		return "" + name + "[" + e.pp() + "]";
	}

	@Override
	public RetExpression toIR(SymbolTable ts) throws TypeException {
		SymbolTable.VariableSymbol var = (SymbolTable.VariableSymbol) ts.lookup(name);
	

		
		if(var == null) {//Message d'erreur si vrai
			throw new TypeException(var + "n'est pas présent dans la table de symbole");
		}
		
		RetExpression indexExp = e.toIR(ts);
		
		
		Pair p = new Pair(indexExp.result,indexExp.type.toLlvmType());
		
		String tmp = Utils.newtmp();
		String result = Utils.newtmp();

		
		RetExpression ret = new RetExpression(new Llvm.IR(Llvm.empty(), Llvm.empty()), var.getType(), result);

		Llvm.Instruction getElementPtr = new Llvm.GetElementPtr(tmp, ret.type.toLlvmType(), "%" + name, p);
		Llvm.Instruction load = new Llvm.load(result, ret.type.toLlvmType(), tmp, ret.type.toLlvmType());
		
		
		ret.ir.append(indexExp.ir);
		ret.ir.appendCode(getElementPtr);
		ret.ir.appendCode(load);

		return ret;
	}


}