package cn.travellerr.LoveYou.utils;


import cn.travellerr.utils.Log;

import static cn.travellerr.Favorability.loveYou;

public class UserMsg {
    private String msg;

    private long timeStamp;

    /**
     * @param msg       一则消息
     * @param timeStamp 时间戳
     * @author Travellerr
     * @implNote 用户的一组消息链
     */
    public UserMsg(String msg, long timeStamp) {
        this.msg = msg;
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        Log.debug("存储时间戳: " + timeStamp);
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMsg() {
        Log.debug("存储消息: " + msg);
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isTimesUp() {
        long duration = loveYou.getDuration() * 1000L;
        long nowTimeStamp = System.currentTimeMillis();
        Log.debug("持续时长 " + duration + " 毫秒");
        Log.debug("是否到时间了" + (nowTimeStamp - this.timeStamp >= duration));
        return nowTimeStamp - this.timeStamp >= duration;
    }
}
