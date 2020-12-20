package TP2.ASD;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;

public class ArrayVariable extends Variable {
    
	private String name;
    
    private int size;

    /**
     * Constructeur de ArrayVariable
     * @param name String
     * @param size Int Taille du tableau
     */
    public ArrayVariable(String name, int size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String pp() {
        return "" + name+ "[" + size + "]";
    }

    @Override
    public RetVariable toIR(SymbolTable ts) throws TypeException {


        SymbolTable.VariableSymbol symbol = new SymbolTable.VariableSymbol(new Array(size), name);
        String result = "%" + name;

        RetVariable ret = new RetVariable(new Llvm.IR(Llvm.empty(), Llvm.empty()), new Array(size), result);

        if(!ts.add(symbol)) {
        	throw new TypeException("ArrayVariable -> Variable déjà déclaré");
        }

        Llvm.Instruction alloca = new Llvm.Alloca(result,new Array(size).toLlvmType());

        ret.ir.appendCode(alloca);


        return ret;
    }
}
