import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Set;

public class SearchUI extends JPanel {
    public SearchUI(){
        setLayout(new FlowLayout());
        JTextField searchField = new JTextField(30);
        searchField.setText("검색할 전화번호 사용자의 이름을 입력하세요.");
        JButton btn = new JButton("검색하기");
        JButton refreshBtn = new JButton("다시 불러오기");
        add(searchField);
        add(btn);
        add(refreshBtn);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(searchField.getText().equals("검색할 전화번호 사용자의 이름을 입력하세요.")){
                    searchField.setText("");
                }
            }
        });

        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HashMap<Integer, DataManager> dataMap = UserDataMap.getInstance().getDataMap();
                HashMap<Integer, DataManager> tempMap = new HashMap<>();
                HashMap<Integer, String> userNameMap = new HashMap<>();

                // 데이터코드와 이름으로 된 해쉬맵 생성
                Set<Integer> dataCodes = dataMap.keySet();
                for (Integer dataCode : dataCodes) {
                    userNameMap.put(dataCode, dataMap.get(dataCode).getUserName());
                }

                // 이름으로 조회
                Set<Integer> userDataCode = userNameMap.keySet();
                for (Integer i : userDataCode) {
//                    System.out.println(searchField.getText() +" : "+userNameMap.get(i));
                    if(userNameMap.get(i).contains(searchField.getText())){
                        DataManager dataManager = dataMap.get(i);
                        System.out.println(i);
                        tempMap.put(i, dataManager);

//                        System.out.println("i = " + i + " dataManger.getName() = "+ dataManager.getUserName());
                        System.out.println(tempMap.keySet());
                    }
                }
                dataMap.clear();
                dataMap.putAll(tempMap);

                UserListUI.getInstance().refreshJList();

            }
        });
        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UserDataMap.getInstance().register();
                    UserListUI.getInstance().refreshJList();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        setSize(600, 100);
        setVisible(true);
    }
}
