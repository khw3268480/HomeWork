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
import java.util.HashMap;
import java.util.Set;

public class EnterDataDialog extends JDialog {

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


    public EnterDataDialog(JFrame frame, String title, boolean modal) throws IOException {
        super(frame, title, modal);
        userData = new File("src/data.txt");
        if (!userData.exists()) {
            userData.createNewFile();
        }
        dataMapInstance = UserDataMap.getInstance();
        fw = new FileWriter(userData.getPath(), true);
        int count = (int) Files.lines(Path.of(userData.getPath())).count();
        userDataCount = count-1;
        if (userDataCount == -1) {
            fw.write("데이터 번호 / 이름 / 전화번호 / 이메일 / 사진경로\n");
            fw.flush();
            userDataCount = 0;
        }
        imageChooserBtn.setSize(30, 30);
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
                String format = String.format("[%d] / %s / %s / %s / %s\n", userDataCount, name, number, email, imagePath);
                try {
                    fw.write(format);
                    fw.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                DataManager dataManager = new DataManager(name, email, number, imagePath);
                dataMapInstance.addDataMap(userDataCount, dataManager);
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


                /** 이미지 파일 복사부분들 **/

//                if(!imageFileFolder.exists()){
//                    imageFileFolder.mkdir();
//                }
//                File[] files = imageFileFolder.listFiles();
//                int length = files.length;
//                for (int i = 0 ; i < length; i++){
//                    copiedFileName = String.valueOf(i++);
//                }
//                try {
//                    fileInput = new FileReader(path);
//                } catch (FileNotFoundException ex) {
//                    throw new RuntimeException(ex);
//                }
//                try {
//                    fileOutput = new FileWriter(imageFileFolder.getAbsolutePath());
//                    int c;
//                    while((c = fileInput.read()) != -1){
//                        fileOutput.write((byte) c);
//                    }
//                    fileOutput.close();
//                    fileInput.close();
//                } catch (Exception ex) {
//                    throw new RuntimeException(ex);
//                }
////                imageFileFolder.
//
            }
        });

    }
}
