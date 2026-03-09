package com.github.flombois.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Abstract test class that verifies the {@link Object#equals(Object)} and
 * {@link Object#hashCode()} contracts as defined by the Java specification.
 * <p>
 * Subclasses must provide three distinct-but-equal instances (a, b, c)
 * and one instance that differs from the others.
 * </p>
 *
 * @param <T> the type under test
 */
public abstract class AbstractEqualsHashCodeTest<T> {

    /**
     * Creates an instance of T. Successive calls with the same arguments
     * must produce equal (but not identical) objects.
     */
    protected abstract T createInstance();

    /**
     * Creates a second instance equal to {@link #createInstance()}.
     */
    protected abstract T createEqualInstance();

    /**
     * Creates a third instance equal to both {@link #createInstance()}
     * and {@link #createEqualInstance()}, used for transitivity testing.
     */
    protected abstract T createThirdEqualInstance();

    /**
     * Creates an instance that is NOT equal to those produced by the other factory methods.
     */
    protected abstract T createDifferentInstance();

    // --- Reflexivity: x.equals(x) must be true ---

    @Test
    void equalsIsReflexive() {
        T a = createInstance();
        assertEquals(a, a);
    }

    // --- Symmetry: x.equals(y) == y.equals(x) ---

    @Test
    void equalsIsSymmetric() {
        T a = createInstance();
        T b = createEqualInstance();
        assertEquals(a, b);
        assertEquals(b, a);
    }

    @Test
    void equalsIsSymmetricForDifferentInstances() {
        T a = createInstance();
        T d = createDifferentInstance();
        assertNotEquals(a, d);
        assertNotEquals(d, a);
    }

    // --- Transitivity: if x.equals(y) and y.equals(z) then x.equals(z) ---

    @Test
    void equalsIsTransitive() {
        T a = createInstance();
        T b = createEqualInstance();
        T c = createThirdEqualInstance();
        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(a, c);
    }

    // --- Consistency: multiple calls return the same result ---

    @Test
    void equalsIsConsistent() {
        T a = createInstance();
        T b = createEqualInstance();
        assertEquals(a, b);
        assertEquals(a, b);
        assertEquals(a, b);
    }

    // --- Null comparison: x.equals(null) must be false ---

    @Test
    void equalsReturnsFalseForNull() {
        T a = createInstance();
        assertNotEquals(null, a);
    }

    // --- Different type: x.equals(otherType) must be false ---

    @Test
    void equalsReturnsFalseForDifferentType() {
        T a = createInstance();
        assertNotEquals("a string", a);
    }

    // --- hashCode contract: equal objects must have equal hash codes ---

    @Test
    void hashCodeIsConsistentWithEquals() {
        T a = createInstance();
        T b = createEqualInstance();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCodeIsConsistent() {
        T a = createInstance();
        int hash = a.hashCode();
        assertEquals(hash, a.hashCode());
        assertEquals(hash, a.hashCode());
    }

    // --- Different objects should ideally have different hash codes ---

    @Test
    void hashCodeDiffersForDifferentInstances() {
        T a = createInstance();
        T d = createDifferentInstance();
        // Not strictly required by the contract, but a good quality indicator.
        // If this fails, it may indicate a poor hash function rather than a bug.
        assertNotEquals(a.hashCode(), d.hashCode(),
                "Hash codes should ideally differ for non-equal objects (not strictly required)");
    }
}
