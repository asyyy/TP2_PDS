package TP2.ASD;

import java.util.List;
import java.util.function.Function;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;

public class Program {
    //List<Proto> lProto; Pour plus tard
    private List<Fonction> lFonc;
    
    //A supprimer
    Expression e;
    
    public Program(List<Fonction> lFonc) {
      this.lFonc = lFonc;
    }

    // Pretty-printer
    public String pp() {
    	String res = "";
        for(Fonction fon : lFonc) {
        	res = res + fon.pp();
        }
        return res;
    }
    
    // IR generation
    //TODO
    public Llvm.IR toIR() throws TypeException {
    	// TODO : change when you extend the language
    	
    	SymbolTable ts = new SymbolTable();
        // computes the IR of the expression
        Expression.RetExpression retExpr = e.toIR(ts);
        // add a return instruction
        Llvm.Instruction ret = new Llvm.Return(retExpr.type.toLlvmType(), retExpr.result);
        retExpr.ir.appendCode(ret);

        return retExpr.ir;
      }
  }