package trefoil2;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Interprets expressions and bindings in the context of a dynamic environment
 * according to the semantics of Trefoil v2.
 */
public class Interpreter {
    /**
     * Evaluates e in the given environment. Returns the resulting value.
     *
     * Throws TrefoilError.RuntimeError when the Trefoil programmer makes a mistake.
     */
    public static Expression interpretExpression(Expression e, DynamicEnvironment environment) {
        if (e instanceof Expression.IntegerLiteral) {
            return e;
        } else if (e instanceof Expression.VariableReference) {
            Expression.VariableReference var = (Expression.VariableReference) e;
            return environment.getVariable(var.getVarname());
        } else if (e instanceof Expression.Plus) {
            Expression.Plus p = (Expression.Plus) e;
            Expression v1 = interpretExpression(p.getLeft(), environment);
            Expression v2 = interpretExpression(p.getRight(), environment);
            if (v1 instanceof Expression.IntegerLiteral
                    && v2 instanceof Expression.IntegerLiteral) {
                return new Expression.IntegerLiteral(
                        ((Expression.IntegerLiteral) v1).getData() +
                                ((Expression.IntegerLiteral) v2).getData()
                );
            } else {
                throw new Trefoil2.TrefoilError.RuntimeError(
                        "+ can only concat two integers");
            }
            // TODO: implement semantics for new AST nodes here, following the examples above
        // TODO: be sure to check for run-time type errors and throw TrefoilError.RuntimeError.
        } else if (e instanceof Expression.BooleanLiteral) {
            return e;
        } else if (e instanceof Expression.Minus) {
            Expression.Minus m = (Expression.Minus) e;
            Expression v1 = interpretExpression(m.getLeft(), environment);
            Expression v2 = interpretExpression(m.getRight(), environment);
            if (v1 instanceof Expression.IntegerLiteral
                    && v2 instanceof Expression.IntegerLiteral) {
                return new Expression.IntegerLiteral(
                        ((Expression.IntegerLiteral) v1).getData() -
                                ((Expression.IntegerLiteral) v2).getData()
                );
            } else {
                throw new Trefoil2.TrefoilError.RuntimeError(
                        "- can only concat two integers");
            }
        } else if (e instanceof Expression.Times) {
            Expression.Times t = (Expression.Times) e;
            Expression v1 = interpretExpression(t.getLeft(), environment);
            Expression v2 = interpretExpression(t.getRight(), environment);
            if (v1 instanceof Expression.IntegerLiteral
                    && v2 instanceof Expression.IntegerLiteral) {
                return new Expression.IntegerLiteral(
                        ((Expression.IntegerLiteral) v1).getData() *
                                ((Expression.IntegerLiteral) v2).getData()
                );
            } else {
                throw new Trefoil2.TrefoilError.RuntimeError(
                        "* can only concat two integers");
            }
        } else if (e instanceof Expression.Equal) {
            Expression.Equal eq = (Expression.Equal) e;
            Expression v1 = interpretExpression(eq.getLeft(), environment);
            Expression v2 = interpretExpression(eq.getRight(), environment);
            if (v1 instanceof Expression.IntegerLiteral
                    && v2 instanceof Expression.IntegerLiteral) {
                return new Expression.BooleanLiteral(
                        ((Expression.IntegerLiteral) v1).getData() ==
                                ((Expression.IntegerLiteral) v2).getData()
                );
            } else {
                throw new Trefoil2.TrefoilError.RuntimeError(
                        "= can only concat two integers");
            }
        } else if (e instanceof Expression.If) {
            Expression.If i = (Expression.If) e;
            Expression condition = interpretExpression(i.getCondition(), environment);
            if (condition instanceof Expression.BooleanLiteral &&
                    !((Expression.BooleanLiteral) condition).isData()) {
                return interpretExpression(i.getRight(), environment);
            }
            return interpretExpression(i.getLeft(), environment);
        } else if (e instanceof Expression.Let) {
            Expression.Let l = (Expression.Let) e;
            String varname = ((Expression.VariableReference) l.getVar()).getVarname();
            Expression def = l.getDef();
            DynamicEnvironment newEnv = environment.extendVariable(varname, def);
            return interpretExpression(l.getBody(), newEnv);
        } else if (e instanceof Expression.Cons) {
            Expression.Cons c = (Expression.Cons) e;
            Expression e1 = interpretExpression(c.getLeft(), environment);
            Expression e2 = interpretExpression(c.getRight(), environment);
            return Expression.cons(e1, e2);
        } else if (e instanceof Expression.NilLiteral) {
            return Expression.nil();
        } else if (e instanceof Expression.IfNil) {
            Expression.IfNil in = (Expression.IfNil) e;
            Expression exp = interpretExpression(in.getExpression(), environment);
            return new Expression.BooleanLiteral(exp.equals(Expression.nil()));
        } else if (e instanceof Expression.IfCons) {
            Expression.IfCons ic = (Expression.IfCons) e;
            Expression exp = interpretExpression(ic.getExpression(), environment);
            if (exp instanceof Expression.Cons) {
                return new Expression.BooleanLiteral(true);
            } else {
                return new Expression.BooleanLiteral(false);
            }
        } else if (e instanceof Expression.Car) {
            Expression.Car c = (Expression.Car) e;
            Expression exp = interpretExpression(c.getExpression(), environment);
            if (exp instanceof Expression.Cons) {
                return ((Expression.Cons) exp).getLeft();
            } else {
                throw new Trefoil2.TrefoilError.RuntimeError("Expression not a cons");
            }
        } else if (e instanceof Expression.Cdr) {
            Expression.Cdr c = (Expression.Cdr) e;
            Expression exp = interpretExpression(c.getExpression(), environment);
            if (exp instanceof Expression.Cons) {
                return ((Expression.Cons) exp).getRight();
            } else {
                throw new Trefoil2.TrefoilError.RuntimeError("Expression not a cons");
            }
        } else if (e instanceof Expression.FuncCall) {
            Expression.FuncCall f = (Expression.FuncCall) e;
            String funcname = f.getFuncname();
            if (!environment.containsFunction(funcname)) {
                throw new Trefoil2.TrefoilError.RuntimeError("function not found");
            }
            if (environment.getFunction(funcname).getFunctionBinding().getArgnames().size() != f.getArgs().size()) {
                throw new Trefoil2.TrefoilError.RuntimeError("Args have different length");
            }
            List<Expression> vals = new LinkedList<>();
            for (int i = 0; i < f.getArgs().size(); i++) {
                vals.add(interpretExpression(f.getArgs().get(i), environment));
            }
            List<String> argnames =
                    environment.getFunction(funcname).getFunctionBinding().getArgnames();
            DynamicEnvironment newEnv =
                    new DynamicEnvironment(environment.getFunction(funcname).getDefiningEnvironment());
            for (int i = 0; i < vals.size(); i++) {
                newEnv = newEnv.extendVariable(argnames.get(i), vals.get(i));
            }
            return interpretExpression(environment.getFunction(funcname).getFunctionBinding().getBody(), newEnv);
        } else {
            // Otherwise it's an expression AST node we don't recognize. Tell the interpreter implementor.
            throw new Trefoil2.InternalInterpreterError("\"impossible\" expression AST node " + e.getClass());
        }
    }

