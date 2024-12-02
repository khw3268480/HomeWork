import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class EditDataDialog extends JDialog {

    private String name, number, email, fileName, imagePath, copiedFileName;

    private File userData, imageFileFolder;

    private FileReader fileInput;
    private FileWriter fileOutput;


    @Override
    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }


    public Integer userDataCount = 0;


    // 삽입할 정보: 이메일, 사진, 전화번호, 이름
    JTextField emailField = new JTextField("이메일", 10);
    JTextField numberField = new JTextField("전화번호", 10);
    JTextField nameField = new JTextField("이름", 10);
    JFileChooser imageChooser = new JFileChooser();
    JButton confirmBtn = new JButton("입력");
    JButton imageChooserBtn = new JButton("파일 선택");
    UserDataMap dataMapInstance;
    FileWriter fw;
    Integer selectedListIndex;
    UserListUI userListUIInstance;
    Integer dataNumberInList;


    public EditDataDialog(JFrame frame, String title, boolean modal) throws IOException {
        super(frame, title, modal);
        userData = new File("src/data.txt");
        if (!userData.exists()) {
            userData.createNewFile();
        }
        dataMapInstance = UserDataMap.getInstance();
        imageChooserBtn.setSize(30, 30);
        UserDataMap.getInstance().getDataMap();
        Container c = getContentPane();
        c.setBackground(Color.WHITE);
        c.setLayout(new FlowLayout());
        c.add(nameField);
        c.add(numberField);
        c.add(emailField);
        c.add(imageChooserBtn);
        c.add(confirmBtn, BorderLayout.NORTH);
        addListener();


        setSize(700, 100);
        setVisible(false);
    }

    public void makeVisible(boolean bool) {
        userListUIInstance = UserListUI.getInstance();
        selectedListIndex = userListUIInstance.getSelectedIndex();
        dataNumberInList = userListUIInstance.getDataNumberInList(selectedListIndex);
        nameField.setText(dataMapInstance.getDataMap().get(dataNumberInList).getUserName());
        emailField.setText(dataMapInstance.getDataMap().get(dataNumberInList).getEmail());
        numberField.setText(dataMapInstance.getDataMap().get(dataNumberInList).getNumber());
        imagePath = dataMapInstance.getDataMap().get(dataNumberInList).getImagePath();
        setVisible(bool);
    }

    public void addListener() {
        nameField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                JTextField jt = (JTextField) e.getSource();
                if (jt.getText().equals("이름")) {
                    jt.setText("");
                }
            }
        });
        numberField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                JTextField jt = (JTextField) e.getSource();
                if (jt.getText().equals("전화번호")) {
                    jt.setText("");
                }
            }
        });
        emailField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                JTextField jt = (JTextField) e.getSource();
                if (jt.getText().equals("이메일")) {
                    jt.setText("");
                }
            }
        });

        confirmBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (nameField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "이름을 입력하세요.", "오류발생!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (numberField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "전화번호를 입력하세요.", "오류발생!", JOptionPane.ERROR_MESSAGE);
                    return;

                }
                if (emailField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "이메일을 입력하세요.", "오류발생!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (imageChooser.getSelectedFile() == null) {
                    JOptionPane.showMessageDialog(null, "이미지 파일을 선택하세요.", "오류발생!", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirmInt = JOptionPane.showConfirmDialog(null, "정보를 입력하시겠습니까?", "확인", JOptionPane.OK_CANCEL_OPTION);
                if (confirmInt == JOptionPane.OK_OPTION) {
                    JOptionPane.showMessageDialog(null, "정보를 성공적으로 입력하였습니다!", "성공", JOptionPane.INFORMATION_MESSAGE);
                }
                name = nameField.getText();
                number = numberField.getText();
                email = emailField.getText();
//                try {
//                    fw.write(format);
//                    fw.flush();
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }

                dataMapInstance.getDataMap().get(dataNumberInList).setUserName(name);
                dataMapInstance.getDataMap().get(dataNumberInList).setEmail(email);
                dataMapInstance.getDataMap().get(dataNumberInList).setNumber(number);
                dataMapInstance.getDataMap().get(dataNumberInList).setImagePath(imagePath);
                BufferedReader bufferedReader;
                ArrayList<String> originalStrings = new ArrayList<>();
                try {
                    bufferedReader = new BufferedReader(new FileReader(userData));
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        originalStrings.add(line);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                for (int a = 1; a < originalStrings.size(); a++) {

                    if (getDataNumberInString(originalStrings.get(a)) == dataNumberInList) {
                        String format = String.format("[%d] / %s / %s / %s / %s\n", dataNumberInList, name, number, email, imagePath);
                        replaceLineData(a, format);
                    }
                    a++;
                }
                UserListUI.getInstance().replaceJList();

                userDataCount++;

            }
        });
        imageChooserBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF & PNG", "jpg", "gif", "png");
                imageChooser.setFileFilter(filter);
                int result = imageChooser.showOpenDialog(null);
                File selectedFile = imageChooser.getSelectedFile();
                if (result == JFileChooser.APPROVE_OPTION) {
                    imagePath = imageChooser.getSelectedFile().getAbsolutePath();
                    fileName = imageChooser.getSelectedFile().getName();
                }
            }
        });
    }

    public int getDataNumberInString(String str) {
        int openBracket = -1, closeBracket = -1;
        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while (openBracket == -1 || closeBracket == -1) {
            if (str.charAt(i) == '[') {
                openBracket = i;
            }
            if (str.charAt(i) == ']') {
                closeBracket = i;
            }
            i++;
        }
        for (int j = openBracket + 1; j < closeBracket; j++) {
            stringBuilder.append(str.charAt(j));
        }
        return Integer.valueOf(stringBuilder.toString());
    }

    public void replaceLineData(int index, String modifiedString) {
        File inputFile = new File("src/data.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile, true));

            String line;
            ArrayList<String> lines = new ArrayList<>();

            // 파일의 모든 줄을 읽고 저장
            while ((line = reader.readLine()) != null) {
                lines.add(line + "\n");
            }


            lines.set(index, modifiedString);


            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(inputFile, false)); // 파일 전체 내용 삭제


            // 수정된 내용 임시 파일에 저장


            for (String modifiedLine : lines) {
                writer.write(modifiedLine);
                writer.flush();
            }
            reader.close();
            writer.close();
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
