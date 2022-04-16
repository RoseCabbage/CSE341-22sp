package trefoil2;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedList;
import java.util.List;

/**
 * An expression AST. See LANGUAGE.md for a list of possibilities.
 */
@Data
public abstract class Expression {
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class IntegerLiteral extends Expression {
        private final int data;

        @Override
        public String toString() {
            return Integer.toString(data);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class BooleanLiteral extends Expression {
        private final boolean data;

        @Override
        public String toString() {
            return Boolean.toString(data);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class VariableReference extends Expression {
        private final String varname;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Plus extends Expression {
        private final Expression left, right;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Minus extends Expression {
        private final Expression left, right;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Times extends Expression {
        private final Expression left, right;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Equal extends Expression {
        private final Expression left, right;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class If extends Expression {
        private final Expression condition, left, right;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Let extends Expression {
        private final Expression var, def, body;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Cons extends Expression {
        private final Expression left, right;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class NilLiteral extends Expression {
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class IfNil extends Expression {
        private final Expression expression;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class IfCons extends Expression {
        private final Expression expression;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Car extends Expression {
        private final Expression expression;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Cdr extends Expression {
        private final Expression expression;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class FuncCall extends Expression {
        private final String funcname;
        private final List<Expression> args;
    }
    // TODO: Your new AST classes here.
    // TODO: Don't forget to copy the @Equals... and @Data annotations onto all your classes.


    // Convenience factory methods
    public static IntegerLiteral ofInt(int x) {
        return new IntegerLiteral(x);
    }
    public static BooleanLiteral ofBoolean(boolean b) {
        return new BooleanLiteral(b);
    }
    public static Expression nil() {
        try {
            return new NilLiteral();
        } catch (Trefoil2.InternalInterpreterError e) {
            throw new Trefoil2.InternalInterpreterError("Nil not implemented");
        }
    }
    public static Expression cons(Expression e1, Expression e2) {
        try {
            return new Cons(e1, e2);
        } catch (Trefoil2.InternalInterpreterError e) {
            throw new Trefoil2.InternalInterpreterError("Cons not implemented");
        }
    }

    /**
     * Tries to convert a PST to an Expression.
     *
     * See LANGUAGE.md for a description of how this should work at a high level.
     *
     * If conversion fails, throws TrefoilError.AbstractSyntaxError with a nice message.
     */
    public static Expression parsePST(ParenthesizedSymbolTree pst) {
        // Either the PST is a Symbol or a Node
        if (pst instanceof ParenthesizedSymbolTree.Symbol) {
            // If it is a symbol, it is either a number, a symbol keyword, or a variable reference.
            ParenthesizedSymbolTree.Symbol symbol = (ParenthesizedSymbolTree.Symbol) pst;
            String s = symbol.getSymbol();
            try {
                return Expression.ofInt(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                switch (s) {
                    case "true":
                        return new BooleanLiteral(true);
                    case "false":
                        return new BooleanLiteral(false);
                    case "nil":
                        return nil();
                    // TODO: add symbol keywords here, following the example above


                    // if the symbol is not a symbol keyword, then it represents a variable reference
                    default:
                        return new VariableReference(s);
                }
            }
        } else {
            // Otherwise it is a Node, in which case it might be a built-in form with a node keyword,
            // or if not, then it is a function call.
            ParenthesizedSymbolTree.Node n = (ParenthesizedSymbolTree.Node) pst;
            List<ParenthesizedSymbolTree> children = n.getChildren();
            if (children.size() == 0) {
                throw new Trefoil2.TrefoilError.AbstractSyntaxError("Unexpected empty pair of parentheses.");
            }
            String head = ((ParenthesizedSymbolTree.Symbol) children.get(0)).getSymbol();
            switch (head) {
                case "+":
                    if (children.size() - 1 /* -1 for head */ != 2) {
                        throw new Trefoil2.TrefoilError.AbstractSyntaxError("Operator " + head + " expects 2 arguments");
                    }
                    return new Plus(parsePST(children.get(1)), parsePST(children.get(2)));
                case "-":
                    if (children.size() - 1 != 2) {
                        throw new Trefoil2.TrefoilError.AbstractSyntaxError("Operator " + head + " expects 2 arguments");
                    }
                    return new Minus(parsePST(children.get(1)), parsePST(children.get(2)));
                case "*":
                    if (children.size() - 1 != 2) {
                        throw new Trefoil2.TrefoilError.AbstractSyntaxError("Operator " + head + " expects 2 arguments");
                    }
                    return new Times(parsePST(children.get(1)), parsePST(children.get(2)));
                case "=":
                    if (children.size() - 1 != 2) {
                        throw new Trefoil2.TrefoilError.AbstractSyntaxError("Operator " + head + " expects 2 arguments");
                    }
                    return new Equal(parsePST(children.get(1)), parsePST(children.get(2)));
                case "if":
                    if (children.size() - 1 != 3) {
                        throw new Trefoil2.TrefoilError.AbstractSyntaxError("Operator " + head +
                                " expects 3 arguments");
                    }
                    return new If(parsePST(children.get(1)), parsePST(children.get(2)),
                            parsePST(children.get(3)));
                case "let":
                    if (children.size() - 1 != 2) {
                        throw new Trefoil2.TrefoilError.AbstractSyntaxError("Operator " + head +
                                " expects 2 arguments");
                    }
                    ParenthesizedSymbolTree.Node node = (ParenthesizedSymbolTree.Node) children.get(1);
                    List<ParenthesizedSymbolTree> childs = node.getChildren();
                    ParenthesizedSymbolTree.Node newnode =
                            (ParenthesizedSymbolTree.Node) childs.get(0);
                    childs = newnode.getChildren();
//                    ParenthesizedSymbolTree var = (ParenthesizedSymbolTree) childs.get(0);
                    return new Let(parsePST((ParenthesizedSymbolTree.Symbol) childs.get(0)),
                            parsePST(childs.get(1)), parsePST(children.get(2)));
//                    return new Let(parsePST(children.get(1)), parsePST(children.get(2)));
                case "cons":
                    if (children.size() - 1 != 2) {
                        throw new Trefoil2.TrefoilError.AbstractSyntaxError("Operator " + head +
                                " expects 2 arguments");
                    }
                    return cons(parsePST(children.get(1)), parsePST(children.get(2)));
                case "nil?":
                    if (children.size() - 1 != 1) {
                        throw new Trefoil2.TrefoilError.AbstractSyntaxError("Operator " + head +
                                " expects 1 arguments");
                    }
                    return new IfNil(parsePST(children.get(1)));
                case "cons?":
                    if (children.size() - 1 != 1) {
                        throw new Trefoil2.TrefoilError.AbstractSyntaxError("Operator " + head +
                                " expects 1 arguments");
                    }
                    return new IfCons(parsePST(children.get(1)));
                case "car":
                    if (children.size() - 1 != 1) {
                        throw new Trefoil2.TrefoilError.AbstractSyntaxError("Operator " + head +
                                " expects 1 arguments");
                    }
                    return new Car(parsePST(children.get(1)));
                case "cdr":
                    if (children.size() - 1 != 1) {
                        throw new Trefoil2.TrefoilError.AbstractSyntaxError("Operator " + head +
                                " expects 1 arguments");
                    }
                    return new Cdr(parsePST(children.get(1)));
                // if the symbol is not a node keyword, then it represents a function call
                default:
                    String funcname =
                            ((ParenthesizedSymbolTree.Symbol) children.get(0)).getSymbol();
                    List<Expression> args = new LinkedList<>();
                    for (int i = 1; i < children.size(); i++) {
                        args.add(parsePST(children.get(i)));
                    }
                    return new FuncCall(funcname, args);
                    // eventually we will add function calls here
//                    throw new Trefoil2.TrefoilError.AbstractSyntaxError("Unrecognized operator " + head);

            }
        }
    }

    // Convenience factory method for unit tests.
    public static Expression parseString(String s) {
        return parsePST(ParenthesizedSymbolTree.parseString(s));
    }
}