    /**
     * Executes the binding in the given environment, returning the new environment.
     *
     * The environment passed in as an argument is *not* mutated. Instead, it is copied
     * and any modifications are made on the copy and returned.
     *
     * Throws TrefoilError.RuntimeError when the Trefoil programmer makes a mistake.
     */
    public static DynamicEnvironment interpretBinding(Binding b, DynamicEnvironment environment) {
        if (b instanceof Binding.VariableBinding) {
            Binding.VariableBinding vb = (Binding.VariableBinding) b;
            Expression value = interpretExpression(vb.getVardef(), environment);
            System.out.println(vb.getVarname() + " = " + value);
            return environment.extendVariable(vb.getVarname(), value);
        } else if (b instanceof Binding.TopLevelExpression) {
            Binding.TopLevelExpression tle = (Binding.TopLevelExpression) b;
            System.out.println(interpretExpression(tle.getExpression(), environment));
            return environment;
        } else if (b instanceof Binding.FunctionBinding) {
            Binding.FunctionBinding fb = (Binding.FunctionBinding) b;
            DynamicEnvironment newEnvironment = environment.extendFunction(fb.getFunname(), fb);
            System.out.println(fb.getFunname() + " is defined");
            return newEnvironment;
        } else if (b instanceof Binding.TestBinding) {
            Binding.TestBinding tb = (Binding.TestBinding) b;
            interpretExpression(tb.getExpression(), environment);
            return environment;
        }
        // TODO: implement the TestBinding here


        // Otherwise it's a binding AST node we don't recognize. Tell the interpreter implementor.
        throw new Trefoil2.InternalInterpreterError("\"impossible\" binding AST node " + b.getClass());
    }


