package TP2;

import java.util.List;

import TP2.Llvm.Type;
import TP2.Utils.LLVMStringConstant;
import TP2.ASD.Pair;
import TP2.ASD.Variable;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

// This file contains a simple LLVM IR representation
// and methods to generate its string representation

public class Llvm {
	static public class IR {
		List<Instruction> header; // IR instructions to be placed before the code (global definitions)
		List<Instruction> code;   // main code

		public IR(List<Instruction> header, List<Instruction> code) {
			this.header = header;
			this.code = code;
		}

		// append an other IR
		public IR append(IR other) {
			header.addAll(other.header);
			code.addAll(other.code);
			return this;
		}

		// append a code instruction
		public IR appendCode(Instruction inst) {
			code.add(inst);
			return this;
		}

		// append a code header
		public IR appendHeader(Instruction inst) {
			header.add(inst);
			return this;
		}

		// Final string generation
		public String toString() {
			// This header describe to LLVM the target
			// and declare the external function printf
			StringBuilder r = new StringBuilder("; Target\n" +
					"target triple = \"x86_64-unknown-linux-gnu\"\n" +
					"; External declaration of the printf function\n" +
					"declare i32 @printf(i8* noalias nocapture, ...)\n" +
					"\n; Actual code begins\n\n");

			for(Instruction inst: header)
				r.append(inst);

			r.append("\n\n");

			// We create the function main
			// TODO : remove this when you extend the language
			r.append("define i32 @main() {\n");


			for(Instruction inst: code)
				r.append(inst);

			// TODO : remove this when you extend the language
			r.append("}\n");

			return r.toString();
		}
	}

	// Returns a new empty list of instruction, handy
	static public List<Instruction> empty() {
		return new ArrayList<Instruction>();
	}


	// LLVM Types
	static public abstract class Type {
		public abstract String toString();
	}

	static public class Int extends Type {
		public String toString() {
			return "i32";
		}
	}
	static public class Void extends Type {
		public String toString() {
			return "void";
		}
	}
	static public class Text extends Type{
		private LLVMStringConstant text;
		public Text(String text) {
			this.text =Utils.stringTransform(text);
		}
		public String toString() {
			return "[" +text.length + " x i8]";
		}
	}
	static public class Array extends Type {


		private int size;

		/**
		 * Constructeur de array
		 * @param size taille du table
		 */
		public Array(int size) {
			this.size = size;
		}
		public Array() {
			this.size = 0;
		}

		@Override
		public String toString() {
			return "["+ size + " * " + "i32]";
		}
	}

	// LLVM IR Instructions
	static public abstract class Instruction {
		public abstract String toString();
	}
	static public class Read extends Instruction {
		private List<Variable> readList;
		public Read (List <Variable> readList) {
			this.readList = readList;
		}
		@Override
        public String toString() {
			String res = "call i32 (i8*, ...) @scanf (i8* getelementptr inbounds (";
			
			for(Variable v : readList) {
				res += "???";
			}
        	res = "???";
            return res;
        }
	}
	static public class Print extends Instruction{
		//List des expressions a print
		private List<Pair> printList;
		/**
		 * Constructeur de print
		 * @param printList listes des expressions a passer en parametre du call de printf
		 */
		public Print(List<Pair> printList) {
			this.printList=printList;
		}
		@Override
        public String toString() {
			String res = "call i32 (i8*, ...) @printf (i8* getelementptr inbounds (";
        	
			for(Pair p : printList) {
				res += p.getType().toString() + ", ";
			}
			res += "i64 0, i64 0))";
            return res;
        }
	}
	/**
	 * 
	 * @author Alexy
	 *	
	 */
	static public class GetElementPtr extends Instruction {

		private String tmpName;

		private Type tmpTypeArray;

		private String name;

		private Type type;

		private Pair index;
        /**
         * Constructeur de GetElement Ptr
         */
        public GetElementPtr(String tmpName,  Type tmpTypeArray, String name, Pair index) {
            this.tmpName = tmpName;
            this.tmpTypeArray = tmpTypeArray;
            this.name = name;
            this.index = index;
        }

        @Override
        public String toString() {
        	
            return tmpName + " = getelementptr " + tmpTypeArray + "," + tmpTypeArray + "* " + name + ", " + 
        						index.getType() + " " + index.getName() + "\n";
        }
    }

	/**
	 * Definir une IntFonction/VoidFonction 
	 * @author Alexy
	 *
	 */
	static public class Define extends Instruction {

		private Type type;

		private String name;

		private List<Pair> lParams;

		/**
		 * Constructeur de Define
		 */
		public Define(Type type, String name, List<Pair> lParams) {
			this.type = type;
			this.name = name;
			this.lParams = lParams;
		}

		@Override
		public String toString() {
			String res = "define " + type + " " + name + "(";

			if(!lParams.isEmpty()) {
				res += lParams.get(0).getType() + " %" + lParams.get(0).getName();

				for(int i = 1;i<lParams.size();i++) {
					res += ", " + lParams.get(i).getType() + " %" + lParams.get(i).getName();
				}          	
			}

			return res + "){\n";
		}
	}
	static public class Return extends Instruction {

		private Type type;


		private String value;

		/**
		 * Constructeur de Return
		 */
		public Return(Type type, String value) {
			this.type = type;
			this.value = value;
		}

