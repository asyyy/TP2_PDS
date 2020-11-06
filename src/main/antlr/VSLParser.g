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
 	//p=listProto { $out = new TP2.ASD.Program($p.out,$f.out);}
	:f=listFonction EOF { $out = new TP2.ASD.Program($f.out);}
    ;

listFonction returns [List<TP2.ASD.Fonction> out]
	:{List<TP2.ASD.Fonction> lFonc = new ArrayList<>();}
	fun = fonction {lFonc.add($fun.out);} (fun2 = fonction {lFonc.add($fun2.out);})*
	{$out = lFonc;}
	;

fonction returns [TP2.ASD.Fonction out]
	:{List<String> lParam = new ArrayList<>();}
	 // INT
	 FUNC INT name = IDENT LP (id = IDENT {lParam.add($id.text);} (VIRG id2 = IDENT {lParam.add($id2.text);})* )? RP
	 b = bloc {$out = new TP2.ASD.IntFonction($name.text,lParam,$b.out);}
	 
	 // VOID
	 //| FUNC VOID name = IDENT LP (id = IDENT {lParam.add($id.text);} (VIRG id2 = IDENT {lParam.add($id2.text);})* )? RP
	 //b = bloc {$out = new ASD.VoidFonction($name.text,lParam,$b.out)}
	;
	
bloc returns [TP2.ASD.Bloc out]
	:v = listDeclaration i = listInstruction { $out = new TP2.ASD.Bloc($v.out,$i.out);}
	| ACCOL v = listDeclaration i = listInstruction ACCOR  { $out = new TP2.ASD.Bloc($v.out,$i.out);}
	;
	
listDeclaration returns [List<TP2.ASD.Variable> out]
	:{List<TP2.ASD.Variable> lDeclaration = new ArrayList<>();}
	decl = variable {lDeclaration.add($decl.out);} ( decl2 = variable {lDeclaration.add($decl2.out);} )* {$out = lDeclaration ;}
	;
	
variable returns [TP2.ASD.Variable out]
	: INT name = IDENT {$out = new TP2.ASD.IntVariable($name.text); } //INT
	//TABLO
	;
	
listInstruction returns [List<TP2.ASD.Instruction> out]
	:{List<TP2.ASD.Instruction> lInst = new ArrayList<>();}
	inst = instruction {lInst.add($inst.out);} ( inst2 = instruction {lInst.add($inst2.out);} )* {$out = lInst;}
	;
instruction returns [TP2.ASD.Instruction out]
	:name = IDENT AFFECT e = expression {$out = new TP2.ASD.Affectation($name.text,$e.out);}	//AFFECTATION SIMPLE
	//| IF THEN FI
	//| IF THEN ELSE FI
	//| WHILE DO DONE
	//| FONCTION exemple : x:= fact(y)
	;
	
expression returns [TP2.ASD.Expression out]
    : l=factor PLUS e=expression  { $out = new TP2.ASD.AddExpression($l.out, $e.out); }
    | l=factor MOINS e=expression  { $out = new TP2.ASD.SubExpression($l.out, $e.out); }
    | f=factor { $out = $f.out; }
    // TODO : that's all?
    ;

factor returns [TP2.ASD.Expression out]
    : l=primary MUL e=factor  { $out = new TP2.ASD.MulExpression($l.out, $e.out); }
    | l=primary DIV e=factor  { $out = new TP2.ASD.DivExpression($l.out, $e.out); } 
    | p=primary { $out = $p.out; }
    ;

primary returns [TP2.ASD.Expression out]
    : INTEGER { $out = new TP2.ASD.IntExpression($INTEGER.int); }
    | LP e=expression RP {$out = $e.out;}
    //| VARIABLE => IDEN
    //| TABLO IDENT LP e=expression RB 
    //| FONC?
    ;