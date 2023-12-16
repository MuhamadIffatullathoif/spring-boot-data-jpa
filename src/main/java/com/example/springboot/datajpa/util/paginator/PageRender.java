package com.example.springboot.datajpa.util.paginator;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PageRender<T> {
    private String url;
    private Page<T> page;
    private int totalPage;
    private int totalElementPage;
    private int currentPage;
    private List<PageItem> pages;

    public PageRender(String url, Page<T> page) {
        this.url = url;
        this.page = page;
        this.pages = new ArrayList<PageItem>();
        totalElementPage = page.getSize();
        totalPage = page.getTotalPages();
        currentPage = page.getNumber() + 1;
        int from, until;
        if (totalPage <= totalElementPage) {
            from = 1;
            until = totalPage;
        } else {
            if (currentPage <= totalElementPage / 2) {
                from = 1;
                until = totalElementPage;
            } else if (currentPage >= totalPage - totalElementPage / 2) {
                from = totalPage - totalElementPage;
                until = totalElementPage;
            } else {
                from = currentPage - totalElementPage / 2;
                until = totalElementPage;
            }
        }

        for (int i = 0; i < until; i++) {
            pages.add(new PageItem(until + i, currentPage == from + i));
        }
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getTotalElementPage() {
        return totalElementPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<PageItem> getPages() {
        return pages;
    }

    public boolean isFirst() {
        return this.page.isFirst();
    }

    public boolean isLast() {
        return this.page.isLast();
    }

    public boolean isHasNext() {
        return this.page.hasNext();
    }

    public boolean isHasPrevious() {
        return this.page.hasPrevious();
    }
}
