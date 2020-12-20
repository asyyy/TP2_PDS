package TP2.ASD;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;

public abstract class Variable {
	public abstract String pp();
	public abstract RetVariable toIR(SymbolTable ts) throws TypeException;
	
	static public class RetVariable{
        // The LLVM IR:
        public Llvm.IR ir;
        // And additional stuff:
        public Type type;
        public String result; // The name containing the expression's result
        // (either an identifier, or an immediate value)

        public RetVariable(Llvm.IR ir,Type type, String result) {
          this.ir = ir;
          this.type = type;
          this.result = result;
        }
      }
}
