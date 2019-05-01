package crabfood;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Restaurant is the class that stores information of a restaurant and all of its
 * branch information. Each branch is stored in a class object 
 * @author User
 */
public class Restaurant {
    
    private String name;
    private ArrayList<Point2D.Double> branchList;
    private HashMap<String, Integer> foodMenu;

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
        branchList.add(new Point2D.Double(x, y));
    }

    public int getBranchTotal() {
        return branchList.size();
    }

    public Point2D.Double getBranch(int branchNo) {
        return branchList.get(branchNo);
    }


    public void addMenuItem(String foodItem, int timeTaken) {
        foodMenu.put(foodItem, timeTaken);
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
