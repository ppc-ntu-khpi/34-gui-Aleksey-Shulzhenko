package com.mybank.gui;

import com.mybank.domain.Bank;
import com.mybank.domain.CheckingAccount;
import com.mybank.domain.Customer;
import com.mybank.domain.SavingsAccount;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Alexander 'Taurus' Babich
 */
public class SWINGDemo {

    private final JEditorPane log;
    private final JButton show;
    private final JButton report;
    private final JComboBox clients;

    public SWINGDemo() {
        log = new JEditorPane("text/html", "");
        log.setPreferredSize(new Dimension(250, 150));

        show = new JButton("Show");
        report = new JButton("Report");

        clients = new JComboBox();
        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            clients.addItem(Bank.getCustomer(i).getLastName() + ", " + Bank.getCustomer(i).getFirstName());
        }
    }

    private void launchFrame() {
        JFrame frame = new JFrame("MyBank clients");
        frame.setLayout(new BorderLayout());

        JPanel cpane = new JPanel();
        cpane.setLayout(new GridLayout(1, 3));

        cpane.add(clients);
        cpane.add(show);
        cpane.add(report);

        frame.add(cpane, BorderLayout.NORTH);
        frame.add(log, BorderLayout.CENTER);

        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer current = Bank.getCustomer(clients.getSelectedIndex());
                String accType = current.getAccount(0) instanceof CheckingAccount ? "Checking" : "Savings";
                String custInfo = "<br>&nbsp;<b><span style=\"font-size:2em;\">" + current.getLastName() + ", " +
                        current.getFirstName() + "</span><br><hr>" +
                        "&nbsp;<b>Acc Type: </b>" + accType +
                        "<br>&nbsp;<b>Balance: <span style=\"color:red;\">$" + current.getAccount(0).getBalance() + "</span></b>";
                log.setText(custInfo);
            }
        });

        report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder reportHtml = new StringBuilder();
                reportHtml.append("<html><body><h1>Customer Report</h1><hr>");

                for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
                    Customer customer = Bank.getCustomer(i);
                    reportHtml.append("<h2>")
                            .append(customer.getLastName()).append(", ").append(customer.getFirstName())
                            .append("</h2>");

                    for (int j = 0; j < customer.getNumberOfAccounts(); j++) {
                        String accType = customer.getAccount(j) instanceof CheckingAccount ? "Checking" : "Savings";
                        reportHtml.append("&nbsp;&nbsp;<b>").append(accType).append(" Account:</b> $")
                                .append(customer.getAccount(j).getBalance()).append("<br>");
                    }
                    reportHtml.append("<hr>");
                }

                reportHtml.append("</body></html>");
                log.setText(reportHtml.toString());
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        Bank.addCustomer("John", "Doe");
        Bank.addCustomer("Fox", "Mulder");
        Bank.addCustomer("Dana", "Scully");
        Bank.getCustomer(0).addAccount(new CheckingAccount(2000));
        Bank.getCustomer(1).addAccount(new SavingsAccount(1000, 3));
        Bank.getCustomer(2).addAccount(new CheckingAccount(1000, 500));

        SWINGDemo demo = new SWINGDemo();
        demo.launchFrame();
    }

}
