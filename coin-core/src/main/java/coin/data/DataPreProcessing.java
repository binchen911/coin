package coin.data;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import coin.conf.CoinConfiguration;

import coin.notify.Notification;
import coin.notify.Notification.DestinationType;

import javax.annotation.Nonnull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DataPreProcessing {
    private static final Log logger =
        LogFactory.getLog(DataPreProcessing.class);

    private final CoinConfiguration conf;
    private final EventBus notifyEventBus;

    public DataPreProcessing(@Nonnull CoinConfiguration conf, @Nonnull EventBus notifyEventBus) {
        Preconditions.checkNotNull(conf, "Configuration file should not be null");
        Preconditions.checkNotNull(notifyEventBus, "Notify event bus should not be null");
        this.conf = conf;
        this.notifyEventBus = notifyEventBus;
    }

    public DataPreProcessing registerTo(EventBus dataEventBus) {
        dataEventBus.register(this);
        return this;
    }

    @Subscribe
    public void handleRawData(CoinData data) {
        if (data.getLatestPrice() > 5400) {
            logger.info("Send notification to 407362048@qq.com");
            triggerNotify(new Notification("407362048@qq.com", DestinationType.MAIL,
                "The latest price of " + data.getType() + " is larger than 1000 now."));
        }
    }

    void triggerNotify(@Nonnull Notification message) {
        notifyEventBus.post(message);
    }
}
