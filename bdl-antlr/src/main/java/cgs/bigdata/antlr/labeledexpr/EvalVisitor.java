package cgs.bigdata.antlr.labeledexpr;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class EvalVisitor extends LabeledExprBaseVisitor<Integer> {

	// 模拟"内存", 存放变量名和变量值的对应关系
	Map<String, Integer> memory = new HashMap<>();

	/*
	    ID '=' expr NEWLINE
	 */
	@Override
	public Integer visitAssign (LabeledExprParser.AssignContext ctx) {
		String id = ctx.ID().getText();     // id 在 '=' 的左侧
		int value = visit(ctx.expr());          // 计算右侧表达式
		memory.put(id, value);                   // 将这个映射关系存入计算器 "内存" 中
		return value;
	}

	/*
		expr NEWLINE;
	 */
	@Override
	public Integer visitPrintExpr (LabeledExprParser.PrintExprContext ctx) {
		int value = visit(ctx.expr());      // 计算 expr 子节点的结果
		System.out.println(value);         // 打印结果
		return 0;                                    // 上面已经打印出结果, 因此这里返回一个假值即可
	}

	/*
		expr op=('+'|'-') expr
	 */
	@Override
	public Integer visitAddSub (LabeledExprParser.AddSubContext ctx) {
		int left = visit(ctx.expr(0));      // 计算子树左侧 expr
		int right = visit(ctx.expr(1));     // 计算子树右侧 expr
		// 计算子树中 op 是 加法 or 减法
		if (ctx.op.getType() == LabeledExprLexer.ADD) return left + right;
		else return left - right;
	}

	/*
		expr op=('*'|'/') expr
	 */
	@Override
	public Integer visitMulDiv (LabeledExprParser.MulDivContext ctx) {
		int left = visit(ctx.expr(0));      // 计算子树左侧 expr
		int right = visit(ctx.expr(1));     // 计算子树右侧 expr
		// 计算子树中 op 是 乘法 or 除法
		if (ctx.op.getType() == LabeledExprLexer.MUL) return left * right;
		else return left / right;
	}

	/*
		ID
	 */
	@Override
	public Integer visitId (LabeledExprParser.IdContext ctx) {
		String id = ctx.ID().getText();
		if (memory.containsKey(id)) return memory.get(id);
		return 0;
	}

	/*
		INT
	 */
	@Override
	public Integer visitInt (LabeledExprParser.IntContext ctx) {
		return Integer.valueOf(ctx.INT().getText());
	}

	/*
		'(' expr ')'
	 */
	@Override
	public Integer visitParents (LabeledExprParser.ParentsContext ctx) {
		return visit(ctx.expr());
	}

	/*
		CLEAR
	 */
	@Override
	public Integer visitClear (LabeledExprParser.ClearContext ctx) {
		memory.clear();
		return 0;
	}
}