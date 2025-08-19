package br.com.dio.model;

public class Space {

    private Integer actual;
    private final int expect;
    private final boolean fixed;

    public Space(final int expect,final boolean fixed) {
        this.expect = expect;
        this.fixed = fixed;

        if (fixed){
            actual = expect;
        }
    }

    public Integer getActual() {
        return actual;
    }

    public int getExpect() {
        return expect;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setActual(final Integer actual) {
        if (fixed) return;
        this.actual = actual;
    }
    public void clearSpace(){
        setActual(null);
    }
}
