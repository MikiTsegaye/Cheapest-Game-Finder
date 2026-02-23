package org.hit.client.model;

import java.io.Serializable;

public class Response implements Serializable {

    private String status;  // למשל: "Success" או "Error"
    private Object body;    // התוכן (למשל רשימת משחקים, או הודעת שגיאה)

    // בנאי ריק (חשוב ל-Gson)
    public Response() {}

    public Response(String status, Object body) {
        this.status = status;
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status='" + status + '\'' +
                ", body=" + body +
                '}';
    }
}