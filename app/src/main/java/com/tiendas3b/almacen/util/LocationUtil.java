package com.tiendas3b.almacen.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;

import com.tiendas3b.almacen.services.LocationService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dfa on 01/09/2016.
 */
public class LocationUtil {

    private static final float gpsAccuracy = 40.0f;
    private static final float wifiAccuracy = 60.0f;//50
    private static final float gsmWifiAccuracy = 60.0f;//45
    private static final float gsmAccuracy = 100.0f;//800
    private static final float fusedAccuracy = 60.0f;//40
    private static final int MAX_VELOCITY_KMH = 140;//km/h180//140
    private static final float NET_PROPORTION = 1.8f;
    private static final float GPS_PROPORTION = 9.0f;
    private static final float FUSED_PROPORTION = 9.0f;//definir
    private static final float RADIUS_SAME_PLACE = -8.f;
    private static float distanceSamePlace = 25.0f;//google recomienda 100 m por la wifi //yo 19
    //	private static float distanceSamePlaceVehicle=0.0f;//definir
    private static long minutes = 1000 * 60;
    private static Location lastLocation;
    private static float degreesDetect = 60.0f;
    private static float angleSup = 300.0f;
    private static float distanceDetect = 100.0f;
    private static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss:SSS", Locale.getDefault());
    private static float bearingMin;
    private static float bearingMax;
    //	private static int activityDetect=DetectedActivity.UNKNOWN;//implementar
    //	private static boolean samePlace;
    private static boolean inCar;
    private static float maxAccuracy = 60.0F;

    public static void setMinutes(long minutes) {
        LocationUtil.minutes = minutes / 2L;
    }

    public static void setLastLocation(Location lastLocation) {
        FileUtil.writeFile("\n" + df.format(new Date()) + "setLastLocation!!!\n");
        //		Log.e("LocationUtil", "setLastLocation:"+df.format(new Date(lastLocation.getTime())));
        LocationUtil.lastLocation = lastLocation;
        bearingMax = bearingMin = lastLocation.getBearing();
    }

    public static void setDegreeDetect(float degreeDetect) {
        LocationUtil.degreesDetect = degreeDetect - 1.0f;
        angleSup = 360 - degreesDetect;
    }

    public static void setDistanceDetect(float distanceDetect) {
        LocationUtil.distanceDetect = distanceDetect;
    }

    //	public static void setDistanceDetect(int activityDetect) {
    //		LocationUtil.activityDetect = activityDetect;
    //	}

    public static void setDistanceSamePlace(float distanceSamePlace) {
        LocationUtil.distanceSamePlace = distanceSamePlace;
    }

    public static float getDistanceSamePlace() {
        return distanceSamePlace;
    }

    public static Location getLastLocation() {
        return lastLocation;
    }

    //	public static boolean isSamePlace() {
    //		return samePlace;
    //	}

    public static void setInCar(boolean inCar) {
        LocationUtil.inCar = inCar;
    }

