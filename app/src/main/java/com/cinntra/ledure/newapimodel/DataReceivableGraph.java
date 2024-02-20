package com.cinntra.ledure.newapimodel;

public class DataReceivableGraph {
    public int id;

    public String TotalDue;

    public String OverDueDaysGroup;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTotalDue() {
        return TotalDue;
    }

    public void setTotalDue(String totalDue) {
        TotalDue = totalDue;
    }

    public String getOverDueDaysGroup() {
        return OverDueDaysGroup;
    }

    public void setOverDueDaysGroup(String overDueDaysGroup) {
        OverDueDaysGroup = overDueDaysGroup;
    }
}
