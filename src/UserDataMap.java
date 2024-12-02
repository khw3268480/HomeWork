import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class UserDataMap {

    private static UserDataMap instance;

    public UserDataMap() {
    }

    public static UserDataMap getInstance() {
        if (instance == null) {
            instance = new UserDataMap();
        }
        return instance;
    }

    HashMap<Integer, DataManager> dataMap = new HashMap<>();


    public HashMap<Integer, DataManager> getDataMap() {
        return dataMap;
    }

    public void addDataMap(Integer dataNumber, DataManager dataManager) {
        dataMap.put(dataNumber, dataManager);
    }

    public void removeDataMap(Integer dataNumber) {
        dataMap.remove(dataNumber);
    }

    public void register() throws FileNotFoundException {
        dataMap.clear();
        File userData = new File("src/data.txt");
        String userName, email, phoneNumber, imagePath;

        Integer dataNumber;
        Scanner scanner = new Scanner(userData);

        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            if (!nextLine.contains("[")) {
                continue;
            }
            nextLine.replace(" ", "");
            String[] split = nextLine.split("/");
            dataNumber = Integer.valueOf(split[0].replace("[", "").replace("]", "").replace(" ", ""));
            userName = split[1].replace(" ", "");
            phoneNumber = split[2].replace(" ", "");
            email = split[3].replace(" ", "");
            imagePath = split[4].replace(" ", "");

            DataManager dataManager = new DataManager(userName, email, phoneNumber, imagePath);
            dataMap.put(dataNumber, dataManager);
        }
    }
}
