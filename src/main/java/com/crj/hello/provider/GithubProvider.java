package com.crj.hello.provider;

import com.alibaba.fastjson.JSON;
import com.crj.hello.dto.AccessTokenDTO;
import com.crj.hello.dto.GithubUser;
import com.crj.hello.utils.OKHttpUtil;
import org.springframework.stereotype.Component;

@Component
public class GithubProvider {

    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        String url = "https://github.com/login/oauth/access_token";
        String json = JSON.toJSONString(accessTokenDTO);
        String string = OKHttpUtil.post(url, json);
        String accessToken = string.split("&")[0].split("=")[1];
        return accessToken;
    }

    public GithubUser getUser(String accessToken){
        String string = OKHttpUtil.get("https://api.github.com/user?access_token=" + accessToken);
        GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
        return githubUser;
    }
}
