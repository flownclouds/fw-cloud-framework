package com.github.liuweijw.business.wechat.controller;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.liuweijw.business.commons.utils.WebUtils;
import com.github.liuweijw.business.pay.commons.beans.HttpResult;
import com.github.liuweijw.core.utils.StringHelper;

@Slf4j
@Controller
@RequestMapping("/wechat/jsdk")
public class WxJsdkController {

	@Autowired
	private WxMpService	wxService;

	@RequestMapping(value = "/wechatParam")
	@ResponseBody
	public HttpResult wechatParam(HttpServletRequest request, @RequestParam("url") String url) {
		if (StringHelper.isBlank(url)) return new HttpResult().failure("url 参数验证失败！");

		String jsdkUrl = WebUtils.buildURLDecoder(url);
		log.info("===WxJsdkController==" + jsdkUrl);
		try {
			WxJsapiSignature jsapi = wxService.createJsapiSignature(WebUtils
					.buildURLDecoder(jsdkUrl));
			return new HttpResult().data(jsapi).success();
		} catch (WxErrorException e) {
			e.printStackTrace();
		}

		return new HttpResult().failure("获取微信签名数据失败！");
	}

	@RequestMapping(value = "/wechatTest")
	@ResponseBody
	public HttpResult wechatTest(HttpServletRequest request) {
		try {
			return new HttpResult().data(this.wxService.getAccessToken()).success();
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
		return new HttpResult().failure("微信数据配置失败！");
	}
}
