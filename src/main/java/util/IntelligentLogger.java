package util;

import java.util.function.*;

import static util.IntelligentLogger.AssertHelper.INSTANCE;

/**
 * @author qianrui
 * 2018/2/7
 */
public interface IntelligentLogger {
    Function<Class<? extends IntelligentLogger>, Consumer<Supplier<String>>> LOGGER_FACTORY = IntelligentLoggerFactory.getFactory();
    Function<Class<? extends IntelligentLogger>, Boolean> ENOUGH_LEVEL = IntelligentLoggerLevel.enoughLevel();
    BiConsumer<Class<? extends IntelligentLogger>, Integer> CHANGE_LEVEL = IntelligentLoggerLevel.changeLevel();


    default void print(Supplier<String> message) {
        if (message == null) {
            return;
        }
        Class<? extends IntelligentLogger> thisType = this.getClass();
        final Boolean enough = ENOUGH_LEVEL.apply(thisType);
        if (enough == null || !enough) {
            return;
        }
        Consumer<Supplier<String>> logger = LOGGER_FACTORY.apply(thisType);
        if (logger == null) {
            return;
        }
        logger.accept(message);
    }

    default void setLoggerlevel(int level) {
        CHANGE_LEVEL.accept(this.getClass(), level);
    }

    AssertHelper ASSERT = INSTANCE;

    enum AssertHelper {
        /**
         * 全局唯一实例
         */
        INSTANCE;

        AssertHelper() {
            if (CHANGE_LEVEL == null) {
                throw new NullPointerException("field CHANGE_LEVEL can not be null");
            }
            if (ENOUGH_LEVEL == null) {
                throw new NullPointerException("field ENOUGH_LEVEL can not be null");
            }
            if (LOGGER_FACTORY == null) {
                throw new NullPointerException("field LOGGER_FACTORY can not be null");
            }
        }
    }

}
