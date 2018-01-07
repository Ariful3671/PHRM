package com.troubleshooters.diu.phrm;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Arif on 07-01-18.
 */

public class Notificaion_ID {

        private static final AtomicInteger c = new AtomicInteger(0);
        public static int getID() {
            return c.incrementAndGet();
        }
}
