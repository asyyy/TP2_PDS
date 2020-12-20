package TP2.ASD;

import TP2.Llvm;

public class Array extends Type {
	
	private int size;
	/**
	 * Constructeur de Array
	 * @param type Type du tableau
	 * @param size taille du table
	 */
	public Array(int size) {

		this.size = size;
	}
	public Array() {
		
	}
	
	private int getSize() {
		return size;
	}
	
	@Override
	public String pp() {		
		return "[]";
	}
	public boolean equals(Object obj) {
      
        return obj instanceof Array;
    }
	
	@Override
	public Llvm.Type toLlvmType() {
		
		return new Llvm.Array(size);
	}

}
