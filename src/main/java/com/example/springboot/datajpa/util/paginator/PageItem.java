package com.example.springboot.datajpa.util.paginator;

import java.util.ArrayList;
import java.util.List;

public class PageItem {
    private int number;
    private boolean current;

    public PageItem(int number, boolean current) {
        this.number = number;
        this.current = current;
    }

    public int getNumber() {
        return number;
    }

    public boolean isCurrent() {
        return current;
    }
}
