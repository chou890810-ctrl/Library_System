package dao;

import model.Loan;
import java.util.List;

public interface LoanDao {
    // 新增借閱紀錄（借書）
    int insert(Loan loan);

    // 更新還書狀態（還書）
    int updateReturnStatus(int loanId, double fineAmount);

    // 查詢所有借閱紀錄
    List<Loan> findAll();

    // 查詢某位會員的借閱紀錄
    List<Loan> findByMemberId(int memberId);

	Loan findById(int loanId);
    
    
}