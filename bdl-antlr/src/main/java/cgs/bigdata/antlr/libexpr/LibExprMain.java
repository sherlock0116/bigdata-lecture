package cgs.bigdata.antlr.libexpr;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class LibExprMain {

// args: rules/t.expr
    public static void main(String[] args) throws IOException {

        String inputFile = null;
        if (args.length > 0) {
            inputFile = args[0];
        }
        InputStream is = System.in;
        if (inputFile != null) {
            is = new FileInputStream(inputFile);
        }

        ANTLRInputStream ais = new ANTLRInputStream(is);
        LibExprLexer lexer = new LibExprLexer(ais);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        LibExprParser parser = new LibExprParser(tokenStream);

        ParseTree tree = parser.prog();
        System.out.println(tree.toStringTree(parser));
    }
}
