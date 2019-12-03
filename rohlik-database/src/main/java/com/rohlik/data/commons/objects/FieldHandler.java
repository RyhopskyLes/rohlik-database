package com.rohlik.data.commons.objects;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.springframework.stereotype.Component;
import com.rohlik.data.commons.interfaces.Setter;

import lombok.NoArgsConstructor;

@Component("fieldHandler")
@NoArgsConstructor
public class FieldHandler {

	
	public <T, R> Setter<T, R> getSetter(Class<T> clazz, String fieldName, Class<R> fieldType) throws Throwable {
		 
	    MethodHandles.Lookup caller = MethodHandles.lookup();
	    MethodType setter = MethodType.methodType(void.class, fieldType);
	    MethodHandle target = caller.findVirtual(clazz, computeSetterName(fieldName), setter);
	    MethodType func = target.type();
 
	    CallSite site = LambdaMetafactory.metafactory(
	            caller,
	            "set",
	            MethodType.methodType(Setter.class),
	            func.erase(),
	            target,
	            func
	    );
 
	    MethodHandle factory = site.getTarget();
	    Setter<T, R> r = (Setter<T, R>) factory.invoke();
 
	    return r;
	}
 
	public String computeSetterName(String name) {
		return "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}
 
}	

