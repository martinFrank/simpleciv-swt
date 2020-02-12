package com.github.martinfrank.simpleciv;

import org.junit.Assert;
import org.junit.Test;

public class MathTest {

    @Test
    public void mathTest() {
        double n1 = 100;
        double e1 = Math.log10(n1);
        Assert.assertEquals(2, e1, 0.002);

        double n2 = 99;
        double e2 = (int) Math.log10(n2);
        Assert.assertEquals(1, e2, 0.002);
    }

    @Test
    public void createTest() {
    }
}
