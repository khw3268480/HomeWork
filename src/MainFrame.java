import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * 1. 삭제 시, 창이 닫히긴 하는데 다시 다른 detailed창 열면 한개씩 추가 되면서 열림 버그 수정해야함.  --------
 * **/

public class MainFrame extends JFrame {


    JButton enterDataBtn = new JButton("정보 입력");
    JButton editDataBtn = new JButton("정보 수정");
    EnterDataDialog enterDataDialog = new EnterDataDialog(this, "정보 입력", true);
    EditDataDialog editDataDialog = new EditDataDialog(this, "정보 입력", true);
    UserListUI userListUI;
    SearchUI searchUI;


    public MainFrame() throws IOException {
        setTitle("전화번호부 프로그램");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = getContentPane();

        UserDataMap dataMapInstance = UserDataMap.getInstance();


        dataMapInstance.register();
        userListUI = UserListUI.getInstance();
        searchUI = new SearchUI();
        c.add(new ButtonPanel(), BorderLayout.NORTH);

        c.add(userListUI, BorderLayout.CENTER);

        c.add(searchUI, BorderLayout.SOUTH);

        setSize(600, 500);
        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        new MainFrame();
    }

    class ButtonPanel extends JPanel {

        public ButtonPanel(){

            setLayout(new FlowLayout());
            enterDataBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    enterDataDialog.setVisible(true);
                }
            });
            editDataBtn.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    editDataDialog.makeVisible(true);
                }
            });
            add(enterDataBtn);
            add(editDataBtn);
        }

    }

}

