import database.Database;
import database.DatabaseObject;
import enums.SearchOperation;
import objects.Cheese;
import objects.Wine;

public class Program {
    private static boolean init = false;

    public static void main(String[] args) {
        if(init) {
            throw new IllegalCallerException("Program may only be started once");
        }
        new Program();
    }

    private Program() {
        init = true;
        System.out.println("Let's build a database for a store that sells cheese!\n"); // <--- build database of different cheese objects

        Database<Cheese> cheeseDB = new Database<Cheese>("cheese-r-us", "cheese", "chs", Cheese.class);
        cheeseDB.save(new Cheese("Feta", 150, 19.95f));
        cheeseDB.save(new Cheese("Mozzarella", 125, 11.95f));
        cheeseDB.save(new Cheese("Halloumi", 200, 27.50f));
        cheeseDB.save(new Cheese("Cheddar", 150, 17.50f));
        cheeseDB.save(new Cheese("Gouda", 1200, 99.54f));

        Cheese parmesan = new Cheese("Parmesan", 250, 87.25f);
        cheeseDB.save(parmesan);

        System.out.println("Let's say the store wants to expand and add a variety of wines to their store\n"); // <--- build database of different wine objects

        Database<Wine> wineDB = new Database<Wine>("cheese-r-us", "wine", "wne", Wine.class);
        wineDB.save(new Wine("Chardonnay", 1000, 12.5f, 69));
        wineDB.save(new Wine("Merlot", 3000, 13.5f, 189));
        wineDB.save(new Wine("Pinot Noir", 750, 14, 429));

        System.out.println("Let's take a look at all of our cheeses and wines"); // <--- get all objects in our databases

        System.out.println("#-CHEESES-#\n");
        for(Cheese c:cheeseDB.findAll()) {
            System.out.println("name: "+c.getName()+"\nweight: "+c.getWeightGrams()+"g\nprice: "+c.getPrice()+" kr\n");
        }
        System.out.println("#-WINES-#\n");
        for(Wine w:wineDB.findAll()) {
            System.out.println("name: "+w.getName()+"\namount: "+w.getAmountML()+"ml\nproof: "+w.getProof()+"%\nprice: "+w.getPrice()+" kr\n");
        }

        System.out.println("Let's pick out all of the cheeses above 20 kr and lower their prices by 10%\n"); // <--- get all objects below a certain amount

        for(Cheese c:cheeseDB.findAll("price", SearchOperation.GREATERTHAN, 20)) {
            System.out.println(c.getName()+" former price: "+c.getPrice());
            c.setPrice(c.getPrice()*0.9f);
            cheeseDB.save(c);
            System.out.println(c.getName()+" updated price: "+c.getPrice()+"\n");
        }

        System.out.println("Let's pick out all of the wines below 200 kr and increase their prices by 20%\n"); // <--- get all objects above a certain amount

        for(Wine w:wineDB.findAll("price", SearchOperation.LESSTHAN, 200)) {
            System.out.println(w.getName()+" former price: "+w.getPrice());
            w.setPrice(w.getPrice()*1.2f);
            wineDB.save(w);
            System.out.println(w.getName()+" updated price: "+w.getPrice()+"\n");
        }

        System.out.println("I think my favorite wine is called \"pinot\"-something\n"); // <--- get object that contains the string

        Wine favoriteWine = wineDB.findOne("name", SearchOperation.CONTAINS, "pinot");
        System.out.println("name: "+favoriteWine.getName()+"\namount: "+favoriteWine.getAmountML()+"ml\nproof: "+favoriteWine.getProof()+"%\nprice: "+favoriteWine.getPrice()+" kr\n");

        System.out.println("Let's remove parmesan from our inventory");

        cheeseDB.delete(parmesan);
    }
}
