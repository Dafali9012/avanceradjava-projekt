import Database.Database;

public class Program {
    private static boolean init = false;

    public static void main(String[] args) {
        if (init) {
            throw new IllegalCallerException("Program may only be started once!");
        }
        new Program();
    }

    private Program() {
        if (init) {
            throw new IllegalCallerException("Program may only be started once!");
        }
        init = true;
        // Program starts here!
        Database<Jacket> jacketDB = new Database<Jacket>("database", "jackets", "jkt", Jacket.class);
        jacketDB.save(new Jacket("19.99 kr", "Magnus", "2020-01-16"));
        jacketDB.save(new Jacket("129.99 kr", "David", "2020-02-24"));
        jacketDB.save(new Jacket("499.99 kr", "Dennis", "2022-06-02"));
    }
}
