package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Created by cobi on 11/12/2016.
 */
@Table(name = "third_party_credential")
public class ThirdPartyCredential {
    @PartitionKey
    private String name;
    private byte[] credential;

    public ThirdPartyCredential() {
    }

    public ThirdPartyCredential(final String name, final byte[] credential) {
        this.name = name;
        this.credential = credential;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public byte[] getCredential() {
        return credential;
    }

    public void setCredential(final byte[] credential) {
        this.credential = credential;
    }
}
