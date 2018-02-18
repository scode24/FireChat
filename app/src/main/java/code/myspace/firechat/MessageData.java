package code.myspace.firechat;

/**
 * Created by myspace on 2/18/2018.
 */

public class MessageData {
    private String senderEmail;
    private String receiverEmail;
    private String msg;
    private String time;

    public MessageData(String senderEmail, String receiverEmail, String msg, String time) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.msg = msg;
        this.time = time;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
