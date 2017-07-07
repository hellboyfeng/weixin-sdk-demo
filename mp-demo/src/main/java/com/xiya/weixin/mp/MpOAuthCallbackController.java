package com.xiya.weixin.mp;

import com.riversoft.weixin.common.oauth2.AccessToken;
import com.riversoft.weixin.common.oauth2.OpenUser;
import com.xiya.weixin.mp.util.JsonMapper;
import com.riversoft.weixin.mp.oauth2.MpOAuth2s;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MpOAuthCallbackController {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(WxCallbackController.class);
    private JsonMapper mapper = JsonMapper.defaultMapper();

    /**
     * 公众号OAuth回调接口
     * @return
     */
    @RequestMapping("/auth")
    @ResponseBody
    public String mp(@RequestParam(value="code") String code, @RequestParam(value="state", required = false) String state) {
        logger.info("code:{}, state:{}", code, state);
        try{
            AccessToken accessToken = MpOAuth2s.defaultOAuth2s().getAccessToken(code);
            OpenUser openUser = MpOAuth2s.defaultOAuth2s().userInfo(accessToken.getAccessToken(), accessToken.getOpenId());
            return mapper.toJson(openUser);
        }catch (Exception e){
            logger.error(e.getMessage());
            return "";
        }

        //do your logic
    }
}
