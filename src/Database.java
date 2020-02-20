import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Database {
    private String rootFolder;

    public Database(String rootFolder) {
        this.rootFolder = rootFolder;
        if (!Files.exists(Paths.get(rootFolder))) {
            try {
                Files.createDirectory(Paths.get(rootFolder));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save(DatabaseObject obj) {
        try {
            List<String> dataList = new ArrayList<>();
            for (String key:obj.getData().keySet()) {
                dataList.add(key+":"+obj.getData().get(key));
            }
            if (!Files.exists(Paths.get(rootFolder + "/" + obj.getSubFolder()))) {
                Files.createDirectory(Paths.get(rootFolder + "/" + obj.getSubFolder()));
            }
            Files.write(Paths.get(rootFolder + "/" + obj.getSubFolder() + "/" + obj.getId()), dataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(DatabaseObject obj) {
        try {
            if(Files.exists(Paths.get(rootFolder + "/" + obj.getSubFolder()+"/"+obj.getId()))) {
                Files.delete(Paths.get(rootFolder + "/" + obj.getSubFolder()+"/"+obj.getId()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DatabaseObject findOne(String key, String value) {
        // loop through all files and pick out the matching one
        return new User("tempUser", "thisisnotsupposedtobehere");
    }

    public void write(String filePath) {

        Path path = Paths.get(filePath);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String filePath, LinkedHashMap<String, String> dataMap) {

        Path path = Paths.get(filePath);
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < dataMap.size(); i++) {
            String dataKey = new ArrayList<>(dataMap.keySet()).get(i);
            dataList.add(dataKey + ":" + dataMap.get(dataKey));
        }
        try {
            Files.write(path, dataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
