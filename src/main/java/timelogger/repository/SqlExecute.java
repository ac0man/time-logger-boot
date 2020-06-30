package timelogger.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * SQL を実行するクラス
 */
@Configuration
public class SqlExecute {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Beanとして登録して利用、テーブル初期化用（開発のみ）
     * 同名のテーブルが存在する場合は削除を行い、新規テーブルを作成する
     *
     * @return null
     */
    @Bean
    public String initTable() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS contents;");
        jdbcTemplate.execute(
                "CREATE TABLE contents (id SERIAL PRIMARY KEY, text TEXT NOT NULL, created_on DATE NOT NULL, created_at TIME(0) NOT NULL);");
        return null;
    }

    /**
     * 入力内容と入力時刻を永続化する
     *
     * @param text       入力内容
     * @param created_at 入力時刻
     * @param created_on 当日日付
     * @return null
     */
    public int insertRecord(String text, LocalDate created_on, LocalTime created_at) {
        int affectedRows;

        try {
            affectedRows = jdbcTemplate.update("INSERT INTO contents (text, created_on, created_at) VALUES (?, ?, ?);", text, created_on, created_at);
        } catch (DataAccessException e) {
            affectedRows = 0;
        }
        return affectedRows;
    }

    /**
     * レコードを全件取得する
     *
     * @return DB照会結果を List<Map<カラム名, 値>> に格納して返却する
     */
    public List<Map<String, Object>> findAllOnToday() {
        return jdbcTemplate.queryForList("SELECT * FROM contents where created_on = current_date ORDER BY id DESC;");
    }
}
