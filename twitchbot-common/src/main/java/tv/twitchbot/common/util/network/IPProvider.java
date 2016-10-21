package tv.twitchbot.common.util.network;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by naomi on 10/19/2016.
 */
public class IPProvider {
    public static String getMyIp() throws IOException {
        final CloseableHttpClient client = HttpClients.createDefault();
        try {
            final HttpUriRequest get = new HttpGet("http://ipinfo.io/ip");
            final CloseableHttpResponse response = client.execute(get);
            try {
                final HttpEntity entity = response.getEntity();
                if (entity == null)
                    throw new RuntimeException("Error getting IP");
                final String body = EntityUtils.toString(entity);
                return body.trim();
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }
}
