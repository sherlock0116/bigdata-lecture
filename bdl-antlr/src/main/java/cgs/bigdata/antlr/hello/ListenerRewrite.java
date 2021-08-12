package cgs.bigdata.antlr.hello;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
public class ListenerRewrite extends HelloBaseListener {

    @Override
    public void enterR(HelloParser.RContext ctx) {
        System.out.println("entering ......");
        String aCase = ctx.getChild(0).getText().toLowerCase();
        String bCase = ctx.getChild(1).getText().toLowerCase();
        System.out.printf("aCase: %s\nbCase: %s\n", aCase, bCase);
    }

    @Override
    public void exitR(HelloParser.RContext ctx) {
        System.out.println("exiting ......");
        String aCase = ctx.getChild(0).getText().toLowerCase();
        String bCase = ctx.getChild(1).getText().toLowerCase();
        System.out.printf("aCase: %s\nbCase: %s\n", aCase, bCase);
    }
}
