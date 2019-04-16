package academy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import student.Student;
import student.StudentController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class RemoveStudentController implements Initializable {

    @FXML
    AnchorPane registrationNumberTextFieldHolder;
    @FXML
    AnchorPane infoHolder;
    @FXML
    TextField registrationNumberTextField;
    @FXML
    Label name;
    @FXML
    Label roll;
    @FXML
    Label cls;
    @FXML
    Label registrationNumber;
    @FXML
    Label error;
    @FXML
    AnchorPane rootPane;

    String registrationNumberText;

    public void setRegistrationNumberTextField(KeyEvent event) throws FileNotFoundException {
        if(event.getCode() == KeyCode.ENTER)
        {
            registrationNumberText = registrationNumberTextField.getText();
            if(!registrationNumberText.isEmpty())
            {
                try {
                    FileInputStream fin = new FileInputStream("src/resources/" + registrationNumberText);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fin);

                    Student student = (Student) objectInputStream.readObject();

                    this.name.setText(student.getName());
                    this.roll.setText(student.getRoll());
                    this.cls.setText(student.getCls());
                    this.registrationNumber.setText(student.getRegistrationNumber());

                    registrationNumberTextFieldHolder.setVisible(false);
                    infoHolder.setVisible(true);


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    public void setRemove(ActionEvent event)
    {
        StudentController studentController = new StudentController();
        boolean isRemove = false;

        try {
            isRemove = studentController.removeStudent(registrationNumberText);
        }catch(Exception e)
        {
            error.setText("Can't remove! try again");
        }

        if(isRemove)
            error.setText("Successfully removed Student");
        else
            error.setText("Can't remove! try again");


    }

    public void setBack(ActionEvent event) throws IOException {
        registrationNumberTextFieldHolder.setVisible(true);
        infoHolder.setVisible(false);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        error.setText("");
        registrationNumberTextFieldHolder.setVisible(true);
        infoHolder.setVisible(false);
    }
}
