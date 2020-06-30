package timelogger.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import timelogger.repository.SqlExecute;

@Service
public class TLService {

    @Autowired
    SqlExecute sqlExecute;

    @Autowired
    HttpSession session;

    /**
     * 初回リクエスト時に実行される。DB照会結果をリストとして ModelAndView に格納する。
     *
     * @param index フォワード先を index.html に設定した ModelAndView オブジェクト
     * @return レコード一覧を格納した ModelAndView オブジェクト
     */
    public ModelAndView doInitLogic(ModelAndView index) {
        // DB照会処理を呼び出し、結果を ModelAndView に格納したものを画面に返却する
        return setSelectContentsResult(index);
    }


    /**
     * 投稿内容が POST された際に実行される。永続化処理と投稿一覧取得処理を行う。
     *
     * @param index フォワード先を index.html に設定した ModelAndView オブジェクト
     * @param text 投稿内容
     * @param token token 二重送信チェック用トークン
     * @return レコード一覧を格納した ModelAndView オブジェクト
     */
    public ModelAndView doSubmitLogic(String text, String token, ModelAndView index) {
        // 永続化処理を呼び出す
        excuteInsertion(text, token);

        // DB照会処理を呼び出し、結果を ModelAndView に格納したものを画面に返却する
        return setSelectContentsResult(index);
    }


    /**
     * 全件DB照会を行い、引数でに受け取った ModelAndView オブジェクトに照会結果を格納する
     *
     * @return レコード一覧を格納した ModelAndView オブジェクト
     */
    private ModelAndView setSelectContentsResult(ModelAndView index) {

        // TODO findAll で取得する際に、SqlExcute クラス側の処理で １つずつ Map (もしくは Bean) に格納するように修正を行う

        List<Map<String, Object>> contentsList = sqlExecute.findAllOnToday();
        if (contentsList != null && contentsList.size() > 0) {
            // リスト内の編集

        }
        return index.addObject("contentsList", contentsList);
    }


    /**
     * token チェック、および insert処理を行う
     * 正常終了した場合、新しいトークンをセッションに格納する処理を呼び出す
     *
     * @param text 入力内容
     * @param token 二重送信チェック用トークン
     */
    private void excuteInsertion(String text, String token) {
        String trimmed = trimText(text);

        // DBへの永続化を行う
        if (hasVerificateOfExcuteInsertion(text, token)) {
            LocalDate created_on = LocalDate.now();
            LocalTime created_at = LocalTime.now();
            int affectedRows = sqlExecute.insertRecord(trimmed, created_on, created_at);

            // insert 処理が成功した場合、新しいトークンをセッションに格納する
            if (affectedRows > 0) {
                setTokenOnSession(token);
            }
        }
    }


    /**
     * 入力内容の先頭、および末尾の空白文字 (全角空白を含む) を除去する
     * 処理結果として空文字、または先頭・末尾の空白が除去された文字列を返却する
     *
     * @param text 入力内容
     * @return trim 済の入力内容
     */
    private String trimText(String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("^[　|\\s]+|[　|\\s]+$", "");
    }


    /**
     * 永続化処理を実行における正当性を検証する
     * token が一致しない、または text が無効な文字列の場合、false を返却する
     *
     * @param text 入力内容
     * @param token 二重送信チェック用トークン
     * @return trim 済の入力内容
     */
    private boolean hasVerificateOfExcuteInsertion(String text, String token) {
        if (token == null) {
            return false;
        }
        String origin = getTokenFromSession();

        if (!token.equals(origin)) {
            return false;
        }
        if ("".equals(text)) {
            return false;
        }
        return true;
    }


    /**
     * session スコープに二重送信チェック用トークンを格納する
     *
     * @param token 二重送信チェック用トークン
     */
    private void setTokenOnSession(String token) {
        String origin = getTokenFromSession();
        // session 内のトークンとクライアントから送信されたトークンが一致した場合
        if (origin.equals(token)) {
            String miliSec = String.valueOf(Instant.now().toEpochMilli());
            session.setAttribute("token", miliSec);
        }
    }


    /**
     * session から取得した token を返却する
     * 取得した結果が null の場合、空文字を返却する
     *
     * @return session から取得した token、または空文字
     */
    private String getTokenFromSession() {
        Object token = session.getAttribute("token");
        return (token == null) ? "" : (String) token;
    }
}
