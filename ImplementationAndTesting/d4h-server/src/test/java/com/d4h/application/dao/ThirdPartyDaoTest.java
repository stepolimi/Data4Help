package com.d4h.application.dao;

import com.d4h.application.dao.ThirdParty.ThirdPartyDao;
import com.d4h.application.model.thirdParty.ThirdParty;
import org.junit.jupiter.api.Test;
import javax.naming.Context;
import static org.junit.jupiter.api.Assertions.*;

public class ThirdPartyDaoTest extends DaoTestBase{


    @Test
    public void emptyGetThirdParties() throws Exception {
        final Context context = container.getContext();

        ThirdPartyDao thirdParties = (ThirdPartyDao) context.lookup("java:global/d4h-server/ThirdPartyDao");

        assertEquals(0, thirdParties.getThirdParties().size());
    }

    @Test
    public void notEmptyGetThirdParties() throws Exception {
        final Context context = container.getContext();
        ThirdParty thirdParty0 = new ThirdParty();
        ThirdParty thirdParty1 = new ThirdParty();

        ThirdPartyDao thirdParties = (ThirdPartyDao) context.lookup("java:global/d4h-server/ThirdPartyDao");

        thirdParties.addThirdParty(thirdParty0);
        assertEquals(1, thirdParties.getThirdParties().size());
        thirdParties.addThirdParty(thirdParty1);
        assertEquals(2, thirdParties.getThirdParties().size());
        thirdParties.deleteThirdParty(thirdParty1);
        assertEquals(1, thirdParties.getThirdParties().size());
        thirdParties.deleteThirdParty(thirdParty0);
        assertEquals(0, thirdParties.getThirdParties().size());
    }

    @Test
    public void emptyGetThirdParty() throws Exception {
        final Context context = container.getContext();

        ThirdPartyDao thirdParties = (ThirdPartyDao) context.lookup("java:global/d4h-server/ThirdPartyDao");

        assertNull( thirdParties.getThirdParty("1"));
    }

    @Test
    public void notEmptyGetThirdParty() throws Exception {
        final Context context = container.getContext();
        ThirdParty thirdParty = new ThirdParty();

        ThirdPartyDao thirdParties = (ThirdPartyDao) context.lookup("java:global/d4h-server/ThirdPartyDao");

        thirdParties.addThirdParty(thirdParty);

        assertNotNull( thirdParties.getThirdParty(thirdParty.getId()));
        thirdParties.deleteThirdParty(thirdParty);
    }
}
