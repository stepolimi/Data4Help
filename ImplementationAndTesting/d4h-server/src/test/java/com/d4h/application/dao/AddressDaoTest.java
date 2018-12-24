package com.d4h.application.dao;


import com.d4h.application.dao.User.AddressDao;
import com.d4h.application.model.user.Address;
import org.junit.jupiter.api.Test;

import javax.naming.Context;

import static org.junit.jupiter.api.Assertions.*;

public class AddressDaoTest extends DaoTestBase{
    @Test
    public void emptyGetAddresss() throws Exception {
        final Context context = container.getContext();

        AddressDao addresss = (AddressDao) context.lookup("java:global/d4h-server/AddressDao");

        assertEquals(0, addresss.getAddress().size());
    }

    @Test
    public void notEmptyGetAddresss() throws Exception {
        final Context context = container.getContext();
        Address address0 = new Address();
        Address address1 = new Address();

        AddressDao addresss = (AddressDao) context.lookup("java:global/d4h-server/AddressDao");

        addresss.addAddress(address0);
        assertEquals(1, addresss.getAddress().size());
        addresss.addAddress(address1);
        assertEquals(2, addresss.getAddress().size());
        addresss.deleteAddress(address1);
        assertEquals(1, addresss.getAddress().size());
        addresss.deleteAddress(address0);
        assertEquals(0, addresss.getAddress().size());
    }

    @Test
    public void emptyGetAddress() throws Exception {
        final Context context = container.getContext();

        AddressDao addresss = (AddressDao) context.lookup("java:global/d4h-server/AddressDao");

        assertNull( addresss.getAddress("1"));
    }

    @Test
    public void notEmptyGetAddress() throws Exception {
        final Context context = container.getContext();
        Address address = new Address();

        AddressDao addresss = (AddressDao) context.lookup("java:global/d4h-server/AddressDao");

        addresss.addAddress(address);

        assertNotNull( addresss.getAddress(address.getId()));
        addresss.deleteAddress(address);
    }
}
