package model;

import java.time.LocalDateTime;

public class Loan {
		private int id;
		private int memberId;
		private int bookId;
		private LocalDateTime borrowedAt;
		private LocalDateTime dueAt;
		private LocalDateTime returnedAt;
		private String status;
		private double fineAmount;
		private String bookTitle;
		private String bookAuthor;
		//空建構子 (給JDBC用)
		public Loan() {}
		
		//建構子 (建立借閱紀錄時用)
		public Loan(int memberId, int bookId, LocalDateTime borrowedAt, LocalDateTime dueAt, 
				String status, double fineAmount) {
			super();
			this.memberId = memberId;
			this.bookId = bookId;
			this.borrowedAt = borrowedAt;
			this.dueAt = dueAt;
			this.status = "BORROWED";
			this.fineAmount = 0.0;
		}

		
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getMemberId() {
			return memberId;
		}

		public void setMemberId(int memberId) {
			this.memberId = memberId;
		}

		public int getBookId() {
			return bookId;
		}

		public void setBookId(int bookId) {
			this.bookId = bookId;
		}

		public LocalDateTime getBorrowedAt() {
			return borrowedAt;
		}

		public void setBorrowedAt(LocalDateTime borrowedAt) {
			this.borrowedAt = borrowedAt;
		}

		public LocalDateTime getDueAt() {
			return dueAt;
		}

		public void setDueAt(LocalDateTime dueAt) {
			this.dueAt = dueAt;
		}

		public LocalDateTime getReturnedAt() {
			return returnedAt;
		}

		public void setReturnedAt(LocalDateTime returnedAt) {
			this.returnedAt = returnedAt;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public double getFineAmount() {
			return fineAmount;
		}

		public void setFineAmount(double fineAmount) {
			this.fineAmount = fineAmount;
		}
		
		public String getBookTitle() {
			return bookTitle;
		}
		
		public void setBookTitle(String bookTitle){
			this.bookTitle = bookTitle;
		}

		public String getBookAuthor() { return bookAuthor; }
		public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }
		
		@Override
		public String toString() {
			return "Loan{" +
	                "id=" + id +
	                ", memberId=" + memberId +
	                ", bookId=" + bookId +
	                ", borrowedAt=" + borrowedAt +
	                ", dueAt=" + dueAt +
	                ", returnedAt=" + returnedAt +
	                ", status='" + status + '\'' +
	                ", fineAmount=" + fineAmount +
	                '}';
			
		}
		
		
}
