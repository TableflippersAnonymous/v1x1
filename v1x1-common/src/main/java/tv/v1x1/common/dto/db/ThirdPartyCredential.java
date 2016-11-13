package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.nio.ByteBuffer;

/**
 * Created by naomi on 11/12/2016.
 */
@Table(name = "third_party_credential")
public class ThirdPartyCredential {
    @PartitionKey
    private String name;
    private ByteBuffer credential;

    public ThirdPartyCredential() {
    }

    public ThirdPartyCredential(final String name, final byte[] credential) {
        this.name = name;
        this.credential = ByteBuffer.wrap(credential);
    }

    public String getName() {
        return name;
    }

    public synchronized byte[] getCredential() {
        final byte[] ret = new byte[credential.remaining()];
        credential.mark();
        credential.get(ret);
        credential.reset();
        return ret;
    }
}
