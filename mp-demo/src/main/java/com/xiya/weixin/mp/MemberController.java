package com.xiya.weixin.mp;

import com.riversoft.weixin.mp.card.Cards;
import com.riversoft.weixin.mp.card.bean.BaseInfo;
import com.riversoft.weixin.mp.card.bean.Coupon;
import com.riversoft.weixin.mp.card.bean.Member;
import com.riversoft.weixin.mp.care.CareMessages;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;


@Controller
@RequestMapping("/member")
public class MemberController {

    @Value("${url}")
    private String url;
    private static Logger logger = LoggerFactory.getLogger(MemberController.class);


    /**
     * 会员卡创建
     * @return
     */
    @RequestMapping("/create")
    @ResponseBody
    public String createMember(){
        Member member = new Member();
        member.setPrerogative("会员卡特权说明");
        member.setBackgroundPic("https://mmbiz.qlogo.cn/mmbiz_jpg/ON84cr4Rib6MX8UugBP9uRmRxOkYqBVMACickSzpNRqYSZia0Y5Danqk3PvmHzeWcxYztTNR5KGKCyGkMuTDmlKEw/0?wx_fmt=jpeg");
     /*   member.setAutoActivate(true);*/
        member.setWxActivate(true);
        member.setSupplyBonus(true);
        member.setBonusUrl("http://www.xiya3333.com");
        member.setSupplyBalance(true);
        member.setBalanceUrl("http://www.xiya3333.com");

        /*自定义激活路径*/
        /*member.setActivateUrl("http://wx.xiya3333.com/business/#/register");*/


        BaseInfo baseInfo = new BaseInfo();
        baseInfo.setLogoUrl("http://mmbiz.qpic.cn/mmbiz_jpg/ON84cr4Rib6M7kPADl39MIwc4OOJmWHnvfSkTu4NtKKkCjd7AwgwS2G8nsLKibZzHLQESBJaeGt7tAMN3gYxYyrw/0?wx_fmt=jpeg");
        baseInfo.setCodeType("CODE_TYPE_BARCODE");
        baseInfo.setBrandName("西亚和美商业股份有限公司");
        baseInfo.setTitle("美食卡");
        baseInfo.setColor("Color100");
        baseInfo.setNotice("卡券使用提醒");
        baseInfo.setDescription("卡券使用说明");
        baseInfo.setSku(new BaseInfo.Sku(1000));
        /* baseInfo.setUseCustomCode(true);
        baseInfo.setGetCustomCodeMode("GET_CUSTOM_CODE_MODE_DEPOSIT");*/

        BaseInfo.DateInfo dateInfo = new BaseInfo.DateInfo();
        /*dateInfo.setType(BaseInfo.DateInfo.DateInfoType.DATE_TYPE_FIX_TERM);
        dateInfo.setFixedTerm(60);
        dateInfo.setFixedTermBegin(0);*/
        dateInfo.setType(BaseInfo.DateInfo.DateInfoType.DATE_TYPE_FIX_TIME_RANGE);
        dateInfo.setBeginTime(new Date());
        Calendar cl = Calendar.getInstance();
        cl.setTime(new Date());
        cl.add(Calendar.YEAR,2);
        dateInfo.setEndTime(cl.getTime());

        baseInfo.setDateInfo(dateInfo);
        baseInfo.setServicePhone("6192632");

        member.setBaseInfo(baseInfo);

        return  Cards.defaultCards().member(member);
    }


    /**
     * 会员卡二维码投放
     * @param cardid
     * @return
     */
    @RequestMapping("/create/qrcode")
    @ResponseBody
    public String createQrcode(@RequestParam(value="cardid") String cardid){
        String json = " {" +
                "\"action_name\": \"QR_CARD\", " +
                "\"expire_seconds\": 1800," +
                "\"action_info\": {" +
                "\"card\": {" +
                "\"card_id\": \"%s\"," +
                "\"outer_str\":\"门店/柜组号/自定义\""+
                "  }" +
                " }" +
                "}";
        logger.info("qrcode card: {}", cardid);
        String data = String.format(json,cardid);
        String res = Cards.defaultCards().createQrcode(data);
        return res;
    }


    /**
     * 激活会员卡
     * @param cardid
     * @param code
     * @param number
     * @param integral
     * @return
     */
    @RequestMapping("/active")
    @ResponseBody
    public String active(@RequestParam(value="cardid") String cardid,@RequestParam(value="code") String code,@RequestParam(value="number") String number,@RequestParam(value="integral") String integral){
        String res = Cards.defaultCards().memberActive(cardid,code,number,integral);
        return res;
    }



    @RequestMapping("/active/geturl")
    @ResponseBody
    public String activeGetUrl(@RequestParam(value="cardid") String cardid){
        String res = Cards.defaultCards().activeGetUrl(cardid);
        return res;
    }


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

    /**
     * 设置开卡字段
     * @return
     */
    @RequestMapping("/active/userform")
    @ResponseBody
    public String activeUserForm(@RequestParam(value="cardid") String cardid) {
        String response = Cards.defaultCards().activateUserform(cardid);
        return response;

    }



}
