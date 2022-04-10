import java.io.*;
import java.util.Scanner;
import java.util.Stack;

/**
 * Interpreter for the Trefoil v1 language.
 *
 * The interpreter's state is a stack of integers.
 *
 * The interpreter also implements a main method to accept input from the keyboard or a file.a
 */
public class Trefoil {
    private Stack<Integer> stack;

    public Trefoil() {
        stack = new Stack<>();
    }

    public static void main(String[] args) {
        Trefoil trefoil = new Trefoil();

        if (args.length == 0) {
            try {
                trefoil.interpret(new Scanner(System.in));
            } catch (TrefoilError e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        } else if (args.length == 1) {
            try {
                trefoil.interpret(new Scanner(new File(args[0])));
            } catch (FileNotFoundException e) {
                System.err.println("File is not found");
                System.exit(1);
            } catch (TrefoilError e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        } else {
            System.err.println("Expected 0 or 1 arguments but got " + args.length);
            System.exit(1);
        }

        // print the stack
        System.out.println(trefoil);
    }

    /**
     * Interpret the program given by the scanner.
     */
    public void interpret(Scanner scanner) {
        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                stack.push(scanner.nextInt());
            } else {
                String token = scanner.next();
                if (token.equals("+")) {
                    checkTwoElements("+");
                    stack.push(pop() + pop());
                } else if (token.equals("-")) {
                    checkTwoElements("-");
                    stack.push(-pop() + pop());
                } else if (token.equals("*")) {
                    checkTwoElements("*");
                    stack.push(pop() * pop());
                } else if (token.equals(".")) {
                    checkOneElement();
                    System.out.println(stack.peek());
                    pop();
                } else if (!token.startsWith(";")) {
                    throw new TrefoilError(token + " is an unknown token");
                }
            }
        }
    }

    private void checkTwoElements(String token) {
        if (stack.size() < 2) {
            throw new TrefoilError(token + " requires at least two elements " +
                                   "but only " + stack.size() + " in Trefoil");
        }
    }

    private void checkOneElement() {
        if (stack.isEmpty()) {
            throw new TrefoilError("There is no element in Trefoil");
        }
    }

    /**
     * Convenience method to interpret the given string. Useful for unit tests.
     */
    public void interpret(String input) {
        // Don't change this method unless you know what you're doing.
        interpret(new Scanner(input));
    }

    /**
     * Pop a value off the stack and return it. Useful for unit tests.
     *
     * @throws TrefoilError if there are no elements on the stack.
     */
    public int pop() {
        if (stack.isEmpty()) {
            throw new TrefoilError("No element on the stack");
        }
        return stack.pop();
    }

    @Override
    public String toString() {
        Stack<Integer> tmp = new Stack<>();
        String ans = "";
        while (!stack.isEmpty()) {
            tmp.push(stack.pop());
        }
        while (!tmp.isEmpty()) {
            int num = tmp.pop();
            ans += num + " ";
            stack.push(num);
        }
        return ans.substring(0, ans.length() - 1);
    }

    /**
     * Throw this error whenever your interpreter detects a problem.
     */
    public static class TrefoilError extends RuntimeException {
        public TrefoilError(String message) {
            super(message);
        }
    }
}
