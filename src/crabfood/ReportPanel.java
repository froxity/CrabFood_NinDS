/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crabfood;

import crabfood.event.Event;
import crabfood.event.EventLog;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.text.DefaultCaret;
import static javax.swing.text.DefaultCaret.ALWAYS_UPDATE;

/**
 *
 * @author User
 */
public class ReportPanel extends javax.swing.JPanel {

    private int deliveryMan;
    /**
     * Creates new form NewJPanel
     */
    LinkedList<Customer> customerList;
    LinkedList<Restaurant> restaurantList;

    public ReportPanel(LinkedList<Customer> customerList, LinkedList<Restaurant> restaurantList, int width, int height) {
        initComponents();
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(ALWAYS_UPDATE);
        this.customerList = customerList;
        this.restaurantList = restaurantList;
        //TimeStamp ts = new TimeStamp(customerList, restaurantList);
        setBackground(Color.BLACK);
        CrabFood_Logo.setIcon(new ImageIcon("images\\crab_food_logo.png"));
        FoodReport.setIcon(new ImageIcon("images\\logoCC.png"));
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

    /**
     * Literally starts the business with a set of customers. This will
     * continuously run until all customer orders are fulfilled.
     * <p>
     * The way this method works is that it checks if there's a customer at a
     * specific timestamp. If there is, it will generate an event using the
     * {@code eventCreator} method with the relevant timestamp for each
     * preceding event that may occur. The console outputs the information every
     * 1 second.
     * <p>
     * Each event is stored in a linked list {@code eventList}. Each time the
     * loop is done, it will check if the event time matches any of the
     * timestamps before output.
     *
     * @param customerList the list of customers for the day
     * @param restaurantList the available restaurant for the day
     */
    public void startDay(LinkedList<Customer> customerList, LinkedList<Restaurant> restaurantList) {

        //Create a priority queue first for the customer to know the order of the event.
        //Order of sorting:
        //EventTime ==> CustNo ==> EventType
        LinkedList<Event> eventList = new LinkedList<>();
        EventLog eventLog = new EventLog();
        textArea.setText(null);
        reportCC.setText(null);
        reportBK.setText(null);
        reportPB.setText(null);
        //Set all to default values
        for (Restaurant res : restaurantList) {
            for (int i = 0; i < res.getBranchTotal(); i++) {
                res.getBranch(i).setAvailTime(0);
                res.getBranch(i).resetBranchOrderComplete();
            }
            res.resetOrderComplete();
        }
        
        //Output the events according to the queue.
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int eventTime = 0;
            int custNo = 1;
            int custServed = 0;
            //String nodeliveryMen = JOptionPane.showInputDialog(null, "CRAB_FOOD\nA new day has started!\nEnter the no of delivery men!");
            int deliveryMen = (int) jSpinner1.getValue();

            @Override
            public void run() {
                //Begin day
                if (eventTime == 0) {
                    jSpinner1.setEnabled(false);
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
                    for (Event event : eventList) {
                        eventLog.addToList(event);
                    }
                    eventLog.startLog();
                    for (Restaurant res : restaurantList) {
                        eventLog.logRestaurant(res);
                    }
                    textArea.append(str);
                    jSpinner1.setEnabled(true);
                    startButton.setEnabled(true);
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
                                s1 += String.format("Branch (%2d,%2d) : %2d\n", res.getBranch(i).getX(), res.getBranch(i).getY(), res.getBranch(i).getBranchOrderComplete());
                                break;
                            case 2:
                                s2 += String.format("Branch (%2d,%2d) : %2d\n", res.getBranch(i).getX(), res.getBranch(i).getY(), res.getBranch(i).getBranchOrderComplete());
                                break;
                            case 3:
                                s3 += String.format("Branch (%2d,%2d) : %2d\n", res.getBranch(i).getX(), res.getBranch(i).getY(), res.getBranch(i).getBranchOrderComplete());
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
        }, 1000, 1000);
    }

    /**
     * Calculates the probable time stamp for a customer's order.
     * <p>
     * The way this method works is that it calculate each time slot of every
     * event that occurs when a customer comes in. When a customer places the
     * order, it will calculate when will the order be taken, finished cooking
     * and delivered.
     * <p>
     * After calculation, it will return an event object with the relevant info.
     *
     * @return the event with all expected timestamp.
     * @param custCurrent the current customer
     * @param custNo the index of said customer
     * @param resCurrent the current restaurant
     * @param eventQueue the priority queue to be added to
     */
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
        resCurrent.getBranch(branchIndex).addCustomer(custCurrent);
        Event event = new Event(custNo, custCurrent, resCurrent, branchIndex, arrivalTime, orderTakenTime + cookingDuration,
                orderTakenTime + cookingDuration, totalTime);
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        startButton = new javax.swing.JButton();
        openLog = new javax.swing.JButton();
        jSpinner1 = new javax.swing.JSpinner();

        setPreferredSize(new java.awt.Dimension(500, 500));

        crustycrab.setBackground(new java.awt.Color(255, 153, 153));
        crustycrab.setIcon(new javax.swing.ImageIcon(getClass().getResource("/crabfood/crusty_crab.png"))); // NOI18N
        crustycrab.setText("CrustyCrab");
        crustycrab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                crustycrabMouseClicked(evt);
            }
        });

        reportCC.setEditable(false);
        reportCC.setBackground(new java.awt.Color(153, 255, 255));
        reportCC.setColumns(20);
        reportCC.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        reportCC.setLineWrap(true);
        reportCC.setRows(5);
        jScrollPane1.setViewportView(reportCC);

        phumbucket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/crabfood/phum_bucket.png"))); // NOI18N
        phumbucket.setText("PhumBucket");
        phumbucket.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                phumbucketMouseClicked(evt);
            }
        });

        reportPB.setEditable(false);
        reportPB.setBackground(new java.awt.Color(153, 255, 255));
        reportPB.setColumns(20);
        reportPB.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        reportPB.setLineWrap(true);
        reportPB.setRows(5);
        jScrollPane2.setViewportView(reportPB);

        burgerkrusty.setIcon(new javax.swing.ImageIcon(getClass().getResource("/crabfood/burger_krusty.png"))); // NOI18N
        burgerkrusty.setText("BurgerKrusty");
        burgerkrusty.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                burgerkrustyMouseClicked(evt);
            }
        });

        reportBK.setEditable(false);
        reportBK.setBackground(new java.awt.Color(153, 255, 255));
        reportBK.setColumns(20);
        reportBK.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        reportBK.setLineWrap(true);
        reportBK.setRows(5);
        jScrollPane3.setViewportView(reportBK);

        FoodReport.setBackground(new java.awt.Color(255, 153, 153));
        FoodReport.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        FoodReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/crabfood/logoCC.png"))); // NOI18N

        CrabFood_Logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/crabfood/crab_food_logo.png"))); // NOI18N

        textArea.setEditable(false);
        textArea.setBackground(new java.awt.Color(0, 0, 0));
        textArea.setColumns(20);
        textArea.setForeground(new java.awt.Color(0, 255, 0));
        textArea.setRows(5);
        jScrollPane4.setViewportView(textArea);

        TimeStampLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        TimeStampLabel.setForeground(new java.awt.Color(102, 255, 51));
        TimeStampLabel.setText("TimeStamp Event");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 255, 0));
        jLabel1.setText("Total Order(s) Each Restaurant:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 255, 0));
        jLabel2.setText("CRUSTY CRAB");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 255, 0));
        jLabel3.setText("PHUM BUCKET");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 255, 0));
        jLabel4.setText("BURGER KRUSTY");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("No of Delivery Men:");

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/crabfood/timestampIcon.png"))); // NOI18N

        startButton.setText("Start Day");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        openLog.setText("Open Log File");
        openLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openLogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(TimeStampLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(openLog)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startButton))
                    .addComponent(jScrollPane4)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(CrabFood_Logo, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(FoodReport))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(crustycrab, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(phumbucket, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(burgerkrusty, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CrabFood_Logo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FoodReport, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(crustycrab, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(phumbucket, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(TimeStampLabel)))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(startButton)
                                    .addComponent(jLabel5)
                                    .addComponent(openLog)
                                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(burgerkrusty, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        startDay(customerList, restaurantList);
        startButton.setEnabled(false);
    }//GEN-LAST:event_startButtonActionPerformed

    private void crustycrabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_crustycrabMouseClicked
        File f = new File("Crusty Crab.txt");
        if (f.exists() && !f.isDirectory()) {
            try {
                Desktop.getDesktop().open(f);
            } catch (IOException ex) {

            }
        }
    }//GEN-LAST:event_crustycrabMouseClicked

    private void phumbucketMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_phumbucketMouseClicked
        File f = new File("Phum Bucket.txt");
        if (f.exists() && !f.isDirectory()) {
            try {
                Desktop.getDesktop().open(f);
            } catch (IOException ex) {

            }
        }
    }//GEN-LAST:event_phumbucketMouseClicked

    private void burgerkrustyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_burgerkrustyMouseClicked
        File f = new File("Burger Krusty.txt");
        if (f.exists() && !f.isDirectory()) {
            try {
                Desktop.getDesktop().open(f);
            } catch (IOException ex) {

            }
        }
    }//GEN-LAST:event_burgerkrustyMouseClicked

    private void openLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openLogActionPerformed
        File f = new File("eventLog.txt");
        if (f.exists() && !f.isDirectory()) {
            try {
                Desktop.getDesktop().open(f);
            } catch (IOException ex) {

            }
        }else{
            JOptionPane.showMessageDialog(null, "File not found!");
        }
    }//GEN-LAST:event_openLogActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CrabFood_Logo;
    private javax.swing.JLabel FoodReport;
    private javax.swing.JLabel TimeStampLabel;
    private javax.swing.JLabel burgerkrusty;
    private javax.swing.JLabel crustycrab;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JButton openLog;
    private javax.swing.JLabel phumbucket;
    private javax.swing.JTextArea reportBK;
    private javax.swing.JTextArea reportCC;
    private javax.swing.JTextArea reportPB;
    private javax.swing.JButton startButton;
    private javax.swing.JTextArea textArea;
    // End of variables declaration//GEN-END:variables
}