		@Override
		public String toString() {
			return "ret " + type + " " + value + "\n}\n\n";
		}
	}
	/**
	 * Appel d'une fonction dans une expression (TODO peut etre plus?) 
	 * @author Alexy
	 *
	 */
	static public class Call extends Instruction {

		private String returnValue;

		private Type returnType;

		private String name;

		private List<Pair> lParams;


		/**
		 * Constructeur de call
		 */
		public Call(String returnValue, Type returnType, String name, List<Pair> lParams) {
			this.returnValue = returnValue;
			this.returnType = returnType;
			this.name = name;
			this.lParams = lParams;
	
		}

		@Override
		public String toString() {
			String params = "";
			String res;

			if(this.returnValue.length() > 0) {
				res = returnValue +" = ";
			}else {
				res = "";
			}
			if(!lParams.isEmpty()) {
				params += lParams.get(0).getType() + " " + lParams.get(0).getName();

				for(int i = 1;i<lParams.size();i++) {
					params += ", " + lParams.get(i).getType() + " " + lParams.get(i).getName();
				}          	
			}

			return res + "call " + returnType + " " + name + "(" + params + ")\n";
		}
	}
	/**
	 * Comparaison pour IF/WHILE
	 * @author Alexy
	 *
	 */
	static public class Icmp extends Instruction {
		//Resultat de la comparaison
		private String res;

		//Type de la comparaison
		private Type type;

		//Valeur a comparer
		private String comp;

		/**
		 * Constructeur de Icmp
		 */
		public Icmp(String lvalue, Type type, String op) {
			this.res = lvalue;
			this.type = type;
			this.comp = op;
		}

		@Override
		public String toString() {
			return res + " = icmp ne " + type + " " + comp + ", 0\n";
		}
	}
	/**
	 * Chargement d'une variable ou Fonction
	 * @author Alexy
	 *
	 */
	static public class load extends Instruction {
		private String name;
		private Type type;

		private String tmpName;
		private Type tmpType;

		public load (String name,Type type,String tmpName,Type tmpType) {
			this.name = name;
			this.type = type;
			this.tmpName = tmpName;
			this.tmpType = tmpType;
		}

		@Override
		public String toString() {
			return tmpName + " = load " +tmpType+", "+type +"* " + name;
		}
	}
	static public class Alloca extends Instruction {
		private String name;
		private Type type;

		public Alloca(String name, Type type) {
			this.name = name;
			this.type = type;
		}
		@Override
		public String toString() {
			return "%"+name+ " = alloca " + type;
		}

	}
	static public class brCond extends Instruction {
		private String cond;
		private String si;
		private String sinon;

		public brCond(String c,String si,String sinon) {
			this.cond = c;
			this.si = si;
			this.sinon = sinon;
		}

		@Override
		public String toString() {
			return "br i1 " + cond + ", label " + si + ", label " + sinon + "\n";
		}

	}
	static public class brIncond extends Instruction {
		//Nom du Label où on se branche
		private String label;
		public brIncond(String label) {
			this.label = label;
		}
		@Override
		public String toString() {
			return "br label " + label + "\n";
		}

	}

	static public class Label extends Instruction {
		//Nom du label
		private String name;

		public Label(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name + ": \n" ;
		}

	}


	static public class Store extends Instruction {
		private Type typeL;
		//Left = la partie que l'on veut enregistrer
		private String left;

		private Type typeR;
		//Right = la où on veut enregistrer
		private String right;

		public Store(Type typeL, String left,Type typeR ,String right) {
			this.typeL = typeL;
			this.left = left;
			this.right = right;
			this.typeR = typeR;
		}

		public String toString() {
			return "store "+ typeL + left +", " + typeR +"* "+ right;
		}
	}

	static public class Add extends Instruction {
		private Type type;
		private String left;
		private String right;
		private String lvalue;

		public Add(Type type, String left, String right, String lvalue) {
			this.type = type;
			this.left = left;
			this.right = right;
			this.lvalue = lvalue;
		}

		public String toString() {
			return lvalue + " = add " + type + " " + left + ", " + right +  "\n";
		}
	}



	static public class Sub extends Instruction {
		private Type type;
		private String left;
		private String right;
		private String lvalue;

		public Sub(Type type, String left, String right, String lvalue) {
			this.type = type;
			this.left = left;
			this.right = right;
			this.lvalue = lvalue;
		}

		public String toString() {
			return lvalue + " = sub " + type + " " + left + ", " + right +  "\n";
		}
	}
	static public class Mul extends Instruction {
		private Type type;
		private String left;
		private String right;
		private String lvalue;

		public Mul(Type type, String left, String right, String lvalue) {
			this.type = type;
			this.left = left;
			this.right = right;
			this.lvalue = lvalue;
		}

		public String toString() {
			return lvalue + " = mul " + type + " " + left + ", " + right +  "\n";
		}
	}
	static public class Div extends Instruction {
		private Type type;
		private String left;
		private String right;
		private String lvalue;

		public Div(Type type, String left, String right, String lvalue) {
			this.type = type;
			this.left = left;
			this.right = right;
			this.lvalue = lvalue;
		}

		public String toString() {
			return lvalue + " = sdiv " + type + " " + left + ", " + right +  "\n";
		}
	}

	// TODO : other instructions
}
