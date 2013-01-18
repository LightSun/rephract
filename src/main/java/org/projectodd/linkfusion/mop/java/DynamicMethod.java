package org.projectodd.linkfusion.mop.java;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.List;

public class DynamicMethod extends AbstractDynamicMember {

    private String name;
    private List<MethodHandle> methods = new ArrayList<MethodHandle>();
    private boolean isStatic;

    public DynamicMethod(CoercionMatrix coercionMatrix, String name, boolean isStatic) {
        super( coercionMatrix );
        this.name = name;
        this.isStatic = isStatic;
    }

    public boolean isStatic() {
        return this.isStatic;
    }

    public String getName() {
        return this.name;
    }
    
    public void addMethodHandle(MethodHandle method) {
        this.methods.add(method);
    }
    
    public InvocationPlan findMethodInvoationPlan(Object[] args) {
        CoercionMatrix matrix = getCoercionMatrix();
        loop: for (MethodHandle each : this.methods) {
            Class<?>[] paramTypes = getPureParameterArray(each );
            if (paramTypes.length == args.length) {
                MethodHandle[] filters = new MethodHandle[paramTypes.length];
                for (int i = 0; i < paramTypes.length; ++i) {
                    if (matrix.isCompatible(paramTypes[i], args[i].getClass())) {
                        filters[i] = matrix.getFilter(paramTypes[i], args[i].getClass());
                    } else {
                        continue loop;
                    }
                }
                return new InvocationPlan(each, filters);
            }
        }
        return null;
    }

    protected Class<?>[] getPureParameterArray(MethodHandle handle) {
        List<Class<?>> paramList = handle.type().parameterList();

        if (isStatic) {
            return paramList.toArray( new Class<?>[ paramList.size()]);

        } else {
            if (paramList.size() == 1) {
                return new Class<?>[0];
            }
            return paramList.subList(1, paramList.size()).toArray(new Class<?>[paramList.size() - 1]);
        }

    }

}