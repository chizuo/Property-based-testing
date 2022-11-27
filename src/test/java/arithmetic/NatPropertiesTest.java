package arithmetic;

import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import static org.junit.Assume.assumeTrue;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

@RunWith(JUnitQuickcheck.class)
public class NatPropertiesTest {
    // a == a
    @Property
    public void numberEqualsSelf(@From(NatGenerator.class) @Size(max = 10) final Nat a) {
        assertEquals(a, a);
    }

    // Inequalities - the relationship is stable under addition and multiplication:
    // For all a,b,c that are elements of the set of natural numbers,
    // if a <= b then: a + c <= b + c && a * c <= b * c
    @Property
    public void PeanoInequalities(@From(NatGenerator.class) @Size(max = 20) final Nat c) {
        Nat a = PeanoNaturalExpression(21);
        Nat b = PeanoNaturalExpression(22);
        assertTrue(a.add(c).lessThan(b.add(c)));
        if (c.isZero())
            assertEquals(a.multiply(c), b.multiply(c));
        else
            assertTrue(a.multiply(c).lessThan(b.multiply(c)));
    }

    // Axiom: No negative values for Peano arithmetic because Peano axioms are for
    // elements in the set of all natural numbers.
    // assume true that a > 0 && b > 0: if a > b then b - a = 0 else b > a then a -
    // b = 0
    @Property
    public void nothingLessThanZero(@From(NatGenerator.class) @Size(max = 20) final Nat a,
            @From(NatGenerator.class) @Size(max = 20) final Nat b) {
        Nat zero = new Zero();
        assumeTrue(!zero.lessThan(b) && !zero.lessThan(a));

        if (a.lessThan(b))
            assertEquals(a.subtract(b), zero);
        else
            assertEquals(b.subtract(a), zero);
    }

    // Assume true b != 0 then if b + b == b * b, then b == 2;
    @Property
    public void bEqualsTwo(@From(NatGenerator.class) @Size(max = 3) final Nat b) {
        assumeTrue(b.add(b) == b.multiply(b) && !b.isZero());
        assertEquals(b, PeanoNaturalExpression(2));
    }

    // Any number a multiplied by zero is zero
    @Property
    public void MultiplyByZero(@From(NatGenerator.class) @Size(max = 1000) final Nat a,
            @From(NatGenerator.class) @Size(max = 0) final Nat b) {
        assertEquals(a.multiply(b), new Zero());
    }

    public Nat PeanoNaturalExpression(int num) {
        return num == 0 ? new Zero() : new Succ(PeanoNaturalExpression(--num));
    }
}
