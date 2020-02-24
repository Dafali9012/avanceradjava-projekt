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
        // Program starts here! //Reality Dream III Riverside?
        Database<Jacket> jacketDB = new Database<Jacket>("database", "jackets", "jkt", Jacket.class);
        //System.out.println(jacketDB.findOne("brand", "Helly Hansen"));
        //System.out.println(jacketDB.findOne("datePublished", ">", Instant.now());
        for(Jacket j:jacketDB.findAll("price", SearchOperation.EQUALS, "129.99")) {
            System.out.println(j.getName()+" "+j.getPrice());
        }
    }
}
