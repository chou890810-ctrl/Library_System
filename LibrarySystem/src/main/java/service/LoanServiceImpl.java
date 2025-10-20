package service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import dao.BookDao;
import dao.LoanDao;
import dao.impl.BookDaoImpl;
import dao.impl.LoanDaoImpl;
import model.Loan;

/**
 * LoanServiceImpl
 * 借還書邏輯層：負責借書、還書、罰金計算與查詢紀錄
 */
public class LoanServiceImpl implements LoanService {

    private final LoanDao loanDao = new LoanDaoImpl();
    private final BookDao bookDao = new BookDaoImpl();

    /**
     * 借書流程：
     * 1️⃣ 檢查庫存
     * 2️⃣ 庫存減一
     * 3️⃣ 新增借閱紀錄
     */
    @Override
    public boolean borrowBook(Loan loan) {
        try {
            int currentStock = bookDao.getStockById(loan.getBookId());
            if (currentStock <= 0) {
                System.out.println("❌ 庫存不足，無法借書");
                return false;
            }

            int insertResult = loanDao.insert(loan);
            if (insertResult > 0) {
                // 借出成功 → 庫存 -1
                return bookDao.updateStock(loan.getBookId(), -1) > 0;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 還書流程：
     * 1️⃣ 根據 loanId 找借閱紀錄
     * 2️⃣ 計算罰金
     * 3️⃣ 更新狀態 → 已歸還
     * 4️⃣ 書庫 +1
     */
    @Override
    public boolean returnBook(int loanId) {
        try {
            Loan target = loanDao.findById(loanId);
            if (target == null) {
                System.out.println("❌ 找不到這筆借閱紀錄");
                return false;
            }

            double fine = calculateFine(target);

            int updateResult = loanDao.updateReturnStatus(loanId, fine);
            if (updateResult <= 0) {
                System.out.println("❌ 更新借閱狀態失敗");
                return false;
            }

            // 書庫 +1
            return bookDao.updateStock(target.getBookId(), 1) > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ✅ 罰金計算：
     * 超過 7 天 → 每天 10 元
     */
    private double calculateFine(Loan loan) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(loan.getDueAt())) {
            long daysLate = Duration.between(loan.getDueAt(), now).toDays();
            return daysLate * 10;
        }
        return 0.0;
    }

    /**
     * 取得所有借閱紀錄
     */
    @Override
    public List<Loan> getAllLoans() {
        return loanDao.findAll();
    }

    /**
     * 依會員 ID 查借閱紀錄
     */
    @Override
    public List<Loan> getLoansByMember(int memberId) {
        return loanDao.findByMemberId(memberId);
    }

	@Override
	public boolean borrowBook(int i, int j) {
		// TODO Auto-generated method stub
		return false;
	}
}
