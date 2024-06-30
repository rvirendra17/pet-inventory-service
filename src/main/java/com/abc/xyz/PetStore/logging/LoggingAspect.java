/*
package com.abc.xyz.PetStore.logging;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LoggingAspect {


    private HttpServletRequest  httpServletRequest;

    @Autowired
    public LoggingAspect(HttpServletRequest httpServletRequest){
        this.httpServletRequest=httpServletRequest;
    }





    private String getTransactionId() {
        String transactionId=null;

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
            transactionId = httpServletRequest.getHeader("transactionId");
        }
        return transactionId;
    }


}
*/
