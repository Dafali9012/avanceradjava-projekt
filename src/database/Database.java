package database;

import annotations.Unsigned;
import enums.SearchOperation;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
            if (!Files.exists(Paths.get(rootFolder + "/" + subFolder))) {
                Files.createDirectory(Paths.get(rootFolder + "/" + subFolder));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(DatabaseObject obj) {
        try {
            List<String> dataList = new ArrayList<>();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                //var b = f.getAnnotation(Unsigned.class);
                dataList.add(f.getName() + ":" + f.get(obj));
            }
            Files.write(Paths.get(rootFolder + "/" + subFolder + "/" + obj.getId() + "." + fileType), dataList);

        } catch (IOException | IllegalAccessException e) {
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

    public C findOne(String key, String val, SearchOperation op) {
        if (op == SearchOperation.GREATERTHAN || op == SearchOperation.LESSTHAN) {
            throw new IllegalArgumentException("Cannot compare Strings with less than/ greater than");
        }
        List<String> filesList = listFiles();
        if(filesList == null) return null;
        for (String path : filesList) {
            String fileName = Paths.get(path).getFileName().toString();
            String id = fileName.substring(0, fileName.length() - (fileType.length() + 1));
            try {
                C obj = (C)fileClass.getConstructor(String.class).newInstance(id);

                for (String line : Files.readAllLines(Paths.get(path))) {
                    String[] splitLine = line.split(":");
                    if (op == SearchOperation.CONTAINS) {
                        if (splitLine[0].equals(key) && splitLine[1].toLowerCase().contains(val.toLowerCase())) {
                            setFields(path, obj);
                            return obj;
                        }
                    }
                    if (op == SearchOperation.EQUALS) {
                        if (splitLine[0].equals(key) && splitLine[1].toLowerCase().equals(val.toLowerCase())) {
                            setFields(path, obj);
                            return obj;
                        }
                    }
                }
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public C findOne(String key, float val, SearchOperation op) {
        List<String> filesList = listFiles();
        if(filesList == null) return null;
        for (String path : filesList) {
            String fileName = Paths.get(path).getFileName().toString();
            String id = fileName.substring(0, fileName.length() - (fileType.length() + 1));
            try {
                C obj = (C) fileClass.getConstructor(String.class).newInstance(id);

                for (String line : Files.readAllLines(Paths.get(path))) {
                    String[] splitLine = line.split(":");
                    if (op == SearchOperation.CONTAINS) {
                        if (splitLine[0].equals(key) && splitLine[1].contains(String.valueOf(val))) {
                            setFields(path, obj);
                            return obj;
                        }
                    }
                    if (op == SearchOperation.EQUALS) {
                        if (splitLine[0].equals(key) && splitLine[1].equals(String.valueOf(val))) {
                            setFields(path, obj);
                            return obj;
                        }
                    }
                    if (op == SearchOperation.GREATERTHAN) {
                        if (splitLine[0].equals(key) && Float.parseFloat(splitLine[1]) > val) {
                            setFields(path, obj);
                            return obj;
                        }
                    }
                    if (op == SearchOperation.LESSTHAN) {
                        if (splitLine[0].equals(key) && Float.parseFloat(splitLine[1]) < val) {
                            setFields(path, obj);
                            return obj;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public C findOne(String key, LocalDate val, SearchOperation op) {
        List<String> filesList = listFiles();
        if(filesList == null) return null;
        for (String path : filesList) {
            String fileName = Paths.get(path).getFileName().toString();
            String id = fileName.substring(0, fileName.length() - (fileType.length() + 1));
            try {
                C obj = (C) fileClass.getConstructor(String.class).newInstance(id);

                for (String line : Files.readAllLines(Paths.get(path))) {
                    String[] splitLine = line.split(":");
                    if (op == SearchOperation.CONTAINS) {
                        if (splitLine[0].equals(key) && splitLine[1].contains(String.valueOf(val))) {
                            setFields(path, obj);
                            return obj;
                        }
                    }
                    if (op == SearchOperation.EQUALS) {
                        if (splitLine[0].equals(key) && splitLine[1].equals(val.toString())) {
                            setFields(path, obj);
                            return obj;
                        }
                    }
                    if (op == SearchOperation.GREATERTHAN) {
                        if (splitLine[0].equals(key) && LocalDate.parse(splitLine[1]).isAfter(val)) {
                            setFields(path, obj);
                            return obj;
                        }
                    }
                    if (op == SearchOperation.LESSTHAN) {
                        if (splitLine[0].equals(key) && LocalDate.parse(splitLine[1]).isBefore(val)) {
                            setFields(path, obj);
                            return obj;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<C> findAll() {
        List<C> result = new ArrayList<>();
        List<String> filesList = listFiles();
        if(filesList == null) return null;
        for (String path : filesList) {
            String fileName = Paths.get(path).getFileName().toString();
            String id = fileName.substring(0, fileName.length() - (fileType.length() + 1));
            try {
                C obj = (C) fileClass.getConstructor(String.class).newInstance(id);
                setFields(path, obj);
                result.add(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result.size() == 0) {
            return null;
        } else {
            return result;
        }
    }

    public List<C> findAll(String key, String val, SearchOperation op) {
        if (op == SearchOperation.GREATERTHAN || op == SearchOperation.LESSTHAN) {
            throw new IllegalArgumentException("Cannot compare Strings with less than/ greater than");
        }
        List<C> result = new ArrayList<>();
        List<String> filesList = listFiles();
        if(filesList == null) return null;

        for (String path : filesList) {
            String fileName = Paths.get(path).getFileName().toString();
            String id = fileName.substring(0, fileName.length() - (fileType.length() + 1));
            try {
                C obj = (C) fileClass.getConstructor(String.class).newInstance(id);

                for (String line : Files.readAllLines(Paths.get(path))) {
                    String[] splitLine = line.split(":");
                    if (op == SearchOperation.CONTAINS) {
                        if (splitLine[0].equals(key) && splitLine[1].toLowerCase().contains(val.toLowerCase())) {
                            setFields(path, obj);
                            result.add(obj);
                            break;
                        }
                    }
                    if (op == SearchOperation.EQUALS) {
                        if (splitLine[0].equals(key) && splitLine[1].toLowerCase().equals(val.toLowerCase())) {
                            setFields(path, obj);
                            result.add(obj);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result.size() == 0) {
            return null;
        } else {
            return result;
        }
    }

    public List<C> findAll(String key, float val, SearchOperation op) {
        List<C> result = new ArrayList<>();
        List<String> filesList = listFiles();
        if(filesList == null) return null;

        for (String path : filesList) {
            String fileName = Paths.get(path).getFileName().toString();
            String id = fileName.substring(0, fileName.length() - (fileType.length() + 1));
            try {
                C obj = (C) fileClass.getConstructor(String.class).newInstance(id);

                for (String line : Files.readAllLines(Paths.get(path))) {
                    String[] splitLine = line.split(":");
                    if (op == SearchOperation.CONTAINS) {
                        if (splitLine[0].equals(key) && splitLine[1].contains(String.valueOf(val))) {
                            setFields(path, obj);
                            result.add(obj);
                            break;
                        }
                    }
                    if (op == SearchOperation.EQUALS) {
                        if (splitLine[0].equals(key) && Float.parseFloat(splitLine[1])==val) {
                            setFields(path, obj);
                            result.add(obj);
                            break;
                        }
                    }
                    if (op == SearchOperation.GREATERTHAN) {
                        if (splitLine[0].equals(key) && Float.parseFloat(splitLine[1]) > val) {
                            setFields(path, obj);
                            result.add(obj);
                            break;
                        }
                    }
                    if (op == SearchOperation.LESSTHAN) {
                        if (splitLine[0].equals(key) && Float.parseFloat(splitLine[1]) < val) {
                            setFields(path, obj);
                            result.add(obj);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result.size() == 0) {
            return null;
        } else {
            return result;
        }
    }

    public List<C> findAll(String key, LocalDate val, SearchOperation op) {
        List<C> result = new ArrayList<>();
        List<String> filesList = listFiles();
        if(filesList == null) return null;

        for (String path : filesList) {
            String fileName = Paths.get(path).getFileName().toString();
            String id = fileName.substring(0, fileName.length() - (fileType.length() + 1));
            try {
                C obj = (C) fileClass.getConstructor(String.class).newInstance(id);

                for (String line : Files.readAllLines(Paths.get(path))) {
                    String[] splitLine = line.split(":");
                    if (op == SearchOperation.CONTAINS) {
                        if (splitLine[0].equals(key) && splitLine[1].contains(String.valueOf(val))) {
                            setFields(path, obj);
                            result.add(obj);
                            break;
                        }
                    }
                    if (op == SearchOperation.EQUALS) {
                        if (splitLine[0].equals(key) && LocalDate.parse(splitLine[1]).equals(val)) {
                            setFields(path, obj);
                            result.add(obj);
                            break;
                        }
                    }
                    if (op == SearchOperation.GREATERTHAN) {
                        if (splitLine[0].equals(key) && LocalDate.parse(splitLine[1]).isAfter(val)) {
                            setFields(path, obj);
                            result.add(obj);
                            break;
                        }
                    }
                    if (op == SearchOperation.LESSTHAN) {
                        if (splitLine[0].equals(key) && LocalDate.parse(splitLine[1]).isBefore(val)) {
                            setFields(path, obj);
                            result.add(obj);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result.size() == 0) {
            return null;
        } else {
            return result;
        }
    }

    private void setFields(String path, C obj) {
        try {
            for (String line : Files.readAllLines(Paths.get(path))) {
                String[] splitLine = line.split(":");
                Field field = fileClass.getDeclaredField(splitLine[0]);
                field.setAccessible(true);
                if (field.getType() == float.class) {
                    field.set(obj, Float.valueOf(splitLine[1]));
                }
                if (field.getType() == String.class) {
                    field.set(obj, splitLine[1]);
                }
                if (field.getType() == LocalDate.class) {
                    field.set(obj, LocalDate.parse(splitLine[1]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> listFiles() {
        try (Stream<Path> walk = Files.walk(Paths.get(rootFolder + "/" + subFolder))) {
            return walk.map(Path::toString)
                    .filter(p -> p.matches(".+\\." + fileType))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
