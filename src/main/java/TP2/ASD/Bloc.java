package TP2.ASD;

import java.util.List;
import java.util.Objects;

import TP2.Llvm;

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
		Objects.requireNonNull(lInst);
		this.lVar = lVar;
		this.lInstru = lInst;
	}
	//Pretty-Printer
	//TODO A corriger plus tard
	public String pp() {
		String res = "";
		for(Instruction i : lInstru) {
			res = res + i.pp();
		}
		return res;
	}
	
	//TODO
	//public Llvm.IR toIr();
}
