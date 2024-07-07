package org.viethm.xml;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ObjectToXmlHelper {
    // Lấy số dấu tab
    private static String getTab(int numOfTab) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= numOfTab; i++) {
            sb.append("\t"); //Adding tab
        }
        return sb.toString();
    }

    // Lấy giá trị kiểu Collection (List, ArrayList, Set, ...)
    private static Collection<?> getListValueOfField(Field field, Object obj) {
        Collection<?> collection = null;
        try {
            Object objValue = field.get(obj);
            if (objValue instanceof Collection) {
                collection = (Collection<?>) objValue;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return collection;
    }

    //Kiểm tra chuỗi có rỗng không
    private static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    private static String createXmlWrapper(Field field, Object obj, int numOfTab) {
        StringBuilder sb = new StringBuilder();
        XmlElementWrapper ann = field.getAnnotation(XmlElementWrapper.class);
        String name = ann.name(); //Get Element's name
        sb.append(getTab(numOfTab));
        sb.append("<"+ name + ">");
        sb.append("\n");
        Collection<?> collections = getListValueOfField(field, obj);
        if (collections != null && !collections.isEmpty()) {
            //Create xml sub elements
            for (Object c : collections) {
                sb.append(convertToXMl(c, numOfTab+1));
                sb.append("\n");
            }
        }
        sb.append(getTab(numOfTab));
        sb.append("</").append(name).append(">"); //End element
        sb.append("\n"); //Create new line
        return sb.toString();
    }

    private static String createXmlElement(Field field, Object obj, int numOfTab) {
        StringBuilder sb = new StringBuilder();
        XmlElement ann = field.getAnnotation(XmlElement.class);
        String name = ann.name();
        String value = getValueOfField(field, obj);
        sb.append(getTab(numOfTab));
        sb.append("<").append(name).append(">").append(value).append("</").append(name).append(">").append("\n");
        return sb.toString();
    }

    // Lấy giá trị kiểu chuỗi
    private static String getValueOfField(Field field, Object obj) {
        String value = "";
        try {
            value = String.valueOf(field.get(obj));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return value;
    }
    // Lấy danh sách field có sử dụng Annotation
    private static List<Field> getFields(Class<?> clazz, Class<? extends Annotation> ann) {
        List<Field> fieldAttribute = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();
        if (ann == null) {
            fieldAttribute.addAll(Arrays.asList(fields));
        }else {
            for (Field field: fields) {
                if (field.isAnnotationPresent(ann)) {
                    fieldAttribute.add(field);
                }
            }
        }
        return fieldAttribute;
    }
    /*
     * Chuyển đối tượng sang chuỗi xml
     */
    public static <T> String convertToXMl(T obj) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        sb.append(convertToXMl(obj, 0));
        return sb.toString();
    }

    private static <T> String convertToXMl(T obj, int numOfTab) {
        StringBuilder sb = new StringBuilder();
        //Get Class of Obj using reflection
        Class<?> clazz = obj.getClass();
        //Check if this class have been annotated with XmlRootElement annotation
        boolean isXmlDoc = clazz.isAnnotationPresent(XmlRootElement.class);

        if(isXmlDoc) {
            XmlRootElement rootNode = clazz.getAnnotation(XmlRootElement.class);
            sb.append(getTab(numOfTab));//Add tab
            sb.append("<").append(rootNode.name()); // Root element
           if (isNotEmpty(rootNode.namespace())) {
               sb.append(" xmlns=\"" + rootNode.namespace() + "\"");
           }
           sb.append(">");
            sb.append("\n");
            List<Field> fields = getFields(clazz, null);
            if (!fields.isEmpty()) {
                //create XML elements
                for (Field field: fields) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(XmlElementWrapper.class)) {
                        sb.append(createXmlWrapper(field, obj, numOfTab + 1));
                    } else if (field.isAnnotationPresent(XmlElement.class)) {
                        sb.append(createXmlElement(field, obj, numOfTab+1));
                    }
                }
            }
            sb.append(getTab(numOfTab));
            sb.append("</").append(rootNode.name()).append(">");
        }
        return sb.toString();
    }
}
