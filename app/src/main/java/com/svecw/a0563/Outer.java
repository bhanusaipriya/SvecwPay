package com.svecw.a0563;

import java.util.HashMap;
import java.util.Map;

public class Outer {
    Inner inner = new Inner();
    Map<String,Inner> mp = new HashMap<>();

    public Outer(Inner inner, Map<String, Inner> mp) {
        this.inner = inner;
        this.mp = mp;
    }

    public Outer() {
    }

    public Inner getInner() {
        return inner;
    }

    public void setInner(Inner inner) {
        this.inner = inner;
    }

    public Map<String, Inner> getMp() {
        return mp;
    }

    public void setMp(Map<String, Inner> mp) {
        this.mp = mp;
    }
}
