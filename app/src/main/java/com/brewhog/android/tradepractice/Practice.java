package com.brewhog.android.tradepractice;

import java.util.Date;
import java.util.List;

public class Practice {
    private int id;
    private String mDate;
    private String ticker;
    private String chartUrl;
    private String chartDoneUrl;
    private List<String> signals;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getChartUrl() {
        return chartUrl;
    }

    public void setChartUrl(String chartUrl) {
        this.chartUrl = chartUrl;
    }

    public String getChartDoneUrl() {
        return chartDoneUrl;
    }

    public void setChartDoneUrl(String chartDoneUrl) {
        this.chartDoneUrl = chartDoneUrl;
    }

    public List<String> getSignals() {
        return signals;
    }

    public void setSignals(List<String> signals) {
        this.signals = signals;
    }
}
