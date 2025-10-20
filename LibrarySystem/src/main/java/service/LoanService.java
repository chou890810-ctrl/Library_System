package service;

import java.util.List;

import model.Loan;

/**
 * LoanService
 * 借閱服務層介面：定義借書、還書、查詢紀錄等操作
 */
public interface LoanService {

    /**
     * 借書作業
     * @param loan 借閱物件（包含 memberId, bookId, borrowedAt, dueAt 等）
     * @return true=借書成功
     */
    boolean borrowBook(Loan loan);

    /**
     * 還書作業
     * @param loanId 借閱紀錄 ID
     * @return true=還書成功
     */
    boolean returnBook(int loanId);

    /**
     * 查詢所有借閱紀錄
     * @return List<Loan>
     */
    List<Loan> getAllLoans();

    /**
     * 依會員 ID 查詢該會員借閱紀錄
     * @param memberId 會員編號
     * @return List<Loan>
     */
    List<Loan> getLoansByMember(int memberId);

	boolean borrowBook(int i, int j);
}