    // Convenience methods for interpreting in the empty environment.
    // Used for testing.
    public static Expression interpretExpression(Expression e) {
        return interpretExpression(e, new DynamicEnvironment());
    }
    public static DynamicEnvironment interpretBinding(Binding b) {
        return interpretBinding(b, DynamicEnvironment.empty());
    }


    /**
     * Represents the dynamic environment, which is a mapping from strings to "entries".
     * In the starter code, the string always represents a variable name and an entry is always a VariableEntry.
     * You will extend it to also support function names and FunctionEntries.
     */
    @Data
    public static class DynamicEnvironment {
        public static abstract class Entry {
            @EqualsAndHashCode(callSuper = false)
            @Data
            public static class VariableEntry extends Entry {
                private final Expression value;
            }

            @EqualsAndHashCode(callSuper = false)
            @Data
            public static class FunctionEntry extends Entry {
                private final Binding.FunctionBinding functionBinding;

                @ToString.Exclude
                private final DynamicEnvironment definingEnvironment;
            }

            // Convenience factory methods

            public static Entry variable(Expression value) {
                return new VariableEntry(value);
            }
            public static Entry function(Binding.FunctionBinding functionBinding, DynamicEnvironment definingEnvironment) {
                return new FunctionEntry(functionBinding, definingEnvironment);
            }
        }

        // The backing map of this dynamic environment.
        private final Map<String, Entry> map;

        public DynamicEnvironment() {
            this.map = new HashMap<>();
        }

        public DynamicEnvironment(DynamicEnvironment other) {
            this.map = new HashMap<>(other.getMap());
        }

        private boolean containsVariable(String varname) {
            return map.containsKey(varname) && map.get(varname) instanceof Entry.VariableEntry;
        }

        public Expression getVariable(String varname) {
            if (!containsVariable(varname)) {
                throw new Trefoil2.TrefoilError.RuntimeError("The variable is not bound");
            }
            return ((Entry.VariableEntry) map.get(varname)).getValue();
        }

        public void putVariable(String varname, Expression value) {
            map.put(varname, new Entry.VariableEntry(value));
        }

        /**
         * Returns a *new* DynamicEnvironment extended by the binding varname -> value.
         *
         * Does not change this! Creates a copy.
         */
        public DynamicEnvironment extendVariable(String varname, Expression value) {
            DynamicEnvironment newEnv = new DynamicEnvironment(this);  // create a copy
            newEnv.putVariable(varname, value);  // mutate the copy
            return newEnv;  // return the mutated copy (this remains unchanged!)
        }

        /**
         * Returns a *new* Dynamic environment extended by the given mappings.
         *
         * Does not change this! Creates a copy.
         *
         * varnames and values must have the same length
         *
         * @param varnames variable names to bind
         * @param values values to bind the variables to
         */
        public DynamicEnvironment extendVariables(List<String> varnames, List<Expression> values) {
            DynamicEnvironment newEnv = new DynamicEnvironment(this);
            assert varnames.size() == values.size();
            for (int i = 0; i < varnames.size(); i++) {
                newEnv.putVariable(varnames.get(i), values.get(i));
            }
            return newEnv;
        }

        private boolean containsFunction(String funname) {
            return map.containsKey(funname) && map.get(funname) instanceof Entry.FunctionEntry;
        }

        public Entry.FunctionEntry getFunction(String funname) {
            if (!containsFunction(funname)) {
                throw new Trefoil2.TrefoilError.RuntimeError("Function not found");
            }
            return (Entry.FunctionEntry) map.get(funname);
        }

        public void putFunction(String funname, Binding.FunctionBinding functionBinding) {
            map.put(funname, Entry.function(functionBinding, this));
            // TODO: bind the function in the backing map
            // Be careful to set up recursion correctly!
            // Hint: Pass definingEnvironment=this to the Entry.function factory, and then call map.put.
            //       That way, by the time Trefoil calls the function, everything points to
            //       the right place. Tricky!
        }

        public DynamicEnvironment extendFunction(String funname, Binding.FunctionBinding functionBinding) {
            DynamicEnvironment newEnv = new DynamicEnvironment(this);  // create a copy of this
            newEnv.putFunction(funname, functionBinding);  // mutate the copy
            return newEnv;  // return the copy
        }

        // Convenience factory methods

        public static DynamicEnvironment empty() {
            return new DynamicEnvironment();
        }

        public static DynamicEnvironment singleton(String varname, Expression value) {
            return empty().extendVariable(varname, value);
        }
    }
}
