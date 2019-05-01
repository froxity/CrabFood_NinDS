package crabfood;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Restaurant is the class that stores the name of the restaurant, its branches 
 * and its menu. The branches are stored in an ArrayList consisting of objects of 
 * internal class known as {@code Branch}. Each branch stores the x and y 
 * coordinates and the availability time if an order was placed for it.<p>
 * 
 * The {@code foodMenu} is stored as a HashMap with the name of the food as the key and
 * the time taken to prepare the food as the value.
 * 
 * @param name the name of the restaurant
 * @param branchList the list of branches
 * @param foodMenu the available menu of said restaurant
 * 
 * @author User
 */
public class Restaurant {

    class Branch {

        private int x;
        private int y;
        private int availTime;

        public Branch(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getAvailTime() {
            return availTime;
        }

        public void setAvailTime(int availTime) {
            this.availTime = availTime;
        }
    }

    private String name;
    private ArrayList<Branch> branchList;
    private HashMap<String, Integer> foodMenu;

    /**
     * Creates a restaurant 
     * @param name 
     */
    public Restaurant(String name) {
        this.name = name;
        branchList = new ArrayList<>();
        foodMenu = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addBranch(int x, int y) {
        branchList.add(new Branch(x, y));
    }

    public int getBranchTotal() {
        return branchList.size();
    }

    public Branch getBranch(int branchNo) {
        return branchList.get(branchNo);
    }

    public ArrayList<Branch> getBranchList() {
        return branchList;
    }

    public void addMenuItem(String foodItem, int timeTaken) {
        foodMenu.put(foodItem, timeTaken);
    }

    public int getPrepTime(String foodItem) {
        if (foodMenu.containsKey(foodItem)) {
            return foodMenu.get(foodItem);
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        String s = "";
        s += "Restaurant name: " + name + "\n";
        s += "Branch Locations : \n";
        for (int i = 0; i < branchList.size(); i++) {
            s += "Branch " + (i + 1) + " => " + branchList.get(i).getX() + ", " + branchList.get(i).getY() + "\n";
        }
        for (String foodName : foodMenu.keySet()) {
            s += foodName + " = " + foodMenu.get(foodName).toString() + " unit time\n";
        }
        return s;
    }
}