package TP2.ASD;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;
import TP2.Utils;
import TP2.Llvm.IR;
import TP2.SymbolTable.FunctionSymbol;
import TP2.SymbolTable.Symbol;
import TP2.SymbolTable.VariableSymbol;


public class IntVariable extends Variable {
	private String name;
	private Type type;
	/**
	 * Constructeur de IntVariable
	 * @param name String
	 */
	public IntVariable(String name) {
		this.name = name;
	}

	public String pp() {
		return name;
	}

	@Override
	public RetVariable toIR(SymbolTable ts) throws TypeException {
		Symbol var = ts.lookup(this.name);
		if(var != null || var instanceof FunctionSymbol) {
			throw new TypeException("IntVariable -> Variable déjà déclaré");
		}

		RetVariable ret = new RetVariable(new Llvm.IR(Llvm.empty(),Llvm.empty()),type,name);
		Llvm.Instruction alloca = new Llvm.Alloca(this.name, this.type.toLlvmType());		
		ret.ir.appendCode(alloca);		
		return ret;
	}
}