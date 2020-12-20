package TP2.ASD;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import TP2.Llvm;
import TP2.Llvm.Return;
import TP2.SymbolTable;
import TP2.SymbolTable.FunctionSymbol;
import TP2.SymbolTable.VariableSymbol;
import TP2.TypeException;
import TP2.Utils;
/**
 * Represente les fonctions Void.
 * @author Alexy
 *
 */
public class VoidFonction extends Fonction{
	//Nom de la fonction
	private String name;

	//Liste des paramètres de la fonction
	private List<Pair> lParam;

	//Bloc d'instructions de la fonction
	private Bloc b;

	/**
	 * Constructeur de Fonction
	 * @param n	Nom de la fonction
	 * @param l Liste des parametres
	 * @param b Bloc des instructions
	 */
	public VoidFonction(String n, List<Pair> l, Bloc b) {
		this.name = n;
		this.lParam = l;
		this.b = b;
	}

	//Pretty-Printer
	public String pp(int nbIndent) {

		String res = "FUNC VOID " + name + "(";
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

		return res + ")\n" + b.pp(nbIndent);
	}


	public RetFonction toIR(SymbolTable ts) throws TypeException {
		SymbolTable.FunctionSymbol s = (SymbolTable.FunctionSymbol) ts.lookup(name);
		
		if(s == null) throw new TypeException(name + "n'est pas dans la table de symbol");
		List<Pair> simpleParam = new ArrayList<Pair>();
		List<VariableSymbol> args = s.getArg();

		if(!name.equals("main")) {

			//Verifie que c'est bien du type void
			if(!s.getType().equals(new Void())) {
				throw new TypeException("Type non similaire :" + s.getType() + " et " + new Void());
				
			}
			//Verifie attribut
			for(Pair param : lParam) {

				VariableSymbol find = s.findArg(param.getName());
				if(find == null) {
					throw new TypeException(param + "n'est pas dans la liste d'attribut");
				}
				if(!find.getType().equals(param.getType())) {
					throw new TypeException("Type non similaire" +find.getName() +" et "+ param.getName());
				}
				Pair p = new Pair();
				p = new Pair(param.getName(),find.getType().toLlvmType());
				
				simpleParam.add(p);
			}


		}else {

			s = new SymbolTable.FunctionSymbol(new Void(), name, new ArrayList<VariableSymbol>(), true);

			if(!ts.add(s)) {
				throw new TypeException(name + "existe déjà");
			}


		}
		String name2 = "@" + name;
		String entry = Utils.newlab("entry");

		RetFonction ret = new RetFonction((new Llvm.IR(Llvm.empty(), Llvm.empty())), s.getType(),name);

		Llvm.Instruction decl = new Llvm.Define(ret.type.toLlvmType(),ret.result,simpleParam);
		Llvm.Instruction entryIns = new Llvm.Label(entry);

		List<Llvm.Instruction> alocParam = new ArrayList<Llvm.Instruction>();


		for(Pair param : lParam) {
			alocParam.add(new Llvm.Alloca("%" + param.getName() + alocParam.size(), param.getType()));
		}

		Llvm.Instruction rReturn = new Llvm.Return(ret.type.toLlvmType(),"");

		ret.ir.appendCode(decl);
		ret.ir.appendCode(entryIns);

		for(Llvm.Instruction param : alocParam) {
			ret.ir.appendCode(param);
		}

		ret.ir.append(b.toIR(ts));
		ret.ir.appendCode(rReturn);

		return ret;
	}

}
