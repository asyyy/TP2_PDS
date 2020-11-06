package TP2.ASD;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;

// Warning: this is the instruction from VSL+, not the LLVM instruction!
  public abstract class Instruction{
    public abstract String pp();
    public abstract RetInstruction toIR(SymbolTable ts) throws TypeException;
    
    static public class RetInstruction {
        // The LLVM IR:
        public Llvm.IR ir;
        // And additional stuff:
  
        public String result; // The name containing the expression's result
        // (either an identifier, or an immediate value)

        public RetInstruction(Llvm.IR ir, String result) {
          this.ir = ir;
          this.result = result;
        }
      }
  }