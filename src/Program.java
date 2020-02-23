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
        Database<User> userDB = new Database<>("database", "users", "usr", User.class);
    }
}
