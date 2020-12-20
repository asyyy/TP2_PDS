package TP2.ASD;

import java.util.List;
import java.util.function.Function;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;
import TP2.ASD.Fonction.RetFonction;

public class Program {
     
	private int nbIndent = 0;
	private List<Prototype> lProto;
    private List<Fonction> lFonc;
    
    public Program(List<Prototype> lProto,List<Fonction> lFonc) {
      this.lProto = lProto;
      this.lFonc = lFonc;
    }

    // Pretty-printer
    public String pp() {
    	String res = "";
    	for(Prototype pro : lProto) {
        	res = res + pro.pp();
        }
        for(Fonction fon : lFonc) {
        	res = res + fon.pp(nbIndent++);
        }
        return res;
    }
    
    // IR generation
    
    public Llvm.IR toIR() throws TypeException {
    	
    	SymbolTable ts = new SymbolTable();
        
        if(lFonc.isEmpty()) {
        	throw new TypeException("Program sans fonction");
        }
        RetFonction ret = null;
        for(int i = 0;i<lFonc.size();i++) {
        	if(i == 0) {
        		ret = lFonc.get(0).toIR(ts);
        	}else {
        		ret = lFonc.get(i).toIR(ts);
        	}
        }

        return ret.ir;
      }
  }