package org.projectodd.rephract.invokers;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public interface Invoker {

    MethodHandle methodHandle(MethodType type) throws Exception;
}
