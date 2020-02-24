import database.Database;
import enums.SearchOperation;

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
        // Program starts here! //Reality Dream III Riverside? //The Suffering Vangough?
        Database<Jacket> jacketDB = new Database<Jacket>("database", "jackets", "jkt", Jacket.class);
        Jacket j = jacketDB.findOne("price", SearchOperation.CONTAINS, 4);
        System.out.println(j.getName() + " " + j.getPrice());
    }
}
