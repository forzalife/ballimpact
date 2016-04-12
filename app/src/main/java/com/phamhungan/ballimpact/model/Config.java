package com.phamhungan.ballimpact.model;

/**
 * Created by MrAn PC on 18-Feb-16.
 */
public class Config {
    private String configId;
    private int ballPlayer1Id;
    private int ballPlayer2Id;
    private int tableId;
    private String player1Name;
    private String player2Name;
    private int lineId;

    public Config(){};

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public int getBallPlayer1Id() {
        return ballPlayer1Id;
    }

    public void setBallPlayer1Id(int ballPlayer1Id) {
        this.ballPlayer1Id = ballPlayer1Id;
    }

    public int getBallPlayer2Id() {
        return ballPlayer2Id;
    }

    public void setBallPlayer2Id(int ballPlayer2Id) {
        this.ballPlayer2Id = ballPlayer2Id;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }
}
