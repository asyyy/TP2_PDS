package TP2.ASD;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.SymbolTable.FunctionSymbol;
import TP2.SymbolTable.Symbol;
import TP2.SymbolTable.VariableSymbol;
import TP2.Utils;

// Concrete class for Expression: constant (integer) case
  public class VarExpression extends Expression {
    String name;
    
    
    public VarExpression(String name) {
      this.name = name;
    }

    public String pp() {
      return name;
    }

    public RetExpression toIR(SymbolTable ts) {
    	//Vérifier existance dans tableSymbole
      Symbol vara = ts.lookup(name);
      if(vara== null) {
    	  System.err.print("VarExpression -> variable non reconnue dans ts");
      }
      if(vara instanceof FunctionSymbol) {
    	  System.err.print("VarExpression -> le nom de la variable représente le nom d'une fonction");
      }
      VariableSymbol var = (VariableSymbol) vara;
      String tnmpName = Utils.newtmp();
      RetExpression ret = new RetExpression(new Llvm.IR(Llvm.empty(), Llvm.empty()), new Int(), name);
      Llvm.Instruction load = new Llvm.load(this.name,var.getType().toLlvmType(),tnmpName);
      ret.ir.appendCode(load);
      return ret;
    }
  }