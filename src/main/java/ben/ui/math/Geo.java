package ben.ui.math;

import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;import java.lang.Double;import java.lang.Math;import java.lang.Object;import java.lang.Override;import java.lang.String;

/**
 * Geographic Position.
 */
@Immutable
public final class Geo {

    /**
     * The radius of the world in meters.
     */
    private static final double RADIUS = 6371000;

    /**
     * The latitude coordinate of the position.
     */
    private final double latitude;

    /**
     * The longitude coordinate of the position.
     */
    private final double longitude;

    /**
     * Constructor.
     * @param latitude the latitude coordinate of the position
     * @param longitude the longitude coordinate of the position
     */
    public Geo(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Constructor from cartesian.
     * <p>
     *     http://en.wikipedia.org/wiki/Geographic_coordinate_system
     *     Z-axis along the axis of the ellipsoid, positive northward
     *     X- and Y-axis in the plane of the equator, X-axis positive toward 0 degrees longitude and Y-axis positive
     *     toward 90 degrees east longitude.
     * </p>
     * @param p the cartesian point
     */
    public Geo(@NotNull Vec3f p) {
        // Assumes that p is on the sphere
        double lat = Math.asin(p.getY());
        double lng = -Math.atan2(p.getZ(), p.getX());

        latitude = Math.toDegrees(lat);
        longitude = Math.toDegrees(lng);
    }

    @NotNull
    @Override
    public String toString() {
        return Geo.class.getSimpleName() + "[latitude: " + latitude + ", longitude: " + longitude + "]";
    }

    /**
     * Get the latitude coordinate of the position.
     * @return the latitude coordinate of the position
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Get the longitude coordinate of the position.
     * @return the longitude coordinate of the position
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Get the cartesian coordinates of the geographic position.
     * @return the cartesian coordinates
     */
    @NotNull
    public Vec3f getCartesian() {
        double lat = Math.toRadians(latitude);
        double lng = Math.toRadians(longitude);

        float x = (float) (Math.cos(lat) * Math.cos(-lng));
        float z = (float) (Math.cos(lat) * Math.sin(-lng));
        float y = (float) Math.sin(lat);

        return new Vec3f(x, y, z);
    }

    /**
     * Interpolate a position using SLERP.
     * @param g1 the first point
     * @param g2 the second point
     * @param ratio how far along (0.0 - 1.0)
     * @return the interpolated point
     */
    @NotNull
    public static Geo interpolatePosition(@NotNull Geo g1, @NotNull Geo g2, float ratio) {
        Vec3f p0 = g1.getCartesian();
        Vec3f p1 = g2.getCartesian();

        // cos omega = p0 . p1
        float omega = (float) Math.acos(p0.dot(p1));

        Vec3f slerp = p0.mul((float) (Math.sin((1.0f - ratio) * omega) / Math.sin(omega))).add(p1.mul((float) (Math.sin(ratio * omega) / Math.sin(omega))));

        return new Geo(slerp);
    }

    /**
     * Calculate the distance between two points.
     * @param g1 the first point
     * @param g2 the second point
     * @return the distance in meters
     */
    public static double distance(@NotNull Geo g1, @NotNull Geo g2) {
        // http://www.movable-type.co.uk/scripts/latlong.html
        double phi1 = Math.toRadians(g1.getLatitude());
        double phi2 = Math.toRadians(g2.getLatitude());
        double deltaPhi = Math.toRadians(g2.getLatitude() - g1.getLatitude());
        double deltaLambda = Math.toRadians(g2.getLongitude() - g1.getLongitude());

        double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2)
                + Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RADIUS * c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Geo geo = (Geo) o;

        if (java.lang.Double.compare(geo.latitude, latitude) != 0) return false;
        return Double.compare(geo.longitude, longitude) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
