/*
package com.abc.xyz.PetStore.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
public class JoinPointConfig {

    @Pointcut(
            "execution(* com.abc.xyz.PetStore.controllers.*.*(..)) "
                    + "||execution(* com.abc.xyz.PetStore.services.impls.*.*(..))"
                    + "||execution(* com.abc.xyz.PetStore.exceptionhandler.*.*(..))"
    )
    public void methodExecution() {log.trace("methodExecution");
    }


    @Pointcut(
            "execution(* com.abc.xyz.PetStore.controllers.*.*(..)) "
                    + "||execution(* com.abc.xyz.PetStore.services.impls.*.*(..))"
                    + "||execution(* com.abc.xyz.PetStore.exceptionhandler.*.*(..))"
    )
    public void methodExecutionReturn() {
        log.trace("methodExecutionReturn");
    }
}
*/
