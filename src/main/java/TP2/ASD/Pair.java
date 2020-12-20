package TP2.ASD;

import TP2.Llvm;
/**
 * Class Pair pour avoir facilement un liste de nom et de type associé
 * @author Alexy
 *
 */
public class Pair {
	private String name;
	private Llvm.Type type;
		
	/**
	 * Constructeur de pair Vide
	 */
	public Pair() {}
	
	/**
	 * Constructeur de Pair
	 * @param name String
	 * @param type Llvm.Type
	 */
	public Pair(String name,Llvm.Type type) {
		this.name = name;
		this.type = type;

	}
	
	public String getName() {
		return name;
	}
	
	public Llvm.Type getType() {
		return type;
	}
	
}
