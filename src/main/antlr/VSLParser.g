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


// TODO : other rules

program returns [TP2.ASD.Program out]
 
	:p=listProto f=listFonction EOF { $out = new TP2.ASD.Program($p.out,$f.out);}
    ;

listFonction returns [List<Fonction> out]
	:{List<Fonction> lFonc = new ArrayList<>();}
	fun = fonction {lFonc.add($fun.out);} (fun2 = fonction  {{lFonc.add($fun2.out);}*
	{$out = lFonc;};

fonction returns [TP2.ASD.Fonction out]
	:{List<String> lParam = new ArrayList<>();}
	 FUNC INT name = IDENT LP (id = IDENT {$lParam.add($id.text);} (VIRG id2 = IDENT {$lParam.add($id2.text);})* )? RP
	 b = bloc {$out = new ASD.IntFonction($name.text,$lParam.out,$b.out)}
	 | FUNC VOID name = IDENT LP (id = IDENT {$lParam.add($id.text);} (VIRG id2 = IDENT {$lParam.add($id2.text);})* )? RP
	 b = bloc {$out = new ASD.VoidFonction($name.text,$lParam.out,$b.out)}
	;
	
bloc returns [TP2.ADS.Bloc out]
	:v = listDeclaration i = listInstruction { $out = new TP2.ASD.Bloc($v.out,$i.out);}
	| ACCOL v = listDeclaration i = listInstruction ACCOR  { $out = new TP2.ASD.Bloc($v.out,$i.out);}
	;
	
listInstruction returns [List<Instruction> out]
	:{List<Instruction> lInst = new ArrayList<>();}
	inst = instruction {lInst.add($inst.out);} ( inst2 = instruction {lInst.add($inst2.out);} )* {$out = lInst}
	;
instruction returns [TP2.ASD.Instruction out]
	:name = IDENT AFFECT e = expression {$out = new TP2.ASD.Affection($name.text,$e.out);}	//AFFECTATION
	| IF THEN FI
	| IF THEN ELSE FI
	| WHILE DO DONE
	| FONCTION  // exemple : x:= fact(y)
	;
	
	
expression returns [TP2.ASD.Expression out]
    : l=factor PLUS e=expression  { $out = new TP2.ASD.AddExpression($l.out, $e.out); }
    | l=factor MOINS e=expression  { $out = new TP2.ASD.SubExpression($l.out, $e.out); }
    | f=factor { $out = $f.out; }
    // TODO : that's all?
    ;

factor returns [TP2.ASD.Expression out]
    : p=primary { $out = $p.out; }
    | l=factor MUL e=expression  { $out = new TP2.ASD.MulExpression($l.out, $e.out); }
    | l=factor DIV e=expression  { $out = new TP2.ASD.DivExpression($l.out, $e.out); }
    | LP e=expression RP {$out = $e.out;}
    ;

primary returns [TP2.ASD.Expression out]
    : INTEGER { $out = new TP2.ASD.IntegerExpression($INTEGER.int); }
    | VARIABLE
    | TABLO?
    
    ;