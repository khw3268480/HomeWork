import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailedUserInfo extends JFrame {

    public DetailedUserInfo(String path, String dataNumber, String name, String phoneNumber, String email) {
        setLayout(new GridLayout(3, 1));
        // 상단에 수정/삭제 버튼
        // 중앙에 사진
        // 하단에 이름,전화번호,이메일
        add(new DeleteButton(this));
        add(new UserImage(path));
        add(new UserInfo(dataNumber, name, phoneNumber, email));

        setSize(500, 300);
        setVisible(true);


    }
}

class DeleteButton extends JPanel {

    public DeleteButton(JFrame parentFrame) {
        setLayout(new FlowLayout());
        JButton deleteButton = new JButton("삭제");
        deleteButton.setBackground(Color.red);
        add(deleteButton);

        deleteButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int confirmInt = JOptionPane.showConfirmDialog(null, "정말 정보를 삭제하시겠습니까?", "재확인", JOptionPane.YES_NO_OPTION);
                if (confirmInt == JOptionPane.YES_OPTION) {
                    UserListUI ListInstance = UserListUI.getInstance();
                    UserDataMap dataMapInstance = UserDataMap.getInstance();
                    int selectedIndex = ListInstance.getSelectedIndex();
                    int whereDataCodeExist = findWhereDataCodeExist(UserListUI.getInstance().getDataNumberInList(selectedIndex));
                    System.out.println(whereDataCodeExist);
                    deleteLineData(whereDataCodeExist); // txt파일에서 삭제 : 검색 시 삭제 할 경우에 문제가 발생함.

                    // UserListUI.getInstance().getDataNumberInList(selectedIndex) <- JList에서 데이터코드 가져오는 애.


                    dataMapInstance.removeDataMap(UserListUI.getInstance().getDataNumberInList(selectedIndex)); // 메모리상에서 제거 <- 얘는 정상이야.

                    JOptionPane.showMessageDialog(null, "정보를 성공적으로 삭제하였습니다!", "삭제 완료", JOptionPane.INFORMATION_MESSAGE);

                    ListInstance.replaceJList();

                    parentFrame.dispose();

                }
            }

        });

        setSize(100, 20);
        setVisible(true);
    }
    public int findWhereDataCodeExist(int searchDataCode ){ // 해당 데이터코드가 몇번째 줄에 있는지 찾는 거.
        File inputfile = new File("src/data.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputfile));
            String line;
            ArrayList<String> lines = new ArrayList<>();

            // 파일의 모든 줄을 읽고 저장
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            for (int i = 1; i < lines.size(); i++) {
                String s = lines.get(i);
                System.out.println(s + " ::: i = " + i + " ::: searchDataCode = " + searchDataCode);
                if (getDataNumberInString(s) == searchDataCode) {
                    return i;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public void deleteLineData(int index) { // index 번째 줄을 삭제.
        File inputFile = new File("src/data.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile, true));

            String line;
            ArrayList<String> lines = new ArrayList<>();

            // 파일의 모든 줄을 읽고 저장
            int a = 0;
            while ((line = reader.readLine()) != null) {
                if (a != index) {
                    lines.add(line + "\n");
                }
                a++;
            }


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

    public int getDataNumberInString(String str) { // txt파일에서 데이터코드만 긁어다 주는 애
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
}

class UserImage extends JPanel {
    public UserImage(String path) {
        setLayout(new FlowLayout());
        JLabel imageLabel = new JLabel(new ImageIcon(path));

        add(imageLabel);

        setSize(500, 120);
        setVisible(true);
    }
}

class UserInfo extends JPanel {
    public UserInfo(String dataNumber, String name, String phoneNumber, String email) {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel dataNumberLabel = new JLabel("번호: " + dataNumber);
        dataNumberLabel.setFont(new Font("Gulim", Font.BOLD, 20));
        JLabel nameLabel = new JLabel("| 이름: " + name);
        nameLabel.setFont(new Font("Gulim", Font.BOLD, 20));

        JLabel phoneNumberLabel = new JLabel("| 전화번호: " + phoneNumber);
        phoneNumberLabel.setFont(new Font("Gulim", Font.BOLD, 20));

        JLabel emailLabel = new JLabel("| 이메일: " + email);
        emailLabel.setFont(new Font("Gulim", Font.BOLD, 20));

        add(dataNumberLabel);
        add(nameLabel);
        add(phoneNumberLabel);
        add(emailLabel);

        setSize(300, 80);
        setVisible(true);
    }
}