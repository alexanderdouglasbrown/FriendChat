package FrendChat.Models;

import FrendChat.Main;
import FrendChat.Presenters.Chat;
import FrendChat.Presenters.Connect;
import FrendChat.Presenters.Login;
import FrendChat.Presenters.Account;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class FrendServer {
    Socket socket;
    PrintWriter out;
    BufferedReader in;

    String newPass = "";

    public Account accountCallback = null;

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
                    out.println("FREND_CHAT_VER_1_00");

                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String response = in.readLine();

                    if (response.substring(0, 12).equals("FREND_SERVER")) {
                        if (response.equals("FREND_SERVER_VER_1_00"))
                            callback.mdlConnectSuccessful();
                        else
                            callback.mdlWrongVersion();
                    } else
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

                    String result = in.readLine();
                    if (result.equals("CREDENTIALS_OKAY"))
                        callback.mdlCredentialsAccepted(username);
                    else if (result.equals("CREDENTIALS_DENIED"))
                        callback.mdlCredentialsRejected();
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

    public void register(String username, String password, String colorHex, Login callback) {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    out.println("REG" + colorHex + username + " " + password);
                    String response = in.readLine();
                    if (response.equals("USER_REGISTERED"))
                        callback.mdlCredentialsAccepted(username);
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
                        } else if (response.equals("PASS_OK")) {
                            out.println("NEW_PASS" + newPass);
                        } else if (response.equals("PASS_UPDATED")) {
                            if (accountCallback != null)
                                accountCallback.mdlPasswordUpdated();
                        } else if (response.equals("PASS_BAD")) {
                            if (accountCallback != null)
                                accountCallback.mdlPasswordInvalid();
                        } else if (response.equals("COLOR_UPDATED")) {
                            if (accountCallback != null)
                                accountCallback.mdlColorUpdated();
                        }
                    }
                } catch (Exception e) {
                    //If the Window is not showing, user is quitting.
                    if (Main.getPrimaryStage().isShowing())
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

    public void updatePassword(String oldPass, String newPass) {
        this.newPass = newPass;
        out.println("CHK_PASS" + oldPass);
    }

    public void updateColor(String color) {
        out.println("UPDATE_COLOR" + color);
    }
}