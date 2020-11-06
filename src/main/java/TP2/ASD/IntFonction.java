package TP2.ASD;

import java.util.List;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;
/**
 * Represente les fonctions IntFonc.
 * @author Alexy
 *
 */
public class IntFonction extends Fonction{
	//Nom de la fonction
	private String name;
	
	//Liste des paramètres de la fonction
	private List<String> lParam;
	
	//Bloc d'instructions de la fonction
	private Bloc b;
	
	/**
	 * Constructeur de Fonction
	 * @param n	Nom de la fonction
	 * @param l Liste des parametres
	 * @param b Bloc des instructions
	 */
	public IntFonction(String n, List<String> l, Bloc b) {
		this.name = n;
		this.lParam = l;
		this.b = b;
	}
	
	//Pretty-Printer
	public String pp() {
		return null;
		
	}
	
	public RetFonction toIR(SymbolTable ts) {
	
		return null;
    }
	
}
