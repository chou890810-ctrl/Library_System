package test;

import service.LoanService;
import service.LoanServiceImpl;

public class TestLoanService {

    public static void main(String[] args) {
        LoanService service = new LoanServiceImpl();

        // 1️⃣ 測試借書流程
        System.out.println("=== 借書測試 ===");
        boolean borrowResult = service.borrowBook(1, 2); // 假設會員ID=1、書籍ID=2
        if (borrowResult) {
            System.out.println("借書成功！");
        } else {
            System.out.println("借書失敗！");
        }

        // 2️⃣ 測試還書流程
        System.out.println("\n=== 還書測試 ===");
        boolean returnResult = service.returnBook(1); // 假設借閱紀錄ID=1
        if (returnResult) {
            System.out.println("還書成功！");
        } else {
            System.out.println("還書失敗！");
        }
    }
}