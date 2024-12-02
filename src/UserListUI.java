import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

public class UserListUI extends JPanel {

    private static UserListUI instance;

    Vector<String> vector = new Vector<>();

    JList<String> list = new JList<>(vector);


    public static UserListUI getInstance() {
        if(instance == null) {
            instance = new UserListUI();
        }
        return instance;
    }

    public Vector<String> getVector() {
        return vector;
    }

    public UserListUI() {
        setLayout(new FlowLayout());

        refreshJList();

        setSize(600, 100);
        setVisible(true);
    }
    public int getSelectedIndex(){
        return list.getSelectedIndex();
    }
    public int getDataNumberFromUIString(int index){
        StringBuilder stringBuilder = new StringBuilder();
        String dataString = vector.get(index);
        int i = 0;
        while (true){
            if(dataString.charAt(i) == ':'){
                break;
            }
            stringBuilder.append(dataString.charAt(i));
            i++;
        }
        return Integer.valueOf(stringBuilder.toString());
    }
    public void refreshJList(){
        UserDataMap dataMapInstance = UserDataMap.getInstance();
        HashMap<Integer, DataManager> dataMap = dataMapInstance.getDataMap();

        Set<Integer> integers = dataMap.keySet();

        vector.clear();
        for (Integer dataNumber : integers) {
            vector.add(dataNumber + ": " + dataMap.get(dataNumber).getUserName());
        }


        removeAll();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = UserListUI.getInstance().getDataNumberFromUIString(getSelectedIndex());
                    DataManager dataManager = dataMap.get(index);
                    DetailedUserInfo detailedUserInfoInstance = DetailedUserInfo.getInstance();
                    detailedUserInfoInstance.setPanelInfo(dataManager.getImagePath(), String.valueOf(index), dataManager.getUserName(), dataManager.getNumber(), dataManager.getEmail());
                }
            }
        });
        add(new JScrollPane(list));
        revalidate();
        repaint();
    }
}
