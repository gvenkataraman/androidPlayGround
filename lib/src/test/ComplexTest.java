package test;

import java.io.Serializable;

/**
 * Created by ganesh on 3/4/15.
 */
public class ComplexTest implements Serializable {
    private int real;
    private int im;
    public ComplexTest(int r, int i) {
        real = r;
        im = i;
    }
    public int getReal() {
        return real;
    }
    public int getIm() {
        return im;
    }
}
