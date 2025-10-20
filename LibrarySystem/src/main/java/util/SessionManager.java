package util;

import model.Member;

public class SessionManager {

    private static Member currentMember; // 存放目前登入的會員

    // 登入時設定會員
    public static void setCurrentMember(Member member) {
        currentMember = member;
    }

    // 取得目前登入的會員
    public static Member getCurrentMember() {
        return currentMember;
    }

    // 登出（清除目前會員）
    public static void logout() {
        currentMember = null;
    }

    // 檢查是否有登入
    public static boolean isLoggedIn() {
        return currentMember != null;
    }
}
