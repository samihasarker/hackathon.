package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeScreenController implements Initializable {

    public static String currentUser;

    @FXML
    AnchorPane rootPane;
    @FXML
    TextField name;
    @FXML
    TextField accountNumber;
    @FXML
    PasswordField password;
    @FXML
    Label error;
    @FXML
    AnchorPane loginHolder;
    @FXML
    AnchorPane createAccountHolder;
    @FXML
    TextField loginAccountNumber;
    @FXML
    PasswordField loginPassword;




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loginHolder.setVisible(true);
        createAccountHolder.setVisible(false);
        error.setText("");

    }

    public void setCreateAccount(ActionEvent event)
    {
        loginHolder.setVisible(false);
        createAccountHolder.setVisible(true);

    }

    public void setLoginAccount(ActionEvent event)
    {
        loginHolder.setVisible(true);
        createAccountHolder.setVisible(false);
    }

    public void setSignup(ActionEvent event)
    {
        String name = this.name.getText();
        String accountNumber = this.accountNumber.getText();
        String password = this.password.getText();

        // from ? to ? command ? data1 ? data2 ? ....

        String delimiter = "?";
        String command = "createAccount";

        String sendingData = "" + delimiter + "" + delimiter  + command + delimiter + name + delimiter + accountNumber + delimiter + password;
        try {
            Socket socket = new Socket(Main.ip, Main.port);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(sendingData);

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            String receivingData = (String) objectInputStream.readObject();

            String[] data = receivingData.split("\\?");

            if(data[3].equals("true"))
            {
                currentUser = accountNumber;
                Parent root = FXMLLoader.load(getClass().getResource("/client/UserScreen.fxml"));
                Scene scene = new Scene(root);
                Stage currWindow = (Stage) rootPane.getScene().getWindow();
                currWindow.setScene(scene);
                currWindow.show();
            }

            else
                error.setText("Can't create account! Try again.");

            objectInputStream.close();
            objectOutputStream.close();
            socket.close();

        } catch (IOException e) {
            System.out.println("Can't connect with server! error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Can't read receivingData! error: " + e.getMessage());
        }

    }

    public void setLogin(ActionEvent event)
    {
        String loginAccountNumber;
        String loginPassword;

        loginAccountNumber = this.loginAccountNumber.getText();
        loginPassword = this.loginPassword.getText();

        String delimiter = "?";
        String command = "login";

        String sendingData = "" + delimiter + "" + delimiter  + command + delimiter + loginAccountNumber + delimiter + loginPassword;
        try {
            Socket socket = new Socket(Main.ip, Main.port);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(sendingData);

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            String receivingData = (String) objectInputStream.readObject();

            String[] data = receivingData.split("\\?");

            if(data[3].equals("true"))
            {
                currentUser = loginAccountNumber;
                Parent root = FXMLLoader.load(getClass().getResource("/client/UserScreen.fxml"));
                Scene scene = new Scene(root);
                Stage currWindow = (Stage) rootPane.getScene().getWindow();
                currWindow.setScene(scene);
                currWindow.show();
            }

            else
                error.setText("Can't login! Try again.");

            objectInputStream.close();
            objectOutputStream.close();
            socket.close();

        } catch (IOException e) {
            System.out.println("Can't connect with server! error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Can't read receivingData! error: " + e.getMessage());
        }
    }
}
