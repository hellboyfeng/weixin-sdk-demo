package com.xiya.weixin.mp;

import com.google.common.collect.Lists;
import com.riversoft.weixin.mp.card.Cards;
import com.riversoft.weixin.mp.card.bean.BaseInfo;
import com.riversoft.weixin.mp.card.bean.Coupon;
import com.riversoft.weixin.mp.card.bean.Discount;
import com.riversoft.weixin.mp.card.bean.Member;
import com.riversoft.weixin.mp.care.CareMessages;
import com.riversoft.weixin.mp.message.MpMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/card")
public class CardController {

    @Value("${url}")
    private String url;
    private static Logger logger = LoggerFactory.getLogger(CardController.class);


    /**
     * 删除卡卷
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public String config(@RequestParam(value="cardid") String cardid) {
        String response = Cards.defaultCards().deleteCard(cardid);
        return response;

    }

    @RequestMapping("/create/coupon")
    @ResponseBody
    public String createCoupon(String detail){
        Coupon coupon = new Coupon();
        coupon.setDetail("优惠详情");

        BaseInfo baseInfo = new BaseInfo();
        baseInfo.setLogoUrl("http://mmbiz.qpic.cn/mmbiz_jpg/ON84cr4Rib6M7kPADl39MIwc4OOJmWHnvfSkTu4NtKKkCjd7AwgwS2G8nsLKibZzHLQESBJaeGt7tAMN3gYxYyrw/0?wx_fmt=jpeg");
        baseInfo.setCodeType("CODE_TYPE_QRCODE");
        baseInfo.setBrandName("西亚和美商业股份有限公司");
        baseInfo.setTitle("2折卡优惠券");
        baseInfo.setColor("Color100");
        baseInfo.setNotice("请出示二维码");
        baseInfo.setDescription("不可与其他优惠同享");
        baseInfo.setSku(new BaseInfo.Sku(0));

        BaseInfo.DateInfo dateInfo = new BaseInfo.DateInfo();
        dateInfo.setType(BaseInfo.DateInfo.DateInfoType.DATE_TYPE_FIX_TERM);
        dateInfo.setFixedTerm(30);
        dateInfo.setFixedTermBegin(0);
        baseInfo.setDateInfo(dateInfo);
        baseInfo.setServicePhone("6192632");
        baseInfo.setUseCustomCode(true);
        baseInfo.setGetCustomCodeMode("GET_CUSTOM_CODE_MODE_DEPOSIT");
        baseInfo.setGetLimit(10);

        coupon.setBaseInfo(baseInfo);
        Cards.defaultCards().coupon(coupon);
        return "";
    }

    @RequestMapping("/deposit")
    @ResponseBody
    public String deposit(@RequestParam(value="cardid") String cardid,@RequestParam(value="codes")String codes){
        String json = "{\"card_id\":\"%s\",\"code\":%s}";
        logger.info("deposit card: {}", cardid);
        String data = String.format(json,cardid,codes);
        Cards.defaultCards().deposit(data);
        return "";
    }

    @RequestMapping("/check")
    @ResponseBody
    public String check(@RequestParam(value="cardid") String cardid,@RequestParam(value="codes")String codes){
        String json = "{\"card_id\":\"%s\",\"code\":%s}";
        logger.info("check card: {}", cardid);
        String data = String.format(json,cardid,codes);
        String res = Cards.defaultCards().checkCode(data);
        return res;
    }

    @RequestMapping("/modifystock")
    @ResponseBody
    public String modifyStock(@RequestParam(value="cardid") String cardid,@RequestParam(value="add")String add,@RequestParam(value="cut")String cut){
        String json = "{\"card_id\":\"%s\",\"increase_stock_value\":%s,\"reduce_stock_value\":%s}";
        logger.info("modifysotck card: {}", cardid);
        String data = String.format(json,cardid,add,cut);
        String res = Cards.defaultCards().modifyStock(data);
        return res;
    }



    @RequestMapping("/create/qrcode")
    @ResponseBody
    public String createQrcode(@RequestParam(value="cardid") String cardid){
        String json = " {" +
                "\"action_name\": \"QR_CARD\", " +
                "\"expire_seconds\": 1800," +
                "\"action_info\": {" +
                "\"card\": {" +
                "\"card_id\": \"%s\" " +
                "  }" +
                " }" +
                "}";
        logger.info("qrcode card: {}", cardid);
        String data = String.format(json,cardid);
        String res = Cards.defaultCards().createQrcode(data);
        return res;
    }


    @RequestMapping("/send")
    @ResponseBody
    public String send(@RequestParam(value="cardid") String cardid){
        CareMessages.defaultCareMessages().card("o2zKvjkNgkA6ZCEhqn80AlTEc340", cardid);
        return "";
    }



    @RequestMapping("/searchcode")
    @ResponseBody
    public String searchCode(@RequestParam(value="cardid") String cardid,@RequestParam(value="code")String code){
        logger.info("get card: {}", cardid);
        String res = Cards.defaultCards().searchCode(cardid,code);
        return res;
    }

    @RequestMapping("/consumecode")
    @ResponseBody
    public String consumeCode(@RequestParam(value="cardid") String cardid,@RequestParam(value="code")String code){
        logger.info("consume card: {}", cardid);
        String res = Cards.defaultCards().consume(cardid,code);
        return res;
    }


}
