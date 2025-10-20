package dao.impl;

import dao.LoanDao;
import model.Loan;
import util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LoanDaoImpl implements LoanDao {

    @Override
    public int insert(Loan loan) {
        String sql = "INSERT INTO loan(member_id, book_id, borrowed_at, due_at, status, fine_amount) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, loan.getMemberId());
            ps.setInt(2, loan.getBookId());
            ps.setTimestamp(3, Timestamp.valueOf(loan.getBorrowedAt()));
            ps.setTimestamp(4, Timestamp.valueOf(loan.getDueAt()));
            ps.setString(5, loan.getStatus());
            ps.setDouble(6, loan.getFineAmount());

            return ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updateReturnStatus(int loanId, double fineAmount) {
        String sql = "UPDATE loan SET returned_at = ?, status = ?, fine_amount = ? WHERE id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now())); // 更新還書時間
            ps.setString(2, "RETURNED"); // 狀態改為已歸還
            ps.setDouble(3, fineAmount);
            ps.setInt(4, loanId);

            return ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Loan> findAll() {
        String sql = "SELECT * FROM loan";
        List<Loan> list = new ArrayList<>();
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Loan l = new Loan();
                l.setId(rs.getInt("id"));
                l.setMemberId(rs.getInt("member_id"));
                l.setBookId(rs.getInt("book_id"));
                l.setBorrowedAt(rs.getTimestamp("borrowed_at").toLocalDateTime());
                l.setDueAt(rs.getTimestamp("due_at").toLocalDateTime());
                Timestamp returned = rs.getTimestamp("returned_at");
                if (returned != null) {
                    l.setReturnedAt(returned.toLocalDateTime());
                }
                l.setStatus(rs.getString("status"));
                l.setFineAmount(rs.getDouble("fine_amount"));
                list.add(l);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Loan> findByMemberId(int memberId) {
        String sql = "SELECT l.*, b.title AS book_title, b.author AS book_author " +
                     "FROM loan l " +
                     "JOIN book b ON l.book_id = b.id " +
                     "WHERE l.member_id = ?";

        List<Loan> list = new ArrayList<>();
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Loan l = new Loan();
                l.setId(rs.getInt("id"));
                l.setMemberId(rs.getInt("member_id"));
                l.setBookId(rs.getInt("book_id"));
                l.setBorrowedAt(rs.getTimestamp("borrowed_at").toLocalDateTime());
                l.setDueAt(rs.getTimestamp("due_at").toLocalDateTime());
                Timestamp returned = rs.getTimestamp("returned_at");
                if (returned != null) {
                    l.setReturnedAt(returned.toLocalDateTime());
                }
                l.setStatus(rs.getString("status"));
                l.setFineAmount(rs.getDouble("fine_amount"));
                // 新增欄位
                l.setBookTitle(rs.getString("book_title"));
                l.setBookAuthor(rs.getString("book_author"));
                list.add(l);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    
    @Override
    public Loan findById(int id) {
        String sql = "SELECT * FROM loan WHERE id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Loan loan = new Loan();
                loan.setId(rs.getInt("id"));
                loan.setMemberId(rs.getInt("member_id"));
                loan.setBookId(rs.getInt("book_id"));
                loan.setBorrowedAt(rs.getTimestamp("borrowed_at").toLocalDateTime());
                loan.setDueAt(rs.getTimestamp("due_at").toLocalDateTime());

                Timestamp returned = rs.getTimestamp("returned_at");
                if (returned != null) {
                    loan.setReturnedAt(returned.toLocalDateTime());
                }

                loan.setStatus(rs.getString("status"));
                loan.setFineAmount(rs.getDouble("fine_amount"));
                return loan;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
