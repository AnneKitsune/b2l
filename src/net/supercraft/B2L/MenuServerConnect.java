/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import net.supercraft.B2L.Networking.ModuleNetworkUdp;
import net.supercraft.B2L.Networking.NetworkedClient;

/**
 *
 * @author jojolepro
 */
public class MenuServerConnect extends Menu {

    JLabel labelIP;
    JTextField textIP;
    JLabel labelDestPort;
    JTextField textDestPort;
    JLabel labelLocalPort;
    JTextField textLocalPort;
    JButton buttonConnect;

    public MenuServerConnect() {
        super();
        currentPanel.setLayout(new FlowLayout());
        
        labelIP = new JLabel("IP:");
        textIP = new JTextField();
        textIP.setPreferredSize(new Dimension(100,25));
        
        labelLocalPort = new JLabel("LocalPort:");
        textLocalPort = new JTextField();
        textLocalPort.setPreferredSize(new Dimension(100,25));
        
        labelDestPort = new JLabel("DestPort:");
        textDestPort = new JTextField();
        textDestPort.setPreferredSize(new Dimension(100,25));
        
        buttonConnect = new JButton("Connect!");
        
        buttonConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {
                    if(textIP.getText().equals("")||textLocalPort.getText().equals("")||textDestPort.getText().equals("")){
                        return;
                    }
                    B2L.getGameInstance().getModuleNetwork().localPort = Integer.valueOf(textLocalPort.getText());
                    B2L.getGameInstance().getModuleNetwork().createNetworkSocket(ModuleNetworkUdp.LOCALHOST, B2L.getGameInstance().getModuleNetwork().localPort);
                    ((NetworkedClient)B2L.getGameInstance().getModuleNetwork()).connect(InetAddress.getByName(textIP.getText()), Integer.valueOf(textDestPort.getText()));
                } catch (UnknownHostException ex) {
                    Logger.getLogger(MenuServerConnect.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        currentPanel.add(labelIP);
        currentPanel.add(textIP);
        currentPanel.add(labelLocalPort);
        currentPanel.add(textLocalPort);
        currentPanel.add(labelDestPort);
        currentPanel.add(textDestPort);
        currentPanel.add(buttonConnect);
    }
}
