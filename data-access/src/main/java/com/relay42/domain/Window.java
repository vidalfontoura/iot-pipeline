package com.relay42.domain;

/**
 * Created by gwen on 1/25/17.
 */
public class Window {
    String ticker;
    long timestamp;

    public Window(String ticker, long timestamp) {
        this.ticker = ticker;
        this.timestamp = timestamp;
    }
}
