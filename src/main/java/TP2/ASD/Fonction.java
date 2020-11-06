package TP2.ASD;

import java.util.List;

import TP2.Llvm;
import TP2.SymbolTable;
import TP2.TypeException;
/**
 * Represente les fonctions d'un Program, sera extends par VoidFonc et IntFonc.
 * @author Alexy
 *
 */
public abstract class Fonction {
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
	public Fonction(String n, List<String> l, Bloc b) {
		this.name = n;
		this.lParam = l;
		this.b = b;
	}
	
	//Pretty-Printer
	public abstract String pp();
	
	public abstract RetFonction toIR(SymbolTable ts);
	
	// Object returned by toIR on expressions, with IR + synthesized attributes
    static public class RetFonction{
      // The LLVM IR:
      public Llvm.IR ir;
      // And additional stuff:
      public Type type; // The type of the expression
      public String result; // The name containing the expression's result
      // (either an identifier, or an immediate value)

      public RetFonction(Llvm.IR ir, Type type, String result) {
        this.ir = ir;
        this.type = type;
        this.result = result;
      }
    }
	
}
