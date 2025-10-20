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
import java.util.List;

import model.Book;
import model.Loan;
import model.Member;
import service.BookService;
import service.BookServiceImpl;
import service.LoanService;
import service.LoanServiceImpl;
import util.SessionManager;

public class BorrowBookUI extends JFrame {

    private JTable bookTable;
    private DefaultTableModel tableModel;
    private BookService bookService = new BookServiceImpl();
    private LoanService loanService = new LoanServiceImpl();

    public BorrowBookUI() {
    	setIconImage(new ImageIcon(getClass().getResource("/images/book_logo.png")).getImage());
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/book_logo.png"));
        setTitle("📘 借書作業");
        setSize(720, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 247, 250)); // 背景統一

        Member current = SessionManager.getCurrentMember();
        if (current == null) {
            JOptionPane.showMessageDialog(this, "請先登入！");
            dispose();
            return;
        }

        // ===== 標題區 =====
        JLabel title = new JLabel("📚 借書作業", SwingConstants.CENTER);
        title.setFont(new Font("微軟正黑體", Font.BOLD, 22));
        title.setForeground(new Color(60, 80, 120));

        JLabel userLabel = new JLabel(
                "登入帳號：" + current.getEmail() + "（ID：" + current.getId() + "）",
                SwingConstants.CENTER);
        userLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        userLabel.setForeground(Color.DARK_GRAY);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 247, 250));
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(userLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // ===== 表格區 =====
        String[] columns = {"書籍ID", "ISBN(書籍編號)", "書名", "作者", "庫存"};
        tableModel = new DefaultTableModel(columns, 0);
        bookTable = new JTable(tableModel);
        bookTable.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        bookTable.getTableHeader().setFont(new Font("微軟正黑體", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);

        // ===== 按鈕區 =====
        JButton borrowBtn = new JButton("📖 確認借書");
        borrowBtn.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        borrowBtn.setBackground(new Color(230, 240, 250));
        borrowBtn.setFocusPainted(false);
        borrowBtn.setBorderPainted(false);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(245, 247, 250));
        btnPanel.add(borrowBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // ===== 載入資料 =====
        loadBooks();

        // ===== 借書事件 =====
        borrowBtn.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "請先選擇要借閱的書籍！");
                return;
            }

            int bookId = (int) tableModel.getValueAt(selectedRow, 0);
            int stock = (int) tableModel.getValueAt(selectedRow, 4);

            if (stock <= 0) {
                JOptionPane.showMessageDialog(this, "⚠️ 這本書庫存不足！");
                return;
            }

            // ✅ 建立 Loan 物件
            Loan loan = new Loan();
            loan.setMemberId(current.getId());
            loan.setBookId(bookId);
            loan.setBorrowedAt(java.time.LocalDateTime.now());
            loan.setDueAt(java.time.LocalDateTime.now().plusDays(7)); // 預設借 7 天
            loan.setStatus("BORROWED");
            loan.setFineAmount(0.0);

            boolean result = loanService.borrowBook(loan);

            if (result) {
                JOptionPane.showMessageDialog(this, "✅ 借書成功！");
                loadBooks();
            } else {
                JOptionPane.showMessageDialog(this, "❌ 借書失敗，請稍後再試！");
            }
        });

        setVisible(true);
    }

    private void loadBooks() {
        tableModel.setRowCount(0);
        List<Book> books = bookService.findAllBooks();
        for (Book b : books) {
            tableModel.addRow(new Object[]{
                    b.getId(),
                    b.getIsbn(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getStock()
                    
            });
        }
    }
}
