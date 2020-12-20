package TP2.ASD;

import java.util.List;
import java.util.Objects;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;
import TP2.ASD.Instruction.RetInstruction;
import TP2.ASD.Variable.RetVariable;

public class Bloc {
	//(dans PDF VSL, 2 listes)
	
	//Liste des déclarations dans un bloc
	private List<Variable> lVar;
	
	//Liste des instructions dans un bloc
	private List<Instruction> lInstru;
	
	
	/**
	 *  Constructeur de Blob
	 * @param lVar liste des déclarations dans un bloc
	 * @param lInst non-null, liste des instruction contenue dans l
	 */
	public Bloc(List<Variable> lVar, List<Instruction> lInst) {
		Objects.requireNonNull(lVar);
		Objects.requireNonNull(lInst);
		this.lVar = lVar;
		this.lInstru = lInst;
	}
	//Pretty-Printer
	public String pp(int nbIndent) {
		String res = "{ \n \t INT ";
		if(!lVar.isEmpty()) {
			// Pour éviter d'avoir INT , n , i ,j , ou INT n, j , i ,
			res += lVar.get(0).pp();
			for(int i = 1 ; i < lVar.size();i++) {
				res += lVar.get(i).pp() +", ";
			}
		}
		
		res += "\n";
		if(!lInstru.isEmpty()) {
			for(Instruction i : lInstru) {
				res += "\t"+ i.pp(nbIndent++) + "\n";
			}
		}
		res += "\n}\n";
		return res;
	}
	/**
	 * Générateur de code Llvm
	 * @param ts Table de symbole du créateur de ce bloc
	 * @return code llvm correspondant
	 */
	public Llvm.IR toIR(SymbolTable ts) throws TypeException{
		SymbolTable blocTs = new SymbolTable(ts);
		Llvm.IR retBloc = new Llvm.IR(Llvm.empty(),Llvm.empty());
		
		
		if(!lVar.isEmpty()) {
			RetVariable retVar = lVar.get(0).toIR(blocTs);
			
			for(int i = 1; i<lVar.size();i++) {
				retVar.ir.append(lVar.get(i).toIR(blocTs).ir);
			}
			retBloc.append(retVar.ir);
			
		}
		if(!lInstru.isEmpty()) {
			RetInstruction retIns = lInstru.get(0).toIR(blocTs);
			
			for(int i = 1; i<lInstru.size();i++) {
				retIns.ir.append(lInstru.get(i).toIR(blocTs).ir);
			}
			retBloc.append(retIns.ir);
		}
		
		return retBloc;
	}
}
