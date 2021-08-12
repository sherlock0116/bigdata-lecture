package cgs.bigdata.antlr.arrayinit;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

/**
 *
 */
public class ArrayInitMain {

	public static void main(String[] args) throws IOException {

		ANTLRInputStream inputStream = new ANTLRInputStream(System.in);
		ArrayInitLexer lexer = new ArrayInitLexer(inputStream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		ArrayInitParser parser = new ArrayInitParser(tokenStream);
		ParseTree tree = parser.init();
		System.out.println(tree.toStringTree(parser));

	}
}
