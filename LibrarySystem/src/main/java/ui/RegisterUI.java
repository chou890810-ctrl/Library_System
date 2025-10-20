package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import dao.MemberDao;
import dao.impl.MemberDaoImpl;
import model.Member;
import util.PasswordUtil;

public class RegisterUI extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private final MemberDao memberDao;

    public RegisterUI() {
        this.memberDao = new MemberDaoImpl();

        setTitle("📝 會員註冊");
        setSize(600, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("/images/book_logo.png")).getImage());

        // ===== 背景圖片設定 =====
        ImageIcon bgIcon = new ImageIcon(getClass().getResource("/images/library_shelves_bg.jpg"));
        JLabel bgLabel = new JLabel();
        bgLabel.setLayout(new BorderLayout());
        setContentPane(bgLabel);

        // 背景縮放（視窗尺寸改變時）
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                Image scaled = bgIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                bgLabel.setIcon(new ImageIcon(scaled));
            }
        });

        // ===== 上方 Logo + 標題 =====
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/book_logo.png"));
        Image scaledLogo = logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo), SwingConstants.CENTER);

        JLabel title = new JLabel("📚 新會員註冊", SwingConstants.CENTER);
        title.setFont(new Font("微軟正黑體", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(logoLabel, BorderLayout.NORTH);
        topPanel.add(title, BorderLayout.SOUTH);

        // ===== 表單區 =====
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setOpaque(false);
        formPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(40, 100, 20, 100));

        JLabel nameLabel = new JLabel("👤 姓名：");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        nameField = new JTextField();

        JLabel emailLabel = new JLabel("📧 Email：");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        emailField = new JTextField();

        JLabel passLabel = new JLabel("🔑 密碼：");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        passwordField = new JPasswordField();

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passLabel);
        formPanel.add(passwordField);

        // ===== 按鈕區 =====
        JButton registerBtn = new JButton("註冊");
        JButton backBtn = new JButton("返回登入");

        Font btnFont = new Font("微軟正黑體", Font.BOLD, 16);
        JButton[] btns = {registerBtn, backBtn};
        for (JButton b : btns) {
            b.setFont(btnFont);
            b.setBackground(new Color(255, 255, 255, 210));
            b.setForeground(new Color(30, 30, 30));
            b.setFocusPainted(false);
            b.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(220, 220, 220)));
            b.setContentAreaFilled(false);
            b.setOpaque(true);
            b.setRolloverEnabled(false);

            // 按下效果
            b.getModel().addChangeListener(e -> {
                if (b.getModel().isPressed()) {
                    b.setBackground(new Color(200, 220, 255, 230));
                } else {
                    b.setBackground(new Color(255, 255, 255, 210));
                }
            });

            // Hover 效果
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    b.setBackground(new Color(220, 235, 255, 230));
                }
                public void mouseExited(java.awt.event.MouseEvent e) {
                    b.setBackground(new Color(255, 255, 255, 210));
                }
            });
        }

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 100, 30, 100));
        btnPanel.add(registerBtn);
        btnPanel.add(backBtn);

        // ===== 佈局 =====
        bgLabel.add(topPanel, BorderLayout.NORTH);
        bgLabel.add(formPanel, BorderLayout.CENTER);
        bgLabel.add(btnPanel, BorderLayout.SOUTH);

        // ===== 註冊事件（維持雜湊與 DAO 流程）=====
        registerBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String rawPassword = new String(passwordField.getPassword()).trim();

            if (name.isEmpty() || email.isEmpty() || rawPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "請填寫完整資訊！", "警告", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 1) 雜湊密碼
            String hashed = PasswordUtil.hashPassword(rawPassword);

            // 2) 組 Member 物件（放雜湊後的密碼）
            Member newMember = new Member();
            newMember.setName(name);
            newMember.setEmail(email);
            newMember.setPasswordHash(hashed);

            // 3) 產生會員編號（時間戳）
            String memberNo = "M" + System.currentTimeMillis();
            newMember.setMemberNo(memberNo);

            // 4) 呼叫 DAO 寫入
            int result = memberDao.insert(newMember);

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "註冊成功！請返回登入。");
                dispose();
                new LoginUI();
            } else {
                JOptionPane.showMessageDialog(this, "註冊失敗，Email 可能重複或資料無效。", "錯誤", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ===== 返回登入 =====
        backBtn.addActionListener(e -> {
            dispose();
            new LoginUI();
        });

        // 啟動時初始化背景
        Image scaled = bgIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        bgLabel.setIcon(new ImageIcon(scaled));

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterUI::new);
    }
}
