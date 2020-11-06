package TP2.ASD;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;
import TP2.Llvm.IR;
import TP2.SymbolTable.FunctionSymbol;
import TP2.SymbolTable.Symbol;
import TP2.SymbolTable.VariableSymbol;

// Concrete class for Expression: constant (integer) case
  public class IntVariable extends Variable {
    String name;
    Type type;
    
    public IntVariable(String name) {
      this.name = name;
    }

    public String pp() {
      return name;
    }

	@Override
	public RetVariable toIR(SymbolTable ts) {
		Symbol var = ts.lookup(this.name);
		if(var != null || var instanceof FunctionSymbol) {
			System.err.println("Declaration -> Variable d�j� d�clar�");
		}
	
		RetVariable ret = new RetVariable(new Llvm.IR(Llvm.empty(),Llvm.empty()),type,name);
		Llvm.Instruction alloca = new Llvm.alloca(this.name, this.type.toLlvmType());		
		ret.ir.appendCode(alloca);		
		return ret;
	}
  }