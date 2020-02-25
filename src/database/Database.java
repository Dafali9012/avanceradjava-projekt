package database;

import enums.SearchOperation;

import java.io.IOException;
import java.lang.reflect.Field;
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
                dataList.add(f.getName() + ":" + f.get(obj));
            }
            Files.write(Paths.get(rootFolder + "/" + subFolder + "/" + obj.getId() + "." + fileType), dataList);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
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

        for (String path : listFiles()) {
            String fileName = Paths.get(path).getFileName().toString();
            String id = fileName.substring(0, fileName.length() - (fileType.length() + 1));
            try {
                C obj = (C) fileClass.getConstructor(String.class).newInstance(id);

                for (String line : Files.readAllLines(Paths.get(path))) {
                    String[] splitLine = line.split(":");
                    if (op == SearchOperation.CONTAINS) {
                        if (splitLine[0].equals(key) && splitLine[1].contains(val)) {
                            setFields(path, obj);
                            return obj;
                        }
                    }
                    if (op == SearchOperation.EQUALS) {
                        if (splitLine[0].equals(key) && splitLine[1].equals(val)) {
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

    public C findOne(String key, float val, SearchOperation op) {
        for (String path : listFiles()) {
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
                        if (splitLine[0].equals(key) && splitLine[1].equals(val)) {
                            setFields(path, obj);
                            return obj;
                        }
                    }
                    if(op == SearchOperation.GREATERTHAN) {
                        if(splitLine[0].equals(key) && Float.valueOf(splitLine[1]) > val) {
                            setFields(path, obj);
                            return obj;
                        }
                    }
                    if(op == SearchOperation.LESSTHAN) {
                        if(splitLine[0].equals(key) && Float.valueOf(splitLine[1]) < val) {
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
        for (String path : listFiles()) {
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
                        if (splitLine[0].equals(key) && splitLine[1].equals(val)) {
                            setFields(path, obj);
                            return obj;
                        }
                    }
                    if(op == SearchOperation.GREATERTHAN) {
                        if(splitLine[0].equals(key) && LocalDate.parse(splitLine[1]).isAfter(val)) {
                            setFields(path, obj);
                            return obj;
                        }
                    }
                    if(op == SearchOperation.LESSTHAN) {
                        if(splitLine[0].equals(key) && LocalDate.parse(splitLine[1]).isBefore(val)) {
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

    private void setFields(String path, C obj) {
        try {
            for (String line : Files.readAllLines(Paths.get(path))) {
                String[] splitLine = line.split(":");
                Field field = fileClass.getDeclaredField(splitLine[0]);
                field.setAccessible(true);
                if(field.getType()==float.class) {
                    field.set(obj, Float.valueOf(splitLine[1]));
                }
                if(field.getType()==String.class) {
                    field.set(obj, splitLine[1]);
                }
                if(field.getType()==LocalDate.class) {
                    field.set(obj, LocalDate.parse(splitLine[1]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> listFiles() {
        try (Stream<Path> walk = Files.walk(Paths.get(rootFolder + "/" + subFolder))) {
            return walk.map(p -> p.toString())
                    .filter(p -> p.matches(".+\\." + fileType))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /*
    public List<C> findAll() {
        try (Stream<Path> walk = Files.walk(Paths.get(rootFolder + "/" + subFolder))) {
            List<String> result = walk.map(p -> p.toString())
                    .filter(p -> p.matches(".+\\." + fileType))
                    .collect(Collectors.toList());

            List<C> objList = new ArrayList<>();

            for (String path : result) {
                String fileName = Paths.get(path).getFileName().toString();
                String id = fileName.substring(0, fileName.length() - (fileType.length() + 1));

                C obj = (C) fileClass.getConstructor(String.class).newInstance(id);

                for (String line : Files.readAllLines(Paths.get(path))) {
                    String[] splitLine = line.split(":");
                    Field field = fileClass.getDeclaredField(splitLine[0]);
                    field.setAccessible(true);
                    field.set(obj, splitLine[1]);
                }
                objList.add(obj);
            }
            return objList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<C> findAll(String key, SearchOperation operator, String value) {
        try (Stream<Path> walk = Files.walk(Paths.get(rootFolder + "/" + subFolder))) {
            List<String> result = walk.map(p -> p.toString())
                    .filter(p -> p.matches(".+\\." + fileType))
                    .collect(Collectors.toList());

            List<C> objList = new ArrayList<>();

            for (String path : result) {
                for (String line : Files.readAllLines(Paths.get(path))) {
                    String[] splitLine = line.split(":");
                    if (key.equals(splitLine[0]) && value.equals(splitLine[1])) {
                        String fileName = Paths.get(path).getFileName().toString();
                        String id = fileName.substring(0, fileName.length() - (fileType.length() + 1));

                        C obj = (C) fileClass.getConstructor(String.class).newInstance(id);

                        for (String line2 : Files.readAllLines(Paths.get(path))) {
                            String[] splitLine2 = line2.split(":");
                            Field field = fileClass.getDeclaredField(splitLine2[0]);
                            field.setAccessible(true);
                            field.set(obj, splitLine2[1]);
                        }
                        objList.add(obj);
                        break;
                    }
                }
            }
            return objList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

     */
}
