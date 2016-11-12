package br.ufc.quimica.keyboard.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import br.ufc.quimica.keyboard.parser.CompoundParser.ElementContext;

/**
 * Evaluator for chemical formulas.
 * Uses the visitor pattern to visit AST nodes. Do the semantic analysis.
 * @author vladymirbezerra
 *
 */
public class Evaluator extends CompoundBaseVisitor<Integer>{

	public Evaluator() {}
	
	private ParseTree makeTree(String formula) {
		ANTLRInputStream input = new ANTLRInputStream(formula);
		CompoundLexer lexer = new CompoundLexer(input);
		lexer.removeErrorListeners();
		lexer.addErrorListener(new SyntaxErrorListener());
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CompoundParser parser = new CompoundParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(new SyntaxErrorListener());
		return parser.s();
	}
	public Integer eval(String formula) {
		ParseTree tree = this.makeTree(formula);
		return this.visit(tree);
	}
	@Override 
	public Integer visitS(CompoundParser.SContext ctx) { 
		if(null != ctx.compound()) {
			return visit(ctx.compound());
		} else if(null != ctx.hidr()) {
			return visit(ctx.hidr());
		} else {
			return 0;
		}
	}
	
	@Override 
	public Integer visitCompound(CompoundParser.CompoundContext ctx) { 
		int value = 0;
		if(ctx.compound() != null) {
			//System.out.println("compound not null");
			return visit(ctx.compound()) + visit(ctx.parens());
		} else if (ctx.parens() != null) {
			return visit(ctx.parens());
		} else {
			for (ElementContext element : ctx.element()) {
				if(null == element) break;	
				value += visit(element);
			}
			return value;
		}
	}
	
	@Override 
	public Integer visitHidr(CompoundParser.HidrContext ctx) { 
		return visit(ctx.compound(0)) + 
				(visit(ctx.compound(1)) * 
						Integer.valueOf(ctx.INT().get(0).getText()));
	}
	
	
	@Override 
	public Integer visitParens(CompoundParser.ParensContext ctx) { 
		return visit(ctx.compound()) * Integer.valueOf(ctx.INT().get(0).getText());
	}
	
	
	@Override 
	public Integer visitElement(CompoundParser.ElementContext ctx) { 
		LookUpTable table = LookUpTable.getTable();
		String name = ctx.ATOM().getText();
		if(null != ctx.INT()) {
			//System.out.println(name);
			return Integer.valueOf(ctx.INT().getText())
					* table.lookAtom(name);
		}
		return table.lookAtom(name); 
	}
}
