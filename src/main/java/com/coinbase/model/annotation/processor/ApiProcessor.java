package com.coinbase.model.annotation.processor;

import com.coinbase.model.annotation.Api;
import com.coinbase.util.Format;
import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * Annotation processing to generate classes in same package:
 * https://stackoverflow.com/questions/19512334/java-annotation-processing-create-a-source-file-in-the-same-package-as-the-anno
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_16)
@SupportedAnnotationTypes({"com.coinbase.model.annotation.*"})
public class ApiProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv){
        super.init(processingEnv);

        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        for (final Element apiElement : roundEnv.getElementsAnnotatedWith(Api.class)) {
            // annotation only valid for interfaces or classes (this includes extending classes)
            validateAnnotationElement(apiElement);

            // process annotated API class, only applies to class implementations
            if (apiElement.getKind() == ElementKind.CLASS) {
                final ApiAnnotatedClass apiAnnotatedClass = new ApiAnnotatedClass((TypeElement) apiElement);
            }
        }
        return false;
    }

    private void validateAnnotationElement(final Element annotatedElement) {
        if (annotatedElement.getKind() != ElementKind.CLASS && annotatedElement.getKind() != ElementKind.INTERFACE) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                    Format.format("Non-class element: [{}] annotated with @{}", annotatedElement.getSimpleName(),
                            Api.class.getSimpleName()));
        }
    }
}
