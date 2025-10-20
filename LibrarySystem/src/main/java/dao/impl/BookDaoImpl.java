package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.BookDao;
import model.Book;
import util.DbConnection;

public class BookDaoImpl implements BookDao {

	@Override
	public int insert(Book book) {
		String sql="INSERT INTO book(isbn,title,author,stock,price)values(?,?,?,?)";
		try(Connection conn=DbConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql)){
			ps.setString(1,book.getIsbn());
			ps.setString(2,book.getTitle());
			ps.setString(3,book.getAuthor());
			ps.setInt(4,book.getStock());
			return ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}

	@Override
	public List<Book> findByTitle(String title) {
		String sql="SELECT*FROM book WHERE title LIKE ?";
		List<Book> list = new ArrayList<>();
		try(Connection conn=DbConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql)){
			ps.setString(1, "%"+title+"%");
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()) {
				Book b =new Book();
				b.setId(rs.getInt("Id"));
				b.setIsbn(rs.getString("isbn"));
				b.setTitle(rs.getString("title"));
				b.setAuthor(rs.getString("author"));
				b.setStock(rs.getInt("stock"));
				list.add(b);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Book> findAll() {
		String sql="SELECT*FROM book";
		List<Book> list =new ArrayList<>();
		try(Connection conn=DbConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql);
		ResultSet rs=ps.executeQuery()){
		
			
			while(rs.next()) {
				Book b=new Book();
				b.setId(rs.getInt("id"));
				b.setIsbn(rs.getString("isbn"));
				b.setTitle(rs.getString("title"));
				b.setAuthor(rs.getString("author"));
				b.setStock(rs.getInt("stock"));
				list.add(b);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int updateStock(int bookId, int change) {
	    // 確保庫存不會變成負數
	    String sql = "UPDATE book SET stock = stock + ? WHERE id = ? AND stock + ? >= 0";
	    try (Connection conn = DbConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, change);
	        ps.setInt(2, bookId);
	        ps.setInt(3, change); // 給條件判斷用
	        return ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return 0;
	    }
	}

	@Override
	public int getStockById(int bookId) {
	    String sql = "SELECT stock FROM book WHERE id = ?";
	    try (Connection conn = DbConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, bookId);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getInt("stock");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return 0; // 找不到就回傳 0
	}

	@Override
	public Book findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
