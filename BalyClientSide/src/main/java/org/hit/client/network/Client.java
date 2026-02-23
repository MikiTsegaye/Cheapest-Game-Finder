package org.hit.client.network;

import com.google.gson.Gson;
import org.hit.client.model.Request;
import org.hit.client.model.Response;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final String host;
    private final int port;
    private final Gson gson;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        this.gson = new Gson();
    }

    // מתודה גנרית לשליחת בקשה וקבלת תשובה
   // כדי לעזור לך לעקוב אחרי כמות הבקשות ולראות בדיוק מתי הלקוח "יוצא לדרך", נוסיף הדפסת לוג בתוך מחלקת ה-Client.java. זה יאפשר לך לראות אם האפליקציה באמת נכנסת ללולאה שולחת עשרות בקשות בשנייה.

    //להלן הקוד המעודכן למחלקת ה-Client.java:
    public <T> Response sendRequest(Request<T> request) {
        // הדפסת לוג בכל פעם שהמתודה נקראת
        System.out.println("[CLIENT LOG] Attempting to send request: " + request.getHeaders().get("action"));

        try (Socket socket = new Socket(host, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner reader = new Scanner(socket.getInputStream())) {

            // המרה ל-JSON ושליחה לשרת
            String jsonRequest = gson.toJson(request);
            writer.println(jsonRequest);
            System.out.println("[CLIENT LOG] JSON sent to server.");

            // המתנה לתשובה ופענוח שלה
            if (reader.hasNextLine()) {
                String jsonResponse = reader.nextLine();
                System.out.println("[CLIENT LOG] Response received from server.");
                return gson.fromJson(jsonResponse, Response.class);
            }
        } catch (Exception e) {
            // הדפסת שגיאה מפורטת במידה והחיבור נכשל
            System.err.println("[CLIENT LOG] Network error: " + e.getMessage());
        }
        return null;
    }
}