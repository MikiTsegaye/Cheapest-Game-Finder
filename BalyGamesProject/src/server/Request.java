package server;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Headers;

import java.io.Serializable;
import java.util.Map;

public class Request<T> implements Serializable {

    private Map<String, String> headers;
    private T body;


    // בנאי ריק (חשוב ל-Gson)
    public Request() {}

    public Request(Map<String, String> zzheaders, T body) {
        this.headers = headers;
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Request{" +
                "headers=" + headers +
                ", body=" + body +
                '}';
    }
}
