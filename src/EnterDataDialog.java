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
import java.util.PriorityQueue;

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


    public Integer userDataNumber = 0;


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
        if (count == 0) {
            fw.write("데이터 번호 / 이름 / 전화번호 / 이메일 / 사진경로\n");
            fw.flush();
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

    public boolean checkNumberFormatRight(String number) { // 정규표현식을 통한 전화번호 포맷 확인
        /** https://inpa.tistory.com/entry/JAVA-%E2%98%95-%EC%A0%95%EA%B7%9C%EC%8B%9DRegular-Expression-%EC%82%AC%EC%9A%A9%EB%B2%95-%EC%A0%95%EB%A6%AC 를 참고했습니다. **/
        return number.matches("^01(?:0|1|[6-9])-(?:\\d{4})-(?:\\d{4})");
    }

    public boolean checkEmailFormatRight(String email) { // 정규표현식을 통한 이메일 포맷 확인 ( 사용법이 어려워, chatGPT를 참고했습니다. )
//        return email.matches("^[a-zA-Z0-9.%+-]@[a-zA-Z0-9.-][.][a-zA-Z]{2,}");
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
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
                if (nameField.getText().isEmpty() || nameField.getText().equals("이름")) {
                    JOptionPane.showMessageDialog(null, "이름을 입력하세요.", "오류발생!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (numberField.getText().isEmpty() || numberField.getText().equals("전화번호")) {
                    JOptionPane.showMessageDialog(null, "전화번호를 입력하세요.", "오류발생!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!checkNumberFormatRight(numberField.getText())) {
                    JOptionPane.showMessageDialog(null, "전화번호 양식이 올바르지 않습니다.\n01x-xxxx-xxxx", "오류발생!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (emailField.getText().isEmpty() || emailField.getText().equals("이메일")) {
                    JOptionPane.showMessageDialog(null, "이메일을 입력하세요.", "오류발생!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!checkEmailFormatRight(emailField.getText())) {
                    JOptionPane.showMessageDialog(null, "이메일 양식이 올바르지 않습니다.\nxxx@xxx.xxx", "오류발생!", JOptionPane.ERROR_MESSAGE);
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
                PriorityQueue<Integer> priorityQueue = UserDataMap.getInstance().getPriorityQueue();
                if (priorityQueue.isEmpty()) {
                    priorityQueue.add(0);
                }
                userDataNumber = priorityQueue.peek() + 1;

                priorityQueue.add(userDataNumber);
                for (Integer i : priorityQueue) {
                    System.out.println(i);
                }
                String format = String.format("[%d] / %s / %s / %s / %s\n", userDataNumber, name, number, email, imagePath);
                try {
                    fw.write(format);
                    fw.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                DataManager dataManager = new DataManager(userDataNumber, name, email, number, imagePath);
                dataMapInstance.addDataMap(userDataNumber, dataManager);
                UserListUI.getInstance().refreshJList();
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
