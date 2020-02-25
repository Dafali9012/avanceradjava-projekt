import database.Database;
import enums.SearchOperation;

public class Program {
    private static boolean init = false;

    public static void main(String[] args) {
        if (init) {
            throw new IllegalCallerException("Program may only be started once");
        }
        new Program();
    }

    private Program() {
        if (init) {
            throw new IllegalCallerException("Program may only be started once");
        }
        init = true;
        // Program starts here!
        Database<Jacket> jacketDB = new Database<Jacket>("database", "jackets", "jkt", Jacket.class);

        Jacket test = jacketDB.findOne("name", "Dennis", SearchOperation.GREATERTHAN);
        System.out.println("name:"+test.getName()+" price:"+test.getPrice()+" exp:"+test.getExpirationDate());
    }
}
