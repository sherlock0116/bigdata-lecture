package cgs.bigdata.antlr.hello;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 *
 */
public class HelloMain {

    private static final String correct = "hello world";
    private static final String wrong = "hello 007";


    public static void main(String[] args) {
        // ① 构建 Antlr 字节输入流
        ANTLRInputStream inputStream = new ANTLRInputStream(correct);
        // ② 构建词法分析器
        HelloLexer lexer = new HelloLexer(inputStream);
        // ③ 构建 Token 流装载词法分析的 Token 结果
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        // ④ 构建语法分析器
        HelloParser parser = new HelloParser(tokenStream);
        // ⑤ 规则入口
        HelloParser.RContext r = parser.r();
        // ⑥ 初始化监听器
        ListenerRewrite listener = new ListenerRewrite();
        // ⑦ 装配语法树分析
        ParseTreeWalker.DEFAULT.walk(listener, r);
    }
}
