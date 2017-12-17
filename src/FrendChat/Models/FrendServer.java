package FrendChat.Models;

import FrendChat.Main;
import FrendChat.Presenters.Chat;
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

    public void connect(String ip, int port, Connect callback) {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    socket = new Socket(ip, port);
                    socket.setSoTimeout(3000);

                    out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("FREND_SERVER");

                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    if (in.readLine().equals("FREND_RECIEVED"))
                        callback.mdlConnectSuccessful();
                    else
                        callback.mdlConnectUnsuccessful();

                } catch (Exception e) {
                    callback.mdlConnectUnsuccessful();
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

    public void login(String username, String password, Login callback) {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    out.println("LOGIN" + username + " " + password);

                    if (in.readLine().equals("CREDENTIALS_OKAY"))
                        callback.mdlCredentialsAccepted();
                    else
                        callback.mdlCredentialsRejected();
                } catch (Exception e) {
                    callback.mdlConnectionError();
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    public void register(String username, String password, String colorHex, Login callback) {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    out.println("REG" + colorHex + username + " " + password);
                    String response = in.readLine();
                    if (response.equals("USER_REGISTERED"))
                        callback.mdlCredentialsAccepted();
                    else if (response.equals("USERNAME_IN_USE"))
                        callback.mdlUsernameInUse();
                    else
                        callback.mdlConnectionError();
                } catch (Exception e) {
                    callback.mdlConnectionError();
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    public void broadcastMessage(String message, Chat callback) {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    out.println("TXT" + message);
                    callback.mdlClearInput();
                } catch (Exception e) {
                    callback.mdlConnectionError();
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    public void chatListen(Chat callback) {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    socket.setSoTimeout(0);
                    while (socket.isConnected()) {
                        String response = in.readLine();
                        if (response.substring(0, 3).equals("BRD")) {
                            callback.mdlChatMessage(response.substring(3, response.length()));
                        } else if (response.substring(0, 4).equals("JOIN")) {
                            callback.mdlJoinUser(response.substring(11, response.length()), response.substring(4, 11));
                        } else if (response.substring(0, 5).equals("LEAVE")) {
                            callback.mdlLeaveUser(response.substring(5, response.length()));
                        }
                    }
                } catch (Exception e) {
                    //If the Window is not showing, user is quitting.
                    if(Main.getPrimaryStage().isShowing())
                        callback.mdlConnectionError();
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    public void requestUserList(Chat callback) {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    out.println("LIST");
                } catch (Exception e) {
                    callback.mdlConnectionError();
                }
                return null;
            }
        };

        new Thread(task).start();
    }
}