package tv.v1x1.modules.channel.link_purger;

import com.google.common.primitives.Ints;
import org.redisson.api.RMapCache;
import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.User;
import tv.v1x1.common.i18n.Language;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.scanners.i18n.I18nDefault;
import tv.v1x1.common.scanners.i18n.I18nDefaults;
import tv.v1x1.common.scanners.permission.DefaultGroup;
import tv.v1x1.common.scanners.permission.Permissions;
import tv.v1x1.common.scanners.permission.RegisteredPermission;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.common.util.data.CompositeKey;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;

/**
 * @author Josh
 */
@Permissions({
        @RegisteredPermission(
                node = "link_purger.permit",
                displayName = "Permit Links",
                description = "This allows you to use the !permit command",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.BROADCASTER, DefaultGroup.MODS}
        ),
        @RegisteredPermission(
                node = "link_purger.whitelisted",
                displayName = "Exempt from Link Purging",
                description = "This makes you immune to being purged by the Link Purger",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.BROADCASTER, DefaultGroup.MODS, DefaultGroup.SUBS}
        )
})
@I18nDefaults({
        @I18nDefault(
                key = "purged",
                message = "Hey %user%, please ask before posting a link! I've purged your messages; feel free to keep chatting!",
                displayName = "Purged",
                description = "Sent when a user is purged"
        ),
        @I18nDefault(
                key = "timeout",
                message = "Hey %user%, I said ask before posting a link! I've timed you out for now; see you soon.",
                displayName = "Timed Out",
                description = "Sent when a user is timed out"
        ),
        @I18nDefault(
                key = "permit",
                message = "Hey %target%, you can post one link now!",
                displayName = "Permitted",
                description = "Sent when a user is permitted"
        ),
        @I18nDefault(
                key = "permitfailed",
                message = "%commander%, %target% is already allowed to post a link.",
                displayName = "Permit Failed",
                description = "Sent when a user is already permitted"
        ),
        @I18nDefault(
                key = "notarget",
                message = "%commander%, I don't see %target% in the channel. I can't add the permit.",
                displayName = "Invalid Target",
                description = "Sent when an invalid user is specified with !permit"
        )
})
public class LinkPurger extends RegisteredThreadedModule<LinkPurgerGlobalConfiguration, LinkPurgerUserConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private RMapCache<byte[], byte[]> offenses;
    private RSetCache<byte[]> permits;

    /* pkg-private */ Language language;
    /* pkg-private */ CommandDelegator delegator;

    public static void main(String[] args) throws Exception {
        new LinkPurger().entryPoint(args);
    }

    @Override
    protected void initialize() {
        super.initialize();
        final RedissonClient redissonClient = getRedisson();
        offenses = redissonClient.getMapCache("Modules|Channel|LinkPurger|offenses", ByteArrayCodec.INSTANCE);
        permits = redissonClient.getSetCache("Modules|Channel|LinkPurger|permits", ByteArrayCodec.INSTANCE);
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
        int offenseNumber = 1;
        offenseNumber += Ints.fromByteArray(offenses.getOrDefault(key, Ints.toByteArray(0)));
        offenses.putAsync(key, Ints.toByteArray(offenseNumber), 1, TimeUnit.HOURS);
        return offenseNumber;
    }

    public void removeOffenses(final Channel channel, final String userId) {
        offenses.putAsync(CompositeKey.makeKey(channel.getId(), userId), Ints.toByteArray(0), 1, TimeUnit.MINUTES);
    }

    public boolean permitUser(final Channel channel, final String userId) {
        removeOffenses(channel, userId);
        return permits.add(CompositeKey.makeKey(channel.getId(), userId), 5, TimeUnit.MINUTES);
    }

    public boolean usePermit(final Channel channel, final User user) {
        return permits.remove(CompositeKey.makeKey(channel.getId(), user.getId()));
    }
}
