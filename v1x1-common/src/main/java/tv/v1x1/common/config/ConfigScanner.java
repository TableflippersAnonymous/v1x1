package tv.v1x1.common.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import tv.v1x1.common.dto.core.ConfigurationDefinition;
import tv.v1x1.common.dto.core.GlobalConfigurationDefinition;
import tv.v1x1.common.dto.core.UserConfigurationDefinition;
import tv.v1x1.common.modules.GlobalConfiguration;
import tv.v1x1.common.modules.UserConfiguration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cobi on 10/24/2016.
 */
public class ConfigScanner {
    private static final Gson GSON = new GsonBuilder().create();

    private ConfigScanner() {
    }

    public static <T extends GlobalConfiguration> GlobalConfigurationDefinition scanGlobal(final Class<T> clazz) {
        final String name = getName(clazz);
        if(name == null)
            return null;
        final String displayName = getDisplayName(clazz);
        final String description = getDescription(clazz);
        final int version = getVersion(clazz);
        final Permission tenantPermission = getTenantPermission(clazz);
        final Map<String, List<ConfigurationDefinition.Field>> complexFields = new HashMap<>();
        final List<ConfigurationDefinition.Field> fields = scanFields(clazz, complexFields);
        return new GlobalConfigurationDefinition(name, displayName == null ? name : displayName, description, version, tenantPermission, fields, complexFields);
    }

    public static <T extends UserConfiguration> UserConfigurationDefinition scanUser(final Class<T> clazz) {
        final String name = getName(clazz);
        if(name == null)
            return null;
        final String displayName = getDisplayName(clazz);
        final String description = getDescription(clazz);
        final int version = getVersion(clazz);
        final Permission tenantPermission = getTenantPermission(clazz);
        final Map<String, List<ConfigurationDefinition.Field>> complexFields = new HashMap<>();
        final List<ConfigurationDefinition.Field> fields = scanFields(clazz, complexFields);
        return new UserConfigurationDefinition(name, displayName == null ? name : displayName, description, version, tenantPermission, fields, complexFields);
    }

    private static String getName(final Class<?> clazz) {
        final ModuleConfig moduleConfig = getAnnotation(clazz, ModuleConfig.class);
        if(moduleConfig == null)
            return null;
        return moduleConfig.value();
    }

    private static String getDisplayName(final Class<?> clazz) {
        final DisplayName displayName = getAnnotation(clazz, DisplayName.class);
        if(displayName == null)
            return null;
        return displayName.value();
    }

    private static String getDescription(final Class<?> clazz) {
        final Description description = getAnnotation(clazz, Description.class);
        if(description == null)
            return "<no description>";
        return description.value();
    }

    private static int getVersion(final Class<?> clazz) {
        final Version version = getAnnotation(clazz, Version.class);
        if(version == null)
            return -1;
        return version.value();
    }

    private static Permission getTenantPermission(final Class<?> clazz) {
        final TenantPermission permission = getAnnotation(clazz, TenantPermission.class);
        if(permission == null)
            return Permission.READ_WRITE;
        return permission.value();
    }

    private static <T extends Annotation> T getAnnotation(final Class<?> clazz, final Class<T> annotation) {
        final T annotationValue = clazz.getAnnotation(annotation);
        if(annotationValue != null)
            return annotationValue;
        final Class<?> superClass = clazz.getSuperclass();
        if(superClass != null)
            return getAnnotation(superClass, annotation);
        return null;
    }

    private static List<ConfigurationDefinition.Field> scanFields(final Class<?> clazz, final Map<String, List<ConfigurationDefinition.Field>> complexFields) {
        final List<ConfigurationDefinition.Field> fields = new ArrayList<>();
        final Class<?> superClass = clazz.getSuperclass();
        if(superClass != null)
            fields.addAll(scanFields(superClass, complexFields));
        fields.addAll(scanFieldsOnClass(clazz, complexFields));
        return fields;
    }

    private static List<ConfigurationDefinition.Field> scanFieldsOnClass(final Class<?> clazz, final Map<String, List<ConfigurationDefinition.Field>> complexFields) {
        final List<ConfigurationDefinition.Field> fields = new ArrayList<>();
        for(final Field field : clazz.getDeclaredFields()) {
            final ConfigurationDefinition.Field scannedField = scanField(field, complexFields);
            if(scannedField != null)
                fields.add(scannedField);
        }
        return fields;
    }

    private static ConfigurationDefinition.Field scanField(final Field field, final Map<String, List<ConfigurationDefinition.Field>> complexFields) {
        final DisplayName displayNameAnnotation = field.getAnnotation(DisplayName.class);
        final String displayName = displayNameAnnotation == null ? field.getName() : displayNameAnnotation.value();
        final Description descriptionAnnotation = field.getAnnotation(Description.class);
        final String description = descriptionAnnotation == null ? "<no description>" : descriptionAnnotation.value();
        final Type typeAnnotation = field.getAnnotation(Type.class);
        final ConfigType configType = typeAnnotation == null ? ConfigType.STRING : typeAnnotation.value();
        final TenantPermission tenantPermissionAnnotation = field.getAnnotation(TenantPermission.class);
        final Permission tenantPermission = tenantPermissionAnnotation == null ? Permission.READ_WRITE : tenantPermissionAnnotation.value();
        final DefaultString defaultStringAnnotation = field.getAnnotation(DefaultString.class);
        final String defaultString = defaultStringAnnotation == null ? null : defaultStringAnnotation.value();
        final DefaultInteger defaultIntegerAnnotation = field.getAnnotation(DefaultInteger.class);
        final Integer defaultInteger = defaultIntegerAnnotation == null ? null : defaultIntegerAnnotation.value();
        final Requires requiresAnnotation = field.getAnnotation(Requires.class);
        final List<String> requires = requiresAnnotation == null ? ImmutableList.of() : ImmutableList.copyOf(Arrays.asList(requiresAnnotation.value()));
        final JsonProperty jsonPropertyAnnotation = field.getAnnotation(JsonProperty.class);
        final String jsonProperty = jsonPropertyAnnotation == null ? field.getName() : jsonPropertyAnnotation.value().isEmpty() ? field.getName() : jsonPropertyAnnotation.value();
        final JsonElement defaultValue = defaultString == null
                ? (defaultInteger == null ? JsonNull.INSTANCE : new JsonPrimitive(defaultInteger))
                : new JsonPrimitive(defaultString);
        String complexType = null;
        if(configType.isComplex()) {
            final ComplexType complexTypeAnnotation = field.getAnnotation(ComplexType.class);
            final Class<?> clazz = complexTypeAnnotation == null ? field.getType() : complexTypeAnnotation.value();
            complexType = clazz.getCanonicalName();
            if(!complexFields.containsKey(complexType)) {
                complexFields.put(complexType, ImmutableList.of());
                complexFields.put(complexType, scanFields(clazz, complexFields));
            }
        }
        return new ConfigurationDefinition.Field(displayName, description, GSON.toJson(defaultValue), configType, requires, tenantPermission, jsonProperty, complexType);
    }
}
