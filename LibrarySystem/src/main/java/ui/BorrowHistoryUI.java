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
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import model.Loan;
import model.Member;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import service.LoanService;
import service.LoanServiceImpl;
import util.SessionManager;

public class BorrowHistoryUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private LoanService loanService = new LoanServiceImpl();

    public BorrowHistoryUI() {
    	setIconImage(new ImageIcon(getClass().getResource("/images/book_logo.png")).getImage());
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/book_logo.png"));
        setTitle("📜 我的借閱紀錄");
        setSize(850, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 247, 250)); // 統一主題色

        Member current = SessionManager.getCurrentMember();
        if (current == null) {
            JOptionPane.showMessageDialog(this, "請先登入！");
            dispose();
            return;
        }

        // ===== 標題與登入資訊 =====
        JLabel title = new JLabel("📜 我的借閱紀錄", SwingConstants.CENTER);
        title.setFont(new Font("微軟正黑體", Font.BOLD, 22));
        title.setForeground(new Color(60, 80, 120));

        JLabel memberInfo = new JLabel("會員：" + current.getEmail() + "（ID：" + current.getId() + "）", SwingConstants.CENTER);
        memberInfo.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        memberInfo.setForeground(Color.DARK_GRAY);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 247, 250));
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(memberInfo, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // ===== 表格區 =====
        String[] columnNames = {"借閱ID", "書籍ID", "書名", "作者", "借出時間", "到期時間", "歸還時間", "狀態", "罰金"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        table.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("微軟正黑體", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // ===== 按鈕區 =====
        JButton exportBtn = new JButton("📁 匯出 Excel");
        JButton closeBtn = new JButton("❌ 關閉");

        exportBtn.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        closeBtn.setFont(new Font("微軟正黑體", Font.BOLD, 16));

        exportBtn.setBackground(new Color(230, 240, 250));
        closeBtn.setBackground(new Color(230, 240, 250));
        exportBtn.setFocusPainted(false);
        closeBtn.setFocusPainted(false);
        exportBtn.setBorderPainted(false);
        closeBtn.setBorderPainted(false);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(245, 247, 250));
        btnPanel.add(exportBtn);
        btnPanel.add(closeBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // ===== 載入資料 =====
        loadData(current.getId());

        // ===== 匯出 Excel =====
        exportBtn.addActionListener(e -> exportToExcel());

        // ===== 關閉 =====
        closeBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void loadData(int memberId) {
        model.setRowCount(0);
        List<Loan> loans = loanService.getLoansByMember(memberId);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Loan l : loans) {
            String statusText = "BORROWED".equals(l.getStatus()) ? "借閱中" : "已歸還";
            String returnTime = (l.getReturnedAt() != null) ? fmt.format(l.getReturnedAt()) : "尚未歸還";

            model.addRow(new Object[]{
                    l.getId(),
                    l.getBookId(),
                    l.getBookTitle(),
                    l.getBookAuthor(),
                    fmt.format(l.getBorrowedAt()),
                    fmt.format(l.getDueAt()),
                    returnTime,
                    statusText,
                    l.getFineAmount()
            });
        }
    }

    private void exportToExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("借閱紀錄");

            // 標題列
            Row header = sheet.createRow(0);
            for (int i = 0; i < model.getColumnCount(); i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(model.getColumnName(i));
            }

            // 資料列
            for (int r = 0; r < model.getRowCount(); r++) {
                Row row = sheet.createRow(r + 1);
                for (int c = 0; c < model.getColumnCount(); c++) {
                    Object value = model.getValueAt(r, c);
                    row.createCell(c).setCellValue(value != null ? value.toString() : "");
                }
            }

            String fileName = "借閱紀錄_" + System.currentTimeMillis() + ".xlsx";
            try (FileOutputStream out = new FileOutputStream(fileName)) {
                workbook.write(out);
            }

            JOptionPane.showMessageDialog(this, "✅ 匯出成功！檔名：" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ 匯出失敗：" + e.getMessage());
        }
    }
}
