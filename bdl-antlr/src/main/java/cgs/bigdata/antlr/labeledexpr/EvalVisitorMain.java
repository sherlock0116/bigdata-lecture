package cgs.bigdata.antlr.labeledexpr;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class EvalVisitorMain {

	public static void main (String[] args) throws IOException {

		// args: data/t.expr
		String inputFile = null;
		if (args.length > 0) {
			inputFile = args[0];
		}
		InputStream is = System.in;
		if (inputFile != null) {
			is = new FileInputStream(inputFile);
		}

		ANTLRInputStream ais = new ANTLRInputStream(is);
		LabeledExprLexer lexer = new LabeledExprLexer(ais);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		LabeledExprParser parser = new LabeledExprParser(tokens);
		ParseTree tree = parser.prog();

		EvalVisitor visitor = new EvalVisitor();
		visitor.visit(tree);
	}
}
