package timelogger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import timelogger.service.TLService;

/**
 * "/" に対するリクエストを受け付けるコントローラークラス
 */
@Controller
@RequestMapping("/")
public class TLController {

    @Autowired
    TLService tLService;

    /**
     * Getリクエストを受け付け、投稿内容の一覧を返却するメソッド
     *
     * @param index : ModelAndView オブジェクト
     * @return index.html View
     */
    @GetMapping
    public ModelAndView init(ModelAndView index) {
        index.setViewName("index");
        return tLService.doInitLogic(index);
    }


    /**
     * Postリクエストを受け付け、永続化処理の実行、および投稿内容の一覧を返却するメソッド
     *
     * @param text : 投稿内容
     * @param token : 二重送信チェック用トークン
     * @return index.html ビュー
     */
    @PostMapping
    public ModelAndView submit(
                @RequestParam(value="text", required=true) String text,
                @RequestParam(value="token") String token,
                ModelAndView index) {

        index.setViewName("index");
        return tLService.doSubmitLogic(text, token, index);
    }
}
