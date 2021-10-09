package stanic.mcplayer.common.utils

import java.lang.reflect.Field

fun <T> findValueByField(field: Field, instance: Any?): T {
    if (!field.isAccessible) field.isAccessible = true

    return field.get(instance) as T
}

fun setFieldValue(field: Field, instance: Any?, value: Any?) {
    if (!field.isAccessible) field.isAccessible = true
    field[instance] = value
}