package org.finos.springbot.workflow.templating;

import java.lang.reflect.Field;
import java.lang.reflect.Type;


public abstract class AbstractClassConverter<X> extends AbstractSimpleTypeConverter<X> {

	private Class<?>[] forClass;

	public AbstractClassConverter(int priority, Rendering<X> r, Class<?>... forClass) {
		super(priority, r);
		this.forClass = forClass;
	}

	@Override
	public boolean canConvert(Field ctx, Type t) {
		for (Class<?> class1 : forClass) {
			if ((t instanceof Class<?> class2) && (class1.isAssignableFrom(class2))) {
				return true;
			}
		}
		
		return false;
	}
	
	
}
