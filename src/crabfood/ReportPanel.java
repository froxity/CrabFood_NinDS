/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crabfood;

import crabfood.event.Event;
import crabfood.event.EventLog;
import java.awt.Color;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class ReportPanel extends javax.swing.JPanel {

    private int deliveryMan;
    /**
     * Creates new form NewJPanel
     */
    public ReportPanel(LinkedList<Customer> customerList, LinkedList<Restaurant> restaurantList,int width,int height) {
        initComponents();
        startDay(customerList, restaurantList);
        //TimeStamp ts = new TimeStamp(customerList, restaurantList);
        setBackground(Color.black);
        CrabFood_Logo.setIcon(new ImageIcon("images\\crab_food.png"));
        crustycrab.setIcon(new ImageIcon("images\\crusty_crab.png"));
        phumbucket.setIcon(new ImageIcon("images\\phum_bucket.png"));
        burgerkrusty.setIcon(new ImageIcon("images\\burger_krusty.png"));
        reportCC.setEditable(false);
        reportPB.setEditable(false);
        reportBK.setEditable(false);
        super.setBounds(0, 0, width, height);
        
    }

    public int getDeliveryMan() {
        return deliveryMan;
    }
    

    public void startDay(LinkedList<Customer> customerList, LinkedList<Restaurant> restaurantList) {

        //Create a priority queue first for the customer to know the order of the event.
        //Order of sorting:
        //EventTime ==> CustNo ==> EventType
        LinkedList<Event> eventList = new LinkedList<>();
        EventLog eventLog = new EventLog();

        //Set all to default values
        for (Restaurant res : restaurantList) {
            for (int i = 0; i < res.getBranchTotal(); i++) {
                res.getBranch(i).setAvailTime(0);
            }
        }

        //Output the events according to the queue.
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int eventTime = 0;
            int custNo = 1;
            int custServed = 0;
            String nodeliveryMen = JOptionPane.showInputDialog(null, "Enter the no of delivery men!");
            int deliveryMen = Integer.parseInt(nodeliveryMen);

            @Override
            public void run() {
                //Begin day
                if (eventTime == 0) {
                    textArea.append("0: A new day has started!\n");
                }
                //check if the event time reaches customer.
                for (Customer cust : customerList) {
                    if (eventTime == cust.getArrivalTime()) {
                        for (Restaurant res : restaurantList) {
                            if (res.getName().equals(cust.getRestaurantName())) {
                                eventList.add(eventCreator(cust, custNo, res, eventLog));
                                custNo++;
                            }
                        }
                    }
                }
                //Loops through the eventList if there's any match for the specified time
                for (Event event : eventList) {
                    //No event == -1
                    if (event.containsEvent(eventTime) > 0) {
                        //Finished cooking event == 3
                        if (event.containsEvent(eventTime) == 3) {
                            //If there's enough deliverymen, send it off at the same time
                            if (deliveryMen > 0) {
                                deliveryMen--;
                            } //If not, delay it.
                            else {
                                event.delayOrderDeliverTime();
                            }
                        }

                        //Once reached, the delivery men gets replenished.
                        if (event.containsEvent(eventTime) == 4) {
                            custServed++;
                            deliveryMen++;
                            event.getRestaurant().orderComplete();
                            event.getBranch().branchOrderComplete();
                        }
                        String str = event.getEventString(eventTime, deliveryMen);
                        textArea.append(str);

                    }
                }

                if (custServed == customerList.size()) {
                    String str = eventTime + ": All customers served and shops are closed!";
                    textArea.append(str);
                    timer.cancel();
                }
                eventTime++;
            }
        }, 0, 1000);
        
        Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {

            @Override
            public void run() {
                String s1 = "";
                String s2 = "";
                String s3 = "";
                int shop = 1;
                for (Restaurant res : restaurantList) {
                    for (int i = 0; i < res.getBranchTotal(); i++) {
                        switch (shop) {
                            case 1:
                                s1 += ("Branch (" + res.getBranch(i).getX() + ", "
                                        + res.getBranch(i).getY() + ") : " + res.getBranch(i).getBranchOrderComplete() + "\n");
                                break;
                            case 2:
                                s2 += ("Branch (" + res.getBranch(i).getX() + ", "
                                        + res.getBranch(i).getY() + ") : " + res.getBranch(i).getBranchOrderComplete() + "\n");
                                break;
                            case 3:
                                s3 += ("Branch (" + res.getBranch(i).getX() + ", "
                                        + res.getBranch(i).getY() + ") : " + res.getBranch(i).getBranchOrderComplete() + "\n");
                                break;
                            default:
                                throw new AssertionError();
                        }
                    }
                    switch (shop) {
                        case 1:
                            s1 += ("Total Orders: " + res.getOrderComplete());
                            break;
                        case 2:
                            s2 += ("Total Orders: " + res.getOrderComplete());
                            break;
                        case 3:
                            s3 += ("Total Orders: " + res.getOrderComplete());
                            break;
                        default:
                            throw new AssertionError();
                    }
                    shop++;
                }
                reportCC.setText(s1); //updating text every single time
                reportPB.setText(s2); //
                reportBK.setText(s3);
            }
        }, 0, 1000);
    }

    public Event eventCreator(Customer custCurrent, int custNo, Restaurant resCurrent, EventLog newLog) {
        //Priority follows the least amount of time taken to complete order from start to finish.
        //Get the coordinate of customer.
        int xCustCoord = custCurrent.getX();
        int yCustCoord = custCurrent.getY();
        //Initialise the time based on the event.
        //Use these to store the required values.
        int arrivalTime = custCurrent.getArrivalTime();
        int distanceDuration = 0;
        int cookingDuration = 0;
        int orderTakenTime = 0;
        int totalTime = -1;

        //The index of branch to choose.
        int branchIndex = -1;

        //Start comparing between branches.
        for (int currentBranch = 0; currentBranch < resCurrent.getBranchTotal(); currentBranch++) {
            //Calculate distance from customer. NO I AM NOT DOING PYTHAGORAS
            int tempDistanceDuration = java.lang.Math.abs(xCustCoord - resCurrent.getBranch(currentBranch).getX())
                    + java.lang.Math.abs(yCustCoord - resCurrent.getBranch(currentBranch).getY());

            //Calculate total time to cook the dish
            int tempCookingDuration = 0;
            for (String foodName : custCurrent.getFoodList()) {
                tempCookingDuration += resCurrent.getPrepTime(foodName);
            }

            //Check if the branch has any previous order before moving to next one
            //If yes, then it will only begin cooking when it delivers the previous one
            //If not, then it will start on the spot.
            int tempOrderTakenTime;
            if (resCurrent.getBranch(currentBranch).getAvailTime() > arrivalTime) {
                tempOrderTakenTime = resCurrent.getBranch(currentBranch).getAvailTime();
            } else {
                tempOrderTakenTime = arrivalTime;
            }

            //Calculate total time. Store the value if the total time is lower.
            if (totalTime == -1 || (tempOrderTakenTime + tempDistanceDuration + tempCookingDuration) < totalTime) {
                orderTakenTime = tempOrderTakenTime;
                distanceDuration = tempDistanceDuration;
                cookingDuration = tempCookingDuration;
                totalTime = orderTakenTime + distanceDuration + cookingDuration;
                branchIndex = currentBranch;
            }
        }

        //Branch will not take more orders until other order is finished.
        resCurrent.getBranch(branchIndex).setAvailTime(orderTakenTime + cookingDuration);
        resCurrent.orderComplete();
        resCurrent.getBranch(branchIndex).branchOrderComplete();

        Event event = new Event(custNo, custCurrent, resCurrent, branchIndex, arrivalTime, orderTakenTime + cookingDuration,
                orderTakenTime + cookingDuration, totalTime);
        newLog.log(custNo, arrivalTime, orderTakenTime + cookingDuration, distanceDuration, resCurrent.getName(), resCurrent.getBranch(branchIndex).getX(), resCurrent.getBranch(branchIndex).getY(), custCurrent.getFoodList(), custCurrent.getSpReq());

        return event;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        crustycrab = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        reportCC = new javax.swing.JTextArea();
        phumbucket = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        reportPB = new javax.swing.JTextArea();
        burgerkrusty = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        reportBK = new javax.swing.JTextArea();
        FoodReport = new javax.swing.JLabel();
        CrabFood_Logo = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        TimeStampLabel = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(500, 500));

        crustycrab.setBackground(new java.awt.Color(255, 153, 153));
        crustycrab.setText("CrustyCrab");

        reportCC.setBackground(new java.awt.Color(153, 255, 255));
        reportCC.setColumns(20);
        reportCC.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        reportCC.setLineWrap(true);
        reportCC.setRows(5);
        jScrollPane1.setViewportView(reportCC);

        phumbucket.setText("PhumBucket");

        reportPB.setBackground(new java.awt.Color(153, 255, 255));
        reportPB.setColumns(20);
        reportPB.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        reportPB.setLineWrap(true);
        reportPB.setRows(5);
        jScrollPane2.setViewportView(reportPB);

        burgerkrusty.setText("BurgerKrusty");

        reportBK.setBackground(new java.awt.Color(153, 255, 255));
        reportBK.setColumns(20);
        reportBK.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        reportBK.setLineWrap(true);
        reportBK.setRows(5);
        jScrollPane3.setViewportView(reportBK);

        FoodReport.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        FoodReport.setForeground(new java.awt.Color(255, 0, 255));
        FoodReport.setText("Food Report");

        CrabFood_Logo.setText("CrabFood");

        textArea.setColumns(20);
        textArea.setRows(5);
        jScrollPane4.setViewportView(textArea);

        TimeStampLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        TimeStampLabel.setForeground(new java.awt.Color(102, 255, 51));
        TimeStampLabel.setText("TimeStamp Event");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(CrabFood_Logo, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(FoodReport))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(crustycrab, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(phumbucket, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(burgerkrusty, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2)
                            .addComponent(jScrollPane4)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(TimeStampLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(53, 53, 53))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(CrabFood_Logo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FoodReport))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(crustycrab, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(phumbucket, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(burgerkrusty, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addComponent(TimeStampLabel)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CrabFood_Logo;
    private javax.swing.JLabel FoodReport;
    private javax.swing.JLabel TimeStampLabel;
    private javax.swing.JLabel burgerkrusty;
    private javax.swing.JLabel crustycrab;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel phumbucket;
    private javax.swing.JTextArea reportBK;
    private javax.swing.JTextArea reportCC;
    private javax.swing.JTextArea reportPB;
    private javax.swing.JTextArea textArea;
    // End of variables declaration//GEN-END:variables
}
