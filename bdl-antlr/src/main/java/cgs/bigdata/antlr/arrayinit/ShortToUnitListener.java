package cgs.bigdata.antlr.arrayinit;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
public class ShortToUnitListener extends ArrayInitBaseListener {

    /**
     * 当进入规则的时候, 将 { 翻译为 "
     * @param ctx
     */
    @Override
    public void enterInit(ArrayInitParser.InitContext ctx) {
        System.out.print("\"");
    }

    /**
     * 离开规则的时候, 将 } 翻译为 "
     * @param ctx
     */
    @Override
    public void exitInit(ArrayInitParser.InitContext ctx) {
        System.out.print("\"");
    }

    /**
     * 将每个整数翻译为四位十六进制形式吗然后加上前缀 "\\u"
     * @param ctx
     */
    @Override
    public void enterValue(ArrayInitParser.ValueContext ctx) {
        Integer value = Integer.valueOf(ctx.INT().getText());
        System.out.printf("\\u%04x", value);
    }

}
