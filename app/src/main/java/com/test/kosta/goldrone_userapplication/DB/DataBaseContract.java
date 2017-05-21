package com.test.kosta.goldrone_userapplication.DB;

import android.provider.BaseColumns;

/**
 * Created by kosta on 2016-07-14.
 */
public class DataBaseContract {

    public static final class DroneEntry implements BaseColumns {

        // Table name
        public static final String TABLE_NAME = "drone";

        public static final String NODE_ID = "nodeid";

        public static final String COLUMN_DRONEID = "droneid";

        public static final String COLUMN_LAT = "lat";

        public static final String COLUMN_LON = "lon";

    }

    public static final class UserEntry implements BaseColumns {

        // Table name
        public static final String TABLE_NAME = "user";

        public static final String COLUMN_LAT = "lat";

        public static final String COLUMN_LON = "lon";

    }

}
