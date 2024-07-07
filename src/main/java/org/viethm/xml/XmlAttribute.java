package org.viethm.xml;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlAttribute {
    String name();
}
