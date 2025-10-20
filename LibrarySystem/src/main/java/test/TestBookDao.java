package test;

import dao.BookDao;
import dao.impl.BookDaoImpl;
import model.Book;

public class TestBookDao {
    public static void main(String[] args) {
        BookDao dao = new BookDaoImpl();

        // 測試新增書籍
        Book b1 = new Book("9781234567897", "Java 入門", "王小明", 5);
        int result = dao.insert(b1);
        System.out.println("新增書籍結果: " + (result > 0 ? "成功" : "失敗"));

        // 測試模糊查詢（書名包含「Java」）
        System.out.println("查詢書名包含 'Java' 的書籍:");
        for (Book b : dao.findByTitle("Java")) {
            System.out.println(b);
        }

        // 測試查詢全部
        System.out.println("所有書籍:");
        for (Book b : dao.findAll()) {
            System.out.println(b);
        }

        // 測試更新庫存
        int updateResult = dao.updateStock(1, 10);
        System.out.println("更新庫存結果: " + (updateResult > 0 ? "成功" : "失敗"));
    }
}
