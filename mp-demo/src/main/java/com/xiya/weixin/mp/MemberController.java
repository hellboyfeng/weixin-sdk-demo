package com.xiya.weixin.mp;

import com.riversoft.weixin.mp.card.Cards;
import com.riversoft.weixin.mp.card.bean.BaseInfo;
import com.riversoft.weixin.mp.card.bean.Coupon;
import com.riversoft.weixin.mp.card.bean.Member;
import com.riversoft.weixin.mp.care.CareMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/member")
public class MemberController {

    @Value("${url}")
    private String url;
    private static Logger logger = LoggerFactory.getLogger(MemberController.class);



    @RequestMapping("/create")
    @ResponseBody
    public String createMember(){
        Member member = new Member();
        member.setPrerogative("消费使用");
     /*   member.setAutoActivate(true);*/
        member.setWxActivate(false);
        member.setSupplyBonus(true);
        member.setSupplyBalance(true);
        member.setActivateUrl("http://www.xiya3333.com");


        BaseInfo baseInfo = new BaseInfo();
        baseInfo.setLogoUrl("http://mmbiz.qpic.cn/mmbiz_jpg/ON84cr4Rib6M7kPADl39MIwc4OOJmWHnvfSkTu4NtKKkCjd7AwgwS2G8nsLKibZzHLQESBJaeGt7tAMN3gYxYyrw/0?wx_fmt=jpeg");
        baseInfo.setCodeType("CODE_TYPE_BARCODE");
        baseInfo.setBrandName("西亚和美");
        baseInfo.setTitle("钻石卡");
        baseInfo.setColor("Color010");
        baseInfo.setNotice("消费使用");
        baseInfo.setDescription("测试使用的会员卡");
        baseInfo.setSku(new BaseInfo.Sku(20));
        /* baseInfo.setUseCustomCode(true);
        baseInfo.setGetCustomCodeMode("GET_CUSTOM_CODE_MODE_DEPOSIT");*/


        BaseInfo.DateInfo dateInfo = new BaseInfo.DateInfo();
        dateInfo.setType(BaseInfo.DateInfo.DateInfoType.DATE_TYPE_FIX_TERM);
        dateInfo.setFixedTerm(60);
        dateInfo.setFixedTermBegin(0);


        baseInfo.setDateInfo(dateInfo);
        baseInfo.setServicePhone("6192632");

        member.setBaseInfo(baseInfo);
        Cards.defaultCards().member(member);
        return "";
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

    @RequestMapping("/active")
    @ResponseBody
    public String active(){
        String code = "303702516424";
        String cardid = "p2zKvjqh6J-K9EVdRV21zpa0Pwg0";
        String number = "123456789";
        String res = Cards.defaultCards().memberActive(cardid,code,number);
        return res;
    }




}
