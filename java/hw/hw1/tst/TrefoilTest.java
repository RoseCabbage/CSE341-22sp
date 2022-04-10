import org.junit.Test;

import static junit.framework.TestCase.*;

public class TrefoilTest {
    @Test
    public void interpretOne() {
        Trefoil trefoil = new Trefoil();
        trefoil.interpret("1");
        assertEquals(1, trefoil.pop());
    }

    @Test
    public void interpretAdd() {
        Trefoil trefoil = new Trefoil();
        trefoil.interpret("1 2 +");
        assertEquals(3, trefoil.pop());
    }

    @Test
    public void interpretSubtract() {
        Trefoil trefoil = new Trefoil();
        trefoil.interpret("10 3 -");
        assertEquals(7, trefoil.pop());
    }

    @Test
    public void interpretMultiply() {
        Trefoil trefoil = new Trefoil();
        trefoil.interpret("2 3 *");
        assertEquals(6, trefoil.pop());
    }

    @Test
    public void testPeriod() {
        Trefoil trefoil = new Trefoil();
        trefoil.interpret("2 3 4 .");
        assertEquals(3, trefoil.pop());
    }

    @Test
    public void interpretAddSplit() {
        Trefoil trefoil = new Trefoil();
        trefoil.interpret("1 2");
        // the interpreter should track the stack across multiple calls to interpret()
        trefoil.interpret("+");
        assertEquals(3, trefoil.pop());
    }

    @Test
    public void toString_() {
        Trefoil trefoil = new Trefoil();
        trefoil.interpret("1 2 3");
        assertEquals("1 2 3", trefoil.toString());
    }

    @Test
    public void testComment() {
        Trefoil trefoil = new Trefoil();
        trefoil.interpret("1 2 3 ;comment!");
        assertEquals("1 2 3", trefoil.toString());
    }


    @Test(expected = Trefoil.TrefoilError.class)
    public void stackUnderflow() {
        Trefoil trefoil = new Trefoil();
        trefoil.interpret("1 +");
    }

    @Test(expected = Trefoil.TrefoilError.class)
    public void testStackError1() {
        Trefoil trefoil = new Trefoil();
        trefoil.interpret("1 *");
    }

    @Test(expected = Trefoil.TrefoilError.class)
    public void testStackError2() {
        Trefoil trefoil = new Trefoil();
        trefoil.interpret("1 -");
    }

    @Test(expected = Trefoil.TrefoilError.class)
    public void testStackError3() {
        Trefoil trefoil = new Trefoil();
        trefoil.interpret(".");
    }

    @Test(expected = Trefoil.TrefoilError.class)
    public void testUnknownToken() {
        Trefoil trefoil = new Trefoil();
        trefoil.interpret("2 3 /");
    }
}