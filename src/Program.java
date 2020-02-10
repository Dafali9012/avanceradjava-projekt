public class Program {
    private static final Program instance = new Program();

    private Program() {
        /*
        DatabaseManager.setRootDirectory("database/");
        DatabaseManager.createDirectory("files/");
        DatabaseManager.setRootDirectory("database/files/");
        DatabaseManager.createDirectory("poopie/");
         */
    }

    public static Program getInstance() {
        return instance;
    }
}
