package TP2.ASD;

import java.util.ArrayList;
import java.util.List;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.SymbolTable.VariableSymbol;
import TP2.TypeException;

public class VoidPrototype extends Prototype{
	private String name;
	private List<Pair> lParam;
	
	public VoidPrototype(String name, List<Pair> lParam) {
		this.name = name;
		this.lParam = lParam;
	}
	
	@Override
	public String pp() {
		  String res = "PROTO VOID " + name + "(";
		  for(int i = 0; i<lParam.size();i++) {
			  //Si le param est un tableau et si c'est le premier (pour la virgule)
			  
			  if(lParam.get(i).getType().equals(new Array())) {
				  if(i==0) {
					  res += lParam.get(i).getName() + "[]";
				  }else {
					  res += ", " +lParam.get(i).getName() + "[]";
				  }
			  }else {
				  if(i==0) {
					  res += lParam.get(i).getName();
				  }else {
					  res += ", " +lParam.get(i).getName();
				  }
			  }
			  
		  }
          
          return res + ")\n";
	}

	@Override
	public RetPrototype toIr(SymbolTable ts) throws TypeException {
		 List<VariableSymbol> listVar= new ArrayList<>();

         SymbolTable.VariableSymbol var = null;
         
         if(ts.lookup(name) != null) {
        	 throw new TypeException("VoidProto -> " + name + " est déjà déclaré");
         }
         
         for(Pair p : lParam) {
        	 if(p.getType().equals(new Array())) {
        		 var = new SymbolTable.VariableSymbol(new Array(), p.getName());
        	 }else {
        		 var = new SymbolTable.VariableSymbol(new Int(), p.getName());
        	 }
         }

         SymbolTable.FunctionSymbol ts2 = new SymbolTable.FunctionSymbol(new Int(), name, listVar, true);

         RetPrototype ret = new RetPrototype(new Llvm.IR(Llvm.empty(), Llvm.empty()), new Int(), "%" + name);

         return ret;
	}

}
