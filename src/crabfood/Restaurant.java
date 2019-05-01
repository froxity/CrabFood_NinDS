package crabfood;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Restaurant is the class that stores the name of the restaurant, its branches
 * and its menu. The branches are stored in an ArrayList consisting of objects
 * of internal class known as {@code Branch}. Each branch stores the x and y
 * coordinates and the availability time if an order was placed for it.<p>
 *
 * The {@code foodMenu} is stored as a HashMap with the name of the food as the
 * key and the time taken to prepare the food as the value.
 *
 * @param name the name of the restaurant
 * @param branchList the list of branches
 * @param foodMenu the available menu of said restaurant
 *
 * @author Cheng Wai Jun
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
     * Creates a restaurant with a specified name.
     *
     * @param name name of restaurant
     */
    public Restaurant(String name) {
        this.name = name;
        branchList = new ArrayList<>();
        foodMenu = new HashMap<>();
    }

    /**
     * Returns the name of the restaurant.
     *
     * @return the name of the restaurant
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a new restaurant name
     *
     * @param name the name of the restaurant
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Create a new branch for the restaurant with specified coordinates.
     *
     * @param x x-coordinate of the new branch
     * @param y y-coordinate of the new branch
     */
    public void addBranch(int x, int y) {
        branchList.add(new Branch(x, y));
    }

    /**
     * Returns the total number of branches.
     *
     * @return the total number of branches
     */
    public int getBranchTotal() {
        return branchList.size();
    }

    /**
     * Returns the specified restaurant branch
     *
     * @param branchNo index of restaurant branch
     * @return the specified branch of the restaurant
     */
    public Branch getBranch(int branchNo) {
        return branchList.get(branchNo);
    }

    /**
     * Returns the whole list of branches.
     *
     * @return the list of branches
     */
    public ArrayList<Branch> getBranchList() {
        return branchList;
    }

    /**
     * Add a food item to the menu list with the time taken to prepare it.
     *
     * @param foodItem the name of the food
     * @param timeTaken the time taken to prepared the food
     */
    public void addMenuItem(String foodItem, int timeTaken) {
        foodMenu.put(foodItem, timeTaken);
    }

    /**
     * Returns the preparation time for a certain food.
     *
     * @param foodItem the name of the food item.
     * @return the time taken to prepare the food. Returns 0 if it does not
     * exist.
     */
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
