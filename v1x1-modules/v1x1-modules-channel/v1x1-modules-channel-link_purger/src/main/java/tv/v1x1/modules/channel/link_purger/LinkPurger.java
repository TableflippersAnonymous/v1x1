package tv.v1x1.modules.channel.link_purger;

import org.redisson.Redisson;
import org.redisson.api.RMapCache;
import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.User;
import tv.v1x1.common.i18n.I18n;
import tv.v1x1.common.i18n.Language;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.common.util.data.CompositeKey;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;

/**
 * @author Josh
 */
public class LinkPurger extends RegisteredThreadedModule<LinkPurgerSettings, LinkPurgerGlobalConfiguration, LinkPurgerTenantConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static {
        final Module module = new Module("link_purger");
        I18n.registerDefault(module, "purged", "Hey %user%, please ask before posting a link! I've purged your messages; feel free to keep chatting!");
        I18n.registerDefault(module, "timeout", "Hey %user%, I said ask before posting a link! I've timed you out for now; see you soon.");
        I18n.registerDefault(module, "permit", "Hey %viewer%, you can post one link now!");
    }

    private CommandDelegator delegator;
    private RedissonClient redissonClient;
    private RMapCache<byte[], Integer> offenses;
    private RSetCache<byte[]> permits;

    /* pkg-private */ Language language;

    public static void main(String[] args) throws Exception {
        new LinkPurger().entryPoint(args);
    }

    @Override
    protected void initialize() {
        super.initialize();
        redissonClient = Redisson.create(getSettings().getRedissonConfig());
        offenses = redissonClient.getMapCache("link_purger|offenses", ByteArrayCodec.INSTANCE);
        permits = redissonClient.getSetCache("link_purger|permits", ByteArrayCodec.INSTANCE);
        delegator = new CommandDelegator("!");
        delegator.registerCommand(new PermitCommand(this));
        registerListener(new LinkPurgerListener(this));
        language = getI18n().getLanguage(null);
    }

    @Override
    public String getName() {
        return "link_purger";
    }

    /* pkg-private */ Logger getLogger() {
        return LOG;
    }

    public int addOffense(final Channel channel, final User user) {
        byte[] key = CompositeKey.makeKey(channel.getId(), user.getId());
        Integer offenseNumber = 1;
        offenseNumber += offenses.getOrDefault(key, 0);
        offenses.putAsync(key, offenseNumber, 1, TimeUnit.HOURS);
        return offenseNumber;
    }

    public void removeOffenses(final Channel channel, final User user) {
        offenses.removeAsync(CompositeKey.makeKey(channel.getId(), user.getId()));
    }

    public boolean permitUser(final Channel channel, final User user) {
        return permits.add(CompositeKey.makeKey(channel.getId(), user.getId()), 5, TimeUnit.MINUTES);
    }
}
