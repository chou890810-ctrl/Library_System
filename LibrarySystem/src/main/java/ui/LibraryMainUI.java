package ui;

import javax.swing.*;
import java.awt.*;
import model.Member;
import util.SessionManager;

public class LibraryMainUI extends JFrame {

    public LibraryMainUI(Member member) {

        setTitle("📚 圖書館系統");
        setSize(890, 704);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("/images/book_logo.png")).getImage());

        // ===== 背景圖片設定 =====
        ImageIcon bgIcon = new ImageIcon(getClass().getResource("/images/library_shelves_bg.jpg"));
        JLabel bgLabel = new JLabel();
        bgLabel.setLayout(new BorderLayout());
        setContentPane(bgLabel);

        // 背景圖片自動縮放
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

        JLabel title = new JLabel("歡迎使用圖書館系統", SwingConstants.CENTER);
        title.setFont(new Font("微軟正黑體", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(logoLabel, BorderLayout.NORTH);
        topPanel.add(title, BorderLayout.SOUTH);

        // ===== 登入者資訊 =====
        Member current = SessionManager.getCurrentMember();
        String userText = (current != null)
                ? "🔐 目前登入：" + current.getEmail() + "（ID: " + current.getId() + "）"
                : "⚠️ 未登入";

        JLabel userLabel = new JLabel(userText, SwingConstants.CENTER);
        userLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        userLabel.setForeground(Color.WHITE);

        // ===== 按鈕區 =====
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        btnPanel.setOpaque(false);

        JButton borrowBtn  = new JButton("📖 借書");
        JButton returnBtn  = new JButton("📕 還書");
        JButton historyBtn = new JButton("📜 借閱紀錄");
        JButton logoutBtn  = new JButton("🚪 登出");

        Font btnFont = new Font("微軟正黑體", Font.BOLD, 17);
        JButton[] btns = {borrowBtn, returnBtn, historyBtn, logoutBtn};

        for (JButton b : btns) {
            b.setFont(btnFont);
            b.setBackground(new Color(255, 255, 255, 210));
            b.setForeground(new Color(30, 30, 30));
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
            b.setContentAreaFilled(false);
            b.setOpaque(true);
            b.setRolloverEnabled(false);

            // ✅ 自訂按下顏色
            b.getModel().addChangeListener(e -> {
                if (b.getModel().isPressed()) {
                    b.setBackground(new Color(200, 220, 255, 230)); // 按下變淡藍
                } else {
                    b.setBackground(new Color(255, 255, 255, 210)); // 放開恢復
                }
            });

            // ✅ 滑鼠懸停（Hover）效果
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    b.setBackground(new Color(220, 235, 255, 230));
                }
                public void mouseExited(java.awt.event.MouseEvent e) {
                    b.setBackground(new Color(255, 255, 255, 210));
                }
            });
        }

        btnPanel.add(borrowBtn);
        btnPanel.add(returnBtn);
        btnPanel.add(historyBtn);
        btnPanel.add(logoutBtn);

        // ===== 整體佈局 =====
        bgLabel.add(topPanel, BorderLayout.NORTH);
        bgLabel.add(userLabel, BorderLayout.CENTER);
        bgLabel.add(btnPanel, BorderLayout.SOUTH);

        // ===== 按鈕事件 =====
        borrowBtn.addActionListener(e -> new BorrowBookUI());
        returnBtn.addActionListener(e -> new ReturnBookUI());
        historyBtn.addActionListener(e -> new BorrowHistoryUI());
        logoutBtn.addActionListener(e -> {
            SessionManager.logout();
            JOptionPane.showMessageDialog(this, "您已登出！", "登出成功", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginUI();
        });

        // ✅ 啟動前立即套用背景縮放
        Image scaled = bgIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        bgLabel.setIcon(new ImageIcon(scaled));

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryMainUI(null));
    }
}
