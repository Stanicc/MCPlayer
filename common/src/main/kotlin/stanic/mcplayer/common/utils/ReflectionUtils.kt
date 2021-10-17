package stanic.mcplayer.common.utils

import java.lang.reflect.Field
import javax.rmi.CORBA.Util.wrapException

fun <T> findValueByField(field: Field, instance: Any?): T {
    if (!field.isAccessible) field.isAccessible = true

    return field.get(instance) as T
}

fun findField(clazz: Class<*>, name: String): Field {
    val exception = try {
        return clazz.getDeclaredField(name)
    } catch (e: NoSuchFieldException) {
        e
    }

    val superClazz = clazz.superclass
    if (superClazz != null && superClazz != Any::class.java) {
        try {
            return superClazz.getField(name)
        } catch (ignored: NoSuchFieldException) {
        }
    }
    throw wrapException(exception)
}

fun setFieldValue(field: Field, instance: Any?, value: Any?) {
    if (!field.isAccessible) field.isAccessible = true
    field[instance] = value
}