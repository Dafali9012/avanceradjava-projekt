import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Database<C> {
    private String rootFolder;
    private String subFolder;
    private String fileType;
    private Class fileClass;

    public Database(String rootFolder, String subFolder, String fileType, Class fileClass) {
        this.rootFolder = rootFolder;
        this.subFolder = subFolder;
        this.fileType = fileType;
        this.fileClass = fileClass;

        try {
            if (!Files.exists(Paths.get(rootFolder))) {
                Files.createDirectory(Paths.get(rootFolder));
            }
            if (!Files.exists(Paths.get(rootFolder+"/"+subFolder))) {
                Files.createDirectory(Paths.get(rootFolder+"/"+subFolder));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(DatabaseObject obj) {
        try {
            List<String> dataList = new ArrayList<>();
            for (String key : obj.getData().keySet()) {
                dataList.add(key + ":" + obj.getData().get(key));
            }
            Files.write(Paths.get(rootFolder + "/" + subFolder + "/" + obj.getId() + "." + fileType), dataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(DatabaseObject obj) {
        try {
            if (Files.exists(Paths.get(rootFolder + "/" + subFolder + "/" + obj.getId() + "." + fileType))) {
                Files.delete(Paths.get(rootFolder + "/" + subFolder + "/" + obj.getId() + "." + fileType));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public C findOne(String key, String value) {
        try (Stream<Path> walk = Files.walk(Paths.get(rootFolder+"/"+subFolder))) {
            List<String> result = walk.map(p -> p.toString())
                    .filter(p -> p.matches(".+\\."+fileType))
                    .collect(Collectors.toList());

            for (String path : result) {
                for (String keyVal : Files.readAllLines(Paths.get(path))) {
                    if (keyVal.equals(key + ":" + value)) {
                        String fileName = Paths.get(path).getFileName().toString();
                        LinkedHashMap<String, String> data = new LinkedHashMap<>();
                        String id = fileName.substring(0, fileName.length()-(fileType.length()+1));

                        for (String line : Files.readAllLines(Paths.get(path))) {
                            String[] splitLine = line.split(":");
                            data.put(splitLine[0], splitLine[1]);
                        }
                        return (C) fileClass.getConstructor(LinkedHashMap.class, String.class).newInstance(data, id);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
