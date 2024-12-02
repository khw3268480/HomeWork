import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DataManager {

    private String userName, email, number, imagePath;
    private Integer dataCode;


    public DataManager(Integer dataCode, String username, String email, String number, String imagePath) {
        this.dataCode = dataCode;
        this.userName = username;
        this.email = email;
        this.number = number;
        this.imagePath = imagePath;
    }

    public Integer getDataCode() {
        return dataCode;
    }

    public void setDataCode(Integer dataCode) {
        this.dataCode = dataCode;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String getImagePath() {
        return imagePath;
    }
}
