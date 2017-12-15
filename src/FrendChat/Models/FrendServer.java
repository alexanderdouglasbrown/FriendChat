package FrendChat.Models;

import FrendChat.Presenters.Connect;
import FrendChat.Presenters.Login;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class FrendServer {
    Socket socket;
    PrintWriter out;
    BufferedReader in;

    private static FrendServer ourInstance = new FrendServer();

    public static FrendServer getInstance() {
        return ourInstance;
    }

    private FrendServer() {
    }

    public void connect(String ip, int port, Connect connect) {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    socket = new Socket(ip, port);
                    socket.setSoTimeout(2000);

                    out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("FREND_SERVER");

                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    if (in.readLine().equals("FREND_RECIEVED"))
                        connect.mdlConnectSuccessful();
                    else
                        connect.mdlConnectUnsuccessful();

                } catch (Exception e) {
                    connect.mdlConnectUnsuccessful();
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    public void closeConnection() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
        }
    }

    public void login(String username, String password, Login login) {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    out.println("LOGIN" + username + " " + password);

                    if (in.readLine().equals("CREDENTIALS_OKAY"))
                        login.mdlCredentialsAccepted();
                    else
                        login.mdlCredentialsRejected();
                } catch (Exception e) {
                    login.mdlConnectionError();
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    public void register(String username, String password, String colorHex, Login login) {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    out.println("REG" + colorHex + username + " " + password);
                    String response = in.readLine();
                    System.out.println(response);
                    if (response.equals("USER_REGISTERED"))
                        login.mdlCredentialsAccepted();
                    else if (response.equals( "USERNAME_IN_USE"))
                        login.mdlUsernameInUse();
                    else
                        login.mdlConnectionError();
                } catch (Exception e) {
                    login.mdlConnectionError();
                }
                return null;
            }
        };

        new Thread(task).start();
    }
}