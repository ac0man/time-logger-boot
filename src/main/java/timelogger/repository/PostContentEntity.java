package timelogger.repository;

import lombok.Getter;

public class PostContentEntity {

    @Getter
    private String id;

    @Getter
    private String text;

    @Getter
    private String created_on;

    @Getter
    private String created_at;

    PostContentEntity() {}

    /**
     * 画面からの入力内容を格納する際に実行される
     *
     * @param text
     * @param created_at
     */
    PostContentEntity(String text, String created_at) {
        this.text = text;
        this.created_at = created_at;
    }

    /**
     * select の実行結果を格納する際に実行される
     *
     * @param id
     * @param text
     * @param created_on
     * @param created_at
     */
    PostContentEntity(String id, String text, String created_on, String created_at) {
        this.id = id;
        this.text = text;
        this.created_on = created_on;
        this.created_at = created_at;
    }
}
