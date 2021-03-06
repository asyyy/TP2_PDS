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
 * Represente les fonctions IntFonc.
 * @author Alexy
 *
 */
public class IntFonction extends Fonction{
	//Nom de la fonction
	private String name;

	//Liste des param�tres de la fonction
	private List<Pair> lParam;

	//Bloc d'instructions de la fonction
	private Bloc b;

	/**
	 * Constructeur de Fonction
	 * @param n	Nom de la fonction
	 * @param l Liste des parametres
	 * @param b Bloc des instructions
	 */
	public IntFonction(String n, List<Pair> l, Bloc b) {
		this.name = n;
		this.lParam = l;
		this.b = b;
	}

	//Pretty-Printer
	public String pp(int nbIndent) {

		String res = "FUNC INT " + name + "(";
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
		SymbolTable.FunctionSymbol fonc = (SymbolTable.FunctionSymbol) ts.lookup(name);

		if(fonc == null) throw new TypeException(name + "n'est pas dans la table de symbol");

		List<Pair> simpleParam = new ArrayList<Pair>();
		List<VariableSymbol> args = fonc.getArg();

		if(!name.equals("main")) {

			//Verifie que c'est bien du type Int
			if(!fonc.getType().equals(new Int())) {
				throw new TypeException("type mismatch: have " + fonc.getType() + " and " + new Int());
			}
			//Verifie attribut
			for(Pair param : lParam) {

				VariableSymbol find = fonc.findArg(param.getName());
				if(find == null ) {
					throw new TypeException(param + "n'est pas dans la liste d'attribut");
				}
				if(!find.getType().equals(param.getType())) {
					throw new TypeException("Type non similaire" +find.getName() +" et "+ param.getName());
				}

				Pair p = new Pair(param.getName(),find.getType().toLlvmType());
				simpleParam.add(p);
			}


		}else {

			fonc = new SymbolTable.FunctionSymbol(new Int(), name, new ArrayList<VariableSymbol>(), true);

			if(!ts.add(fonc)) {
				throw new TypeException(name + "existe d�j�");
			}


		}
		String name2 = "@" + name;
		String result = "%return";
		String entry = Utils.newlab("entry");
		String tmp = Utils.newtmp();

		RetFonction ret = new RetFonction((new Llvm.IR(Llvm.empty(), Llvm.empty())), fonc.getType(),name);

		Llvm.Instruction decl = new Llvm.Define(ret.type.toLlvmType(),ret.result,simpleParam);
		Llvm.Instruction entryIns = new Llvm.Label(entry);
		Llvm.Instruction alloc = new Llvm.Alloca(result, ret.type.toLlvmType());
		Llvm.Instruction load = new Llvm.load(tmp, ret.type.toLlvmType(), result,ret.type.toLlvmType());

		List<Llvm.Instruction> alocParam = new ArrayList<Llvm.Instruction>();


		for(Pair param : lParam) {
			alocParam.add(new Llvm.Alloca("%" + param + alocParam.size(), param.getType()));
		}

		Llvm.Instruction rReturn = new Llvm.Return(ret.type.toLlvmType(),tmp);

		ret.ir.appendCode(decl);
		ret.ir.appendCode(entryIns);
		ret.ir.appendCode(alloc);

		for(Llvm.Instruction param : alocParam) {
			ret.ir.appendCode(param);
		}

		ret.ir.append(b.toIR(ts));
		ret.ir.appendCode(load);
		ret.ir.appendCode(rReturn);

		return ret;
	}

}
