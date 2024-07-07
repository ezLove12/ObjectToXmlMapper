package org.kafka.demo;

import java.lang.annotation.*;

@Documented
//cho phép các công cụ tạo tài liệu như javadoc tạo ra chú thích khi
// một annotation được chú thích với @Documented trên một lớp, phương thức hoặc trường thông tin về annotation
// sẽ xuất hiện trong java doc
@Target(ElementType.TYPE)
/*
Sử dụng để chỉ định kiểu phần tử mà annotation này có thể được áp dụng
ELementType.Type = annotation này được sử dụng cho các kiểu(type) như class, interface, enums hoặc annotations khác
*/
@Inherited
/*
Chỉ định một annotation được áp dụng trên một lớp sẽ được kế thừa lại bởi lớp con của nó
trừ khi lớp con ghi đè annotation đó bằng một annotation khác => Chỉ có hiệu lực với annotation có "RetentionPolicy.RUNTIME
* */
@Retention(RetentionPolicy.RUNTIME)
/*
* Chỉ định mức độ giữ lại (retention policy của một annotations bao gồm 3 giá trị
* + SOURCE: annotation được giữ lại trong mã nguồn và sẽ bị loại bỏ bởi trình biên dịch. Chúng không tồn tại dưới dang bytecode và không truy cập được ở runtime
* + CLASS: Annotation được giữ lại trong bytecode nhưng không có sẵn ở runtime => đây là giá trị mặc định
* + RUNTIME: annotation được giữ lại trong bytecode để biên dịch và không có sẵn tại runtime.
* Điều này cho phép các công cụ và các framework có thể sử dụng reflection để truy cập các thông từ các annotations này trong quá trình thực thi
* */
public @interface XmlRootElement {
    String name();
    String namespace() default "";
}
