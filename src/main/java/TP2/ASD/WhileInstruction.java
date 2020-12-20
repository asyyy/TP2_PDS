package TP2.ASD;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;
import TP2.Utils;

public class WhileInstruction extends Instruction {
    
	//Condition du while
	private Expression e;
	//Corps du while
	private Bloc b;

    /**
     * Constructeur
     * @param expression condition du while
     * @param bloc corps de la boucle
     */
    public WhileInstruction(Expression e, Bloc b) {
        this.e = e;
        this.b = b;
    }

    @Override
    public String pp(int nbIndent) {
        String ret = Utils.indent(nbIndent) + "WHILE " + this.e.pp() + "\n";

        ret += Utils.indent(nbIndent) + "DO\n";
        
        ret += this.b.pp(nbIndent);

        return ret + Utils.indent(nbIndent) + "DONE\n";
    }

    @Override
    public RetInstruction toIR(SymbolTable ts) throws TypeException {
        Expression.RetExpression cond = this.e.toIR(ts);

        if (!cond.type.equals(new Int())) {
            throw new TypeException("Type non similaire :"+cond.type + " et " + new Int());
        }

        String tmp = Utils.newtmp();
        String wWhile = Utils.newlab("while");
        String dDo = Utils.newlab("do");
        String done = Utils.newlab("done");

        Llvm.Instruction icmp = new Llvm.Icmp(tmp, cond.type.toLlvmType(), cond.result);
        Llvm.Instruction brCond = new Llvm.brCond(tmp, "%" + dDo, "%" + done);
        Llvm.Instruction brUncond = new Llvm.brIncond("%" + wWhile);

        RetInstruction ret = new RetInstruction(new Llvm.IR(Llvm.empty(), Llvm.empty()), tmp);

        ret.ir.appendCode(brUncond);
        ret.ir.appendCode(new Llvm.Label(wWhile));
        ret.ir.append(cond.ir);
        ret.ir.appendCode(icmp);
        ret.ir.appendCode(brCond);
        ret.ir.appendCode(new Llvm.Label(dDo));
        ret.ir.append(this.b.toIR(ts));
        ret.ir.appendCode(brUncond);
        ret.ir.appendCode(new Llvm.Label(done));

        return ret;
    }
}
