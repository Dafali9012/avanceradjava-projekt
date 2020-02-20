public class Program {
    private static boolean init = false;

    public static void main(String[] args) {
        if(init) {
            throw new IllegalCallerException("Program may only be started once!");
        }
        new Program();
    }

    private Program() {
        if(init) {
            throw new IllegalCallerException("Program may only be started once!");
        }
        init = true;
        // Program starts here!

        Database db = new Database("database");
        User user = new User("daniel", "12345");
        db.save(user);
        db.delete(user);
    }
}
