package net.headlezz.udacityproject1.favorites;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

public interface FavoriteColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey String _ID = "_id";

    @DataType(DataType.Type.TEXT) @NotNull String TITLE = "title";

    @DataType(DataType.Type.TEXT) @NotNull String POSTERPATH = "posterpath";

    @DataType(DataType.Type.REAL) @NotNull String AVRATING = "avrating";

    @DataType(DataType.Type.INTEGER) @NotNull String RELEASEDATE = "release";

    @DataType(DataType.Type.TEXT) @NotNull String OVERVIEW = "overview";

}
