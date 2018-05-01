package tv.v1x1.common.scanners;

import java.lang.annotation.Annotation;

public abstract class ClassScanner {
    protected static <T extends Annotation> T getAnnotation(final Class<?> clazz, final Class<T> annotation) {
        final T annotationValue = clazz.getAnnotation(annotation);
        if(annotationValue != null)
            return annotationValue;
        final Class<?> superClass = clazz.getSuperclass();
        if(superClass != null)
            return getAnnotation(superClass, annotation);
        return null;
    }
}
