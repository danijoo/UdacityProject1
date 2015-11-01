package net.headlezz.udacityproject1.favorites;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version=FavoriteDatabase.VERSION, packageName="net.headlezz.udacityproject1.provider")
public class FavoriteDatabase {

    public static final int VERSION = 1;

    @Table(FavoriteColumns.class) public static final String favorites = "favorites";
}
