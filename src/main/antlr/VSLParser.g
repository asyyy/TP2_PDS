parser grammar VSLParser;

options {
  language = Java;
  tokenVocab = VSLLexer;
}

@header {
  package TP2;
  import java.util.List;
  import java.util.stream.Collectors;
  import java.util.Arrays;
}


program returns [TP2.ASD.Program out]
 	
	: p=listProto f=listFonction EOF { $out = new TP2.ASD.Program($p.out,$f.out);}
    ;
    
listProto returns [List<TP2.ASD.Prototype> out]
	:{List<TP2.ASD.Prototype> lProto = new ArrayList<>();}
	pro = prototype {lProto.add($pro.out);} (pro2 = prototype {lProto.add($pro2.out);})*
	{$out = lProto;}
	;

prototype returns [TP2.ASD.Prototype out]
	:{List<TP2.ASD.Pair> lParam = new ArrayList<>();}
	 // INT
	 PROTO INT name = IDENT LP (lp = listPair)? RP
	 	{$out = new TP2.ASD.IntPrototype($name.text,$lp.out);}
	 
	 // INT
	 |PROTO VOID name = IDENT LP (lp = listPair)? RP
	 	{$out = new TP2.ASD.VoidPrototype($name.text,$lp.out);}
	;

listFonction returns [List<TP2.ASD.Fonction> out]
	:{List<TP2.ASD.Fonction> lFonc = new ArrayList<>();}
	fun = fonction {lFonc.add($fun.out);} (fun2 = fonction {lFonc.add($fun2.out);})*
	{$out = lFonc;}
	;

fonction returns [TP2.ASD.Fonction out]
	:{List<String> lParam = new ArrayList<>();}
	 // INT
	 FUNC INT name = IDENT LP (lp = listPair)? RP
	 b = bloc {$out = new TP2.ASD.IntFonction($name.text,$lp.out,$b.out);}
	 
	 // VOID
	 | FUNC VOID name = IDENT LP (lp = listPair)? RP
	 b = bloc {$out = new TP2.ASD.VoidFonction($name.text,$lp.out,$b.out);}
	;
listPair returns [List<TP2.ASD.Pair> out]
	:{List<TP2.ASD.Pair> lPair = new ArrayList<>();}
	p = pair {lPair.add($p.out);} ( VIRG p2 = pair {lPair.add($p2.out);})*
	{$out = lPair;}
	;
pair returns [TP2.ASD.Pair out]
	:id = IDENT {$out = new TP2.ASD.Pair($id.text,new TP2.Llvm.Int());}
	|id = IDENT LC RC {$out = new TP2.ASD.Pair($id.text,new TP2.Llvm.Array());}
	|id = IDENT LC inte=INTEGER RC {$out = new TP2.ASD.Pair($id.text,new TP2.Llvm.Array($inte.int));}
	;	
bloc returns [TP2.ASD.Bloc out]
	:v = listDeclaration i = listInstruction { $out = new TP2.ASD.Bloc($v.out,$i.out);}
	| ACCOL v = listDeclaration i = listInstruction ACCOR  { $out = new TP2.ASD.Bloc($v.out,$i.out);}
	;
	
listDeclaration returns [List<TP2.ASD.Variable> out]
	:{List<TP2.ASD.Variable> lDeclaration = new ArrayList<>();}
	INT decl = variable {lDeclaration.add($decl.out);} (VIRG decl2 = variable {lDeclaration.add($decl2.out);} )* {$out = lDeclaration ;}
	;
	
variable returns [TP2.ASD.Variable out]
	: name = IDENT {$out = new TP2.ASD.IntVariable($name.text); } //INT
	| name = IDENT LC inte = INTEGER RC  {$out = new TP2.ASD.ArrayVariable($name.text,$inte.int); }
	;
	
listInstruction returns [List<TP2.ASD.Instruction> out]
	:{List<TP2.ASD.Instruction> lInst = new ArrayList<>();}
	inst = instruction {lInst.add($inst.out);} ( inst2 = instruction {lInst.add($inst2.out);} )* {$out = lInst;}
	;
instruction returns [TP2.ASD.Instruction out]
	:name = IDENT AFFECT e = expression {$out = new TP2.ASD.Affectation($name.text,$e.out);}					//AFFECTATION SIMPLE
	| IF e = expression THEN b=bloc FI  { $out = new TP2.ASD.IfInstruction($e.out, $b.out, null); }				//IF THEN
	| IF e = expression THEN b=bloc ELSE b2 = bloc FI  { $out = new TP2.ASD.IfInstruction($e.out, $b.out, $b2.out); } 	//IF THEN ELSE
	| WHILE e=expression DO b=bloc DONE { $out = new TP2.ASD.WhileInstruction($e.out, $b.out); } 					// WHILE 
	| RETURN e = expression { $out = new TP2.ASD.ReturnInstruction($e.out);}										//Return
	| name = IDENT LC e=expression RC AFFECT e2=expression { $out = new TP2.ASD.AffectArray($name.text, $e2.out, $e.out); } 	//AFFECATION ARRAY
	| PRINT li = listExpression {$out = new TP2.ASD.PrintInstruction($li.out)} // PRINT
	| READ lv = listDeclaration {$out = new TP2.ASD.ReadInstruction($lv.out)} // READ
	;
	
listExpression returns [List<TP2.ASD.Expression> out]
	: {List<TP2.ASD.Expression> lExp = new ArrayList<>();}
		e = expression {lExp.add($e.out);} ( VIRG e2 = expression {lExp.add($e.out);})*
		{$out = lExp;}
	;
expression returns [TP2.ASD.Expression out]
    : l=factor PLUS e=expression  { $out = new TP2.ASD.AddExpression($l.out, $e.out); }
    | l=factor MOINS e=expression  { $out = new TP2.ASD.SubExpression($l.out, $e.out); }
    | f=factor { $out = $f.out; }
    ;

factor returns [TP2.ASD.Expression out]
    : l=primary MUL e=factor  { $out = new TP2.ASD.MulExpression($l.out, $e.out); }
    | l=primary DIV e=factor  { $out = new TP2.ASD.DivExpression($l.out, $e.out); } 
    | p=primary { $out = $p.out; }
    ;

primary returns [TP2.ASD.Expression out]
    : inte = INTEGER { $out = new TP2.ASD.IntExpression($inte.int); }														//Constante
    | LP e=expression RP {$out = $e.out;}																				//Expression entre paranthèse			
    | name = IDENT {$out = new TP2.ASD.VarExpression($name.text);}																//Variable
    | { List<TP2.ASD.Expression> lArg = new ArrayList<TP2.ASD.Expression>(); } 
      name = IDENT LP (e = expression {lArg.add($e.out);} 																//Fonction dans une expression
    		(VIRG  e2 = expression {lArg.add($e2.out);})* )? RP {$out = new TP2.ASD.FonctionExpression($name.text,lArg);}
    | name = IDENT LC e=expression RC { $out = new TP2.ASD.ArrayExpression($name.text, $e.out); }						//ARRAY Expression						
    | t = TEXT {$out = new TP2.ASD.TextExpression($t.text)}																//Text pour le PRINTinstruction
    ;
    