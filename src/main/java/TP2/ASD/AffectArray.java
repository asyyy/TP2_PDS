package TP2.ASD;

import java.util.ArrayList;
import java.util.List;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;
import TP2.Utils;
import TP2.ASD.Expression.RetExpression;

public class AffectArray extends Instruction {
    
	private String name;

    //Partie droite de l'instruction
    private Expression e;
    
    private Expression index;

    /**
     * Constructeur de AffectArray
     * @param name String nom du tableau
     * @param e expression a affecté
     * @param index les index dans le tablea
     */
    public AffectArray(String name, Expression e, Expression index) {
        this.name = name;
        this.e = e;
        this.index = index;
    }

    @Override
    public String pp(int nbIdent) {

        return name + "[" +index.pp() +"] := " + e.pp();
    }

    @Override
    public RetInstruction toIR(SymbolTable ts) throws TypeException {
        SymbolTable.VariableSymbol var = (SymbolTable.VariableSymbol) ts.lookup(name);
        
       
		RetExpression indexExp = e.toIR(ts);
		List<Pair> coupleExpResultType = new ArrayList<>();
			
		Pair p = new Pair(indexExp.result,indexExp.type.toLlvmType());
		
        Expression.RetExpression retExpr = e.toIR(ts);

        Type typeTmp = null;

        if(var.getType() instanceof Array) {
            typeTmp = (Array) var.getType();
        }

        if (!retExpr.type.equals(typeTmp)) {
            throw new TypeException("Type non similaire " + typeTmp + " et " + retExpr.type);
        }

        RetInstruction ret = new RetInstruction(new Llvm.IR(Llvm.empty(), Llvm.empty()), name);

        ret.ir.append(indexExp.ir);
        String tmp = Utils.newtmp();

        Llvm.Instruction getElementPtr = new Llvm.GetElementPtr(tmp, typeTmp.toLlvmType(), "%" + name, p);
        Llvm.Instruction store = new Llvm.Store(retExpr.type.toLlvmType(), retExpr.result, retExpr.type.toLlvmType(), tmp);

        ret.ir.appendCode(getElementPtr);
        ret.ir.appendCode(store);

        return ret;
    }
}
