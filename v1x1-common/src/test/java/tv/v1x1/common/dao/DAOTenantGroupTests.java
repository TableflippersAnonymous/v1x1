package tv.v1x1.common.dao;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by naomi on 12/18/2016.
 */
public class DAOTenantGroupTests {
    @Test
    public void testGlobalTenantIsCorrect() {
        Assert.assertEquals(DAOTenantGroup.GLOBAL_TENANT.getId().toString(), "493073c3-8a6f-38fa-8e38-16af0b436482");
    }
}
