package ui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.util.List;

import model.Loan;
import model.Member;
import service.LoanService;
import service.LoanServiceImpl;
import util.SessionManager;

public class ReturnBookUI extends JFrame {

    private JTable loanTable;
    private DefaultTableModel tableModel;
    private LoanService loanService = new LoanServiceImpl();

    public ReturnBookUI() {
    	setIconImage(new ImageIcon(getClass().getResource("/images/book_logo.png")).getImage());
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/book_logo.png"));
        setTitle("📕 還書作業");
        setSize(800, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 247, 250)); // 統一背景

        Member current = SessionManager.getCurrentMember();
        if (current == null) {
            JOptionPane.showMessageDialog(this, "請先登入！");
            dispose();
            return;
        }

        // ===== 標題與登入者資訊 =====
        JLabel title = new JLabel("📕 還書作業", SwingConstants.CENTER);
        title.setFont(new Font("微軟正黑體", Font.BOLD, 22));
        title.setForeground(new Color(60, 80, 120));

        JLabel userLabel = new JLabel(
                "會員：" + current.getEmail() + "（ID：" + current.getId() + "）",
                SwingConstants.CENTER);
        userLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        userLabel.setForeground(Color.DARK_GRAY);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 247, 250));
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(userLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // ===== 表格設定（新增「書名」欄位）=====
        String[] columnNames = {"借閱ID", "書籍ID", "書名", "借出時間", "到期時間", "狀態", "罰金"};
        tableModel = new DefaultTableModel(columnNames, 0);
        loanTable = new JTable(tableModel);
        loanTable.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        loanTable.getTableHeader().setFont(new Font("微軟正黑體", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(loanTable);
        add(scrollPane, BorderLayout.CENTER);

        // ===== 按鈕區 =====
        JButton returnBtn = new JButton("📗 確認還書");
        returnBtn.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        returnBtn.setBackground(new Color(230, 240, 250));
        returnBtn.setFocusPainted(false);
        returnBtn.setBorderPainted(false);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(245, 247, 250));
        btnPanel.add(returnBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // ===== 載入借閱資料 =====
        loadLoanData(current.getId());

        // ===== 還書事件 =====
        returnBtn.addActionListener(e -> {
            int selectedRow = loanTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "請先選擇要歸還的書籍！");
                return;
            }

            int loanId = (int) tableModel.getValueAt(selectedRow, 0);
            boolean result = loanService.returnBook(loanId);

            if (result) {
                JOptionPane.showMessageDialog(this, "✅ 還書成功！");
                loadLoanData(current.getId());
            } else {
                JOptionPane.showMessageDialog(this, "❌ 還書失敗，請確認借閱紀錄是否存在！");
            }
        });

        setVisible(true);
    }

    private void loadLoanData(int memberId) {
        tableModel.setRowCount(0);
        List<Loan> loans = loanService.getLoansByMember(memberId);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Loan l : loans) {
            String statusText = "BORROWED".equals(l.getStatus()) ? "借閱中" : "已歸還";
            if (!"RETURNED".equals(l.getStatus())) {
                tableModel.addRow(new Object[]{
                        l.getId(),
                        l.getBookId(),
                        l.getBookTitle(), // ✅ 新增書名顯示
                        fmt.format(l.getBorrowedAt()),
                        fmt.format(l.getDueAt()),
                        statusText,
                        l.getFineAmount()
                });
            }
        }
    }
}
