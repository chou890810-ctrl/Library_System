package dao;

import java.util.List;

import model.Book;

public interface BookDao {
	//新增一本書
	int insert(Book book);
	 
	//以書名查詢
	List<Book> findByTitle(String title);
	
	//查詢全部書籍
	List<Book> findAll();
	
	//更新庫存數量
	int updateStock(int bookId, int change);
	
	
	int getStockById(int bookId);

	Book findById(int id);

	
}
