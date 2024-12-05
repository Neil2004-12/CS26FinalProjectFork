/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.cs26finalproject;

/**
 *
 * @author dionardvale
 */
public class RegistrationForm extends javax.swing.JFrame {

    /**
     * Creates new form RegistrationForm
     */
    public RegistrationForm() {
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

        mainBackgroundRegister = new javax.swing.JPanel();
        mainBackgroundRegister2 = new javax.swing.JPanel();
        createAccountLabel = new javax.swing.JLabel();
        firstNameLabel = new javax.swing.JLabel();
        firstNameTextField = new javax.swing.JTextField();
        lastNameLabel = new javax.swing.JLabel();
        lastNameTextField = new javax.swing.JTextField();
        emailLabel = new javax.swing.JLabel();
        emailTextField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordTextField = new javax.swing.JPasswordField();
        usernameLabel = new javax.swing.JLabel();
        usernameTextField = new javax.swing.JTextField();
        confirmPasswordLabel = new javax.swing.JLabel();
        confirmPasswordTextField = new javax.swing.JPasswordField();
        signInRegisterButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainBackgroundRegister.setBackground(new java.awt.Color(0, 102, 153));
        mainBackgroundRegister.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        mainBackgroundRegister.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainBackgroundRegister2.setBackground(new java.awt.Color(249, 239, 196));
        mainBackgroundRegister2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        createAccountLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        createAccountLabel.setForeground(new java.awt.Color(0, 0, 0));
        createAccountLabel.setText("Create Account");
        mainBackgroundRegister2.add(createAccountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, -1, -1));

        firstNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        firstNameLabel.setForeground(new java.awt.Color(0, 0, 0));
        firstNameLabel.setText("First Name");
        mainBackgroundRegister2.add(firstNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 100, -1));

        firstNameTextField.setBackground(new java.awt.Color(204, 204, 204));
        firstNameTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        firstNameTextField.setForeground(new java.awt.Color(0, 0, 0));
        mainBackgroundRegister2.add(firstNameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 220, -1));

        lastNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lastNameLabel.setForeground(new java.awt.Color(0, 0, 0));
        lastNameLabel.setText("Last Name");
        mainBackgroundRegister2.add(lastNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 180, 100, -1));

        lastNameTextField.setBackground(new java.awt.Color(204, 204, 204));
        lastNameTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lastNameTextField.setForeground(new java.awt.Color(0, 0, 0));
        mainBackgroundRegister2.add(lastNameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, 220, -1));

        emailLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        emailLabel.setForeground(new java.awt.Color(0, 0, 0));
        emailLabel.setText("Email");
        mainBackgroundRegister2.add(emailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 100, 50, -1));

        emailTextField.setBackground(new java.awt.Color(204, 204, 204));
        emailTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        emailTextField.setForeground(new java.awt.Color(0, 0, 0));
        emailTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextFieldActionPerformed(evt);
            }
        });
        mainBackgroundRegister2.add(emailTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 130, 220, -1));

        passwordLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        passwordLabel.setForeground(new java.awt.Color(0, 0, 0));
        passwordLabel.setText("Password");
        mainBackgroundRegister2.add(passwordLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 180, -1, -1));

        passwordTextField.setBackground(new java.awt.Color(204, 204, 204));
        passwordTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        passwordTextField.setForeground(new java.awt.Color(0, 0, 0));
        passwordTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordTextFieldActionPerformed(evt);
            }
        });
        mainBackgroundRegister2.add(passwordTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 210, 220, -1));

        usernameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        usernameLabel.setForeground(new java.awt.Color(0, 0, 0));
        usernameLabel.setText("Username");
        mainBackgroundRegister2.add(usernameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, -1, -1));

        usernameTextField.setBackground(new java.awt.Color(204, 204, 204));
        usernameTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        usernameTextField.setForeground(new java.awt.Color(0, 0, 0));
        mainBackgroundRegister2.add(usernameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 290, 220, -1));

        confirmPasswordLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        confirmPasswordLabel.setForeground(new java.awt.Color(0, 0, 0));
        confirmPasswordLabel.setText("Confirm Password");
        mainBackgroundRegister2.add(confirmPasswordLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 260, -1, -1));

        confirmPasswordTextField.setBackground(new java.awt.Color(204, 204, 204));
        confirmPasswordTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        confirmPasswordTextField.setForeground(new java.awt.Color(0, 0, 0));
        mainBackgroundRegister2.add(confirmPasswordTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 290, 220, -1));

        signInRegisterButton.setBackground(new java.awt.Color(0, 102, 153));
        signInRegisterButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        signInRegisterButton.setForeground(new java.awt.Color(255, 255, 255));
        signInRegisterButton.setText("Sign Up");
        mainBackgroundRegister2.add(signInRegisterButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 370, 220, 30));

        mainBackgroundRegister.add(mainBackgroundRegister2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 740, 420));

        getContentPane().add(mainBackgroundRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 810, 480));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void passwordTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordTextFieldActionPerformed

    private void emailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailTextFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(RegistrationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegistrationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegistrationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegistrationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegistrationForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel confirmPasswordLabel;
    private javax.swing.JPasswordField confirmPasswordTextField;
    private javax.swing.JLabel createAccountLabel;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JTextField firstNameTextField;
    private javax.swing.JLabel lastNameLabel;
    private javax.swing.JTextField lastNameTextField;
    private javax.swing.JPanel mainBackgroundRegister;
    private javax.swing.JPanel mainBackgroundRegister2;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JPasswordField passwordTextField;
    private javax.swing.JButton signInRegisterButton;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JTextField usernameTextField;
    // End of variables declaration//GEN-END:variables
}
