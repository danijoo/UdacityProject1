package net.headlezz.udacityproject1.favorites;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = FavoriteProvider.AUTHORITY, database = FavoriteDatabase.class, packageName="net.headlezz.udacityproject1.provider")
public class FavoriteProvider {

    public static final String AUTHORITY = "net.headlezz.udacityproject1.FavoriteProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    @TableEndpoint(table = FavoriteDatabase.favorites)
    public static class Favorites {

        @ContentUri(
                path = "favorites",
                type = "vnd.android.cursor.dir/list",
                defaultSort = FavoriteColumns.TITLE + " ASC")
        public static final Uri FAVORITES_URI = Uri.parse("content://" + AUTHORITY + "/favorites");
    }
}
