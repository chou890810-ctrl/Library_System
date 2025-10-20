package dao.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.MemberDao;
import model.Member;
import util.DbConnection;

public class MemberDaoImpl implements MemberDao {

	public static void main(String[] args) {
		

	}

	@Override
	public int insert(Member member) {
		String sql="INSERT INTO member(member_no,name,email,password_hash) VALUES(?,?,?,?)";
		try(Connection conn=DbConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql)){
			
			
			ps.setString(1,member.getMemberNo());
			ps.setString(2,member.getName());
			ps.setString(3,member.getEmail());
			ps.setString(4,member.getPasswordHash());
			
			return ps.executeUpdate();
			
			
		}catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		
	}

	@Override
	public Member findByEmail(String email) {
		String sql="SELECT * From member WHERE email=?";
		try(Connection conn=DbConnection.getConnection();
			PreparedStatement ps=conn.prepareStatement(sql)){
			ps.setString(1,email);
			ResultSet rs=ps.executeQuery();
			
			if(rs.next()) {
				Member m=new Member();
				m.setId(rs.getInt("id"));
				m.setMemberNo(rs.getString("member_no"));
				m.setName(rs.getString("name"));
				m.setEmail(rs.getString("email"));
				m.setPasswordHash(rs.getString("password_hash"));
				return m;
			}
			
		}catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Member> findAll() {
		String sql="SELECT*FROM member";
		List<Member>list =new ArrayList<>();
		
		try(Connection conn = DbConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql);
				ResultSet rs=ps.executeQuery()){
			while(rs.next()){
				Member m=new Member();
				m.setId(rs.getInt("id"));
				m.setMemberNo(rs.getString("member_no"));
				m.setName(rs.getString("name"));
				m.setEmail(rs.getString("email"));
	            m.setPasswordHash(rs.getString("password_hash"));
	            list.add(m);  
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return list;
	}
	
	
	
	@Override
	public Member findByEmailAndPassword(String email, String passwordHash) {
	    String sql = "SELECT * FROM member WHERE email=? AND password_hash=?";
	    try (Connection conn = DbConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, email);
	        ps.setString(2, passwordHash);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            Member m = new Member();
	            m.setId(rs.getInt("id"));
	            m.setMemberNo(rs.getString("member_no"));
	            m.setName(rs.getString("name"));
	            m.setEmail(rs.getString("email"));
	            m.setPasswordHash(rs.getString("password_hash"));
	            return m;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}