    /**
     * Checks if the given location is better than the current best location.
     *
     * @param location            the new location
     * @param currentBestLocation the current best location
     * @return true if the new location is better than the current one, false
     * otherwise
     */
    public static synchronized boolean isBetterLocation(final Location location, final Location currentBestLocation) {
        //		Log.v("LocationUtil", "---------------------------------");
        //		Log.v("LocationUtil", df.format(new Date(lastLocation.getTime())));
        final long timeLocation = location.getTime();
        String date = df.format(new Date(timeLocation));
        float bearing = location.getBearing();
        String provider = location.getProvider();
        float speed = location.getSpeed();
        float accuracy = location.getAccuracy();
        String a = "\n" + date + " pro:" + provider + " loc:" + location.getLatitude() + "," + location.getLongitude() + " acc:" + accuracy + " bea:" + bearing;
        //		Log.v("LocationUtil", a);
        FileUtil.writeFile(a);

        if(accuracy > maxAccuracy){
            return false;
        }

        if (lastLocation == null) {
            lastLocation = new Location(location);
            return true;
        }
        FileUtil.writeFile("lastLocation: " + lastLocation.getLatitude() + "," + lastLocation.getLongitude());

        // Fix for phones that do not set the time field
        if (timeLocation == 0L) {
            location.setTime(System.currentTimeMillis());
        }

        final long timeDelta = timeLocation - lastLocation.getTime();
        final long diferenceTimeInMilliseconds = Math.abs(timeDelta);
        //		final float diferenceTimeInSeconds = Double.valueOf(Math.ceil(diferenceTimeInMilliseconds / 1000.0)).floatValue();
        final float diferenceTimeInSeconds = diferenceTimeInMilliseconds / 1000.0f;
        if (diferenceTimeInSeconds == 0.0f) {
            return false;
        }
        //		final float diferenceTimeHours =  ((diferenceTimeInSeconds/ 60.0f) / 60.0f);
        float velocityCurrentKmH = 0;
        //		if(diferenceTimeHours > 0.0) {
        final float distanceSuspectMeters = lastLocation.distanceTo(location);
        //		final float distanceSuspectKilometers = distanceSuspectMeters/1000.0f;
        float velocityCurrentMSeg = distanceSuspectMeters / diferenceTimeInSeconds;
        velocityCurrentKmH = velocityCurrentMSeg * 3.6f;

        //		velocityCurrent = distanceSuspectKilometers / diferenceTimeHours;
        String b = " difSec:" + diferenceTimeInSeconds/*+", difHours"+diferenceTimeHours*/ + ", distMeters:" + distanceSuspectMeters + ", vel:" + velocityCurrentKmH + "km/h" + " spe:" + speed * 3.6 + "km/h";
        //		Log.v("LocationUtil", b);
        FileUtil.writeFile(b);
        if (velocityCurrentKmH > MAX_VELOCITY_KMH) {
            if (distanceSuspectMeters < distanceSamePlace) {
                velocityCurrentMSeg = speed = lastLocation.getSpeed();
                location.setSpeed(speed);
            } else {
                FileUtil.writeFile(" velocity!!!");
                return false;
            }
            //			Log.v("LocationUtil", "velocity!!!");
        }
        //		}

        final boolean isSignificantlyOlder = timeDelta < -minutes;
        if (isSignificantlyOlder) {
            //			Log.v("LocationUtil", "isSignificantlyOlder!!!");
            return false;
        }
        if (speed == 0.0f) {
            speed = velocityCurrentMSeg;
            location.setSpeed(speed);
            FileUtil.writeFile(" speed2:" + speed);
        }
        if (location.getSpeed() * 3.6 > MAX_VELOCITY_KMH) {
            //			Log.v("LocationUtil", "velocity2!!!");
            FileUtil.writeFile(" velocity2!!!");
            return false;
        }

        final float lastAccuracy = lastLocation.getAccuracy();
        if (currentBestLocation == null) {
            if (!location.hasAccuracy()) {
                location.setAccuracy(lastAccuracy);
            }
            if (!location.hasBearing()) {
                location.setBearing(lastLocation.getBearing());
            }
            if (!location.hasSpeed()) {
                location.setSpeed(lastLocation.getSpeed());
            }
            FileUtil.writeFile(" currentBestLocation == null");
            return true;
        }

        float lastBearing = lastLocation.getBearing();
        //		writeFile(" lastBearing:"+lastBearing);


        if (lastBearing == .0f) {
            if (bearing != .0f) {
                lastLocation.setBearing(bearing);
                FileUtil.writeFile(" bearing2:" + bearing);
            }
        } else if (bearing == .0f) {
            location.setBearing(lastBearing);
            bearing = lastBearing;
            FileUtil.writeFile(" bearing3:" + bearing);
        }

        /**
         * se puede comentar lo sieguente ya que con las geocercas ya no es necesario
         */
        final float currentAccuracy = currentBestLocation.getAccuracy();
        if (distanceSuspectMeters < distanceSamePlace) {
            //			if (currentBestLocation == null) {
            //				location.setAccuracy(lastLocation.getAccuracy());
            //			}

            //			if (currentBestLocation!= null && location.hasAccuracy() && currentBestLocation.hasAccuracy()) {
            if (location.hasAccuracy() && lastLocation.hasAccuracy()) {
                //				final float accuracyDelta = accuracy - lastAccuracy;
                //			Log.v("LocationUtil","new="+location.getProvider()+location.getAccuracy()+" old="+currentBestLocation.getProvider()+currentBestLocation.getAccuracy());
                //				final boolean isMoreAccurate = accuracyDelta < RADIUS_SAME_PLACE;

                FileUtil.writeFile("location.hasAccuracy() && lastLocation.hasAccuracy()");
                if (accuracy < lastAccuracy) {
                    if (accuracy < currentAccuracy) {
                        FileUtil.writeFile(" SamePlace!!!1");
                        return true;
                    } else {
                        location.setLatitude(lastLocation.getLatitude());
                        location.setLongitude(lastLocation.getLongitude());
                        //						location.setTime(timeLocation);
                        location.setAccuracy(currentAccuracy);
                        FileUtil.writeFile(" SamePlace!!!2");
                        return true;
                    }
                } else if (currentAccuracy < lastAccuracy) {
                    //					location.set(lastLocation);
                    //					location.setTime(timeLocation);
                    location.setLatitude(lastLocation.getLatitude());
                    location.setLongitude(lastLocation.getLongitude());
                    location.setAccuracy(currentAccuracy);
                    FileUtil.writeFile(" SamePlace!!!3");
                    return true;
                } else {
                    //					location.set(lastLocation);
                    //					location.setTime(timeLocation);
                    location.setLatitude(lastLocation.getLatitude());
                    location.setLongitude(lastLocation.getLongitude());
                    location.setAccuracy(lastAccuracy);
                    FileUtil.writeFile(" SamePlace!!!4");
                    return true;
                }
            } else {
                FileUtil.writeFile(" SamePlace FALSE!!!");
                return false;
            }
        }
        //		samePlace=false;
        if (distanceSuspectMeters > distanceDetect) {

            if (isImpossible(provider, velocityCurrentKmH)) {
                FileUtil.writeFile("isImpossible");
                return false;
            }

            //			Log.v("LocationUtil", "distanceSuspectMeters:"+distanceSuspectMeters);
            LocationService.getInstance().setSendNow(true);
            //			Log.v("LocationUtil", "distanceDetect!");
            FileUtil.writeFile(" distanceDetect!!!");
            return true;
        }

        if (bearing != lastBearing && bearing > 0.0f && lastBearing > 0.0f && location.hasAccuracy() && accuracy < 15) {//13 estimado si es buena opcion hacer constante//no applica debajo de segundo piso
            float degreesRotated = 0.0f;
            if (inConflictZone(bearing, lastBearing)) {
                if (bearing < lastBearing) {
                    degreesRotated = Math.abs(bearing + 360.0f - lastBearing);
                    FileUtil.writeFile(" degreesRotated1:" + degreesRotated);
                } else {
                    degreesRotated = Math.abs(bearing - (lastBearing + 360.0f));
                    FileUtil.writeFile(" degreesRotated2:" + degreesRotated);
                }
            } else {
                degreesRotated = Math.abs(bearing - lastBearing);
                FileUtil.writeFile(" degreesRotated3:" + degreesRotated);
            }
            //		Log.v("LocationUtil", "degreesRotated:"+degreesRotated);
            //			writeFile(" degreesRotated:"+degreesRotated);
            if (degreesRotated > degreesDetect) {
                LocationService.getInstance().setSendNow(true);
                //			Log.v("LocationUtil", "degreeDetect!");
                FileUtil.writeFile(" degreeDetect! >" + degreesDetect);
                return true;
            }
        }

        //		if(currentBestLocation == null){
        //			return true;
        //		}

        //		float currentBestBearing = currentBestLocation.getBearing();
        //		writeFile(" currentBestBearing:"+currentBestBearing);
        if (inCar) {
            if (inConflictZone(bearing, bearingMax)) {
                if (bearing < angleSup) {
                    if (lastBearing > angleSup) {
                        bearing = bearing + 360.0f;
                        bearingMax = bearing > bearingMax ? bearing : bearingMax;
                    } else {
                        bearingMax = bearing > bearingMax ? bearing : bearingMax;
                    }
                }
            } else {
                bearingMax = bearing > bearingMax ? bearing : bearingMax;
            }
            if (inConflictZone(bearing, bearingMin)) {
                if (bearing > degreesDetect) {
                    if (lastBearing < degreesDetect) {
                        bearing = bearing - 360.0f;
                        bearingMin = bearing < bearingMin ? bearing : bearingMin;
                    } else {
                        bearingMin = bearing < bearingMin ? bearing : bearingMin;
                    }
                }
            } else {
                bearingMin = bearing < bearingMin ? bearing : bearingMin;
            }
            FileUtil.writeFile(" min:" + bearingMin + " max:" + bearingMax);
            if (bearingMax - bearingMin > degreesDetect) {
                LocationService.getInstance().setSendNow(true);
                //			Log.v("LocationUtil", "max - min!");
                FileUtil.writeFile(" max-min detect!!!");
                return true;
            }
        }

        if (location.hasAccuracy() && currentBestLocation.hasAccuracy()) {
            final float accuracyDelta = accuracy - currentBestLocation.getAccuracy();
            //			Log.v("LocationUtil","new="+location.getProvider()+location.getAccuracy()+" old="+currentBestLocation.getProvider()+currentBestLocation.getAccuracy());
            final boolean isMoreAccurate = accuracyDelta < 0;
            if (isMoreAccurate) {
                //				Log.v("LocationUtil", "isMoreAccurate");
                FileUtil.writeFile(" isMoreAccurate!!!");
                return true;
            }
            final long timeDelta2 = timeLocation - currentBestLocation.getTime();
            if (accuracyDelta == 0 && timeDelta2 > 0) {
                //				Log.v("LocationUtil", "isMoreAccurate");
                FileUtil.writeFile(" isSameAccurate but recent!!!");
                return true;
            }

            final boolean isSignificantlyNewer = timeDelta > minutes;
            final boolean isNewer2 = timeDelta2 > 0;
            final boolean isSignificantlyLessAccurate = accuracyDelta > 5.0f;
            if (isSignificantlyNewer && isNewer2 && !isSignificantlyLessAccurate) {
                FileUtil.writeFile(" isSignificantlyNewer!!!");
                return true;
            }
        }
        //		Log.i("LocationUtil", "false!!!");
        FileUtil.writeFile(" END!!!");
        return false;
    }

