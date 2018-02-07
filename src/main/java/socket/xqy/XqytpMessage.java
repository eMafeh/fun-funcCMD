package socket.xqy;

import util.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by snb on 2017/9/6  14:49
 *
 * @author kelaite
 */
public class XqytpMessage implements Serializable {
    private String message;
    private String backHost;
    private int backport;
    private Date sendTime;
    private static final long serialVersionUID = 1;

    public XqytpMessage() {
    }

    public XqytpMessage(String message, String backHost, int backport) {
        this.message = message;
        this.backHost = backHost;
        this.backport = backport;
        sendTime = new Date();
    }

    @Override
    public String toString() {
        return backHost + ":" + backport + "(" + DateUtils.getSimpledateformat("yyyy年MM月dd日HH:mm:ss").get().format(sendTime) + ") : " + message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getBackHost() {
        return backHost;
    }

    public void setBackHost(String backHost) {
        this.backHost = backHost;
    }

    public int getBackport() {
        return backport;
    }

    public void setBackport(int backport) {
        this.backport = backport;
    }

}
