package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class UserScreenController implements Initializable {

    @FXML
    AnchorPane rootPane;
    @FXML
    Label user;
    @FXML
    Label accountNumber;
    @FXML
    Label name;
    @FXML
    Label balance;
    @FXML
    TextField transferAccountNumber;
    @FXML
    TextField ammount;
    @FXML
    Button submit;
    @FXML
    Label error;

    boolean isWithdraw, isDeposit, isTransferBalance;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        isWithdraw = isDeposit = isTransferBalance = false;
        error.setText("");
        submit.setVisible(false);
        transferAccountNumber.setVisible(false);
        ammount.setVisible(false);
        initializeUserInfo();

    }

    public void setWithdraw(ActionEvent event)
    {
        isWithdraw = true;
        isDeposit = false;
        isTransferBalance = false;

        transferAccountNumber.setVisible(false);
        ammount.setVisible(true);
        submit.setVisible(true);
    }

    public void setDeposit(ActionEvent event)
    {
        isWithdraw = false;
        isDeposit = true;
        isTransferBalance = false;

        transferAccountNumber.setVisible(false);
        ammount.setVisible(true);
        submit.setVisible(true);
    }

    public void setTransferBalance(ActionEvent event)
    {
        isWithdraw = false;
        isDeposit = false;
        isTransferBalance = true;

        transferAccountNumber.setVisible(true);
        ammount.setVisible(true);
        submit.setVisible(true);
    }

    public void setSubmit(ActionEvent event)
    {
        try {
            Socket socket = new Socket(Main.ip, Main.port);

            if(isWithdraw)
            {
                String ammount = this.ammount.getText();
                if(ammount.isEmpty()) ammount = "0";

                String delimiter = "?";
                String command = "withdraw";

                String sendingData = WelcomeScreenController.currentUser + delimiter + "" + delimiter  + command + delimiter + ammount;

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(sendingData);

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                String receivingData = (String) objectInputStream.readObject();

                String[] data = receivingData.split("\\?");

                if(data[3].equals("true")) {
                    error.setText("Successfully Withdrawn " + ammount + " Taka Only.");
                    this.ammount.setVisible(false);
                    this.submit.setVisible(false);

                }
                else
                    error.setText("Can't withdrawn!");


                objectInputStream.close();
                objectOutputStream.close();
                socket.close();

            }

            else if(isDeposit)
            {
                String ammount = this.ammount.getText();
                if(ammount.isEmpty()) ammount = "0";

                String delimiter = "?";
                String command = "deposit";

                String sendingData = WelcomeScreenController.currentUser + delimiter + "" + delimiter  + command + delimiter + ammount;

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(sendingData);

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                String receivingData = (String) objectInputStream.readObject();

                String[] data = receivingData.split("\\?");

                if(data[3].equals("true")) {
                    error.setText("Successfully deposit " + ammount + " Taka Only.");
                    this.ammount.setVisible(false);
                    this.submit.setVisible(false);
                }
                else
                    error.setText("Incorrect input!");

                objectInputStream.close();
                objectOutputStream.close();
                socket.close();

            }

            else if(isTransferBalance)
            {
                String from = WelcomeScreenController.currentUser;
                String to = transferAccountNumber.getText();
                String ammount = this.ammount.getText();

                String delimiter = "?";
                String command = "transferBalance";

                String sendingData = from + delimiter + to + delimiter  + command + delimiter + ammount;

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(sendingData);

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                String receivingData = (String) objectInputStream.readObject();

                String[] data = receivingData.split("\\?");

                if(data[3].equals("true"))
                {
                    error.setText("Successfully transfer " + ammount + " Taka Only. to " + to);
                    this.ammount.setVisible(false);
                    this.submit.setVisible(false);
                    this.transferAccountNumber.setVisible(false);
                }
                else
                    error.setText("Can't transfer balance!");


            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        initializeUserInfo();

    }

    public void setLogout(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/client/WelcomeScreen.fxml"));
        Scene scene = new Scene(root);
        Stage currWindow = (Stage) rootPane.getScene().getWindow();
        currWindow.setScene(scene);
        currWindow.show();
    }

    private void initializeUserInfo()
    {
        String delimiter = "?";
        String command = "initializeUserInfo";

        String sendingData = WelcomeScreenController.currentUser + delimiter + "" + delimiter  + command;
        try {
            Socket socket = new Socket(Main.ip, Main.port);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(sendingData);

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            String receivingData = (String) objectInputStream.readObject();

            String[] data = receivingData.split("\\?");

            //String sendingData = "" + delimiter + user + delimiter  + command + delimiter + tmpUser.getName() + delimiter + tmpUser.getAccountNumber() + delimiter + tmpUser.getBalance();

            String name = data[3];
            String accountNumber = data[4];
            String balance = data[5];

            this.user.setText("Welcome " + name);
            this.accountNumber.setText(accountNumber);
            this.name.setText(name);
            this.balance.setText(balance);

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
