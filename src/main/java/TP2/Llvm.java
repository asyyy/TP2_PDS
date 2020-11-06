package TP2;

import java.util.List;
import java.util.ArrayList;

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

	// TODO : other types


	// LLVM IR Instructions
	static public abstract class Instruction {
		public abstract String toString();
	}
	static public class load extends Instruction {
		String name;
		Type type;
		String tmpName;
		public load (String name,Type type,String tmpName) {
			this.name = name;
			this.type = type;
			this.tmpName = tmpName;
		}
		@Override
		public String toString() {
			return tmpName + " = load " +type+", "+type +"* " + name;
		}
	}
	static public class alloca extends Instruction {
		String name;
		Type type;
		
		public alloca(String name, Type type) {
			this.name = name;
			this.type = type;
		}
		@Override
		public String toString() {
			return "%"+name+ " = alloca " + type;
		}
		
	}
	static public class brCond extends Instruction {
		String cond;
		String si;
		String sinon;
		
		public brCond(String c,String si,String sinon) {
			this.cond = c;
			this.si = si;
			this.sinon = sinon;
		}
		
		@Override
		public String toString() {
			return "br i1 " + this.cond + ", label " + this.si + ", label " + this.sinon + "\n";
		}
		
	}
	static public class brIncond extends Instruction {
		//Nom du Label où on se branche
		String label;
		public brIncond(String label) {
			this.label = label;
		}
		@Override
		public String toString() {
			return "br label " + this.label + "\n";
		}
		
	}
	
	static public class Label extends Instruction {
		//Nom du label
		String name;
		
		public Label(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name + ": \n" ;
		}
		
	}


	static public class Store extends Instruction {
		Type type;
		//Left = la partie que l'on veut enregistrer
		String left;
		
		//Right = la où on veut enregistrer
		String right;

		public Store(Type type, String left, String right) {
			this.type = type;
			this.left = left;
			this.right = right;
		}
		
		public String toString() {
			return "store "+ this.type + this.left +", " + this.type +"* "+ this.right;
		}
	}

	static public class Add extends Instruction {
		Type type;
		String left;
		String right;
		String lvalue;

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
	

	static public class Return extends Instruction {
		Type type;
		String value;

		public Return(Type type, String value) {
			this.type = type;
			this.value = value;
		}

		public String toString() {
			return "ret " + type + " " + value + "\n";
		}
	}
	static public class Sub extends Instruction {
		Type type;
		String left;
		String right;
		String lvalue;

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
		Type type;
		String left;
		String right;
		String lvalue;

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
		Type type;
		String left;
		String right;
		String lvalue;

		public Div(Type type, String left, String right, String lvalue) {
			this.type = type;
			this.left = left;
			this.right = right;
			this.lvalue = lvalue;
		}

		public String toString() {
			return lvalue + " = div " + type + " " + left + ", " + right +  "\n";
		}
	}

	// TODO : other instructions
}
