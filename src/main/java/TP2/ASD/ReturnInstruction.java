package TP2.ASD;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;
import TP2.Utils;
import TP2.ASD.Expression.RetExpression;

public class ReturnInstruction extends Instruction {

   
	private Expression e;

    /**
     * Constructeur de ReturnInstruction
     * @param expression Expression à retourner
     */
    public ReturnInstruction(Expression e) {
        this.e = e;
    }

    @Override
    public String pp(int nbIdent) {
        return Utils.indent(nbIdent) + "RETURN " + this.e.pp();
    }

    @Override
    public RetInstruction toIR(SymbolTable ts) throws TypeException {
        RetExpression retE = this.e.toIR(ts);
        
        //TODo verification que le type du return correspond au type de la fonction
        String rReturn = "%return";
        RetInstruction ret = new RetInstruction(new Llvm.IR(Llvm.empty(), Llvm.empty()), rReturn);

        Llvm.Instruction store = new Llvm.Store(retE.type.toLlvmType(),retE.result,retE.type.toLlvmType(),rReturn);

        ret.ir.append(retE.ir);
        ret.ir.appendCode(store);

        return ret;
    }


}
