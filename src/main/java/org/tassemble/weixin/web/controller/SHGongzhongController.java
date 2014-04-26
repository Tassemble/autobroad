package org.tassemble.weixin.web.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author CHQ
 * @date Mar 9, 2014
 * @since 1.0
 */
@Controller
@RequestMapping("/shxjq")
public class SHGongzhongController {

	
	private static final Logger LOG = LoggerFactory.getLogger(SHGongzhongController.class);
	
	
	@RequestMapping(method=RequestMethod.GET, value="/validateDevelopSignature")
	public void validateDevelopSignature(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value="signature", required=true) String signature,
			@RequestParam(value="timestamp", required=true) String timestamp, 
			@RequestParam(value="nonce", required=true) String nonce,
			@RequestParam(value="echostr", required=true) String echostr) {
		String token = DigestUtils.md5Hex("shxjq_token");
		
		List<String> fields = Arrays.asList(token, timestamp, nonce);
		Collections.sort(fields, String.CASE_INSENSITIVE_ORDER);
		
		if (signature.equalsIgnoreCase(DigestUtils.shaHex(StringUtils.join(fields, "")))) {
			try {
				response.getOutputStream().write(echostr.getBytes("UTF-8"));
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			return;
		}
		LOG.error("failed to reponse to weixin server for "
				+ "validateDevelopSignature request, parameters" + fields.toString()
				+ "echostr:" + echostr);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/processVoice")
	public void processVoice(HttpServletRequest request, HttpServletResponse response) {
		try {
			String body = IOUtils.toString(request.getInputStream(), "UTF-8");
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
}
