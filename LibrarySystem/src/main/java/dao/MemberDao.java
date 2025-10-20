package dao;


import java.util.List;

import model.Member;

public interface MemberDao {
		//新增會員
		int insert(Member member);
		
		//以Email查找會員(登入會用)
		Member findByEmail(String email);
		
		//查詢所有會員
		List <Member> findAll();
		
		public Member findByEmailAndPassword(String email, String password);
		
		
		
}
