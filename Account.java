import java.util.*;

public class Account {

    //cache存取最大限制
    private static final Integer MAX_ACCOUNT_SIZE = 50;

    //設定權限為private，讓其他人只能透過getInstance取得Account資料
    private static Account account;
    private static Map<String, Account> CACHE_KEY_ACCOUNT = new HashMap<>();
    private static LinkedList<String> accessOrder;
    private String key;

    //禁止實例化(new Account)
    private Account(String key) {
        this.key = key;
    }

    //建構方法設為private，禁止new Account
    private Account(){
        throw new IllegalStateException("Can't create instance of Account");
    }

    //加上synchronized，防止getInstance方法發生衝突
    public static synchronized Account getInstance(String key) {
        if (account == null) {
            account = new Account(key);
            init();
        } else if (!account.key.equals(key)) {
            // 如果 key 不同，則更新 account
            account = new Account(key);
        }

        updateCache(key);

        return account;
    }

    //初始化CACHE_KEY_ACCOUNT和accessOrder
    private static void init() {
        // 從資料庫載入資料，假設載入一個帳號大約需要10秒(不必實作載入的動作)
        CACHE_KEY_ACCOUNT = new HashMap<>();
        accessOrder = new LinkedList<>();
    }

    //更新 cache，僅保留最近存取的 50 筆資料(刪除第一筆資料、新增一筆資料)
    private static void updateCache(String key) {
        CACHE_KEY_ACCOUNT.put(key, account);
        accessOrder.addLast(key);

        if (accessOrder.size() > MAX_ACCOUNT_SIZE) {
            String oldKey = accessOrder.removeFirst();
            CACHE_KEY_ACCOUNT.remove(oldKey);
        }
    }
}
