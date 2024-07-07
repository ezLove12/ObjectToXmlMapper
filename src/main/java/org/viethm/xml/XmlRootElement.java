package org.viethm.xml;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlRootElement {
    String name();
    String namespace() default "";
}
