/** 
 * Copyright (c) 2017, RITS All Rights Reserved. 
 * 
 */ 
package com.alipay.fund.trans.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.trade.model.builder.RequestBuilder;

/** 
 * @ClassName: AbsAlipayService <br/> 
 * @Description: TODO  <br/> 
 * 
 * @author maojianghui 
 * @date: 2017年6月9日 下午2:37:34 <br/>
 * @version  
 * @since JDK 1.6 
 */
public abstract class AbsAlipayService {
    protected Log log = LogFactory.getLog(getClass());

    // 验证bizContent对象
    protected void validateBuilder(RequestBuilder builder) {
        if (builder == null) {
            throw new NullPointerException("builder should not be NULL!");
        }

        if (!builder.validate()) {
            throw new IllegalStateException("builder validate failed! " + builder.toString());
        }
    }

    // 调用AlipayClient的execute方法，进行远程调用
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected AlipayResponse getResponse(AlipayClient client, AlipayRequest request) {
        try {
            AlipayResponse response = client.execute(request);
            if (response != null) {
                log.info(response.getBody());
            }
            return response;

        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }
    }

}
