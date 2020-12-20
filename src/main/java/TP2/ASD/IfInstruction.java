package TP2.ASD;


import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;
import TP2.Utils;

public class IfInstruction extends Instruction {
	// Condition
    private Expression exp;

    //Bloc IF
    private Bloc b1;

    //BLOC ELSE
    private Bloc b2;

    /**
     * Constructeur de IfInstruction
     * @param exp La condition
     * @param b1 Bloc du if
     * @param b2 Bloc du else
     */
    public IfInstruction(Expression exp, Bloc b1, Bloc b2) {
        this.exp = exp;
        this.b1 = b1;
        this.b2 = b2;
    }

    public String pp(int nbIndent) {
    	String res = Utils.indent(nbIndent) + "IF " + exp.pp() + "\nTHEN\n" ;
        
        res += b1.pp(nbIndent++);

        if(b2 != null) {//Si il y a un else
            res += Utils.indent(nbIndent) + "ELSE\n";
            res += b2.pp(nbIndent++);
        }

        return res + Utils.indent(nbIndent) + "\nFI";
    }

    @Override
    public RetInstruction toIR(SymbolTable ts) throws TypeException {
        Expression.RetExpression cond = exp.toIR(ts);
        //cond.type doit etre un Int pour être comparer a 0
        if (cond.type instanceof Int) { 
        	
        	String res = Utils.newtmp();
            String si = Utils.newlab("if");
            String sinon = Utils.newlab("else");
            String fi = Utils.newlab("fi");

            Llvm.Instruction icmp = new Llvm.Icmp(res, cond.type.toLlvmType(), cond.result);
            Llvm.Instruction brCond = null;

            if(b2 != null) {
                brCond = new Llvm.brCond(res, "%" + si, "%" + sinon);
            } else {
                brCond = new Llvm.brCond(res, "%" + si, "%" + fi);
            }

            Llvm.Instruction brUncond = new Llvm.brIncond("%" + fi);

            RetInstruction ret = new RetInstruction(new Llvm.IR(Llvm.empty(), Llvm.empty()), res);

            ret.ir.append(cond.ir);
            ret.ir.appendCode(icmp);
            ret.ir.appendCode(brCond);
            ret.ir.appendCode(new Llvm.Label(si));
            ret.ir.append(this.b1.toIR(ts));
            ret.ir.appendCode(brUncond);

            if(b2 != null) {//Si il y a un else
                ret.ir.appendCode(new Llvm.Label(sinon));
                ret.ir.append(this.b2.toIR(ts));
                ret.ir.appendCode(brUncond);
            }

            ret.ir.appendCode(new Llvm.Label(fi));

            return ret;
        }else {
        	throw new TypeException("Types différents " + cond.type );
        }

        
    }

}

