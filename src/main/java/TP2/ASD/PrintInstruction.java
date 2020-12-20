package TP2.ASD;

import java.util.ArrayList;
import java.util.List;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;
import TP2.ASD.Expression.RetExpression;
import TP2.ASD.Instruction.RetInstruction;

public class PrintInstruction extends Instruction{
	
	private List<Expression> lExp;
	
	public PrintInstruction(List<Expression>lExp) {
		this.lExp = lExp;
	}
	@Override
	public String pp(int nbIndent) {
		String res = "PRINT ";
		for(int i = 0;i<lExp.size();i++) {
			if(i==0) {
				res += lExp.get(0).pp();
			}else {
				res += ", " +lExp.get(i).pp();
			}
		}
		return res;
	}

	@Override
	public RetInstruction toIR(SymbolTable ts) throws TypeException {
		RetInstruction ret = new RetInstruction(new Llvm.IR(Llvm.empty(), Llvm.empty()), "printf");
		List<Pair> lParam = new ArrayList<>();
		Pair p = new Pair();
		for(Expression e : lExp) {
			if(e instanceof TextExpression) {
				String eString = ((TextExpression) e).getString();
				p = new Pair(eString,new Llvm.Text(eString));
			}else {
				RetExpression eret = e.toIR(ts);
				p = new Pair(e.pp(),eret.type.toLlvmType());
			}
		}
		Llvm.Instruction call = new Llvm.Print(lParam);
		ret.ir.appendCode(call);
		return ret;
	}

}
