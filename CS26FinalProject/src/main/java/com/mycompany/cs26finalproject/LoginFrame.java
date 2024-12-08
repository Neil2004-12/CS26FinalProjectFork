/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.cs26finalproject;

/**
 *
 * @author dionardvale
 */
public class LoginFrame extends javax.swing.JFrame {
//set comment
    /**
     * Creates new form LoginFrame
     */
    public LoginFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainBackgroundPanel = new javax.swing.JPanel();
        mainBackgroundPanel2 = new javax.swing.JPanel();
        tastManagerTitle = new javax.swing.JLabel();
        emailTextLabel = new javax.swing.JLabel();
        emailTextField = new javax.swing.JTextField();
        passwordTextLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        checkBoxRememberMe = new javax.swing.JCheckBox();
        signInButton = new javax.swing.JButton();
        signInWithGoogleButton = new javax.swing.JButton();
        dontHaveAnAccountYetTextLabel = new javax.swing.JLabel();
        logoPictureLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainBackgroundPanel.setBackground(new java.awt.Color(0, 102, 153));
        mainBackgroundPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainBackgroundPanel2.setBackground(new java.awt.Color(249, 239, 196));
        mainBackgroundPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tastManagerTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        tastManagerTitle.setForeground(new java.awt.Color(0, 0, 0));
        tastManagerTitle.setText("Task Manager");
        mainBackgroundPanel2.add(tastManagerTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 169, -1));

        emailTextLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        emailTextLabel.setForeground(new java.awt.Color(0, 0, 0));
        emailTextLabel.setText("Email");
        mainBackgroundPanel2.add(emailTextLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 60, 20));

        emailTextField.setBackground(new java.awt.Color(204, 204, 204));
        emailTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        emailTextField.setForeground(new java.awt.Color(0, 0, 0));
        emailTextField.setText("Enter your Email");
        emailTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextFieldActionPerformed(evt);
            }
        });
        mainBackgroundPanel2.add(emailTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 270, 30));

        passwordTextLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        passwordTextLabel.setForeground(new java.awt.Color(0, 0, 0));
        passwordTextLabel.setText("Password");
        mainBackgroundPanel2.add(passwordTextLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, -1, -1));

        passwordField.setBackground(new java.awt.Color(204, 204, 204));
        passwordField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        passwordField.setForeground(new java.awt.Color(0, 0, 0));
        passwordField.setText("jPasswordField1");
        passwordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordFieldActionPerformed(evt);
            }
        });
        mainBackgroundPanel2.add(passwordField, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 270, 30));

        checkBoxRememberMe.setBackground(new java.awt.Color(249, 239, 196));
        checkBoxRememberMe.setForeground(new java.awt.Color(0, 0, 0));
        checkBoxRememberMe.setText("Remember Me");
        checkBoxRememberMe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxRememberMeActionPerformed(evt);
            }
        });
        mainBackgroundPanel2.add(checkBoxRememberMe, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, -1, -1));

        signInButton.setBackground(new java.awt.Color(0, 102, 153));
        signInButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        signInButton.setForeground(new java.awt.Color(255, 255, 255));
        signInButton.setText("Sign In");
        mainBackgroundPanel2.add(signInButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 313, 270, 30));

        signInWithGoogleButton.setBackground(new java.awt.Color(204, 204, 204));
        signInWithGoogleButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        signInWithGoogleButton.setForeground(new java.awt.Color(0, 0, 0));
        signInWithGoogleButton.setText("Sign In with Google");
        signInWithGoogleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signInWithGoogleButtonActionPerformed(evt);
            }
        });
        mainBackgroundPanel2.add(signInWithGoogleButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 270, 30));

        dontHaveAnAccountYetTextLabel.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        dontHaveAnAccountYetTextLabel.setForeground(new java.awt.Color(0, 0, 0));
        dontHaveAnAccountYetTextLabel.setText("Don't have an account yet?");
        mainBackgroundPanel2.add(dontHaveAnAccountYetTextLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 390, 150, -1));

        logoPictureLabel.setMaximumSize(new java.awt.Dimension(40, 40));
        mainBackgroundPanel2.add(logoPictureLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 70, 300, 290));

        mainBackgroundPanel.add(mainBackgroundPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 32, 740, 420));

        getContentPane().add(mainBackgroundPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 810, 480));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void emailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailTextFieldActionPerformed

    private void passwordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordFieldActionPerformed

    private void signInWithGoogleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signInWithGoogleButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_signInWithGoogleButtonActionPerformed

    private void checkBoxRememberMeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxRememberMeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBoxRememberMeActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        MainDashboard.main(args);
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            //todo: change to LoginFrame once done with testing
            public void run() {
                //to do: uncomment after testing
                //new LoginFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkBoxRememberMe;
    private javax.swing.JLabel dontHaveAnAccountYetTextLabel;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JLabel emailTextLabel;
    private javax.swing.JLabel logoPictureLabel;
    private javax.swing.JPanel mainBackgroundPanel;
    private javax.swing.JPanel mainBackgroundPanel2;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordTextLabel;
    private javax.swing.JButton signInButton;
    private javax.swing.JButton signInWithGoogleButton;
    private javax.swing.JLabel tastManagerTitle;
    // End of variables declaration//GEN-END:variables
}
