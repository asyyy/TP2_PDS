package TP2.ASD;

import java.util.List;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;
import TP2.ASD.Instruction.RetInstruction;

public class ReadInstruction extends Instruction {
	private List<Variable> lVar;
	
	public ReadInstruction(List<Variable> lVar) {
		this.lVar = lVar;
	}
	@Override
	public String pp(int nbIndent) {
		String res = "READ ";
		for(int i = 0;i<lVar.size();i++) {
			if(i == 0) {
				res += lVar.get(0).pp();
			}else {
				res += ", " +lVar.get(i).pp();
			}
		}
		return res;
	}
	@Override
	public RetInstruction toIR(SymbolTable ts) throws TypeException {
		RetInstruction ret = new RetInstruction(new Llvm.IR(Llvm.empty(), Llvm.empty()), "scanf");
		//Llvm.Instruction call = new Llvm.Read(lParam);
		return null;
	}
	

}
