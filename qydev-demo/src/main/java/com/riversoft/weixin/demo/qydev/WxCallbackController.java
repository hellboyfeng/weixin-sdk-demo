package com.riversoft.weixin.demo.qydev;

import com.riversoft.weixin.common.decrypt.MessageDecryption;
import com.riversoft.weixin.common.exception.WxRuntimeException;
import com.riversoft.weixin.common.jsapi.JsAPISignature;
import com.riversoft.weixin.common.message.XmlMessageHeader;
import com.riversoft.weixin.common.util.JsonMapper;
import com.riversoft.weixin.demo.commons.DuplicatedMessageChecker;
import com.riversoft.weixin.qy.base.CorpSetting;
import com.riversoft.weixin.qy.contact.Users;
import com.riversoft.weixin.qy.contact.user.ReadUser;
import com.riversoft.weixin.qy.jsapi.JsAPIs;
import com.riversoft.weixin.qy.message.QyXmlMessages;
import com.riversoft.weixin.qy.oauth2.QyOAuth2s;
import com.riversoft.weixin.qy.oauth2.bean.QyUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by exizhai on 10/7/2015.
 */
@Controller
public class WxCallbackController {

    private static Logger logger = LoggerFactory.getLogger(WxCallbackController.class);

    /**
     * token/aesKey 是和企业号应用绑定的，这里为了演示方便使用外部注入，实际使用的时候一个企业号可能有多个应用，这样的话需要有个分发逻辑。
     * 比如callback url可以定义为  /wx/qy/[应用的ID]，通过ID查询不同的token和aesKey
     */
    @Value("${agent.default.token}")
    private String token;

    @Value("${agent.default.aesKey}")
    private String aesKey;

    @Value("${url}")
    private String url;

    @RequestMapping("/index")
    @ResponseBody
    public String index(String agentid,String code,String state){
        QyUser qyUser = QyOAuth2s.defaultOAuth2s().userInfo(code);
        ReadUser readUser = Users.defaultUsers().get(qyUser.getUserId());
        String createJson = JsonMapper.nonEmptyMapper().toJson(readUser);
        return createJson;
    }


    @RequestMapping("/auth")
    @ResponseBody
    public String auth(){
        JsAPISignature jsAPISignature = JsAPIs.defaultJsAPIs().createJsAPISignature(url);
        String createJson = JsonMapper.nonEmptyMapper().toJson(jsAPISignature);
        return createJson;
    }

    @RequestMapping("/groupauth")
    @ResponseBody
    public String groupauth(){
        JsAPISignature jsAPISignature = JsAPIs.defaultJsAPIs().createJsAPISignature(url);
        String createJson = JsonMapper.nonEmptyMapper().toJson(jsAPISignature);
        JsAPISignature gjsAPISignature = JsAPIs.defaultJsAPIs().createJsAPIGroupSignature(url);
        String createGroupJson = JsonMapper.nonEmptyMapper().toJson(gjsAPISignature);
        return createJson+"|"+createGroupJson;
    }

    /**
     * 企业号回调接口
     * 这里为了演示方便使用单个URL，实际使用的时候一个企业号可能有多个应用，这样的话需要有个分发逻辑：
     * 比如callback url可以定义为  /wx/qy/[应用的ID]，通过ID查询不同的token和aesKey
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @param content
     * @return
     */
    @RequestMapping("/wx/qy")
    @ResponseBody
    public String qy(@RequestParam(value="msg_signature") String signature,
                           @RequestParam(value="timestamp") String timestamp,
                           @RequestParam(value="nonce") String nonce,
                           @RequestParam(value="echostr", required = false) String echostr,
                           @RequestBody(required = false) String content) {

        logger.info("msg_signature={}, nonce={}, timestamp={}, echostr={}", signature, nonce, timestamp, echostr);

        CorpSetting corpSetting = CorpSetting.defaultSettings();

        try {
            MessageDecryption messageDecryption = new MessageDecryption(token, aesKey, corpSetting.getCorpId());
            if (!StringUtils.isEmpty(echostr)) {
                String echo = messageDecryption.decryptEcho(signature, timestamp, nonce, echostr);
                logger.info("消息签名验证成功.");
                return echo;
            } else {
                XmlMessageHeader xmlRequest = QyXmlMessages.fromXml(messageDecryption.decrypt(signature, timestamp, nonce, content));
                XmlMessageHeader xmlResponse = qyDispatch(xmlRequest);
                if(xmlResponse != null) {
                    try {
                        return messageDecryption.encrypt(QyXmlMessages.toXml(xmlResponse), timestamp, nonce);
                    } catch (WxRuntimeException e) {
                    }
                }
            }
        } catch (Exception e) {
            logger.error("callback failed.", e);
        }

        return "";
    }

    private XmlMessageHeader qyDispatch(XmlMessageHeader xmlRequest) {
        //添加处理逻辑

        //需要同步返回消息（被动响应消息）给用户则构造一个XmlMessageHeader类型，比较鸡肋，因为处理逻辑如果比较复杂响应太慢会影响用户感知，建议直接返回null；
        //如果有消息需要发送给用户则可以调用主动消息发送接口进行异步发送
        return null;
    }

}
