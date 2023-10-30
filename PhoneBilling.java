import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class PhoneBilling {
    private static final double DAYTIME_RATE = 4.0;
    private static final double NIGHTTIME_RATE = 3.0;
    private static final double DIFFERENT_NETWORK_RATE = 5.0;
    private static final double  VAT_RATE = 0.16;

    private JTextField callDuration;
    private JTextField phoneNumberField;
    private JButton callButton;
    private JButton cancelButton;
    private Timer callTimer;
    private long startTime;
    private double cost;
    private boolean isDaytime = true;
    private boolean isDifferentNetwork =false;


    public static void main(String[] args) {

       SwingUtilities.invokeLater(() ->{
           new PhoneBilling().createAndShowGUI();
       });
    }

    private void createAndShowGUI(){
        JFrame frame = new JFrame("Phone Billing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);

        JPanel contentPanel = new JPanel (new BorderLayout());

        phoneNumberField = new JTextField("Enter Number");
        callDuration = new JTextField("Call Duration 00:00");
        callDuration.setEditable(false);

        JPanel inputPanel =  new JPanel(new FlowLayout());
        inputPanel.add(phoneNumberField);
        contentPanel.add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        callButton = new JButton("Call");
        callButton.setBackground(Color.GREEN);
        cancelButton = new JButton(" Cancel");
        cancelButton.setBackground(Color.RED);

        buttonPanel.add(callButton);
        buttonPanel.add(cancelButton);

        contentPanel.add(buttonPanel, BorderLayout.CENTER);
        contentPanel.add(callDuration, BorderLayout.SOUTH);
        frame.add(contentPanel);

        callButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              startCall();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endCall();
            }
        });
        frame.setVisible(true);
    }
    private void startCall() {
        String phoneNumber = phoneNumberField.getText();
        if (phoneNumber.isEmpty() || phoneNumber.equals("Enter Number")) {
            JOptionPane.showMessageDialog(null, "Please enter a valid phone number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the phone number belongs to the same network
        isDifferentNetwork = !phoneNumber.startsWith("07");

        callButton.setEnabled(false);
        cancelButton.setEnabled(true);
        startTime = System.currentTimeMillis();

        callTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCallDuration();
            }
        });

        callTimer.start();
    }


    private void endCall(){

        if (callTimer != null && callTimer.isRunning()){
            callTimer.stop();
        }
        callButton.setEnabled(false);
        cancelButton.setEnabled(true);

        long callDuration = (System.currentTimeMillis()-startTime) / 1000;

        calculateCost(callDuration);
    }
    private  void updateCallDuration(){
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        long minutes = elapsedTime / 60;
        long seconds  =  elapsedTime % 60 ;
        DecimalFormat df = new DecimalFormat("00");
        callDuration.setText("Call Duration: " + df.format(minutes) + ":" + df.format(seconds));
    }
    private void calculateCost(long callDuration){
        double rate = isDaytime? DAYTIME_RATE : NIGHTTIME_RATE;
        if (isDifferentNetwork){
            rate = DIFFERENT_NETWORK_RATE;
        }
        cost = (rate*callDuration) / 60;
        if(callDuration > 120){
           cost += cost*VAT_RATE;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        JOptionPane.showMessageDialog(null, "Call Cost: "+ df.format(cost) +" Shillings", "Phone Bill", JOptionPane.INFORMATION_MESSAGE);
    }
}

