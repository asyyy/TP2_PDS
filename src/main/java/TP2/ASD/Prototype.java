package TP2.ASD;

import java.util.List;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;

public abstract class Prototype {
	private String name;
	private List<Pair> lParam;
	public abstract String pp();
	public abstract RetPrototype toIr(SymbolTable ts) throws TypeException;
	static public class RetPrototype{
	      // The LLVM IR:
	      public Llvm.IR ir;
	      // And additional stuff:
	      public Type type; // The type of the expression
	      public String result; // The name containing the expression's result
	      // (either an identifier, or an immediate value)

	      public RetPrototype(Llvm.IR ir, Type type, String result) {
	        this.ir = ir;
	        this.type = type;
	        this.result = result;
	      }
	    }
}
