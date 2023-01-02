package com.andela.irrigation_system.util;

import ch.hsr.geohash.GeoHash;
import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GeoHashUtils {
    /**
     * Возвращает геохэш размером.
     *
     * @param latitude  широта
     * @param longitude долгота
     * @param precision точность
     *                  <li>1 - 5,000km X 5,000 km</li>
     *                  <li>2 - ≤ 1,250km X 625km</li>
     *                  <li>3 - ≤ 156km X 156km</li>
     *                  <li>4 - ≤ 39.1km X 19.5km</li>
     *                  <li>5 - ≤ 4.89km X 4.89km</li>
     *                  <li>6 - ≤ 1.22km X 0.61km</li>
     *                  <li>7 - ≤ 153m X 153m</li>
     *                  <li>8 - ≤ 38.2m X 19.1m</li>
     *                  <li>9 - ≤ 4.77m X 4.77m</li>
     *                  <li>10 - ≤ 1.19m X 0.596m</li>
     *                  <li>11 - ≤ 149mm X 149mm</li>
     *                  <li>12 - ≤ 37.2mm X 18.6mm</li>
     * @return геохэш указанной точности
     */
    public static String geoHash(float latitude, float longitude, int precision) {
        Preconditions.checkArgument(precision > 0, "precision must be greater than 0");
        Preconditions.checkArgument(precision < 13, "precision must be less than 13");
        return GeoHash.geoHashStringWithCharacterPrecision(latitude, longitude, precision);
    }
}
