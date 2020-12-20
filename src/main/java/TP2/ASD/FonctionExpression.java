package TP2.ASD;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;
import TP2.Utils;

public class FonctionExpression extends Expression {
    
    private String name;

    //Argument de la fonctions
    private List<Expression> lArgs;

    /**
     * Constructeur
     * @param name Nom de la fonction dans l'expression
     * @param lArgs Valeurs des lArgs
     */
    public FonctionExpression(String name, List<Expression> lArgs) {
        this.name = name;
        this.lArgs = lArgs;
    }

    @Override
    public String pp() {
        String ret = this.name + "(";

        for(Expression attribut : this.lArgs) {
            ret += attribut.pp() + ", ";
        }

        return ret + ")";
    }

    @Override
    public RetExpression toIR(SymbolTable ts) throws TypeException {
        SymbolTable.FunctionSymbol fonc = (SymbolTable.FunctionSymbol) ts.lookup(name);
       

        if(fonc == null) {
        	throw new TypeException(name + "n'existe pas dans la table des symboles");
        }

        //Gestion des arguments de la fonction
        List<Pair> simpleParam = new ArrayList<>();
       
        String result = Utils.newtmp();

        RetExpression ret = new RetExpression(new Llvm.IR(Llvm.empty(), Llvm.empty()), fonc.getType(), result);
        RetExpression tmp = null;
        Pair p = new Pair();
        
        for(Expression e : this.lArgs) {
            tmp = e.toIR(ts);
            ret.ir.append(tmp.ir);
            
            
            p = new Pair(tmp.result,tmp.type.toLlvmType());
                  
            simpleParam.add(p);
        }

        Llvm.Instruction call = new Llvm.Call(result, fonc.getType().toLlvmType(), "@" + name, simpleParam);

        ret.ir.appendCode(call);

        return ret;
    }
}
