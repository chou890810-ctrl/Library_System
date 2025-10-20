package service;

import java.util.List;

import model.Book;

/**
 * BookService
 * 書籍服務層介面：提供書籍相關的操作定義
 */
public interface BookService {

    /**
     * 取得所有書籍資料
     * @return List<Book>
     */
    List<Book> findAllBooks();

    /**
     * 依書籍 ID 查詢書籍
     * @param id 書籍編號
     * @return Book 物件或 null
     */
    Book findBookById(int id);

    /**
     * 新增書籍
     * @param book 書籍物件
     * @return true=新增成功
     */
    boolean addBook(Book book);

    /**
     * 修改庫存數量（例如 +1 或 -1）
     * @param bookId 書籍 ID
     * @param change 庫存變化量（負值代表借出）
     * @return true=更新成功
     */
    boolean updateStock(int bookId, int change);

    /**
     * 查詢某書籍目前庫存
     * @param bookId 書籍 ID
     * @return int 庫存數量
     */
    int getStockById(int bookId);
}
