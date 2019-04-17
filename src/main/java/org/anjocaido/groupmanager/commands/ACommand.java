package org.anjocaido.groupmanager.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author sarhatabaot
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ACommand {
    /**
     * @return name
     */
    String name();

    /**
     * @return usage
     */
    String usage();

    /**
     * @return permission
     */
    String permissions() default "";

    /**
     * @return minimum amount of arguments
     */
    int min() default 0;

    /**
     *
     * @return maximum amount of arguments
     */
    int max() default -1;

}