    private static boolean isImpossible(final String provider, float velocityCurrentKmH) {//verificar si se necesita una proporcion para el gps!!!
        float lastVelocity = 0.0f;
        if (LocationManager.NETWORK_PROVIDER.equals(provider)) {
            if ((lastVelocity = lastLocation.getSpeed() * 3.6f) > 5.0f && velocityCurrentKmH / lastVelocity > NET_PROPORTION) {
                FileUtil.writeFile(" NvelocityCurrent:" + velocityCurrentKmH + " lastSpeed:" + lastLocation.getSpeed());
                return true;
            }
        } else if (LocationManager.GPS_PROVIDER.equals(provider)) {
            if ((lastVelocity = lastLocation.getSpeed() * 3.6f) > 5.0f && velocityCurrentKmH / (lastVelocity) > GPS_PROPORTION) {
                FileUtil.writeFile(" GvelocityCurrent:" + velocityCurrentKmH + " lastSpeed:" + lastLocation.getSpeed());
                return true;
            }
        } else if (LocationService.FUSED_PRIVIDER.equals(provider)) {
            if ((lastVelocity = lastLocation.getSpeed() * 3.6f) > 5.0f && velocityCurrentKmH / (lastVelocity) > FUSED_PROPORTION) {
                FileUtil.writeFile(" FvelocityCurrent:" + velocityCurrentKmH + " lastSpeed:" + lastLocation.getSpeed());
                return true;
            }
        }
        return false;
    }

    private static boolean inConflictZone(float bearing, float lastBearing) {
        if ((bearing > angleSup && lastBearing < degreesDetect) || (lastBearing > angleSup && bearing < degreesDetect)) {
            return true;
        }
        return false;
    }

    //	private static boolean isSameProvider(final String provider1, final String provider2) {
    //		// Log.v("LocationUtil", "p1="+provider1+" p2="+provider2);
    //		if (provider1 == null) {
    //			return provider2 == null;
    //		}
    //		return provider1.equals(provider2);
    //	}

    private static boolean isAirplaneModeOn(final Context context) {
        return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }

}
