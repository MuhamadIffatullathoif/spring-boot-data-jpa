package com.example.springboot.datajpa.util.paginator;

import java.util.ArrayList;
import java.util.List;

public class PageItem {
    private int name;
    private boolean current;

    public PageItem(int name, boolean current) {
        this.name = name;
        this.current = current;
    }

    public int getName() {
        return name;
    }

    public boolean isCurrent() {
        return current;
    }
}
