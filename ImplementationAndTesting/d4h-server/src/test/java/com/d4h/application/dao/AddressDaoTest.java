package com.d4h.application.dao;


import com.d4h.application.dao.User.AddressDao;
import com.d4h.application.dao.User.UsersDao;
import com.d4h.application.model.user.Address;
import org.junit.jupiter.api.Test;

import javax.naming.Context;

import static org.junit.jupiter.api.Assertions.*;

public class AddressDaoTest extends DaoTestBase{
    @Test
    public void emptyGetAddresss() throws Exception {
        final Context context = container.getContext();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        assertEquals(0, users.getAddress().size());
    }

    @Test
    public void notEmptyGetAddresss() throws Exception {
        final Context context = container.getContext();
        Address address0 = new Address();
        Address address1 = new Address();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        users.addAddress(address0);
        assertEquals(1, users.getAddress().size());
        users.addAddress(address1);
        assertEquals(2, users.getAddress().size());
        users.deleteAddress(address1);
        assertEquals(1, users.getAddress().size());
        users.deleteAddress(address0);
        assertEquals(0, users.getAddress().size());
    }

    @Test
    public void emptyGetAddress() throws Exception {
        final Context context = container.getContext();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        assertNull( users.getAddress("1"));
    }

    @Test
    public void notEmptyGetAddress() throws Exception {
        final Context context = container.getContext();
        Address address = new Address();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        users.addAddress(address);

        assertNotNull( users.getAddress(address.getId()));
        users.deleteAddress(address);
    }
}
