package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import dao.MemberDao;
import dao.impl.MemberDaoImpl;
import model.Member;
import util.PasswordUtil;
import util.SessionManager;

public class LoginUI extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private final MemberDao memberDao;

    public LoginUI() {
        memberDao = new MemberDaoImpl();
        setTitle("🔐 會員登入");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("/images/book_logo.png")).getImage());

        // ===== 背景圖片設定 =====
        ImageIcon bgIcon = new ImageIcon(getClass().getResource("/images/library_shelves_bg.jpg"));
        JLabel bgLabel = new JLabel();
        bgLabel.setLayout(new BorderLayout());
        setContentPane(bgLabel);

        // 背景自動縮放
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                Image scaled = bgIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                bgLabel.setIcon(new ImageIcon(scaled));
            }
        });

        // ===== 上方標題 + Logo =====
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/book_logo.png"));
        Image scaledLogo = logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo), JLabel.CENTER);

        JLabel title = new JLabel("📘 會員登入", JLabel.CENTER);
        title.setFont(new Font("微軟正黑體", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(logoLabel, BorderLayout.NORTH);
        topPanel.add(title, BorderLayout.SOUTH);

        // ===== 表單區 =====
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setOpaque(false);
        formPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(40, 100, 40, 100));

        JLabel emailLabel = new JLabel("📧 Email：");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        emailField = new JTextField();

        JLabel passwordLabel = new JLabel("🔑 密碼：");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        passwordField = new JPasswordField();

        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        // ===== 按鈕區 =====
        JButton loginBtn = new JButton("登入");
        JButton registerBtn = new JButton("註冊");

        Font btnFont = new Font("微軟正黑體", Font.BOLD, 16);
        JButton[] btns = {loginBtn, registerBtn};

        for (JButton b : btns) {
            b.setFont(btnFont);
            b.setBackground(new Color(255, 255, 255, 210));
            b.setForeground(new Color(30, 30, 30));
            b.setFocusPainted(false);
            b.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(220, 220, 220)));
            b.setContentAreaFilled(false);
            b.setOpaque(true);
            b.setRolloverEnabled(false);

            // Hover 與按下效果
            b.getModel().addChangeListener(e -> {
                if (b.getModel().isPressed()) {
                    b.setBackground(new Color(200, 220, 255, 230));
                } else {
                    b.setBackground(new Color(255, 255, 255, 210));
                }
            });
        }

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);

        // ===== 佈局 =====
        bgLabel.add(topPanel, BorderLayout.NORTH);
        bgLabel.add(formPanel, BorderLayout.CENTER);
        bgLabel.add(btnPanel, BorderLayout.SOUTH);

        // ===== 登入事件（完全保留你原本的邏輯）=====
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String inputPassword = new String(passwordField.getPassword());
                String hashedInput = PasswordUtil.hashPassword(inputPassword); // 加密密碼
                Member loginUser = memberDao.findByEmailAndPassword(email, hashedInput);

                if (loginUser != null) {
                    JOptionPane.showMessageDialog(LoginUI.this, "登入成功！歡迎 " + loginUser.getName());
                    SessionManager.setCurrentMember(loginUser);
                    dispose();
                    new LibraryMainUI(loginUser);
                } else {
                    JOptionPane.showMessageDialog(LoginUI.this, "帳號或密碼錯誤！");
                }
            }
        });

        // ===== 註冊事件 =====
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterUI();
            }
        });

        // 初始化背景
        Image scaled = bgIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        bgLabel.setIcon(new ImageIcon(scaled));

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginUI::new);
    }
}
