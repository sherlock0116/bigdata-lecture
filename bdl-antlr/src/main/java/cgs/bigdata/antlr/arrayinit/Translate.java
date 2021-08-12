package cgs.bigdata.antlr.arrayinit;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
public class Translate {


    public static void main(String[] args) throws IOException {

        ANTLRInputStream inputStream = new ANTLRInputStream(System.in);
        ArrayInitLexer lexer = new ArrayInitLexer(inputStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ArrayInitParser parser = new ArrayInitParser(tokenStream);
        ArrayInitParser.InitContext init = parser.init();

        ParseTreeWalker.DEFAULT.walk(new ShortToUnitListener(), init);
        System.out.println();
    }
}

/*
{23, 45, 56}
^D
"\u0017\u002d\u0038"
 */