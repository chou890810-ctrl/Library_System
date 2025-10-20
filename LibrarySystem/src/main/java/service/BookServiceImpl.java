package service;

import java.util.List;

import dao.BookDao;
import dao.impl.BookDaoImpl;
import model.Book;

/**
 * BookServiceImpl
 * 書籍服務層：封裝書籍查詢、新增、庫存修改邏輯
 */
public class BookServiceImpl implements BookService {

    private final BookDao bookDao = new BookDaoImpl();

    @Override
    public List<Book> findAllBooks() {
        return bookDao.findAll();
    }

    @Override
    public Book findBookById(int id) {
        return bookDao.findById(id);
    }

    @Override
    public boolean addBook(Book book) {
        return bookDao.insert(book) > 0;
    }

    @Override
    public boolean updateStock(int bookId, int change) {
        return bookDao.updateStock(bookId, change) > 0;
    }

    @Override
    public int getStockById(int bookId) {
        return bookDao.getStockById(bookId);
    }
}

