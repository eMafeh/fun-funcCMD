package socket.core;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by snb on 2017/9/6  14:49
 */
public class XqytpMessage implements Serializable {
    String message;
    Date sendtime;
    String backhost;
    int backport;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
    private static final long serialVersionUID = 1;

    public XqytpMessage() {
    }

    public XqytpMessage(String message, String backhost, int backport) {
        this.message = message;
        this.backhost = backhost;
        this.backport = backport;
        sendtime = new Date();
    }

    public static String jsonMessage(String message, String backhost, int backport) {
        return JSON.toJSON(new XqytpMessage(message, backhost, backport)).toString();
    }

    public static XqytpMessage readObject(String json) {
        return JSON.parseObject(json, XqytpMessage.class);
    }

    @Override
    public String toString() {
        return backhost + ":" + backport + "(" + sdf.format(sendtime) + ") : " + message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

    public String getBackhost() {
        return backhost;
    }

    public void setBackhost(String backhost) {
        this.backhost = backhost;
    }

    public int getBackport() {
        return backport;
    }

    public void setBackport(int backport) {
        this.backport = backport;
    }
}
