package com.cinntra.ledure.test;

import java.util.List;

public class ParentItem {
    private String title;
    private List<
ChildItem
> childItemList;
    private boolean isExpanded;

    public ParentItem(String title, List<ChildItem> childItemList) {
        this.title = title;
        this.childItemList = childItemList;
        this.isExpanded = false; // Initially, parent item is collapsed
    }

    public String getTitle() {
        return title;
    }

    public List<ChildItem> getChildItemList() {
        return childItemList;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}

